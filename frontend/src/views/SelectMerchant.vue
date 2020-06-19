<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div v-for="merchant in merchantsList" v-bind:key="merchant.mid" class="panel is-shadowless">
        <div class="card">
          <div class="card-content">
            <div class="media">
              <div class="media-left">
                <i class="fa fa-user-circle is-size-3" aria-hidden="true"></i>
              </div>
              <div class="media-content">
                <p class="title is-4" style="color: #009c55;">{{merchant.name}}</p>
                <p class="subtitle is-6">{{merchant.address}}</p>
              </div>
            </div>
            <div align="right">
              <button class="button">
                <router-link
                  :to="{ name: 'PaginatedProducts', params: {merchant: merchant}}"
                >View Products</router-link>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
import axios from "axios";
export default {
  name: "SelectMerchant",
  components: {
    Logout
  },

  created: function() {
    this.getMerchants();
  },

  methods: {
    getMerchants() {
      axios
        .get(process.env.VUE_APP_MERCHANT_SERVER + "/merchants/all")
        .then(response => {
          this.merchantsList = response.data.merchants;
        });
    }
  },

  data: function() {
    return {
      merchantsList: []
    };
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>