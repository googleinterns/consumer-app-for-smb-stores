<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <gmap-map :center="center" :zoom="16" style="width:100%;  height: 400px;">
          <gmap-marker
            :key="customer_index"
            v-for="(marker, customer_index) in customer_markers"
            :position="marker.position"
            icon="http://maps.google.com/mapfiles/ms/icons/green-dot.png"
          />

          <gmap-marker
            :key="merchant_index"
            v-for="(marker, merchant_index) in merchant_markers"
            :position="marker.position"
            icon="http://maps.google.com/mapfiles/ms/icons/orange-dot.png"
          />
        </gmap-map>
      </div>
    </div>

    <div
      @click="ItemDetails(merchant)"
      v-for="merchant in merchants"
      class="box"
      v-bind:key="merchant.merchantId"
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
import * as Geofire from "geofire";
import axios from "axios";

export var merchantexp;
export var itemexp;
export var time;
export default {
  name: "merchantList",
  props: ["orderId", "address", "items", "name"],
  components: {
    Logout
  },
  data() {
    return {
      merchants: [],
      timeString: "",
      center: {
        lat: 28.535517,
        lng: 77.391029
      },
      customer_markers: [],
      merchant_markers: [],
      FirebaseRef: null,
      geoFireRef: null,
      geoQuery: null
    };
  },
  mounted() {
    console.log("received orderID", this.orderId);
    console.log(this.$getUserId());
    this.FirebaseRef = firebase.database().ref("MerchantLocation");
    this.geoFireRef = new Geofire.GeoFire(this.FirebaseRef);

    this.geolocate();
  },

  methods: {
    ItemDetails(merchantdetails) {
      console.log(merchantdetails, "from prev page");
      merchantexp = merchantdetails;
      itemexp = merchantdetails.itemDetails;
      time = this.timeString;
      this.$router.push({
        name: "itemDetails",
        params: {
          orderId: this.orderId,
          merchantId: merchantdetails.merchantId
        }
      });
    },
    notifyMerchants() {
      var itemsList = [];
      this.items.forEach(item => {
        itemsList.push({
          product_name: item.item_name,
          quantity: item.item_quantity,
          EAN: item.EAN
        });
      });
      console.log("In notify merchants");
      var customer_name = "";
      if (!firebase.auth().currentUser.isAnonymous) {
        customer_name = firebase.auth().currentUser.displayName;
      } else {
        customer_name = this.name;
      }

      var merchantIDs = [
        "1NIEhX1qQfPZv7oUZnSZjJdCkzf1",
        "VxWQKTpSLLRlsuhQzdb3rapz5zv1",
        "cG4TthNTwwMSbtCeTRZbc38qyVi2"
      ];

      merchantIDs.forEach(mid => {
        axios.post(
          process.env.VUE_APP_MERCHANT_SERVER + "/order/merchant/" + mid,
          {
            oid: this.orderId,
            items: itemsList,
            location: [this.center.lat, this.center.lng],
            customer_name: customer_name,
            customer_address: this.address
          }
        );
      });
    },
    geolocate: function() {
      navigator.geolocation.getCurrentPosition(position => {
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
        const marker = {
          lat: this.center.lat,
          lng: this.center.lng
        };
        this.customer_markers.push({ position: marker });
        this.GeoQueryFn();
      });
    },
    GeoQueryFn() {
      this.geoQuery = this.geoFireRef.query({
        center: [this.center.lat, this.center.lng],
        radius: 1
      });
      var that = this;
      this.geoQuery.on("key_entered", (key, location) => {
        const marker = {
          lat: location[0],
          lng: location[1]
        };
        that.merchant_markers.push({ position: marker });
      });
      this.notifyMerchants();
    }
  },
  created() {
    let dbref = firebase.database();
    var userId = this.$getUserId();
    var mdb = dbref.ref("users/" + userId + "/" + this.orderId + "/merchants");
    mdb.on("child_added", snapshot => {
      var data = snapshot.val();
      this.merchants.push(data);

      var dateObj = new Date(data.deliveryTime * 1000);
      var hours = dateObj.getUTCHours();
      var minutes = dateObj.getUTCMinutes();

      var seconds = dateObj.getSeconds();

      this.timeString =
        hours.toString().padStart(2, "0") +
        ":" +
        minutes.toString().padStart(2, "0") +
        ":" +
        seconds.toString().padStart(2, "0");
      (" hours");
    });
  }
};
</script>

<style  scoped>
.box {
  margin-top: 10px;
}
.bd-lead {
  padding: 0.75rem;
}
</style>
