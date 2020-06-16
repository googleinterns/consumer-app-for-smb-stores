<template>
  <div class="bd-lead">
    <div v-if="pastOrders.length">
      <div class="columns is-mobile">
        <div class="column">
          <h1 class="title is-4" align="left">Order History</h1>
        </div>
        <div class="column" align="right">
          <button class="button is-info is-light">
            <router-link to="/placeOrder">Order Now</router-link>
          </button>
        </div>
      </div>
      <div>
        <div class="panel" v-for="order in pastOrders" v-bind:key="order.orderID">
          <div class="card">
            <div class="card-content">
              <div class="content">
                <p align="left">
                  <b>Ordered On:</b>
                  {{order.order.date_of_order | formatDate}}
                </p>
                <p
                  :style="{color: order.order.order_status == 'COMPLETED' ? 'green':'red'}"
                  align="left"
                >
                  <b>Status:</b>
                  {{order.order.order_status}}
                </p>
                <p align="left">
                  <b>Served By:</b>
                  {{order.order.merchant_name}}, {{order.order.merchant_address}}
                </p>
                <p align="left">
                  <b>Offers Availed:</b>
                  {{order.order.offers_availed}}
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
                      <th>Total:</th>
                      <th align="center">{{order.total}}</th>
                    </tfoot>
                    <tr v-for="(item, index) in order.all_items" v-bind:key="index">
                      <td>
                        {{item.item_name}}
                        <div v-if="item.quantity > 1">
                          <small>
                            <strong>Qty:</strong>
                            {{item.quantity}}
                          </small>
                        </div>
                      </td>
                      <td align="center">{{item.quantity * item.unit_price}}</td>
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
        <img src="../assets/cart.png" alt="Cart" />
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
</template>

<script>
import api from "../Api";
export default {
  name: "PastOrders",

  data: function() {
    return {
      pastOrders: "",
      response: "",
      errors: ""
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
      })
      .catch(e => {
        this.$log.debug(e);
        this.errors = e;
      });
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>