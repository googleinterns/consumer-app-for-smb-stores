<template>
    <div class="bd-lead">
            <div class="container">
                <div class="field is-grouped">
                    <p class="control is-expanded has-icons-left">
                        <input v-model.trim="inputValue" class="input" type="search" placeholder="Find Item"/>
                        <span class="icon is-medium is-left" style="cursor: pointer">
                            <i class="fa fa-search"></i>
                        </span>
                    </p>
                    <p class="control">
                        <button v-on:click="addItem(inputValue)" class="button is-primary">
                        Add
                        </button>
                    </p>
                </div>
            </div>
        <div>
            <div class="container is-shadowless" v-if="itemsInCart.length  > 0">
                <div class="columns is-vcentered">
                    <div class="column is-full-mobile">
                        <section class="panel is-shadowless">
                            <div class="panel-block table-container is-flex-widescreen-only">
                                <table class="table is-striped is-fullwidth" style="table-layout:fixed;word-wrap:break-word;">
                                    <tbody>
                                        <tr v-bind:key="index" v-for="(item, index) in itemsInCart">
                                            <td>{{item.item_name}}</td>
                                            <td align="right">
                                                <div class="field has-addons has-addons-right">
                                                    <span v-on:click="decrementQuantity(item)" class="icon is-medium is-left" style="color: #17a1b9c7">
                                                        <i class="fa fa-minus-circle" aria-hidden="true"></i>
                                                    </span>
                                                    <p class="control">
                                                        <input v-model=item.quantity class="input is-small" size=2 style="text-align: center;" type="text" placeholder="Quantity" disabled>
                                                    </p>
                                                    <span v-on:click="incrementQuantity(item)" class="icon is-medium is-left" style="color: #17a1b9c7">
                                                         <i class="fa fa-plus-circle" aria-hidden="true"></i>
                                                    </span>
                                                    <div>
                                                        <span v-on:click="removeItem(item)" class="icon is-medium is-left" style="color: red"> 
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
                <button v-on:click="emptyCart()" class="button is-danger is-light" type="submit" value="Place Order">
                    <span>Empty Cart</span>
                </button>&nbsp;
                <button v-on:click="placeOrder()" class="button is-primary is-light" type="submit" value="Place Order">
                    <router-link to="/biddingMerchants"><span >Place Order 
                    </span>
                    </router-link>
                </button>
            </p>
        </div>
    </div>
</template>

<script>
import api from "../Api";
import firebase from "firebase";
export default {
    name: 'SelectItems',
    mounted(){
        var userId = firebase.auth().currentUser.uid
        api.getAllItemsInCart(userId)
            .then(response => {
                this.$log.debug("Data loaded: ", response.data)
                this.$log.debug(userId)
                this.$log.debug("Over here")
                this.itemsInCart = response.data
            })
            .catch(error =>{
                this.$log.debug(error)
                this.error = "Failed to load data"
            })
            .finally(() => this.loading = false)
    },
    data: function(){
        return{
            loading: true,
            inputValue: "",
            itemsInCart: [
            ]
        }
    },
    methods: {
        addItem(item){
            if (item.length != 0){
                var result = this.itemsInCart.find(obj => {
                    return obj.item_name.toLowerCase() === item.toLowerCase()
                })
                if (result){
                    result.quantity+=1
                    this.inputValue = ''
                    this.updateItemQuantity(result)
                }else{
                    item = item.replace(/^./, item[0].toUpperCase());
                    var userId = firebase.auth().currentUser.uid
                    api.addItem(userId, item, 1).then( (response) => {  
                        this.$log.debug("New item added:", response);  
                        this.itemsInCart.push({
                            item_name: response.data.item_name,  
                            quantity: response.data.quantity 
                            })  
                        }).catch((error) => {  
                            this.$log.debug(error);  
                            this.error = "Failed to add item"  
                        });  
                            this.inputValue = ''  
                        }
            }
        },

        incrementQuantity(itemObj){
            itemObj.quantity += 1
            this.updateItemQuantity(itemObj)
        },

        decrementQuantity(itemObj){
            if (itemObj.quantity >= 2){
                itemObj.quantity -= 1
                this.updateItemQuantity(itemObj)
            }else {
                this.removeItem(itemObj);
            }
        },
        
        removeItem(itemObj){
            var userId = firebase.auth().currentUser.uid
            api.deleteItem(userId, itemObj.item_name).then((response) => {
                this.$log.debug("Item removed:", itemObj);
                this.$log.info("Item deleted:", response.data);
                this.itemsInCart.splice(this.itemsInCart.indexOf(itemObj), 1)  
            }).catch((error) => {  
                this.$log.debug(error);  
                this.error = "Failed to remove item"  
            });
        },

        updateItemQuantity(itemObj){
            var userId = firebase.auth().currentUser.uid
            this.$log.info(itemObj.item_name, itemObj.quantity);
            api.changeItemQuantity(userId, itemObj.item_name, itemObj.quantity).then((response) => {  
                this.$log.info("Item quantity updated:", response.data);  
            }).catch((error) => {  
                this.$log.debug(error)  
                this.error = "Failed to update quantity"  
            });
        },

        emptyCart(){
            this.itemsInCart.forEach(this.removeItem);
        },

        placeOrder(){
            var userId = firebase.auth().currentUser.uid
            var itemsForOrder = []

            this.itemsInCart.forEach(item =>(
                itemsForOrder.push({
                    "item_name": item.item_name,
                    "quantity": item.quantity,
                    "unit_price": 30
                })                
                )
            )

            api.placeOrder(userId, "Merchant A", "New Street, Delhi", "Free delivery",itemsForOrder)
            .then(response => {
                this.$log.info("Order placed", response)
                this.itemsInCart = []   //Need to update based on items
            }).catch((error) => {  
                this.$log.debug(error)  
                this.error = "Failed to place order"  
            });
        }
    }
}
</script>
<style scoped>
.bd-lead{
    padding: 0.75rem;
}
</style>