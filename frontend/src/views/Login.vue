<template>
  <div>
    <div class="Site-content">
      <div style="margin-top: 11%"></div>

      <img src="../assets/googlelogo.png" alt="googlelogo" />
      <p style="font-size: 1.2em; padding-top: 15px;">Bharat Ka Digital Kirana</p>
      <section id="firebaseui-auth-container"></section>
    </div>
  </div>
</template>

<script>
import firebase from "firebase";
import * as firebaseui from "firebaseui";
import "firebaseui/dist/firebaseui.css";

export default {
  name: "Login",
  mounted() {
    let ui = firebaseui.auth.AuthUI.getInstance();
    if (!ui) {
      ui = new firebaseui.auth.AuthUI(firebase.auth());
    }
    var uiConfig = {
      signInSuccessUrl: "/home",
      signInOptions: [firebase.auth.GoogleAuthProvider.PROVIDER_ID, 
      firebaseui.auth.AnonymousAuthProvider.PROVIDER_ID]
    };
    ui.start("#firebaseui-auth-container", uiConfig);
  }
};
</script>
