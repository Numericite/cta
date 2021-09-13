const settings = () => {}

const mutations = {
  initParams(settings) {
    settings.rowsPerPageItems =  [10, 25, 50]
  }
}

const dataTable = {
  namespaced: true,
  settings,
  mutations
}
export default dataTable
