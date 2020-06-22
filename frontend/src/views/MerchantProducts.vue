<template>
  <div v-if="!validURL && !loading">
    <div class="container">
      <div class="content is-large">No page exists for {{merchantName}}</div>
    </div>
  </div>
</template>

<script>
// import axios from 'axios';
import { db } from "../main.js";
export default {
  name: "MerchantProducts",
  props: ["merchantName"],

  data: function() {
    return {
      merchantsList: [],
      validURL: false,
      loading: true
    };
  },

  created: function() {
    db.collection("merchant_links")
      .doc(this.merchantName)
      .get()
      .then(snap => {
        if (snap.exists) {
          this.validURL = true;
          var merchant = {
            mid: snap.data().mid,
            name: snap.data().name
          };
          this.$router.push({
            name: "PaginatedProducts",
            params: { merchant: merchant }
          });
        } else {
          console.log("Invalid URL");
        }
        this.loading = false;
      });
  }
};
</script>