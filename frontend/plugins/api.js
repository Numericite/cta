import Api from '~/api'

export default ({ app, req }, inject) => {
  inject('api', new Api(app.$axios))
}
