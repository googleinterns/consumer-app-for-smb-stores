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
            <tab /> <StarRating
              :inline="true"
              inactive-color="#FFFFFF"
              active-color="#008CBA"
              v-bind:border-width="2"
              :padding="1"
              :glow="2"
              :star-size="20"
              border-color="	#008CBA"
              glow-color="#008CBA"
              :rounded-corners="true"
              :read-only="true"
              :rating="merchant.rating"
              :show-rating="false"
            ></StarRating>
            <br />
            <StarRating
              :inline="true"
              inactive-color="#FFFFFF"
              active-color="#008CBA"
              v-bind:border-width="2"
              :padding="1"
              :glow="2"
              :star-size="20"
              border-color="	#008CBA"
              glow-color="#008CBA"
              :rounded-corners="true"
              :read-only="true"
              :rating="merchant.rating"
              :show-rating="false"
            ></StarRating>
            <br />
            <span class="is-size-6">{{merchant.merchantAddress}}</span>
            <div
              class="content has-text-danger is-size-6"
              v-for="item in merchant.itemDetails"
              v-bind:key="item.key"
            >
              <div></div>
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
          <strong
            class="is-pulled-right"
            style="color: #162ac9"
          >Delivery in {{merchant.deliveryTime}}</strong>
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
import StarRating from "vue-star-rating";

