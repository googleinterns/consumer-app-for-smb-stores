<template>
    <div class="container is-fullhd">
    <section class="section">
        <div class="container">
        <div class="field is-grouped">
                <p class="control is-expanded">
                    <input v-if="Object.keys(selectedItem).length === 0" ref="dropdowninput" v-model.trim="inputValue" class="input" type="search" placeholder="Find item"/>
                </p>
                <p class="control">
                    <button v-on:click="addItem(inputValue)" class="button is-info">
                    Add
                    </button>
                </p>
            </div>
        </div>
    </section>
        <div class="container">
            <div v-bind:key="item.name" v-for="item in itemsInCart">
                <div class="card is-mobile">
                    <div class="card-content">
                        <div class="columns is-mobile">
                            <div class="column">
                                <strong>{{item.name}}</strong>
                            </div>
                            <div class="column">
                                <button class="button" v-on:click="decrementQuantity(item)">-</button>
                                <button class="button is-static">{{item.quantity}}</button>
                                <button class="button" v-on:click="incrementQuantity(item)">+</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <section class="section">
        <div v-if="itemsInCart.length  > 0" class="columns is-mobile is-centered">
            <div class="column is-half">
                <button class="button is-info is-light">Place Order</button>
            </div>
        </div>
        </section>

        
    </div>
</template>

<script>
import api from "../Api";
export default {
    name: 'SelectItems',
    mounted(){
        api.getAll
            .then(response => {
                this.$log.debug("Data loaded: ", response.data)
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
            selectedItem: {},
            inputValue: "",
            availableItems:[
                "Surf Excel",
                "Parle-G",
                "Rin",
                "Rice",
                "Cookies",
                "Britannia"
            ],
            itemsInCart: [
                // {
                //     name: "Surf",
                //     quantity: 4
                // }
            ]
        }
    },
    methods: {
        addItem(item){
            if (item.length != 0){
                var result = this.itemsInCart.find(obj => {
                    return obj.name.toLowerCase() === item.toLowerCase()
                })
                if (result){
                    result.quantity+=1
                    this.$emit('addItem', this.item);
                }else{
                    item = item.replace(/^./, item[0].toUpperCase());
                    var itemAdded = {name: item, quantity: 1}
                    this.itemsInCart.push(itemAdded);
                    this.$emit('addItem', this.item);
                }
            }
        },

        incrementQuantity(itemObj){
            itemObj.quantity += 1
            this.$emit('incrementQuantity', this.itemObj);
        },

        decrementQuantity(itemObj){
            if (itemObj.quantity >= 0){
                itemObj.quantity -= 1
                if (itemObj.quantity > 0)
                    this.$emit('decrementQuantity', this.itemObj);
            }

            if (itemObj.quantity < 0){
                this.itemsInCart.pop(itemObj);
                this.$emit('decrementQuantity', this.itemObj);
            }
        },
        resetSelection () {
            this.selectedItem = {}
            this.$nextTick( () => this.$refs.dropdowninput.focus() )
            this.$emit('on-item-reset')
        },

        selectItem (theItem) {
            this.selectedItem = theItem 
            this.inputValue = ''
            this.$emit('on-item-selected', theItem)
        },

        itemVisible (item) {
            let currentName = item.toLowerCase()
            let currentInput = this.inputValue.toLowerCase()
            return currentName.includes(currentInput)
        },
    }
}
</script>

