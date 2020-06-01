import axios from 'axios' 

const SERVER_URL = 'http://localhost:9000';  
const instance = axios.create({  
  baseURL: SERVER_URL,  
  timeout: 1000  
});  

export default {  
  // (C)reate  
  createNew: (item, quantity) => instance.post('items', {itemName: item, itemQuantity: quantity}),  
  // (R)ead  
  getAll: () => instance.get('items', {  
    transformResponse: [function (data) {  
      return data? JSON.parse(data)._embedded.items : data;  
    }]  
  }),  
//   // (U)pdate  
//   updateForId: (id, text, completed) => instance.put('todos/'+id, {title: text, completed: completed}),  
//   // (D)elete  
//   removeForId: (id) => instance.delete('todos/'+id)  
}