<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <gmap-map :center="center" :zoom="16" style="width:100%;  height: 400px;">
          <gmap-marker
            :key="index"
            v-for="(marker, index) in customer_markers"
            :position="marker.position"
            icon="http://maps.google.com/mapfiles/ms/icons/green-dot.png"
          />

          <gmap-marker
            :key="index"
            v-for="(marker, index) in merchant_markers"
            :position="marker.position"
            icon="http://maps.google.com/mapfiles/ms/icons/orange-dot.png"
          />
        </gmap-map>
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
            <div
              class="has-text-grey-light"
              v-for="item in merchant.itemDetails"
              v-bind:key="item.key"
            >
              <span v-if="!item.isAvailable">{{item.merchantItemName}} not available</span>
            </div>
            <p class="subtitle is-6 is-pulled-left">
              Delivery in
              <span class="has-text-info">{{timeString}}</span>
            </p>
          </div>
        </div>
        <div id="delivery" class="card">
          <p>Click on merchant to get more details and placing the order</p>
        </div>
      </div>
    </div>

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
import * as Geofire from "geofire";

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
      timeString: "",
      center: {
        lat: null,
        lng: null
      },
      customer_markers: [],
      merchant_markers: [],
      FirebaseRef: null,
      geoFireRef: null,
      geoQuery: null
    };
  },
  mounted() {
    this.FirebaseRef = firebase.database().ref("MerchantLocation");
    this.geoFireRef = new Geofire.GeoFire(this.FirebaseRef);
    this.geolocate();
  },

  methods: {
    ItemDetails(merchantdetails) {
      merchantexp = merchantdetails;
      itemexp = merchantdetails.itemDetails;
      time = this.timeString;
      this.$router.push("/orderDetails");
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

      var seconds = dateObj.getSeconds();


      this.timeString =
        hours.toString().padStart(2, "0") +
        ":" +
        minutes.toString().padStart(2, "0") +

        ":" +
        seconds.toString().padStart(2, "0");
        " hours";

    });
    console.log(this.merchants.length);
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
