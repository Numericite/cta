
export default function({ $axios, $config, redirect, app, store }) {
  // Set API default URL
  $axios.defaults.baseURL = $config.API_URL;

  // OnError, display a toast
  // $axios.onError(error => {
  //   if (app.$toast && error && error.response)
  //     app.$toast.error('Error while making request: ' + error.response.message)
  //   console.log(error.response)
  // })

  //INTERCEPTOR TO SET JWT TOKEN, CAN'T SET IT GLOBALLY... ISSUE WITH NUXTJS : https://github.com/nuxt/nuxt.js/issues/2680
  $axios.interceptors.request.use(request => {
    // Get token from auth.js store
    const token = store.state.auth.token

    // Update token axios header
    if (token) {
      request.headers.common['X-Auth-Token'] = token
    }
    return request
  })

  // INTERCEPT 401 / 403 and logout
  $axios.interceptors.response.use(function (response) {
    return response;
  }, function (error) {

    console.log(error.response)

    //SHOW BAD REQUESTS ERRORS FOR DEBUG IN DEV
    if (app.context.isDev && (400 === error.response.status)) {
      console.log(error.response)
    }

    if (401 === error.response.status || 403 === error.response.status) {
      store.commit('auth/destroy')
      window.location = '/';
    } else {
      return Promise.reject(error);
    }
  });
}
