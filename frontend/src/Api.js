import axios from 'axios'

const instance = axios.create({ timeout: 25000 });

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

  fetchOngoingOrders: (user_id) => instance.get(process.env.VUE_APP_SERVER_URL + '/getOngoingOrders?user=' + user_id, {
    transformResponse: [function (data) {
      return data ? JSON.parse(data) : data;
    }]
  }),

  updateOrderStatusToProcessing: (user_id, order_id, servicing_merchant_name, servicing_merchant_address, offers, item_details) =>
    instance.post(process.env.VUE_APP_SERVER_URL + '/updateOrderDetails', {
      user_id: user_id,
      order_id: order_id,
      servicing_merchant_name: servicing_merchant_name,
      servicing_merchant_address: servicing_merchant_address,
      offers: offers,
      item_details: item_details
    })
  ,

  placeOrder: (user_id, order_id, servicing_merchant_name, servicing_merchant_address, offers, item_details) =>
    instance.post(process.env.VUE_APP_SERVER_URL + '/placeOrder', {
      user_id: user_id,
      order_id: order_id,
      servicing_merchant_name: servicing_merchant_name,
      servicing_merchant_address: servicing_merchant_address,
      offers: offers,
      item_details: item_details
    }),
    fetchItemsForAnOrder: (orderId) => instance.get(process.env.VUE_APP_SERVER_URL + '/getItemDetailsForOrderID?orderId=' + orderId, {
      transformResponse: [function (data) {
        return data ? JSON.parse(data) : data;
      }]
    }),
    
    updateUserDetails : (user_id, user_address, user_contactNo) =>
    instance.post(process.env.VUE_APP_SERVER_URL+'/userDetails',{
      user_id : user_id,
      user_address: user_address,
      user_contactNo : user_contactNo
    })
}