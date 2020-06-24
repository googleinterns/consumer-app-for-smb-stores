<template>
  <div class="bd-lead">
    <div class="container">
      <div class="field is-grouped">
        <p class="control is-expanded has-icons-left">
          <input
            v-on:keyup.enter="addProduct(inputValue, '', 'https://semantic-ui.com/images/wireframe/image.png')"
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
          <button
            v-on:click="addItem(inputValue, '', 'https://semantic-ui.com/images/wireframe/image.png')"
            class="button is-info"
          >Add</button>
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
                      @click="addProduct(item.ItemName, item.EAN, item.ItemImage)"
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
                            <strong class="is-size-7-mobile is-size-6">MRP: ₹{{item.price}}</strong>
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
                      <td width="40px" class="field">
                        <figure class="image is-32x32">
                          <img :src="item.item_image" />
                        </figure>
                      </td>
                      <td>{{item.item_name}}</td>
                      <td align="right">
                        <div class="field has-addons has-addons-right">
                          <span
                            v-on:click="decrementQuantity(item)"
                            class="icon is-medium is-left"
                            style="color: #3298dc"
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
                            style="color: #3298dc"
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
        <button v-on:click="placeOrder()" class="button is-info" type="submit" value="Place Order">
          <span>Place Order</span>
        </button>
      </p>
    </div>
  </div>
</template>

<script>
import firebase from "firebase";
import PriorityQueue from "js-priority-queue";
import api from "../Api";

var levenshtein = require("levenshtein-edit-distance");

export default {
  name: "SelectItems",
  created() {
    var userId = this.$getUserId();
    console.log(userId);
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
            item_quantity: cartItem.val().item_quantity,
            EAN: cartItem.val().EAN,
            item_image: cartItem.val().item_image
          });
        });
      });
  },
  data: function() {
    return {
      inputValue: "",
      itemsInCart: [],
      products: [],
      orderId: "",
    };
  },
  methods: {
    addProduct(item, EAN, itemImage) {
      this.products = [];
      this.addItem(item, EAN, itemImage);
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
    addToCart(item, EAN, itemImage) {
      var userId = this.$getUserId();
      var reference = firebase.database().ref("user_cart/" + userId + "/");
      var itemDoc = reference.push({
        item_name: item,
        item_quantity: 1,
        item_image: itemImage
      });
      var item_id = itemDoc.key;
      this.itemsInCart.push({
        item_id: item_id,
        item_name: item,
        item_quantity: 1,
        item_image: itemImage
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

    addItem(item, EAN, itemImage) {
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
          this.addToCart(item, EAN, itemImage);
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

    getEPOCHvalue() {
      var now = new Date();
      var secondsSinceEpoch = Math.round(now.getTime() / 1000);
      return secondsSinceEpoch;
    },

    getFiveRandomDigits() {
      var result = "";
      var characters = "0123456789";
      var charactersLength = characters.length;
      for (var i = 0; i < 5; i++) {
        result += characters.charAt(
          Math.floor(Math.random() * charactersLength)
        );
      }
      return result;
    },

    getOrderId() {
      return this.getEPOCHvalue() + ":" + this.getFiveRandomDigits();
    },

    placeOrder() {
      this.orderId = this.getOrderId();
      var itemsForOrder = [];

      this.itemsInCart.forEach(item => {
        if (item.EAN === undefined) {
          itemsForOrder.push({
            item_name: item.item_name,
            quantity: item.item_quantity
          });
        } else {
          itemsForOrder.push({
            item_name: item.item_name,
            quantity: item.item_quantity,
            EAN: item.EAN
          });
        }
      });
     var userId = this.$getUserId();
     if (firebase.auth().currentUser.isAnonymous){
       localStorage.setItem('anonId', this.$getUserId())
       localStorage.setItem('isAnon', false);
     }
      api
        .placeOrder(userId, this.orderId, itemsForOrder)
        .then(response => {
          if (response.status == 200) {
            this.emptyCart();
            this.$router.push({
              name: "UserInfo",
              params: {
                orderId: this.orderId
              }
            });
          }
        })
        .catch(error => {
          this.$log.debug(error);
        });

      console.log(itemsForOrder);
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
