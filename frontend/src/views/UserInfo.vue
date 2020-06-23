<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <div class="field is-horizontal">
          <div class="field-label is-normal">
            <label class="label is-family-primary is-size-3 has-text-info">Confirm your details</label>
          </div>
          <div class="field-body">
            <div class="field-label is-normal">
              <label class="label is-pulled-left is-family-primary">Customer Name</label>
            </div>
            <div class="field">
              <p class="control is-expanded has-icons-left">
                <input
                  v-model="userName"
                  class="input is-capitalized is-family-primary"
                  type="text"
                  placeholder="Enter your Name"
                />
                <span class="icon is-small is-left">
                  <i class="fas fa-user"></i>
                </span>
              </p>
            </div>
          </div>
        </div>

        <div class="field is-horizontal">
          <div class="field-label"></div>

          <div class="field-body">
            <div class="field is-expanded">
              <div class="field has-addons">
                <p class="control">
                  <a class="button is-static">+91</a>
                </p>
                <p class="control is-expanded">
                  <input
                    v-model="contactNo"
                    class="input is-capitalized is-family-primary"
                    type="tel"
                    placeholder="Your phone number"
                  />
                </p>
              </div>
            </div>
          </div>
        </div>

        <div class="field is-horizontal">
          <div class="field-label is-normal">
            <label class="label is-pulled-left">Delivery address</label>
          </div>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <textarea
                  v-model="address"
                  class="input is-capitalized is-family-primary"
                  type="text"
                  placeholder="Your delivery Address"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="field is-horizontal">
          <div class="field-label">
            <!-- Left empty for spacing -->
          </div>
          <div class="field-body">
            <div class="field is-centre">
              <div class="control">
                <button @click="submit" class="button is-success is-fullwidth">Confirm and continue</button>
              </div>
            </div>
          </div>
          <p v-if="isAnonymous" style="font-size: 1em; padding-top: 15px;">Already have an account?</p>
          <section id="firebaseui-auth-container"></section>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
import api from "../Api";
import firebase from "firebase";
import * as firebaseui from "firebaseui";
import "firebaseui/dist/firebaseui.css";

export default {
  props: ["orderId"],
  name: "UserInfo",
  components: {
    Logout
  },
  data() {
    return {
      address: "",
      contactNo: "",
      userName: "",
      isAnonymous: false,
      itemsInCart: [],
      itemsForOrder: [],
      data:[]
    };
  },
  mounted() {
    var userId = this.$getUserId();
    console.log("checking");
    var self = this;
    this.isAnonymous = firebase.auth().currentUser.isAnonymous;
    firebase
      .database()
      .ref("user_cart/" + userId)
      .once("value")
      .then(function(cart) {
        cart.forEach(function(cartItem) {
          self.itemsInCart.push({
            item_id: cartItem.key,
            item_name: cartItem.val().item_name,
            item_quantity: cartItem.val().item_quantity,
            EAN: cartItem.val().EAN,
            item_image: cartItem.val().item_image
          });
        });
       
      });

    if (firebase.auth().currentUser.isAnonymous) {
      let ui = firebaseui.auth.AuthUI.getInstance();
      if (!ui) {
        ui = new firebaseui.auth.AuthUI(firebase.auth());
      }
     // var anonymousUser = firebase.auth().currentUser;
      var uiConfig = {
        signInSuccessUrl: "/userDetails/" + this.orderId,
        signInOptions: [firebase.auth.GoogleAuthProvider.PROVIDER_ID],
       // autoUpgradeAnonymousUsers: true,
        // callbacks: {
        //   // signInFailure callback must be provided to handle merge conflicts which
        //   // occur when an existing credential is linked to an anonymous user.
        //   signInFailure: function(error) {
        //     // For merge conflicts, the error.code will be
        //     // 'firebaseui/anonymous-upgrade-merge-conflict'.
        //     if (error.code != "firebaseui/anonymous-upgrade-merge-conflict") {
        //       return Promise.resolve();
        //     }
        //     // The credential the user tried to sign in with.
        //     var cred = error.credential;
        //     // Copy data from anonymous user to permanent user and delete anonymous
        //     // user.
        //     // ...
        //     var ref = firebase.database().ref("user_cart/")
        //     var child = ref.child(anonymousUser.uid);
        //     child.once("value", function(snapshot) {
        //       //ref.child(firebase.auth().currentUser.uid).set(snapshot.val());
        //       child.remove();
        //       self.data=snapshot.val();
        //     });
        //     console.log(firebase.auth().currentUser.uid)
            
        //     // Finish sign-in after data is copied.
        //     return firebase.auth().signInWithCredential(cred);
        //   }
        // }
      };
       firebase.database().ref("user_cart/"+firebase.auth().currentUser.uid).set(this.data);

      ui.start("#firebaseui-auth-container", uiConfig);
    }

    api.fetchUserDetails(userId).then(response => {
      console.log("inside");
      if (response === null) return;

      self.address = response.data.user_address;
      self.contactNo = response.data.user_contactNo;
      self.userName = response.data.user_name;
    });
  },
  methods: {
    emptyCart() {
      var userId = this.$getUserId();
      firebase
        .database()
        .ref()
        .child("user_cart/" + userId)
        .remove();
      this.itemsInCart = [];
    },
    submit() {
      var userId = this.$getUserId();

      api
        .updateUserDetails(userId, this.address, this.contactNo, this.userName)
        .catch(error => {
          this.$log.debug(error);
        });
      console.log("jaldi" + this.itemsInCart);
      this.itemsInCart.forEach(item => {
        if (item.EAN === undefined) {
          this.itemsForOrder.push({
            item_name: item.item_name,
            quantity: item.item_quantity
          });
        } else {
          this.itemsForOrder.push({
            item_name: item.item_name,
            quantity: item.item_quantity,
            EAN: item.EAN
          });
        }
      });
      console.log("mujhe sona h" + this.itemsForOrder);

      // var itemsForOrder = [];

      api
        .placeOrder(userId, this.orderId, this.itemsForOrder)
        .then(response => {
          if (response.status == 200) {
            this.emptyCart();
            this.$router.push({
              name: "merchantList",
              params: {
                orderId: this.orderId,
                address: this.address,
                cust_name: this.userName
              }
            });
          }
        })
        .catch(error => {
          this.$log.debug(error);
        });
    }
  }
};
</script>

<style  scoped>
.bd-lead {
  padding: 0.75rem;
}
.field {
  margin: 20px 5px 10px 5px;
}
</style>