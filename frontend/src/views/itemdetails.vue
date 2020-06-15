<template>
  <div>
    <Logout />
    <div class="container">
      <h1 class="title">Order Details</h1>
      <div class="card-content">
        <div class="media">
          <div class="media-content">
            <p class="title is-4 has-text-info is-size-4">
              {{merchantvalue.merchantName}}
              <span
                class="is-pulled-right"
              >{{merchantvalue.totalPrice}} ₹</span>
            </p>
            <div>
              <span class="icon">
                <i class="fas fa-home has-text-info"></i>
              </span>
              {{merchantvalue.merchantAddress}}
            </div>
            <div>Discount offer for: {{merchantvalue.offers}} ₹</div>
          </div>
        </div>
      </div>
      <div id="itemslist" v-for="item in itemvalues" class="card" v-bind:key="item.key">
        <div v-if="item.isAvailable" class="card-content">
          <div class="media">
            <div class="media-content">
              <p class="title is-4 has-text-info is-size-5">
                {{item.merchantItemName}}
                <span class="is-pulled-right">{{item.unitPrice}} ₹</span>
              </p>
            </div>
          </div>
          <div>
            <div class="content has-text-grey-light is-pulled-left">quantity : {{item.quantity}}</div>
          </div>
        </div>
      </div>
      <div class="card" id="bottom">
        <p id="time">
          Estimated time for delivery to your location is
          <span
            class="has-text-info"
          >{{deliveryTime}}</span>
        </p>

        <div class="content has-text-centered">
          <button
            id="bot"
            @click="confirm"
            class="button is-success is-large"
          >Confirm Order</button>
        
        </div>
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
