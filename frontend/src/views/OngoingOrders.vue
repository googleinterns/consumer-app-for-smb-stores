<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div v-if="ongoingOrders.length">
        <div class="columns is-mobile">
          <div class="column">
            <h1 class="title is-4" align="left">Ongoing Orders</h1>
            <p align="left"><small> Click on order to see bids</small></p>
          </div>
        </div>
        <div>
          <div class="panel" v-for="order in ongoingOrders" v-bind:key="order.order.order_id">
            <div v-on:click="getOngoingOrderDetails(order.order.order_id)" class="card">
              <div class="card-content">
                <div class="content">
                  <p align="left">
                    <b>Ordered On:</b>
                    {{order.order.date_of_order | formatDate}}
                  </p>
                  <div>
                    <table
                      class="table is-narrow is-striped"
                      style="table-layout:fixed;word-wrap:break-word;"
                    >
                      <thead>
                        <th>Item</th>
                        <th align="center">Quantity</th>
                      </thead>
                      <tr v-for="(item, index) in order.all_items" v-bind:key="index">
                        <td>
                          {{item.item_name}}
                        </td>
                        <td align="center">{{item.quantity}}</td>
                      </tr>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="container">
          <div>
            <section class="section">
              <p class="content is-medium">No orders yet!</p>
              <p class="content is-medium">Start with creating your order on Order Now!</p>
            </section>
          </div>
        </div>
        <button class="button is-info is-light">
          <router-link to="/placeOrder">Order Now</router-link>
        </button>
      </div>
    </div>
  </div>
</template>


<script>
import Logout from "@/components/Logout.vue";
import api from "../Api";
export default {
  name: "OngoingOrders",
  components: {
    Logout
  },
  mounted: function() {
    this.getOngoingOrders();
  },
  data: function() {
    return {
      ongoingOrders: []
    };
  },
  methods: {
    getOngoingOrders() {
      var userId = this.$getUserId();
      api.fetchOngoingOrders(userId).then(response => {
        this.ongoingOrders = response.data;
        console.log(this.ongoingOrders, "these are ongoing orders")
      });
    },

    getOngoingOrderDetails(orderId){
        console.log("pushing id", orderId)
        this.$router.push({name: 'merchantList', params: {"orderId": orderId}})
    }
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>
