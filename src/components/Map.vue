<template>
  <div>
    <h1 style="margin: 30px">Geolocation Services</h1>
    <div style="display: flex; align-items: center; justify-content: space-between; margin-left: 20px">
      <h3>Enter Your Coordinates
        <input style="margin: 15px;" v-model="inputLat" placeholder="Enter Latitude">
        <input style="margin: 15px;" v-model="inputLng" placeholder="Enter Longitude">
        <button class="btn-hover color-1" @click="getLocation()">Show Country</button>
      </h3>
      <div style="margin-right: 190px">
        <h4>Your GPS coordinates:</h4>
          <p>{{ userCoordinates.lat.toFixed(4) }} Latitude, {{ userCoordinates.lng.toFixed(4) }} Longitude</p>
      </div>
    </div>
    <b-row style="margin-top: 20px">
      <b-col>
        <div>
          <h2>Distance to North Pole</h2>
          <h4>Distance to the north pole from the given coordinates:</h4>
          <p>{{ northPoleDistanceCoord.toFixed(2) }} km</p>
          <h4>Distance to the north pole from your GPS coordinates:</h4>
          <p>{{ northPoleDistanceGPS.toFixed(2) }} km</p>
          <h2>Distance to Moon's Core</h2>
          <h4>Distance to the moon's core from the given coordinates:</h4>
          <p>{{ moonDistanceCoord.toFixed(2) }} km</p>
          <h4>Distance to the moon's core from your GPS coordinates:</h4>
          <p>{{ moonDistanceGPS.toFixed(2) }} km</p>
        </div>
      </b-col>
      <b-col>
        <GmapMap
            :center="userCoordinates"
            :zoom="4"
            style="width: 960px; height: 760px; margin-left: auto; margin-right: 10px"
            ref="mapRef"
        ></GmapMap>
      </b-col>
    </b-row>
    <b-overlay :show="isBusy" no-wrap fixed opacity="0.5" />
  </div>
</template>

<script>
export default {
  name: 'MapA',
  data() {
    return {
      map: null,
      marker: null,
      inputLat: null,
      inputLng: null,
      checkMarker: false,
      isBusy: false,
      northPoleDistanceCoord: 0,
      northPoleDistanceGPS: 0,
      moonDistanceCoord: 0,
      moonDistanceGPS: 0,
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
          this.northPoleDistanceGPS = this.calculateDistanceBetweenCoordinates(this.userCoordinates.lat,  this.userCoordinates.lng, 90, 0)
        });
  },
  mounted() {
    this.isBusy = true
    this.$getLocation({})
        .then(coordinates => {
          this.userCoordinates = coordinates;
          this.isBusy = false
        });
    this.$refs.mapRef.$mapPromise.then(map => this.map = map)

    // const distFrom = require('distance-from')
    // var northPoleCoordinates = [90, 0]
    // var currCoordinates = [this.userCoordinates.lat, this.userCoordinates.lng]
    // this.northPoleDistance = distFrom(currCoordinates).to(northPoleCoordinates).in('mi')
  },
  methods: {
    getLocation() {
      if (this.checkMarker) {
        this.marker.setMap(null);
      }
      // eslint-disable-next-line no-undef
      var myLatlng = new google.maps.LatLng(this.inputLat, this.inputLng);
      if (this.inputLat !== null && this.inputLng !== null) {
        // eslint-disable-next-line no-undef
        var center = new google.maps.LatLng(myLatlng);
        this.map.panTo(center);
      }
      // eslint-disable-next-line no-undef
      this.marker = new google.maps.Marker({
        position: myLatlng,
        title:"Hello World!"
      });
      this.marker.setMap(this.map)
      this.checkMarker = true
      this.northPoleDistanceCoord = this.calculateDistanceBetweenCoordinates(this.inputLat,  this.inputLng, 90, 0)
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
h2 {
  color: blue;
}
input {
  height: 40px;
  margin-right: 10px;
  font-size: 20px !important;
}
.buttons {
  margin: 10%;
  text-align: center;
}
button {
  text-align: center;
  border: none;
  box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
  border-radius: 12px !important;
  margin: 20pt;
  padding: 0;
  width: 140px;
  height: 70px;
  overflow: visible;
  background: dodgerblue;
  color: white;
  font: inherit;
  font-size: smaller !important;
  line-height: normal;
  -webkit-font-smoothing: inherit;
  -moz-osx-font-smoothing: inherit;
  -webkit-appearance: none;
}
.btn-hover {
  width: 200px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  margin: 20px;
  height: 55px;
  text-align:center;
  border: none;
  background-size: 300% 100%;

  border-radius: 50px;
  moz-transition: all .4s ease-in-out;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}
.btn-hover:hover {
  background-position: 100% 0;
  moz-transition: all .4s ease-in-out;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}

.btn-hover:focus {
  outline: none;
}

.btn-hover.color-1 {
  background-image: linear-gradient(to right, #25aae1, #40e495, #30dd8a, #2bb673);
  box-shadow: 0 4px 15px 0 rgba(49, 196, 190, 0.75);
}
p {
  font-size: 20px;
}
</style>
