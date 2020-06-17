<template>
  <div class="bd-lead">
    <div class="container">
      <div class="field is-grouped">
        <p class="control is-expanded has-icons-left">
          <input
            v-on:keyup.enter="addProduct(inputValue)"
            @keyup="findSuggestedProducts(inputValue)"
            v-model="inputValue"
            class="input is-capitalized is-family-primary"
            type="search"
            placeholder="Find Item"
          />
          <span class="icon is-medium is-left" style="cursor: pointer">
            <i class="fa fa-search"></i>
          </span>
        </p>
        <p class="control">
          <button v-on:click="addItem(inputValue)" class="button is-primary">Add</button>
        </p>
      </div>
      <div class="is-shadowless" v-if="products.length>0">
        <div class="columns is-vcentered">
          <div class="column is-full-mobile">
            <section class="panel">
              <div class="panel-block table-container is-flex-widescreen-only">
                <table class="table is-fullwidth" style="table-layout:fixed;word-wrap:break-word;">
                  <tbody>
                    <tr
                      @click="addProduct(item.ItemName)"
                      v-bind:key="index"
                      v-for="(item, index) in products"
                    >
                      <td class="field is-grouped is-size-5-desktop is-vcentered">
                        <figure id="product" class="image is-48x48">
                          <img :src="item.ItemImage" />
                        </figure>
                        <div class="is-family-primary">
                          {{item.ItemName}}
                          <div>
                            <strong
                              class="is-size-7-mobile is-size-6 has-text-danger"
                            >MRP: ₹{{item.price}}</strong>
                          </div>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
    <div>
      <br />
      <div class="container" v-if="itemsInCart.length  > 0">
        <div class="columns is-vcentered">
          <div class="column is-full-mobile">
            <section class="panel">
              <div class="panel-block table-container is-flex-widescreen-only">
                <table class="table is-fullwidth" style="table-layout:fixed;word-wrap:break-word;">
                  <tbody>
                    <tr v-bind:key="item.item_id" v-for="(item) in itemsInCart">
                      <td>{{item.item_name}}</td>
                      <td align="right">
                        <div class="field has-addons has-addons-right">
                          <span
                            v-on:click="decrementQuantity(item)"
                            class="icon is-medium is-left"
                            style="color: #17a1b9c7"
                          >
                            <i class="fa fa-minus-circle" aria-hidden="true"></i>
                          </span>
                          <p class="control">
                            <input
                              v-model="item.item_quantity"
                              class="input is-small"
                              size="2"
                              style="text-align: center;"
                              type="text"
                              placeholder="Quantity"
                              disabled
                            />
                          </p>
                          <span
                            v-on:click="incrementQuantity(item)"
                            class="icon is-medium is-left"
                            style="color: #17a1b9c7"
                          >
                            <i class="fa fa-plus-circle" aria-hidden="true"></i>
                          </span>
                          <div>
                            <span
                              v-on:click="removeItem(item)"
                              class="icon is-medium is-left"
                              style="color: red"
                            >
                              <i class="fa fa-trash-alt"></i>
                            </span>
                          </div>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
    <div v-if="itemsInCart.length  > 0" class="column has-text-centered">
      <p>
        <button
          v-on:click="emptyCart()"
          class="button is-danger is-light"
          type="submit"
          value="Empty Cart"
        >
          <span>Empty Cart</span>
        </button>&nbsp;
        <button
          v-on:click="placeOrder()"
          class="button is-primary is-light"
          type="submit"
          value="Place Order"
        >
          <router-link to="/merchantBids">
            <span>Place Order</span>
          </router-link>
        </button>
      </p>
    </div>
  </div>
</template>

<script>
import api from "../Api";
import firebase from "firebase";
import PriorityQueue from "js-priority-queue";

var levenshtein = require("levenshtein-edit-distance");

