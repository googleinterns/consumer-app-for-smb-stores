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
      data: []
    };
  },
  mounted() {
    console.log("checking");
    var self = this;
    this.isAnonymous = firebase.auth().currentUser.isAnonymous;
    if (firebase.auth().currentUser.isAnonymous) {
      let ui = firebaseui.auth.AuthUI.getInstance();
      if (!ui) {
        ui = new firebaseui.auth.AuthUI(firebase.auth());
      }
      var uiConfig = {
        signInSuccessUrl: "/userDetails/" + this.orderId,
        signInOptions: [firebase.auth.GoogleAuthProvider.PROVIDER_ID]
      };
      ui.start("#firebaseui-auth-container", uiConfig);
    }

    api.fetchUserDetails(firebase.auth().currentUser.uid).then(response => {
      console.log("inside", firebase.auth().currentUser.uid, "user param");
      if (response === null) return;

      self.address = response.data.user_address;
      self.contactNo = response.data.user_contactNo;
      self.userName = response.data.user_name;
    });
  },
  methods: {
    submit() {

      api
        .updateUserDetails(firebase.auth().currentUser.uid, this.address, this.contactNo, this.userName)
        .catch(error => {
          this.$log.debug(error);
        });

      this.$router.push({
        name: "merchantList",
        params: {
          orderId: this.orderId,
          address: this.address,
          cust_name: this.userName
        }
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