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
            class="button is-success is-focussed is-pulled-right"
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
                  <strong class="has-text-grey is-size-5"> x {{item.quantity}}</strong>
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
          Estimated delivery time:
          <strong>{{deliveryTime}}</strong>
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
import { itemexp } from "./merchantsList.vue";
import { merchantexp } from "./merchantsList.vue";
import { time } from "./merchantsList.vue";
import Logout from "@/components/Logout.vue";
import axios from "axios";
import api from "../Api";

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
      });

      api.updateOrderStatusToProcessing(
        this.$getUserId(),
        this.orderId,
        this.merchantvalue.merchantId,
        this.merchantvalue.merchantName,
        "Nehru Market, FNG Road, Sector 115, Noida",
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
    }
  },
  mounted() {
    this.merchantvalue = merchantexp;
    this.itemvalues = itemexp;
    this.deliveryTime = time;
    console.log(this.merchantvalue, "This is merchantvalue");
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
