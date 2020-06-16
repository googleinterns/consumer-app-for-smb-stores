<template>
  <div class="bd-lead">
    <!-- <div class="columns is-vcentered"> -->
    <div class="columns is-mobile">
      <div class="column">
        <h1 class="title is-6" align="left">Start Shopping Now</h1>
      </div>
      <div class="column" align="right">
        <button class="button is-info is-light">
          <router-link to="/placeOrder">Order Now</router-link>
        </button>
      </div>
    </div>

    <div>
      <div class="columns is-multiline is-variable is-mobile">
        <div
          v-for="card in suggestedItems"
          v-bind:key="card.item_key"
          class="column is-6-mobile is-3-desktop is-2-widescreen is-3-tablet"
        >
          <div class="card">
            <div>
              <div>
                <center>
                  <figure class="image is-square">
                    <img :src="card.item_image" alt="Image" />
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
                          <strong>{{card.item_name}}</strong>
                        </p>
                      </span>
                      <p align="left">
                        MRP:
                        <strike>₹ {{card.item_mrp}}</strike>
                      </p>
                      <small>
                        <p
                          align="left"
                          style="color: #df7518"
                        >Least Price: ₹ {{card.item_lowest_price}}</p>
                      </small>
                    </small>
                  </div>
                </div>
                <div>
                  <button
                    class="button is-link is-small is-fullwidth"
                    style="background-color: #0ca1e5;"
                    v-on:click="addItemToCart(card)"
                  >
                    <span>
                      <strong>
                        <big>{{cartAddition}}</big>
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
</template>

<script>
import firebase from "firebase";
export default {
  name: "SuggestedItems",
  data: function() {
    return {
      cartAddition: "Add To Cart",
      suggestedItems: [
        {
          item_key: 1,
          item_image: require("../assets/surfExcel.jpg"),
          item_name: "Surf Excel 1 kg",
          item_mrp: 180,
          item_lowest_price: 160,
          EAN: 8901030648922
        },
        {
          item_key: 2,
          item_image: require("../assets/biscuit.jpeg"),
          item_name: "Britannia Good Day 100 gm",
          item_mrp: 20,
          item_lowest_price: 17,
          EAN: 8901030547171
        },
        {
          item_key: 3,
          item_image: require("../assets/realJuice.jpg"),
          item_name: "Real Apple Juice 1L",
          item_mrp: 130,
          item_lowest_price: 100,
          EAN: 3289196260321 
        },
        // {
        //   item_key: 4,
        //   item_image: require("../assets/rin.jpg"),
        //   item_name: "Rin 250 gm",
        //   item_mrp: 60,
        //   item_lowest_price: 56,
        //   EAN: 
        // },
        {
          item_key: 5,
          item_image: require("../assets/lifebuoy.jpeg"),
          item_name: "Lifebuoy 190 ml",
          item_mrp: 85,
          item_lowest_price: 70,
          EAN: 8961014026702
        },
        {
          item_key: 6,
          item_image: require("../assets/head&shoulder.jpg"),
          item_name: "Head & Shoulder 650 ml",
          item_mrp: 550,
          item_lowest_price: 500,
          EAN: "037000061991"
        },
        {
          item_key: 7,
          item_image: require("../assets/dettol.jpg"),
          item_name: "Dettol 250 ml",
          item_mrp: 100,
          item_lowest_price: 95,
          EAN: 5011417559192
        },
        {
          item_key: 8,
          item_image: require("../assets/haldiramChips.jpeg"),
          item_name: "Haldiram Chips",
          item_mrp: 20,
          item_lowest_price: 18,
          EAN: 8904004400090
        },
        {
          item_key: 9,
          item_image: require("../assets/fortune-sunflower-oil.jpg"),
          item_name: "Fortune Sunflower Oil 1L",
          item_mrp: 120,
          item_lowest_price: 105,
          EAN: 8904004400090
        }
      ]
    };
  },
  methods: {
    addItemToCart(item) {
      var userId = this.$getUserId();
      var pushRef = firebase.database().ref("user_cart/" + userId + "/");
      var reference = firebase
        .database()
        .ref("user_cart/" + userId + "/")
        .orderByChild("item_name")
        .equalTo(item.item_name);
      reference.once("value").then(function(cartItem) {
        if (!cartItem.exists()) {
          pushRef.push({
            item_name: item.item_name,
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
.bd-lead {
  padding: 0.75rem;
}

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