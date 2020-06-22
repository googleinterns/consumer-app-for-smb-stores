<template>
  <div>
    <Logout />
    <div class="container">
      <div class="box">
        <article class="media">
          <br />
          <div class="media-left">
            <i class="fa fa-user-circle is-size-3" aria-hidden="true"></i>
          </div>
          <div class="media-content">
            <div class="content">
              <strong class="is-size-5">{{merchantvalue.merchantName}}</strong>
              <br />
              <div class="is-size-6">{{merchantvalue.merchantAddress}}</div>
              <br />
            </div>
          </div>
        </article>
        <nav class="level is-mobile">
          <div class="level-left">
            <strong class="is-size-6.5">Order Price: ₹ {{merchantvalue.totalPrice}}</strong>
          </div>
          <button
            class="button is-success is-rounded is-focussed is-pulled-right"
            @click="confirm()"
          >Confirm Order</button>
        </nav>
      </div>
      <div id="itemslist" v-for="item in itemvalues" v-bind:key="item.key">
        <div v-if="item.isAvailable" class="box">
          <article class="media">
            <div class="media-left">
              <figure class="image is-48x48">
                <img :src="item.imageURL" alt="Placeholder image" />
              </figure>
            </div>
            <div class="media-content">
              <div class="content">
                <div class="has-text-grey is-size-6.5">
                  <strong class="is-size-5">{{item.merchantItemName}}</strong>
                  <strong class="has-text-grey is-size-5">(Qty: {{item.quantity}})</strong>
                </div>

                <div class="has-text-grey is-size-6.5">
                  Price:
                  <strong class="is-size-6.5 is-pulled-right">₹ {{item.unitPrice}}</strong>
                </div>
                <strong class="has-text-grey is-size-6.5">
                  Total Price:
                  <strong
                    class="is-size-6.5 is-pulled-right"
                  >₹ {{item.unitPrice*item.quantity}}</strong>
                </strong>
              </div>
            </div>
          </article>
        </div>
      </div>
      <div class="card" id="bottom">
        <p id="time">
          Estimated time for delivery to your location is
          <span
            class="has-text-info"
          >{{deliveryTime}}</span>
        </p>
      </div>
    </div>
    <div v-if="collectContact">
      <div class="field is-grouped">
        <p class="control is-expanded">
          <input class="input" type="text" v-model="contactNo" placeholder="Enter Phone No." />
        </p>
        <p class="control">
          <a class="button is-info" v-on:click="confirm()">OK</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
import axios from "axios";
import api from "../Api";
import firebase from "firebase";

export default {
  props: ["orderId", "merchantId"],
  name: "itemDetails",
  components: {
    Logout
  },
  data() {
    return {
      merchantvalue: [],
      itemvalues: [],
      deliveryTime: "",
      collectContact: false,
      contactNo: 9354682711
    };
  },
  methods: {
    confirm() {
      axios.post(
        process.env.VUE_APP_MERCHANT_SERVER +
          "order/confirm/merchant/" +
          this.merchantId,
        {
          oid: this.orderId,
          customer_contact_no: this.contactNo
        }
      );

      let confirmedItems = [];

      this.itemvalues.forEach(item => {
        confirmedItems.push({
          item_name: item.merchantItemName,
          quantity: item.quantity,
          unit_price: item.unitPrice
        });
        this.removeItemFromCart(item.merchantItemName);
      });

      api.updateOrderStatusToProcessing(
        this.$getUserId(),
        this.orderId,
        this.merchantvalue.merchantName,
        this.merchantvalue.merchantAddress,
        "Free Delivery",
        confirmedItems
      );
      this.$router.push({
        name: "OrderConfirmation",
        query: {
          merchantName: this.merchantvalue.merchantName,
          time: this.deliveryTime
        }
      });
    },

    removeItemFromCart(item_name) {
      var userId = this.$getUserId();
      var itemKeyFound = "";
      var reference = firebase
        .database()
        .ref("user_cart/" + userId + "/")
        .orderByChild("item_name")
        .equalTo(item_name);
      reference.on("value", function(cartItem) {
        cartItem.forEach(function(data) {
          console.log(item_name, data.key);
          itemKeyFound = data.key;
        });
      });

      if (itemKeyFound.length != 0) {
        console.log(item_name);
        firebase
          .database()
          .ref()
          .child("user_cart/" + userId + "/" + itemKeyFound)
          .remove();
      }
    }
  },
  created() {
    let dbref = firebase.database();
    var userId = this.$getUserId();
    var mdb = dbref.ref(
      "users/" + userId + "/" + this.orderId + "/merchants/" + this.merchantId
    );

    mdb.on("value", snapshot => {
      var data = snapshot.val();
      console.log(data);
      this.merchantvalue = data;
      var time = data.deliveryTime;
      time = parseInt(time / 60);
      if (parseInt(time / 60) === 0) {
        if (parseInt(time % 60) === 1)
          this.deliveryTime += parseInt(time % 60) + " min";
        else this.deliveryTime += parseInt(time % 60) + " mins";
      } else {
        if (parseInt(time / 60) < 2)
          this.deliveryTime += parseInt(time / 60) + " hour ";
        else this.deliveryTime += parseInt(time / 60) + " hours ";
        if (parseInt(time % 60) !== 0) {
          if (time % 60 === 1)
            this.deliveryTime += parseInt(time % 60) + " min";
          else this.deliveryTime += parseInt(time % 60) + " mins";
        }
      }
      this.itemvalues = data.itemDetails;
    });
  }
};
</script>

<style  scoped>
.container {
  margin-top: 50px;
}
#bottom {
  flex-flow: column;
  display: flex;
}
#bot {
  margin-top: 10px;
  margin-bottom: 50px;
}
#time {
  margin-top: 15px;
}
</style>
