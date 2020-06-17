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
        <button class="button" type="submit" value="Order History">
          <router-link to="/orderHistory">
            <span>
              <i class="fa fa-book" aria-hidden="true"></i> Order History
            </span>
          </router-link>
        </button>&nbsp;
        <button class="button" type="submit" value="Place Order">
          <router-link to="/home">
            <span>
              <i class="fa fa-home" aria-hidden="true"></i> Home
            </span>
          </router-link>
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
  }
};
</script>