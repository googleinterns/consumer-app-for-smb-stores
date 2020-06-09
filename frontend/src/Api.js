import axios from 'axios'

const instance = axios.create({
  timeout: 10000
});


export default {

  data() {
    return {
      response: [],
      errors: []
    }
  },

  fetchPastOrders: (user_id) => instance.get(process.env.VUE_APP_SERVER_URL + '/getOrderDetails?user=' + user_id, {
    transformResponse: [function (data) {
      return data ? JSON.parse(data) : data;
    }]
  }),

  placeOrder: (user_id, servicing_merchant_name, servicing_merchant_address, offers, item_details) =>
    instance.post(process.env.VUE_APP_SERVER_URL + '/placeOrder', {
      user_id: user_id,
      servicing_merchant_name: servicing_merchant_name,
      servicing_merchant_address: servicing_merchant_address,
      offers: offers,
      item_details: item_details
    }),

  addItem: (user_id, item_name, quantity) => instance.post(process.env.VUE_APP_SERVER_URL + '/addNewItem', { user_id: user_id, item_name: item_name, quantity: quantity }),

  getAllItemsInCart: (user_id) => instance.get(process.env.VUE_APP_SERVER_URL + '/getItemsInCart?user=' + user_id, {
    transformResponse: [function (data) {
      console.log(data);
      return data ? JSON.parse(data) : data;
    }]
  }),

  changeItemQuantity: (user_id, item_name, quantity) => instance.post(process.env.VUE_APP_SERVER_URL + '/updateItemQuantity', { user_id: user_id, item_name: item_name, quantity: quantity }),

  deleteItem: (user_id, item_name) => instance.post(process.env.VUE_APP_SERVER_URL + '/removeItemFromCart', { user_id: user_id, item_name: item_name })

}