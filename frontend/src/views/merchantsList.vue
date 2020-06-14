<template>
  <div>
    <Logout />
    <div
      @click="ItemDetails(merchant)"
      v-for="merchant in merchants"
      class="box"
      v-bind:key="merchant.key"
    >
      <article class="media">
        <div class="media-left">
          <i class="fa fa-user-circle is-size-3" aria-hidden="true"></i>
        </div>
        <div class="media-content">
          <strong class="is-size-5">{{merchant.merchantName}}</strong>
          <span class="is-size-6">,{{merchant.merchantAddress}}</span>
          <div></div>
          <div
            class="content has-text-danger is-size-6"
            v-for="item in merchant.itemDetails"
            v-bind:key="item.key"
          >
            <p>
              <small v-if="!item.isAvailable">{{item.merchantItemName}} not available</small>
            </p>
          </div>
        </div>
      </article>
      <nav class="level is-mobile">
        <div class="level-left">
          <strong class="has-text-danger is-size-6">Total Price: {{merchant.totalPrice}} ₹</strong>
        </div>
        <strong class="has-text-success is-pulled-right">Delivery in {{timeString}}</strong>
      </nav>
    </div>
    <div class="box">
      <strong>Click on merchant to get more details and placing the order</strong>
    </div>
  </div>
</template>

<script>
import firebase from "firebase";
import Logout from "@/components/Logout.vue";

export var merchantexp;
export var itemexp;
export var time;

export default {
  name: "merchantList",
  components: {
    Logout
  },
  data() {
    return {
      merchants: [],
      timeString: ""
    };
  },
  methods: {
    ItemDetails(merchantdetails) {
      merchantexp = merchantdetails;
      itemexp = merchantdetails.itemDetails;
      time = this.timeString;
      this.$router.push("/orderDetails");
    }
  },
  created() {
    let dbref = firebase.database();
    var userId = firebase.auth().currentUser.uid;
    var mdb = dbref.ref("users/" + userId + "/Order1/merchants");
    mdb.on("child_added", snapshot => {
      var data = snapshot.val();
      this.merchants.push(data);

      var dateObj = new Date(data.deliveryTime * 1000);
      var hours = dateObj.getUTCHours();
      var minutes = dateObj.getUTCMinutes();

      this.timeString =
        hours.toString().padStart(2, "0") +
        ":" +
        minutes.toString().padStart(2, "0") +
        " hours";
    });
  }
};
</script>

<style  scoped>
.box {
  margin-top: 10px;
}
</style>
