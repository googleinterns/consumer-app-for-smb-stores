<template>
  <div class="container">
    <img src="../assets/googlelogo.png" alt="googlelogo" />
    <p>Bharat ka digital Kirana</p>
    <section id="firebaseui-auth-container"></section>
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
      signInSuccessUrl: "/home", // This redirect can be achived by route using callback.
      signInOptions: [firebase.auth.GoogleAuthProvider.PROVIDER_ID]
    };
    ui.start("#firebaseui-auth-container", uiConfig);
  }
};
</script>

<style  scoped>
.container {
  margin-top: 150px;
  margin-bottom: 50px;
}
p {
  margin-bottom: 50px;
}
</style>