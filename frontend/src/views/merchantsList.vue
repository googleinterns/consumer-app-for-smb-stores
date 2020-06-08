<template>
  <div class="container">
    <div
      @click="ItemDetails(merchant)"
      v-for="merchant in merchants"
      class="card"
      v-bind:key="merchant.key"
    >
      <div class="card-content">
        <div class="media">
          <div class="media-content">
            <p class="title is-4 has-text-info is-size-2.5">
              {{merchant.merchantName}}
              <span class="is-pulled-right">{{merchant.totalPrice}} ₹</span>
            </p>
          </div>
        </div>
        <div class="has-text-grey-light" v-for="item in merchant.items" v-bind:key="item.key">
          <span v-if="!item.isAvailable">{{item.itemName}} not available</span>
        </div>
        <p class="subtitle is-6 is-pulled-left">Delivery in {{merchant.deliveryTime}}</p>
      </div>
    </div>
    <div id="delivery" class="card">
      <p>Click on merchant to get more details and placing the order</p>
    </div>
  </div>
</template>

<script>
import { appfirebase } from "../main.js";
import firebase from "firebase";

export var merchantexp;
export var itemexp;

export default {
  name: "merchantList",
  data() {
    return {
      merchants: []
    };
  },
  methods: {
    ItemDetails(merchantdetails) {
      merchantexp = merchantdetails;
      itemexp = merchantdetails.items;
      this.$router.push("/details");
    }
  },
  created() {
    let dbref = appfirebase.database();
    var userId = firebase.auth().currentUser.uid;
    console.log(userId);
    var mdb = dbref.ref("users/" + userId + "/Order1/merchants");
    //console.log(mdb);
    mdb.on("child_added", snapshot => {
      var data = snapshot.val();
      this.merchants.push(data);
      console.log(data);
    });
  }
};
</script>

<style  scoped>
.container {
  margin-top: 50px;
}
.card-content {
  margin-bottom: 0px;
}
#delivery {
  min-height: 30px;
  margin-top: 2px;
}
</style>
