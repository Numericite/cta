<template>
  <div class="userHero w-full bg-blue-darker relative overflow-hidden">
    <div class="flexContainer py-10 sm:py-12 flex">

      <svg class="hero__wave absolute pin-t pin-l z-0"
           width="508"
           height="255"
           viewBox="0 0 508 250">
        <path fill="#432CB4"
              fill-rule="nonzero"
              d="M0 0c58.667 5.931 98.583 30.08 119.749 72.444 31.749 63.548 83.108 136.63 222.108 133.072 92.667-2.372 147.938 12.08 165.815 43.355L0 250V0z" />
      </svg>

      <!-- Avatar -->
      <div class="avatar z-10 mb-6 mr-12 sm:mr-0 sm:ml-4">
        <div class="h-48 sm:h-20 w-48 sm:w-20 rounded-full border-8 sm:border-2 border-blue-lighter border-solid overflow-hidden flex justify-center items-start">
          <img v-if="$store.state.auth.user.avatar_path"
               :src="$store.state.auth.user.avatar_path"
               class="w-full"
               alt="Votre image de profil chez Crée ton avenir">
          <picture v-else
                   class="w-full h-auto">
            <source srcset="~/assets/img/profile.webp"
                    type="image/webp">
            <img src="~/assets/img/profile.png"
                 class="w-full h-auto"
                 alt="Image de profil par défaut">
          </picture>
        </div>
      </div>
      <!-- End Avatar -->

      <!-- Content -->
      <div class="flex w-full z-10 ml-8 sm:ml-0 items-center sm:relative">

        <div v-if="$store.state.auth.user"
             class="w-full flex justify-between sm:pl-8">
          <h1 class="text-white text-6xl sm:text-4xl font-extrabold sm:w-2/3">
            Bonjour {{ $store.state.auth.user.firstName }} {{ $store.state.auth.user.lastName }},
          </h1>
        </div>

        <div class="roletext w-full flex justify-end sm:pt-2 sm:absolute sm:pin-t">
          <h1 class="text-blue text-5.5xl sm:text-4xl font-extrabold">
            {{ $store.state.auth.user.config.accountType === 'parent' ? 'Parent' : 'Enseignant' }}
          </h1>
        </div>

      </div>
      <!-- End Content -->

    </div>
  </div>
</template>

<script>
export default {
  name: 'ManagerHero'
}
</script>

<style lang="scss" scoped>
.flexContainer {
  transform: translateY(7%);
  @screen sm {
  }
  .roletext {
    @screen sm {
      transform: translate(-10px, -30px);
    }
  }
}
.hero__wave {
  @screen sm {
  }
}
</style>
