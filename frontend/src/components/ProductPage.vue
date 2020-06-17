<template>
  <div>
    <div class="container">
      <div>
        <div class="columns is-multiline is-variable is-mobile">
          <div
            v-for="item in itemsavailable"
            v-bind:key="item.EAN"
            class="column is-6-mobile is-3-desktop is-2-widescreen is-3-tablet"
          >
            <div class="card">
              <div>
                <div>
                  <center>
                    <figure class="image is-square">
                      <img :src="item.image_url" alt="Image" />
                    </figure>
                  </center>
                </div>
                <div
                  class="box is-shadowless"
                  style="padding-top: 5px; padding-bottom: 10px; padding-right: 10px; padding-left: 10px;"
                >
                  <div>
                    <div>
                      <small>
                        <span>
                          <p align="left" class="is-clipped ellipsis is-ellipsis-2">
                            <strong>{{item.product_name}}</strong>
                          </p>
                        </span>
                        <p align="left">
                          MRP:
                          <strike>₹ {{item.MRP}}</strike>
                        </p>
                        <small>
                          <p align="left" style="color: #df7518">Get At: ₹ {{item.discounted_price}}</p>
                        </small>
                      </small>
                    </div>
                  </div>
                  <div>
                    <button
                      class="button is-link is-small is-fullwidth"
                      style="background-color: #0ca1e5;"
                      v-on:click="addItemToCart(item)"
                    >
                      <span>
                        <strong>
                          <big>Add to cart</big>
                        </strong>
                      </span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import firebase from "firebase";
export default {
  name: "ProductPage",
  props: ["itemsavailable"],
  methods: {
    addItemToCart(item) {
      var userId = this.$getUserId();
      var pushRef = firebase.database().ref("user_cart/" + userId + "/");
      var reference = firebase
        .database()
        .ref("user_cart/" + userId + "/")
        .orderByChild("item_name")
        .equalTo(item.product_name);
      reference.once("value").then(function(cartItem) {
        if (!cartItem.exists()) {
          pushRef.push({
            item_name: item.product_name,
            item_quantity: 1,
            EAN: item.EAN
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.ellipsis {
  display: block;
  display: -webkit-box;
  margin: 0 auto;
  line-height: 1.4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  word-wrap: break-word;
}

.is-ellipsis-2 {
  -webkit-line-clamp: 1;
}
</style>