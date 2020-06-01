<template>
  <button @click="logout">Logout</button>
</template>

<script>
// @ is an alias to /src

import firebase from "firebase";

export default {
  name: "Home",
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
};
</script>
