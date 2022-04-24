<template>
  <div>
    <div style="display: flex; align-items: center; justify-content: space-between">
      <div>
        <h1>Enter coordinates of your location </h1>
        <input v-model="inputLat" placeholder="latitude">
        <input v-model="inputLng" placeholder="longitude">
        <button @click="getLocation()">Send</button>
      </div>
      <div>
        <h1>Your coordinates:</h1>
        <p>{{ userCoordinates.lat.toFixed(4) }} Latitude, {{ userCoordinates.lng.toFixed(4) }} Longitude</p>
      </div>
      <div>
        <h1>Distance to North Pole from your coordinates:</h1>
        <p>{{ northPoleDistance.toFixed(2) }} km</p>
      </div>
    </div>
    <GmapMap
      :center="userCoordinates"
      :zoom="7"
      style="width: 840px; height: 560px; margin: 32px auto"
      ref="mapRef"
      ></GmapMap>
  </div>
</template>

<script>
export default {
  name: 'MapA',
  data() {
    return {
      map: null,
      inputLat: null,
      inputLng: null,
      northPoleDistance: 0,
      userCoordinates: {
        lat: 0,
        lng: 0
      },
    }
  },
  created() {
    this.$getLocation({})
        .then(coordinates => {
          this.userCoordinates = coordinates;
        });
  },
  mounted() {
    this.$refs.mapRef.$mapPromise.then(map => this.map = map)

    // const distFrom = require('distance-from')
    // var northPoleCoordinates = [90, 0]
    // var currCoordinates = [this.userCoordinates.lat, this.userCoordinates.lng]
    // this.northPoleDistance = distFrom(currCoordinates).to(northPoleCoordinates).in('mi')
    this.northPoleDistance = this.calculateDistanceBetweenCoordinates(39.8649,  32.7461, 90, 0)
  },
  methods: {
    getLocation() {
      if (this.inputLat !== null && this.inputLng !== null) {
        // eslint-disable-next-line no-undef
        var center = new google.maps.LatLng(this.inputLat,this.inputLng);
        this.map.panTo(center);
      }
      // eslint-disable-next-line no-undef
      var marker = new google.maps.Marker({
        position: this.userCoordinates,
        title:"Hello World!"
      });
      marker.setMap(this.map)
    },
    calculateDistanceBetweenCoordinates(lat1, lon1, lat2, lon2) {
      var earthRadiusKm = 6371;

      var dLat = this.degreesToRadians(lat2 - lat1);
      var dLon = this.degreesToRadians(lon2 - lon1);

      lat1 = this.degreesToRadians(lat1);
      lat2 = this.degreesToRadians(lat2);

      var a =
          Math.sin(dLat / 2) * Math.sin(dLat / 2) +
          Math.sin(dLon / 2) *
          Math.sin(dLon / 2) *
          Math.cos(lat1) *
          Math.cos(lat2);
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return earthRadiusKm * c;
    },
    degreesToRadians(degrees) {
      return (degrees * Math.PI) / 180;
    }
  },
  computed: {
    mapCoordinates() {
      if (!this.map) {
        return {
          lat: 0,
          lng: 0
        }
      }
      return {
        lat: this.map.getCenter().lat().toFixed(4),
        lng: this.map.getCenter().lng().toFixed(4)
      }
    }
  }
}
</script>

<style>
input {
  height: 40px;
  margin-right: 10px;
}
button {
  border: none;
  border-radius: 12px;;
  margin: 0;
  padding: 0;
  width: 80px;
  height: 45px;
  overflow: visible;
  background: cornflowerblue;
  color: white;
  font: inherit;
  line-height: normal;
  -webkit-font-smoothing: inherit;
  -moz-osx-font-smoothing: inherit;
  -webkit-appearance: none;
}
</style>
