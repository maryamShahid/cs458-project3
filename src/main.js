import Vue from 'vue'
import App from './App.vue'
import VueGeolocation from "vue-browser-geolocation/src"
import * as VueGoogleMaps from 'vue2-google-maps'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false

Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

Vue.use(VueGeolocation)
Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyBrc2sxK8F2KQTbmWcLlzqIc66_WinSNqI'
  }
})

new Vue({
  render: h => h(App),
}).$mount('#app')
