<template>
  <div class="container is-fullhd">
    <div v-if="pastOrders.length">
      <section class="section">
      <div v-for="order in pastOrders" v-bind:key="order.orderID">
          <div class="card column">
            <header class="card-header">
              <p class="card-header-title">
                {{order.order.date_of_order.slice(0, 10)}}
              </p>
            </header>
            <div class="card-content">
              <div class="content">
                <p><b>Items:</b> <content v-for="(item, index) in order.allItems" v-bind:key="index">
                  {{item.item_name}} 
                  <content v-if="index != (order.allItems.length-1)">, </content>
                  </content></p>
                <p><b>Status:</b> {{order.order.order_status}}</p>
                <p><b>Delivered By:</b> {{order.order.merchant_name}}</p>
              </div>
            </div>
        </div>
      </div>
      </section>
    </div>
    <div v-else>
      <section class="section">
        <div class="container">
            <img  src="../assets/cart.png" alt="Cart">
            <div class="content">
                <br>
              <p>No orders yet!<br></p>
              <p>Start with creating your order on Order Now!</p> 
            </div>
      </div>
      </section>
    </div>
    <section class="section">
      <div class="container">
      <div class="columns is-mobile is-centered">
        <div class="column is-half">
          <button class="button is-info is-light"><router-link to="/placeOrder">Order Now</router-link></button>
        </div>
      </div>
      </div>
    </section>
  </div>
</template>

<script>
import api from "../Api";
import firebase from "firebase";
export default {
  name: 'PastOrders',

  data: function(){
    return{
      pastOrders:[
      ], 
      response: "",
      errors: ""
    }
  },

  mounted() {
      api.fetchPastOrders(firebase.auth().currentUser.uid)
      .then( response => {
          this.pastOrders = response.data
          this.$log.debug("Orders :", response);
      })
      .catch(e => {
        this.$log.debug(e)
        this.errors = e;
      })
  }
}
</script>
