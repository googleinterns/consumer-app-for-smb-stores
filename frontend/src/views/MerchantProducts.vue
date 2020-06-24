<template>
  <div v-if="!validURL && !loading">
    <div class="container">
      <div class="content is-large">No page exists for {{merchantName}}</div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
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
    axios
      .get(
        process.env.VUE_APP_MERCHANT_SERVER +
          "/merchant/domain/" +
          this.merchantName
      )
      .then(response => {
        var merchant = {
          mid: response.data.mid,
          name: response.data.store_name
        };
        console.log(merchant);
        this.$router.push({
          name: "PaginatedProducts",
          params: { merchant: merchant }
        });
      });
  }
};
</script>