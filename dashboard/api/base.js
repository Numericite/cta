export default class BaseApi {
  constructor($axios) {
    this.api = $axios
    this.$axios = $axios
  }
}