export default {
  name: "SelectItems",
  created() {
    var userId = this.$getUserId();
    let self = this;
    firebase
      .database()
      .ref("user_cart/" + userId)
      .once("value")
      .then(function(cart) {
        cart.forEach(function(cartItem) {
          self.itemsInCart.push({
            item_id: cartItem.key,
            item_name: cartItem.val().item_name,
            item_quantity: cartItem.val().item_quantity
          });
        });
      });
  },
  data: function() {
    return {
      inputValue: "",
      itemsInCart: [],
      products: []
    };
  },
  methods: {
    addProduct(item) {
      this.products = [];
      this.addItem(item);
    },

    findSuggestedProducts(productName) {
      if (productName == "") {
        this.products = [];
        return;
      }

      var vm = this;
      var productsQueue = new PriorityQueue({
        comparator: function(item1, item2) {
          var distance1 = levenshtein(productName, item1.ItemName);
          var distance2 = levenshtein(productName, item2.ItemName);
          return (
            distance1 / item1.ItemName.length -
            distance2 / item2.ItemName.length
          );
        }
      });

      firebase
        .firestore()
        .collection("Items")
        .get()
        .then(function(querySnapshot) {
          querySnapshot.forEach(function(doc) {
            productsQueue.queue(doc.data());
          });

          while (vm.products.length > 0) vm.products.pop();
          while (vm.products.length < 5) {
            var d = productsQueue.dequeue();
            vm.products.push(d);
          }
        });
    },
    addToCart(item) {
      var userId = this.$getUserId();
      var reference = firebase.database().ref("user_cart/" + userId + "/");
      var itemDoc = reference.push({
        item_name: item,
        item_quantity: 1
      });
      var item_id = itemDoc.key;
      this.itemsInCart.push({
        item_id: item_id,
        item_name: item,
        item_quantity: 1
      });
    },

    updateQuantityInCart(item_id, value) {
      var userId = this.$getUserId();
      var update = {};
      update["user_cart/" + userId + "/" + item_id + "/item_quantity"] = value;
      firebase
        .database()
        .ref()
        .update(update);
    },

    removeItemFromCart(item_id) {
      var userId = this.$getUserId();
      firebase
        .database()
        .ref()
        .child("user_cart/" + userId + "/" + item_id)
        .remove();
    },

    addItem(item) {
      this.products = [];
      if (item.length != 0) {
        var result = this.itemsInCart.find(obj => {
          return obj.item_name.toLowerCase() === item.toLowerCase();
        });
        if (result) {
          result.item_quantity += 1;
          this.inputValue = "";
          this.updateQuantityInCart(result.item_id, result.item_quantity);
        } else {
          item = item.replace(/^./, item[0].toUpperCase());
          this.addToCart(item);
          this.inputValue = "";
        }
      }
    },

    incrementQuantity(itemObj) {
      itemObj.item_quantity += 1;
      this.updateQuantityInCart(itemObj.item_id, itemObj.item_quantity);
    },

    decrementQuantity(itemObj) {
      if (itemObj.item_quantity >= 2) {
        itemObj.item_quantity -= 1;
        this.updateQuantityInCart(itemObj.item_id, itemObj.item_quantity);
      } else {
        this.removeItem(itemObj);
      }
    },

    removeItem(itemObj) {
      console.log(itemObj.item_id, "item");
      this.removeItemFromCart(itemObj.item_id, itemObj.item_quantity);
      this.itemsInCart.splice(this.itemsInCart.indexOf(itemObj), 1);
    },

    emptyCart() {
      var userId = this.$getUserId();
      firebase
        .database()
        .ref()
        .child("user_cart/" + userId)
        .remove();
      this.itemsInCart = [];
    },

    placeOrder() {
      console.log(process.env.VUE_APP_SERVER_URL)
      //var userId = firebase.auth().currentUser.uid;

      var userId = this.$getUserId();
      var itemsForOrder = [];

      this.itemsInCart.forEach(item =>
        itemsForOrder.push({
          item_name: item.item_name,
          quantity: item.item_quantity,
          unit_price: 30
        })
        
      );

      api
        .placeOrder(
          userId,
          "Merchant A",
          "New Street, Delhi",
          "Free delivery",
          itemsForOrder
        )
        .then(response => {
          if (response) {
            this.emptyCart(); //Need to be done as per response
            this.itemsInCart = []; //Need to update based on items
          }
        })
        .catch(error => {
          this.$log.debug(error);
        });
    }
  }
};
</script>
<style scoped>
.bd-lead {
  padding: 0.75rem;
}
#product {
  margin-right: 0.75rem;
}
</style>
