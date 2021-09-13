import BaseApi from './base'

export default class GeoGouvApi extends BaseApi {
  getCities(city) {
    return this.$axios.get('https://geo.api.gouv.fr/communes?nom=' + city)
  }
}
