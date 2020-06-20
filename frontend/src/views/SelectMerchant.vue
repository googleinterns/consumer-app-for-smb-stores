<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div
        v-for="(merchant) in merchantsList"
        v-bind:key="merchant.mid"
        class="panel is-shadowless"
      >
        <div class="card">
          <div class="card-content">
            <div class="media">
              <div class="media-left">
                <i class="fa fa-user-circle is-size-3" aria-hidden="true"></i>
              </div>
              <div class="media-content">
                <p class="title is-6">{{merchant.name}}</p>
                <p class="subtitle is-6">{{merchant.address}}</p>
              </div>
            </div>
            <div align="right">
              <button
                class="button is-info"
                v-on:click="moveToPaginatedProducts(merchant)"
              >View Products</button>
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
          this.merchantsList.forEach(merchant => {
            (merchant.name = this.namesList[this.count]),
              (merchant.address = this.addressList[this.count]);
            this.count += 1;
          });
        });

      console.log(this.merchantsList);
    },
    moveToPaginatedProducts(merchant) {
      this.$router.push({
        name: "PaginatedProducts",
        params: { merchant: merchant }
      });
    }
  },

  data: function() {
    return {
      merchantsList: [],
      namesList: [
        "Mahendra Kirana Store",
        "Kondli Baazar",
        "Shop for More",
        "Goel Stores",
        "Surendra Kirana Store",
        "Get It For Value",
        "New Store",
        "Reliance Store",
        "",
        ""
      ],
      addressList: [
        "B-7, Nehru Market, Delhi",
        "C-78, Sector 2, Indira Road, Delhi",
        "R-89, Sunil Block, Gandhi Road, Delhi",
        "G-7, Ground Floor, Kamla Market, Delhi",
        "5-F, Rakesh Marg, New Road, Delhi",
        "Delhi",
        "Delhi",
        "Delhi",
        "Delhi"
      ],
      count: 0
    };
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>