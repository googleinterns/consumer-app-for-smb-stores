<template>
  <nav class="navbar is-light">
    <div class="container">
      <div class="navbar-brand">
        <a class="navbar-item" style="font-weight:bold;">
          <router-link to="/home">
            <img src="../assets/google-logo-png.png" alt="SMB Kirana" />
          </router-link>
        </a>
        <span
          onclick="document.querySelector('.navbar-menu').classList.toggle('is-active');"
          class="navbar-burger burger"
          data-target="navMenu"
        >
          <span></span>
          <span></span>
          <span></span>
        </span>
      </div>
      <div id="navMenu" class="navbar-menu">
        <div class="navbar-end">
          <a class="navbar-item">
            <router-link to="/ongoingOrders">
              <a class="is-fullwidth">Ongoing Orders</a>
            </router-link>
          </a>
          <a class="navbar-item">
            <router-link to="/selectMerchant">
              <a class="is-fullwidth">Find Products</a>
            </router-link>
          </a>
          <a class="navbar-item">
            <router-link to="/placeOrder">
              <a class="is-fullwidth">My Cart</a>
            </router-link>
          </a>
          <a class="navbar-item">
            <router-link to="/orderHistory">
              <a class="is-fullwidth">Order History</a>
            </router-link>
          </a>
          <a @click="logout" class="navbar-item">
            <a class="is-fullwidth">Logout</a>
          </a>
        </div>
      </div>
    </div>
  </nav>
</template>


<script>
import firebase from "firebase";

export default {
  name: "Logout",
  methods: {
    logout: function() {
      localStorage.removeItem("isAnon");
      localStorage.removeItem("anonId");
      firebase
        .auth()
        .signOut()
        .then(() => {
          this.$router.replace("/home").catch(() => {});
        });
    }
  }
};
</script>