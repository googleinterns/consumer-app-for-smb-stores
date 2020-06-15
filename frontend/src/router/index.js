import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import firebase from 'firebase'
import itemDetails from '../views/itemdetails.vue'
import merchantList from '../views/merchantsList.vue'


Vue.use(VueRouter)


const routes = [
  {
    path: '*',
    redirect: '/login'
  },
  {
    path: '/',
    redirect: '/login'
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
    path: '/merchantBids',
    name : 'merchantList',
    component : merchantList,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/orderDetails',
    name : 'itemDetails',
    component : itemDetails,
    meta: {
      requiresAuth: true
    }
  },

]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  const currentuser = firebase.auth().currentUser;
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  if (requiresAuth && !currentuser) next('login');
  else if (!requiresAuth && currentuser) next('home');
  else next();
});

export default router
