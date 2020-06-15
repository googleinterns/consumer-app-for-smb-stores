import Vue from 'vue'
import firebase from 'firebase'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import VueMeta from 'vue-meta';
import moment from 'moment';
import * as VueGoogleMaps from "vue2-google-maps";
import { config } from '../googleMap-config.js';
import { firebaseConfig } from '../firebase-config.js';
import VueLogger from 'vuejs-logger';
import { firestorePlugin } from 'vuefire'

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


Vue.use(VueGoogleMaps, {
  load: {
    key: config.apiKey,
  }
});

Vue.use(firestorePlugin);
export const db = firebase.firestore();


