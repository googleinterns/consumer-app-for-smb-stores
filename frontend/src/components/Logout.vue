<template>
    <nav class="navbar is-light">
      <div class="container">
        <div class="navbar-brand">
          <a class="navbar-item" href="#" style="font-weight:bold;">
          <img src="../assets/google-logo-png.png" alt="SMB Kirana">
          </a>
          <span onclick="document.querySelector('.navbar-menu').classList.toggle('is-active');" class="navbar-burger burger" data-target="navMenu">
            <span></span>
            <span></span>
            <span></span>
          </span>
        </div>
        <div id="navMenu" class="navbar-menu">
          <div class="navbar-end">
            <a @click="logout" class="navbar-item"><button class="button is-light">Logout</button></a>
          </div>
        </div>
      </div>
    </nav>
</template>


<script>
import firebase from "firebase";

export default {
    name: 'Logout',
    methods: {
    logout: function() {
      firebase
        .auth()
        .signOut()
        .then(() => {
          this.$router.replace("login");
        });
    }
  }
}
</script>