export default {
  name: "merchantList",
  props: ["orderId", "address", "cust_name"],
  components: {
    Logout,
    StarRating
  },
  data() {
    return {
      merchants: [],
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
   // this.notification();
    this.geolocate();
  },
  methods: {
    ItemDetails(merchantdetails) {
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
        "VxWQKTpSLLRlsuhQzdb3rapz5zv1",
        // "1NIEhX1qQfPZv7oUZnSZjJdCkzf1",
         "cG4TthNTwwMSbtCeTRZbc38qyVi2",
        "0HTBsSc4x0Zi6W6rGiVE6ItIUgA2"
      ];

      var customer_name = "";

      customer_name = self.cust_name;

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
    // notification() {
    //   // const messaging = firebase.messaging();
    //   // messaging.usePublicVapidKey(
    //   //    "BLV4FVm9jWAeO7zYhfJLrvcWgbXr1ewHnQCLxmfg0DZDdvXvZ2mAjFyGW5A6Y9WWyz2sSBqseMBj_zQHrolEmv0",
    //   // );
    //   firebase
    //     .database()
    //     .ref("newNotifications/" + this.$getUserId() + "/packageDispatched")
    //     // .set({
    //     //   trigger: 0
    //     //  }),
    //     // firebase
    //     //   .database()
    //     //   .ref("newNotifications/" + this.$getUserId() + "/packageDelivered")
    //     //   .on("child_added",snapshot=>{
    //     //     console.log(snapshot.val());
    //     //   })
    //     firebase
    //       .database()
    //       .ref("newNotifications/" + this.$getUserId() + "/packageDispatched")
    //       .on("child_changed", function(snapshot) {
    //         console.log(snapshot);
    //         if (Notification.permission == "granted") {
    //           navigator.serviceWorker.getRegistration().then(function(reg) {
    //             var options = {
    //               icon:
    //                 "https://kirana-g.web.app/img/google-logo-png.0fa3fe04.png",
    //               vibrate: [100, 50, 100],
    //               data: {
    //                 dateOfArrival: Date.now(),
    //                 primaryKey: 1
    //               }
    //               // actions: [
    //               //   {
    //               //     action: "explore",
    //               //     title: "Explore this new world",
    //               //     icon: "images/checkmark.png"
    //               //   },
    //               //   {
    //               //     action: "close",
    //               //     title: "Close notification",
    //               //     icon: "images/xmark.png"
    //               //   }
    //               // ]
    //             };
    //             reg.showNotification(
    //               "Your order has been dispatched!",
    //               options
    //             );
    //           });
    //         }
    //       }),
    //     firebase
    //       .database()
    //       .ref("newNotifications/" + this.$getUserId() + "/packageDelivered")
    //       .on("child_changed", function(snapshot) {
    //         console.log(snapshot);

    //         if (Notification.permission == "granted") {
    //           navigator.serviceWorker.getRegistration().then(function(reg) {
    //             var options = {
    //               icon:
    //                 "https://kirana-g.web.app/img/google-logo-png.0fa3fe04.png",
    //               vibrate: [100, 50, 100],
    //               data: {
    //                 dateOfArrival: Date.now(),
    //                 primaryKey: 1
    //               }
    //               // actions: [
    //               //   {
    //               //     action: "explore",
    //               //     title: "Explore this new world",
    //               //     icon: "images/checkmark.png"
    //               //   },
    //               //   {
    //               //     action: "close",
    //               //     title: "Close notification",
    //               //     icon: "images/xmark.png"
    //               //   }
    //               // ]
    //             };
    //             reg.showNotification("Your order has been delivered!", options);
    //           });
    //         }

    //         //   if (Notification.permission == "granted") {
    //         //   navigator.serviceWorker.getRegistration().then(function(reg) {
    //         //     reg.showNotification("Your order has been delivered!");
    //         //   });
    //         // }
    //       });
    // },

    // notification() {
    //   const messaging = firebase.messaging();
    //   messaging.usePublicVapidKey(
    //      "BLV4FVm9jWAeO7zYhfJLrvcWgbXr1ewHnQCLxmfg0DZDdvXvZ2mAjFyGW5A6Y9WWyz2sSBqseMBj_zQHrolEmv0"
    //   );
    //   messaging
    //     .requestPermission()
    //     .then(() => {
    //       console.log("Notification permission granted.");
    //       messaging.getToken().then(token => {
    //         console.log(token);
    //         firebase
    //           .database()
    //           .ref("Notifications/" + this.$getUserId() + "/dispatchedToken")
    //           .set({
    //             value: token,
    //             packageDispatchedTrigger: 0,
    //           });
    //           firebase
    //           .database()
    //           .ref("Notifications/" + this.$getUserId() + "/deliveredToken")
    //           .set({
    //             value: token,
    //             packageDeliveredTrigger: 0,
    //           });
    //       });
    //     })
    //     .catch(err => {
    //       console.log("Unable to get permission to notify.", err);
    //     });
    //   console.log("mine "+this.$getUserId())
    //   var that=this
    //   firebase
    //     .database()
    //     .ref("Notifications/" + this.$getUserId() + "/dispatchedToken")
    //     .on("child_changed", function(snapshot) {
    //       var trigger = snapshot.val();
    //       console.log(trigger);
    //       var to;
    //       firebase
    //         .database()
    //         .ref("Notifications/" + that.$getUserId() + "/dispatchedToken/value")
    //         .once("value")
    //         .then(function(snapshot) {
    //           to = snapshot.val();

    //           var notification = {
    //             title: "SMB Kirana Consumer",
    //             body: "Order has been dispatched!",
    //             icon: "https://kirana-g.web.app/img/google-logo-png.0fa3fe04.png", //DO Something
    //             click_action: "http://localhost:8081" //DO Something
    //           };

    //           fetch("https://fcm.googleapis.com/fcm/send", {
    //             method: "POST",
    //             headers: {
    //               "Authorization":"key=AAAALWfjInI:APA91bHzhzEPoJ6393OxUL8FqnW_9razZGHNaLhJcCWwzwB1p2ZtsZYtHTQM35BYxi07Px80k47dP82ufR4UDcypkkMntNvGJ8jyucgWaw3qCj5Bip-KYgOncmPFbm-MrnKKAwcCtOYC",
    //               "Content-Type": "application/json"
    //             },
    //             body: JSON.stringify({
    //               notification: notification,
    //               to: to
    //             })
    //           })
    //             .then(function(response) {
    //               console.log(response);
    //             })
    //             .catch(function(error) {
    //               console.error(error);
    //             });
    //         });
    //     });

    //       firebase
    //     .database()
    //     .ref("Notifications/" + this.$getUserId() + "/deliveredToken")
    //     .on("child_changed", function(snapshot) {
    //       var trigger = snapshot.val();
    //       console.log(trigger);
    //       var to;
    //       firebase
    //         .database()
    //         .ref("Notifications/" + that.$getUserId() + "/deliveredToken/value")
    //         .once("value")
    //         .then(function(snapshot) {
    //           to = snapshot.val();

    //           var notification = {
    //             title: "SMB Kirana Consumer",
    //             body: "Order has been delivered!",
    //             icon: "https://kirana-g.web.app/img/google-logo-png.0fa3fe04.png", //DO Something
    //             click_action: "http://localhost:8081" //DO Something
    //           };

    //           fetch("https://fcm.googleapis.com/fcm/send", {
    //             method: "POST",
    //             headers: {
    //               "Authorization":
    //                 "key=AAAALWfjInI:APA91bHzhzEPoJ6393OxUL8FqnW_9razZGHNaLhJcCWwzwB1p2ZtsZYtHTQM35BYxi07Px80k47dP82ufR4UDcypkkMntNvGJ8jyucgWaw3qCj5Bip-KYgOncmPFbm-MrnKKAwcCtOYC",
    //               "Content-Type": "application/json"
    //             },
    //             body: JSON.stringify({
    //               notification: notification,
    //               to: to
    //             })
    //           })
    //             .then(function(response) {
    //               console.log(response);
    //             })
    //             .catch(function(error) {
    //               console.error(error);
    //             });
    //         });
    //     });

    //   messaging.onMessage(payload => {
    //     console.log("Message received. ", payload);
    //   });
    // },

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
      if (this.prevRoute.path == "/userDetails/" + this.orderId) {
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
      console.log(data);

      var time = data.deliveryTime;
      time = parseInt(time / 60);
      if (parseInt(time / 60) == 0) {
        if (time % 60 == 1) data.deliveryTime = parseInt(time % 60) + " min";
        else data.deliveryTime = parseInt(time % 60) + " mins";
      } else {
        if (parseInt(time / 60) < 2)
          data.deliveryTime = parseInt(time / 60) + " hour ";
        else data.deliveryTime = parseInt(time / 60) + " hours ";
        if (time % 60 != 0) {
          if (time % 60 == 1) data.deliveryTime += parseInt(time % 60) + " min";
          else data.deliveryTime += parseInt(time % 60) + " mins";
        }
      }
      // var that=this;

      var rdb = dbref.ref("MerchantRatings/" + data.merchantId);
      rdb.once("value").then(snapshot => {
        // var orderref=dbref.ref("MerchantRatings/"+data.merchantId+"/Number_of_Orders");

        var d = snapshot.val();
        var Quality = d.TotalRatings.Quality;
        var Timeliness = d.TotalRatings.Timeliness;
        var ordersNum = d.Number_of_Orders.count;
        var timeAvg = parseFloat(Quality) / parseFloat(ordersNum);
        var qualityAvg = parseFloat(Timeliness) / parseFloat(ordersNum);
        var finalRating = (timeAvg + qualityAvg) / 2;
        console.log(data);
        data.rating = finalRating;
      });
      console.log(data);
      this.merchants.push(data);
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
