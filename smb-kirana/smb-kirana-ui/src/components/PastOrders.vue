<template>
  <div class="container is-fullhd">
    <div v-if="pastOrders.length">
      <section class="section">
      <div v-for="order in pastOrders" v-bind:key="order.orderID">
          <div class="card column">
            <header class="card-header">
              <p class="card-header-title">
                {{order.date}}
              </p>
            </header>
            <div class="card-content">
              <div class="content">
                <p><b>Items:</b> <content v-for="(item, index) in order.items" v-bind:key="item">
                  {{item}}
                  <content v-if="index != (order.items.length-1)">, </content> 
                  </content></p>
                <p><b>Total Cost:</b> {{order.totalCost}}</p>
                <p><b>Delivered By:</b> {{order.servedBy}}</p>
              </div>
            </div>
            <!-- <footer class="card-footer">
              <a href="#" v-on:click="reorder(order)" class="card-footer-item">
                <router-link to="/placeOrder">Reorder</router-link>
              </a>
            </footer> -->
        </div>
      </div>
      </section>
    </div>
    <div v-else>
      <section class="section">
        <div class="container">
        <div class="columns is-mobile is-centered">
          <div class="column is-half">
          <img class="image" src="../assets/cart.png" alt="Cart">
          <div class="content">
              <br>
            <p>No orders yet!<br></p>
            <p>Start with creating your order on Order Now!</p> 
          </div>
          </div>
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
export default {
  name: 'PastOrders',
  methods: {
    reorder(orderDetails){
      let itemsInCart = []

      orderDetails.items.array.forEach(element => {
        itemsInCart.push({name: element, quantity: 1})
      });
    }
  },
  data: function(){
    return{
      pastOrders:[
        {
          orderID: 1,
          date: "18-05-2020",
          items: ["Britania", "Rice", "Dal"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 2,
          date: "10-05-2020",
          items: ["Rice", "Dal"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 3,
          date: "18-04-2020",
          items: ["Surf Excel", "Dal"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 4,
          date: "18-03-2020",
          items: ["Cookies", "Dal"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 5,
          date: "18-02-2020",
          items: ["Rice"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 6,
          date: "18-02-2020",
          items: ["Rice"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
        {
          orderID: 7,
          date: "18-02-2020",
          items: ["Rice"],
          totalCost: 250,
          servedBy: "Merchant-A"
        },
         {
          orderID: 8,
          date: "18-02-2020",
          items: ["Rice"],
          totalCost: 250,
          servedBy: "Merchant-A"
        }
      ]
    }
  }
}
</script>
