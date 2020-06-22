<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <gmap-map :center="center" :zoom="15" style="width:100%;  height: 400px;">
          <gmap-marker
            :position="center"
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

    <div v-if="merchants.length == 0">
      <div class="card">
        <div class="card-content">
          <span>
            <i class="fa fa-spinner fa-spin" style="font-size:40px"></i>
          </span>
          <p>We are waiting for bids</p>
        </div>
      </div>
    </div>

    <div v-if="merchants.length>0" class="box">
      <strong>Click on Merchant for Details and to Place Order</strong>
    </div>
    <div v-if="merchants.length>0">
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
            <br />
            <span class="is-size-6">{{merchant.merchantAddress}}</span>
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
            <strong class="is-size-6" style="color: #162ac9">Total Price: ₹ {{merchant.totalPrice}}</strong>
          </div>
          <strong class="is-pulled-right" style="color: #162ac9">Delivery in {{timeString}}</strong>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import firebase from "firebase";
import Logout from "@/components/Logout.vue";
import * as Geofire from "geofire";
import axios from "axios";
import api from "../Api";

export var merchantexp;
export var itemexp;
export var time;
export default {
  name: "merchantList",
  props: ["orderId", "address", "cust_name"],
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
      geoQuery: null,
      prevRoute: null,
      itemsInCart: []
    };
  },

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.prevRoute = from;
    });
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

    getCartItems() {
      var userId = this.$getUserId();
      let self = this;
      var merchantIDs = [
        "VxWQKTpSLLRlsuhQzdb3rapz5zv1"
        // "1NIEhX1qQfPZv7oUZnSZjJdCkzf1",
        // "cG4TthNTwwMSbtCeTRZbc38qyVi2",
        // "0HTBsSc4x0Zi6W6rGiVE6ItIUgA2"
      ];

      var customer_name = "";
      if (!firebase.auth().currentUser.isAnonymous) {
        customer_name = firebase.auth().currentUser.displayName;
      } else {
        customer_name = self.cust_name;
      }

      api.fetchItemsForAnOrder(self.orderId).then(response => {
        response.data.forEach(item => {
          if (item.EAN != null) {
            self.itemsInCart.push({
              product_name: item.item_name,
              quantity: item.quantity,
              EAN: item.EAN
            });
          } else {
            self.itemsInCart.push({
              product_name: item.item_name,
              quantity: item.quantity
            });
          }
        });
        merchantIDs.forEach(mid => {
          axios.post(
            process.env.VUE_APP_MERCHANT_SERVER + "/order/merchant/" + mid,
            {
              oid: self.orderId,
              items: self.itemsInCart,
              location: [self.center.lat, self.center.lng],
              customer_name: customer_name,
              customer_address: self.address,
              user_id: userId
            }
          );
        });
      });
    },

    notifyMerchants() {
      this.getCartItems();
    },
    geolocate: function() {
      navigator.geolocation.getCurrentPosition(position => {
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
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
      if (this.prevRoute.path == "/placeOrder") {
        this.notifyMerchants();
      }
    }
  },
  created() {
    let dbref = firebase.database();
    var userId = this.$getUserId();
    var mdb = dbref.ref("users/" + userId + "/" + this.orderId + "/merchants");
    mdb.on("child_added", snapshot => {
      var data = snapshot.val();
      this.merchants.push(data);

      var time = data.deliveryTime;
      time = time / 60;
      if (time / 60 == 0) {
        this.timeString = (time % 60) + "mins";
      } else {
        if (time / 60 < 2) this.timeString = parseInt(time / 60) + " hour ";
        else this.timeString = parseInt(time / 60) + " hours ";
        if (time % 60 != 0) this.timeString += (time % 60) + " mins";
      }
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
