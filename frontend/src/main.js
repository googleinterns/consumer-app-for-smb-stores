import Vue from 'vue'
import firebase from 'firebase'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import VueMeta from 'vue-meta';
import moment from 'moment';
import * as VueGoogleMaps from "vue2-google-maps";
import { config } from './googleMap-config.js';
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
    console.log("Trying log in", firebase.auth().currentUser)
    if (firebase.auth().currentUser == null){
      console.log("LOGGG")
      firebase.auth().signInAnonymously()
    }
    app =
      Vue.filter('formatDate', function (value) {
        if (value) {
          return moment(String(value)).format('DD-MMM-YYYY')
        }
      })
      Vue.filter('formatStatus', function(value){
        if (value){
          return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase()
        }
      })
    Vue.use(VueLogger, options);
    Vue.use(VueMeta, {
      keyname: 'head'
    })
    Vue.mixin({
      data: function () {
        return {
          userId: null
        }
      },
      methods: {
        $getUserId() {
          if (firebase.auth().currentUser == null){
            firebase.auth().signInAnonymously()
          }
          return firebase.auth().currentUser.uid
        },
        getRandomID() {
          var result = "";
          var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
          var charactersLength = characters.length;
          for (var i = 0; i < 20; i++) {
            result += characters.charAt(
              Math.floor(Math.random() * charactersLength)
            );
          }
          return result;
        },
      }

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


