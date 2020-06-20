import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import itemDetails from '../views/itemdetails.vue'
import merchantList from '../views/merchantsList.vue'
import Ratings from '../views/Ratings.vue'



Vue.use(VueRouter)


const routes = [
  {
    path: '*',
    redirect: '/home'
  },
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/orderHistory',
    name: 'OrderHistory',
    component: () => import('../views/OrderHistory.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/ongoingOrders',
    name: 'OngoingOrders',
    component: () => import('../views/OngoingOrders.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/selectMerchant',
    name: 'SelectMerchant',
    component: () => import('../views/SelectMerchant.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/placeOrder',
    name: 'PlaceOrder',
    component: () => import('../views/PlaceOrder.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/merchantBids/:orderId&:address&:cust_name',
    name: 'merchantList',
    props: true,
    component: merchantList,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/orderConfirmation',
    name: 'OrderConfirmation',
    props: true,
    component: () => import('../views/OrderConfirmationPage.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/orderDetails/:orderId&:merchantId',
    name: 'itemDetails',
    props: true,
    component: itemDetails,

    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/ratings/:merchantName&:merchantID&:orderID',
    name: 'Ratings',
    component: Ratings,
    props: true,
    meta: {
      requiresAuth: true
    }
  },

  {
    path: '/merchantWiseProducts/:merchant',
    name: 'PaginatedProducts',
    props: true,
    component: () => import('../views/PaginatedProducts.vue'),
    meta: {
      requiresAuth: true
    }
  },
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// router.beforeEach((to, from, next) => {
//   const currentuser = firebase.auth().currentUser;
//   const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
//   if (requiresAuth && !currentuser) next('home');
//   else if (!requiresAuth && currentuser) next('home');
//   else next();
// });

export default router
