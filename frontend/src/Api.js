import axios from 'axios' 

const instance = axios.create({ 
  timeout: 10000 
});

// console.log(process.env.VUE_APP_SERVER_URL)

export default {  

  data(){
    return{
      response: [],
      errors: []
    }
  },

  fetchPastOrders: (userID)=> instance.get(process.env.VUE_APP_SERVER_URL + '/getOrderDetails?user=' + userID, {
      transformResponse: [function(data) {
        // console.log(data);
        return data? JSON.parse(data) : data;
      }]
    }),

  placeOrder: (userId, servicingMerchantName, servicingMerchantAddress, offers, itemDetails) => 
  instance.post(process.env.VUE_APP_SERVER_URL + '/placeOrder', {userId: userId, servicingMerchantName: servicingMerchantName, 
                                servicingMerchantAddress: servicingMerchantAddress, 
                                offers: offers, 
                                itemDetails: itemDetails}),

  addItem: (userIdentifier, itemName, quantity) => instance.post(process.env.VUE_APP_SERVER_URL + '/addNewItem', {userID: userIdentifier, itemName: itemName, quantity: quantity}),
  
  getAllItemsInCart: (userID) => instance.get(process.env.VUE_APP_SERVER_URL + '/getItemsInCart?user=' + userID, {
    transformResponse: [function(data) {
      console.log(data);
      return data? JSON.parse(data) : data;
    }]
  }),

  changeItemQuantity: (userID, itemName, quantity) => instance.post(process.env.VUE_APP_SERVER_URL + '/updateItemQuantity', {userID: userID, itemName: itemName, quantity: quantity}),

  deleteItem: (userID, itemName) => instance.post(process.env.VUE_APP_SERVER_URL + '/removeItemFromCart', {userID: userID, itemName: itemName})

}