<template>
  <div class="bd-lead">
    <div v-if="loading">
      <div class="card is-shadowless">
        <div class="card-content">
          <span>
            <i class="fa fa-spinner fa-spin" style="font-size: 80px"></i>
          </span>
          <div>
            <p class="is-size-4">Loading</p>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <div v-if="pastOrders.length">
        <div class="columns is-mobile">
          <div class="column">
            <h1 class="title is-4" align="left">Order History</h1>
          </div>
          <div class="column" align="right">
            <button class="button is-info" v-on:click="placeOrder()">Order Now</button>
          </div>
        </div>
        <div>
          <div class="panel" v-for="order in pastOrders" v-bind:key="order.order.order_id">
            <div v-if="order.order.order_status != 'ONGOING'">
              <div class="card">
                <div class="card-content">
                  <div class="is-pulled-right" v-if="order.order.order_status == 'COMPLETED'">
                    <button class="button is-info" v-on:click="rateNow(order)">Rate Now</button>
                  </div>
                  <div class="content">
                    <p align="left">
                      <b>Ordered On:</b>
                      {{order.order.date_of_order | formatDate}}
                    </p>
                    <p align="left">
                      <b>Status:</b>
                      <span
                        :style="{color: order.order.order_status == 'COMPLETED' ? 'green':'pass'}"
                      >{{order.order.order_status | formatStatus}}</span>
                    </p>
                    <p></p>
                    <p align="left">
                      <b>Served By:</b>
                      {{order.order.merchant_name}}, {{order.order.merchant_address}}
                    </p>
                    <div>
                      <table
                        class="table is-narrow is-striped"
                        style="table-layout:fixed;word-wrap:break-word;"
                      >
                        <thead>
                          <th>Item</th>
                          <th align="center">Price</th>
                        </thead>
                        <tfoot>
                          <th>Total</th>
                          <th align="center">{{order.total}}</th>
                        </tfoot>
                        <tr v-for="(item, index) in order.all_items" v-bind:key="index">
                          <td v-if="item.unit_price != 0">
                            {{item.item_name}}
                            <span class="has-text-grey">x{{item.quantity}}</span>
                          </td>
                          <td
                            v-if="item.unit_price != 0"
                            align="center"
                          >{{item.quantity * item.unit_price}}</td>
                        </tr>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="container">
          <img src="../assets/cart.png" alt="Cart" />
          <div>
            <section class="section">
              <p class="content is-medium">No orders yet!</p>
              <p class="content is-medium">Start with creating your order on Order Now!</p>
            </section>
          </div>
        </div>
        <button class="button is-info" v-on:click="placeOrder()">Order Now</button>
      </div>
    </div>
  </div>
</template>

<script>
import api from "../Api";
export default {
  name: "PastOrders",

  data: function() {
    return {
      pastOrders: "",
      response: "",
      errors: "",
      loading: true
    };
  },

  mounted() {
    api
      .fetchPastOrders(this.$getUserId())
      .then(response => {
        this.pastOrders = response.data;

        for (var i = 0; i < this.pastOrders.length; i++) {
          var sum = 0;
          for (var j = 0; j < this.pastOrders[i].all_items.length; j++) {
            sum +=
              this.pastOrders[i].all_items[j].unit_price *
              this.pastOrders[i].all_items[j].quantity;
          }
          this.$set(this.pastOrders[i], "total", sum);
        }

        this.loading = false;
      })
      .catch(e => {
        this.$log.debug(e);
        this.errors = e;
      });
  },

  methods: {
    placeOrder() {
      this.$router.push("/placeOrder");
    },
    rateNow(order) {
      this.$router.push({
        name: "Ratings",
        params: {
          merchantName: order.order.merchant_name,
          merchantID: order.order.merchant_id,
          orderID: order.order.order_id
        }
      });
    }
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>