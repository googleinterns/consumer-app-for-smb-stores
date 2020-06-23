<template>
  <div>
    <Logout />
    <div>
      <div class="container">
        <div class="card is-shadowless">
          <div class="card-content">
            <div class="title is-5">
              <div>Your order has been confirmed</div>
              <br />
              <div>{{merchantName}} will deliver your order in estimated time of {{time}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="column has-text-centered">
      <p>
        <button
          class="button is-info"
          v-on:click="goToOrderHistory()"
          type="submit"
          value="Order History"
        >
          <span>
            <i class="fa fa-book" aria-hidden="true"></i> Order History
          </span>
        </button>&nbsp;
        <button
          class="button is-info"
          v-on:click="goToHome()"
          type="submit"
          value="Place Order"
        >
          <span>
            <i class="fa fa-home" aria-hidden="true"></i> Home
          </span>
        </button>
      </p>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
export default {
  name: "OrderConfirmation",
  data: function() {
    return {
      merchantName: "",
      time: ""
    };
  },
  components: {
    Logout
  },
  mounted() {
    (this.merchantName = this.$route.query.merchantName),
      (this.time = this.$route.query.time);
  },
  methods: {
    goToOrderHistory() {
      this.$router.push("/orderHistory");
    },
    goToHome() {
      this.$router.push("/home");
    }
  }
};
</script>