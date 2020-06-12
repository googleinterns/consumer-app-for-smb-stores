import Vue from 'vue'
import firebase from 'firebase'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import VueMeta from 'vue-meta';
import moment from 'moment';

import { firebaseConfig } from '../firebase-config.js';
import VueLogger from 'vuejs-logger';
const options = {
  isEnabled: true,
  logLevel: 'debug',
  stringifyArguments: false,
  showLogLevel: true,
  showMethodName: false,
  separator: '|',
  showConsoleColors: true
};

firebase.initializeApp(firebaseConfig);
firebase.analytics();

let app = '';
firebase.auth().onAuthStateChanged(() => {
  if (!app) {
    app =
      Vue.filter('formatDate', function (value) {
        if (value) {
          return moment(String(value)).format('DD-MMM-YYYY')
        }
      })
    Vue.use(VueLogger, options);
    Vue.use(VueMeta, {
      keyname: 'head'
    })

    new Vue({
      router,
      render: h => h(App)
    }).$mount('#app');
  }
});

Vue.config.productionTip = false
