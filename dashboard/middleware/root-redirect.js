export default ({params, route, redirect}) => {
  if (route.fullPath === '/') {
    redirect('/login')
  }
}
