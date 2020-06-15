<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="columns">
        <div class="container">
          <div class="content has-text-weight-bold column">{{merchant.merName}}</div>
        </div>
      </div>
      <div class="container">
        <ProductPage v-bind:itemsavailable="this.products" />
      </div>
      <div v-if="reachedLastPage" class="container">
        <!-- <div > -->
          <!-- <div class="card"> -->
            <div class="card-content"><p style="color: #ff0300">Hey! You have reached the last page!</p></div>
          <!-- </div> -->
        <!-- </div> -->
      </div>
      <div class="columns is-mobile is-centered">
        <div class="column is-half">
      <div class="field is-grouped is-grouped-centered">
        <p class="control">
          <button class="button" v-on:click="getPrevPage()">
            <span class="icon is-medium is-left" style="color: #17a1b9c7">
              <i class="fa fa-angle-double-left" aria-hidden="true"></i>
            </span>
          </button>
        </p>
        <p class="control">
          <button class="button disabled">{{page}}</button>
        </p>
        <p class="control">
          <button class="button" v-on:click="getNextPage()"><span class="icon is-medium is-left" style="color: #17a1b9c7">
              <i class="fa fa-angle-double-right" aria-hidden="true"></i>
            </span></button>
        </p>
      </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import Logout from "@/components/Logout.vue";
import ProductPage from "@/components/ProductPage.vue";
import axios from "axios";

export default {
  name: "PaginatedProducts",
  props: ["merchant"],
  components: {
    Logout,
    ProductPage
  },

  data: function() {
    return {
      page: 1,
      products: [],
      EANstack: [],
      reachedLastPage: false
    };
  },

  mounted: function() {
    this.getFirstPage();
  },

  methods: {
    getFirstPage() {
      axios
        .get(
          process.env.VUE_APP_MERCHANT_SERVER +
            "products/page?mid=" +
            this.merchant.mid
        )
        .then(response => {
          this.products = response.data.products;
          this.EANstack.push(this.products[this.products.length - 1].EAN);
        });
    },

    getNextPage() {
      if (!this.reachedLastPage) {
        axios
          .get(
            process.env.VUE_APP_MERCHANT_SERVER +
              "products/page?mid=" +
              this.merchant.mid +
              "&last_ean=" +
              this.EANstack[this.EANstack.length - 1]
          )
          .then(response => {
            if (response.data.products.length === 0) {
              this.reachedLastPage = true;
              return;
            }
            this.products = response.data.products;
            this.EANstack.push(this.products[this.products.length - 1].EAN);
            this.page += 1;
          });
      }
    },

    getPrevPage() {
      if (this.reachedLastPage) {
        this.reachedLastPage = false;
      }
      if (this.page >= 3) {
        this.EANstack.pop();
        this.EANstack.pop();
        var EANtoQuery = this.EANstack[this.EANstack.length - 1];
        axios
          .get(
            process.env.VUE_APP_MERCHANT_SERVER +
              "products/page?mid=" +
              this.merchant.mid +
              "&last_ean=" +
              EANtoQuery
          )
          .then(response => {
            this.products = response.data.products;
            this.EANstack.push(this.products[this.products.length - 1].EAN);
          });
        this.page -= 1;
      } else if (this.page == 2) {
        this.EANstack = [];
        this.getFirstPage();
        this.page -= 1;
      }
    }
  }
};
</script>


<style>
.bd-lead {
  padding: 0.75rem;
}
</style>