import Vue from 'vue'
import firebase from 'firebase'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'

import { firebaseConfig } from '../firebase-config.js';

firebase.initializeApp(firebaseConfig);
firebase.analytics();

let app = '';
firebase.auth().onAuthStateChanged(() => {
  if (!app) {
    app =
      new Vue({
        router,
        render: h => h(App)
      }).$mount('#app');
  }
});

Vue.config.productionTip = false



