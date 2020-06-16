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
              <div class="is-size-6">{{merchantvalue.merchantAddress}}</div>
              <br />
            </div>
          </div>
        </article>
        <nav class="level is-mobile">
          <div class="level-left">
            <strong class="has-text-danger is-size-6.5">Order Price: ₹ {{merchantvalue.totalPrice}}</strong>
          </div>
          <button
            class="button is-success is-rounded is-focussed is-pulled-right"
            @click="confirm"
          >Confirm Order</button>
        </nav>
      </div>
      <div id="itemslist" v-for="item in itemvalues" v-bind:key="item.key">
        <div v-if="item.isAvailable" class="box">
          <article class="media">
            <div class="media-content">
              <div class="content">
                <strong class="is-size-5">{{item.merchantItemName}}</strong>
                <div class="has-text-grey is-size-6.5">
                  Price:
                  <strong
                    class="has-text-danger is-size-6.5 is-pulled-right"
                  >₹ {{item.unitPrice}}</strong>
                </div>
                <div class="has-text-grey is-size-6.5">
                  Qty:
                  <strong class="has-text-danger is-size-6. is-pulled-right">{{item.quantity}}</strong>
                </div>
                <strong class="has-text-grey is-size-6.5">
                  Total Price:
                  <strong
                    class="has-text-danger is-size-6.5 is-pulled-right"
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
  </div>
</template>

<script>
import { itemexp } from "./merchantsList.vue";
import { merchantexp } from "./merchantsList.vue";
import { time } from "./merchantsList.vue";
import Logout from "@/components/Logout.vue";

export default {
  name: "itemDetails",
  components: {
    Logout
  },
  data() {
    return {
      merchantvalue: [],
      itemvalues: [],
      deliveryTime: ""
    };
  },
  methods: {
    confirm() {
      this.$router.push("home");
    }
  },
  mounted() {
    this.merchantvalue = merchantexp;
    this.itemvalues = itemexp;
    this.deliveryTime = time;
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
