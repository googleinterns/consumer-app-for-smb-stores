<template>
<div><Logout />
  <div class="bd-lead">
    <div class="container">
      <div>
           <input v-if="{cust_name}"
            v-model="cust_name"
            class="input is-capitalized is-family-primary"
            placeholder="Enter your name"
          />
          <input
            v-model="address"
            class="input is-capitalized is-family-primary"
            placeholder="Enter your address"
          />
           <input
            v-model="contactNo"
            class="input is-capitalized is-family-primary"
            placeholder="Enter your mobile no."
          />
          <button @click="submit">Submit</button>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import Logout from "@/components/Logout.vue";
import api from "../Api";

export default {
 props: ["orderId", "cust_name"],
  name: "UserInfo",
  components: {
    Logout
  },
  data() {
    return {
        address:"",
      contactNo: ""
    };
  },
  methods: {
    submit() {
         var userId = this.$getUserId();
        //  api.fetchItemsForAnOrder(self.orderId).then(response => {
        // response.data.forEach(item => {
        //   if (item.EAN != null) {
        //     self.itemsInCart.push({
        //       product_name: item.item_name,
        //       quantity: item.quantity,
        //       EAN: item.EAN
        //     });
        //   } else {
        //     self.itemsInCart.push({
        //       product_name: item.item_name,
        //       quantity: item.quantity
        //     });
        //   }
        // });
        //  });

         firebase
        .database()
        .ref("user_cart/" + userId)
        .once("value")
        .then(function(cart) {
          cart.forEach(function(cartItem) {
            this.itemsInCart.push({
              item_id: cartItem.key,
              item_name: cartItem.val().item_name,
              item_quantity: cartItem.val().item_quantity,
              EAN: cartItem.val().EAN
            });
          });
        });
        
        api.updateUserDetails(userId,this.address,this.contactNo).catch(error=>{
            this.$log.debug(error);
        })

         this.$router.push({
        name: "merchantList",
        params: {
          orderId: this.orderId,
          address: this.address,
          cust_name: this.cust_name
        }
      });
        api
        .placeOrder(userId, this.orderId, "", "", "", this.itemsInCart)
        .catch(error => {
          this.$log.debug(error);
        });
     
    }
  }
};
</script>