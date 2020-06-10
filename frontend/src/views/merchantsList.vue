<template>
<div>
  <Logout />
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
        <div class="has-text-grey-light" v-for="item in merchant.itemDetails" v-bind:key="item.key">
          <span v-if="!item.isAvailable">{{item.merchantItemName}} not available</span>
        </div>
        <p class="subtitle is-6 is-pulled-left">Delivery in <span class="has-text-info">{{timeString}}</span></p>
      </div>
    </div>
    <div id="delivery" class="card">
      <p>Click on merchant to get more details and placing the order</p>
    </div>
  </div>
  </div>
</template>

<script>
import { appfirebase } from "../main.js";
import firebase from "firebase";
import Logout from "@/components/Logout.vue";

export var merchantexp;
export var itemexp;
export var time;

export default {
  name: "merchantList",
  data() {
    return {
      merchants: [],timeString:""
    };
  },
  methods: {
    ItemDetails(merchantdetails) {
      merchantexp = merchantdetails;
      itemexp = merchantdetails.itemDetails;
      time=this.timeString;
      this.$router.push("/orderDetails");
    }
  },
  created() {
    let dbref = appfirebase.database();
    var userId = firebase.auth().currentUser.uid;
    var mdb = dbref.ref("users/" + userId + "/Order1/merchants");
    mdb.on("child_added", snapshot => {
      var data = snapshot.val();
      this.merchants.push(data);
      console.log(this.merchants);

      var dateObj = new Date(data.deliveryTime * 1000); 
           var hours = dateObj.getUTCHours(); 
           var minutes = dateObj.getUTCMinutes(); 
           var seconds = dateObj.getSeconds(); 
  
          this.timeString = hours.toString().padStart(2, '0') 
                + ':' + minutes.toString().padStart(2, '0') 
                + ':' + seconds.toString().padStart(2, '0'); 
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
