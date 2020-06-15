<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div v-for="merchant in merchantsList" v-bind:key="merchant.mid" class="panel is-shadowless">
        <div class="card">
          <div class="card-content">
            <div class="columns is-mobile">
              <p class="column" align="left" style="color: #435ad6;">
                <strong style="color: #435ad6;">{{merchant.merName}}</strong>
              </p>
              <p class="column" align="right">
                <button class="button is-small is-primary is-light">
                  <router-link
                    :to="{ name: 'PaginatedProducts', params: {merchant: merchant}}"
                  >View Products</router-link>
                </button>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
export default {
  name: "SelectMerchant",
  components: {
    Logout
  },

  data: function() {
    return {
      merchantsList: [
        {
          merName: "Goel Stores",
          merchantAddress: "New Avalon Street, Delhi",
          mid: "VxWQKTpSLLRlsuhQzdb3rapz5zv1"
        },
        {
          merName: "Kondli Baazar",

          merchantAddress: "Park Avenue Street, Delhi",
          mid: 234
        },
        {
          merName: "Ask for More",
          merchantAddress: "Govindpuri Street, Delhi",
          mid: 456
        },
        {
          merName: "Satkar Shop",
          merchantAddress: "RamGhat road, Delhi",
          mid: 34456
        },
        {
          merName: "Sai Store",
          merchantAddress: "Rajnagar, Delhi",
          mid: 346
        },
        {
          merName: "Singh Kirana Store",
          merchantAddress: "BR Road, Delhi",
          mid: 3444456
        },
        {
          merName: "Smile Store",
          merchantAddress: "New Avalon Street, Delhi",
          mid: 34465456
        }
      ]
    };
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>