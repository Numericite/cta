<template>
  <div class="big-container flex justify-center w-full">
    <div class="sub-container flex flex-col items-center justify-center ">
      <picture>
        <source srcset="~/assets/img/logo-violet.webp"
                type="image/webp">
        <img src="~/assets/img/logo-violet.png"
             alt="Logo de l'association crée ton avenir">
      </picture>
      <h1 class="error-main-title font-bold">404</h1>
      <div class="error-subtitle uppercase text-center">Page introuvable</div>
      <div class="bottom-line w-full"/>
      <nuxt-link to="/">
        <button class="btn-redirect text-3xl bg-white rounded-lg">Revenir en arrière</button>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
import Button from '../mixins/button'
export default {
  name: 'Error',
  components: { Button }
}
</script>

<style>
.big-container {
  min-height: 80vh;
  color: #3B3B3B;
  width: 100%;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.sub-container {
  max-width: 450px;
  width: 100%;
}
.error-main-title {
  font-size: 200px;
}
.error-subtitle {
  font-size: x-large;
  letter-spacing: 5px;
  word-spacing: 10px;
  margin-top: 20px;
  min-width: 100%;
}

.bottom-line {
  background: #3B3B3B;
  color: black;
  min-height: 1px;
  height: 1px;
  margin-top: 20px;
  margin-bottom: 30px;
}
.btn-redirect {
  border: 1px solid #3B3B3B;
  padding: 10px 15px;
  min-width: 200px;
}
.btn-redirect:hover {
  color: #ff8f5e;
}

</style>
