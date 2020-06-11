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
    <gmap-map :center="center" :zoom="17" style="width:100%;  height: 400px;">
      <gmap-marker
        :key="index"
        v-for="(m, index) in markers"
        :position="m.position"
        icon="http://maps.google.com/mapfiles/ms/icons/green-dot.png"
      />

      <gmap-marker
        :key="index2"
        v-for="(m, index2) in markers2"
        :position="m.position"
        icon="http://maps.google.com/mapfiles/ms/icons/orange-dot.png"
      />
    </gmap-map>
  </div>
</template>

<script>
import firebase from "firebase";
import Logout from "@/components/Logout.vue";
import * as Geofire from "geofire";
var j;

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
      center: { lat: 45.508, lng: -73.587 },
      markers: [],
      markers2: [],
      places: [],
      currentPlace: null,
      MerchantLocations: [
        [19.07627, 72.8776452],
        [19.076348, 72.8742348],
        [19.0786873, 72.87],
        [19.0726378, 72.87],
        [19.07987573, 72.8765839],
        [19.038475, 72.875739],
        [19.0596, 72.8295],
        [19.2307, 72.8567],
        [19.07609, 72.877426],
        [28.610001, 77.230003],
        [19.2183, 72.9781],
        [19.0279532, 72.88076479],
        [19.02887438, 72.88079917],
        [19.02777, 72.8804372],
        [19.0299372, 72.880763762],
        [19.0286372, 72.88057482],
        [27.2038, 77.5011],
        [19.12399 , 72.9092],
        [19.1234, 72.914271],
        [19.1235,72.9092]
      ],
      FirebaseRef: null,
      geoFireRef: null,
      geoQuery: null
    };
  },
  mounted() {
    this.FirebaseRef = firebase
      .database()
      .ref()
      .push();
    this.geoFireRef = new Geofire.GeoFire(this.FirebaseRef);

    for (j = 0; j < this.MerchantLocations.length; j++) {
      this.geoFireRef.set("Merchant" + j, this.MerchantLocations[j]);
    }

    this.geolocate();
  },

  methods: {
    ItemDetails(merchantdetails) {
      merchantexp = merchantdetails;
      itemexp = merchantdetails.itemDetails;
      time = this.timeString;
      this.$router.push("/orderDetails");
    },
    addMarker() {
      var i;
      for (i = 0; i < this.MerchantLocations.length; i++) {
        this.currentPlace = this.MerchantLocations[i];
        const marker = {
          lat: this.currentPlace[0],
          lng: this.currentPlace[1]
        };
        this.markers.push({ position: marker });
      }
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
        this.markers.push({ position: marker });
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
        console.log("Entered " + key + " with location " + location);
        const marker = {
          lat: location[0],
          lng: location[1]
        };
        that.markers2.push({ position: marker });
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
      console.log(this.merchants);

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
