<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <div>
          <div>
            <h1 class="is-family-primary title is-size-3 has-text-info">Edit details</h1>
          </div>
        </div>
        <div class="field">
          <div class="field is-family-primary is-normal">
            <label class="label is-pulled-left is-family-primary">Customer Name</label>
          </div>
          <div class="field-body">
            <div class="field is-expanded is-normal">
              <p class="control has-icons-left">
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

        <div class="field">
          <div class="field-label is-normal">
            <label class="label is-pulled-left">Contact Number</label>
          </div>
          <div class="field-body">
            <div class="field is-expanded is-normal">
              <p class="control field is-grouped is-expanded">
                <span class="button is-static">+91</span>
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

        <!-- <div class="field">
  <label class="label">Label</label>
  <p class="control">
    <div class="field has-addons">
      <p class="control">
        <input type="text"/>
      </p>
      <p class="control">
        <a class="button">fghjk</a>
      </p>
    </div>
  </p>
        </div>-->

        <div class="field">
          <div class="field-label is-normal">
            <label class="label is-pulled-left">Delivery Address</label>
          </div>
          <div class="field-body">
            <div class="field is-expanded">
              <div class="control">
                <textarea
                  v-model="address"
                  class="input is-expanded is-capitalized is-family-primary"
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
        </div>
         <p v-if="isAnonymous" style="font-size: 1em; padding-top: 15px;">Already have an account?</p>
          <section id="firebaseui-auth-container"></section>
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
    this.userName = firebase.auth().currentUser.displayName;
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
        .updateUserDetails(
          firebase.auth().currentUser.uid,
          this.address,
          this.contactNo,
          this.userName
        )
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