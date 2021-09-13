<template>
  <div class="activity__right xl:w-3/5 w-full xl:p-12 xl:pr-32 flex flex-col justify-center sm:pt-16 p-6">

    <h1 id="actTitle" class="title text-blue font-bold">
      Mes environnements favorables
    </h1>

    <div class="mt-16">
      <h2 class="title text-4xl text-blue font-bold mb-6">
        À quoi ça sert ?
      </h2>
      <p class="text-blue text-3xl text-justify">
        Nous l’avons vu précédemment, choisir un domaine est intéressant mais il faut
        <strong>avoir plus d’indices pour trouver le métier qui te correspond.</strong>
        En imaginant les environnements où il te plairait de travailler demain,
        tu recueilles d’autres pistes qui te seront très utiles quand tu devras choisir un métier..
      </p>
    </div>

    <div class="mt-16">
      <h2 class="title text-4xl text-blue font-bold mb-6">
        Détails de l'activité
      </h2>
      <p class="text-blue text-3xl text-justify">
        Nous allons te proposer de faire un bond de 7 ans dans le futur et <strong>de te projeter directement dans ton premier emploi.</strong>
        Tu y es ? Ferme les yeux et regarde mentalement ce que tu vois autour de toi.
      </p>
    </div>

    <button class="button button--blue mt-8 self-start"
            @click="$emit('startActivity')">
      Je commence l'activité
    </button>

  </div>
</template>

<script>
export default {
  created() {
    this.$emit( 'stopActivity' )
  },
  mounted() {
    document.getElementById('actTitle').scrollIntoView({
      behavior: 'smooth'
    });
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  h1 {
    font-size: 2.5rem;
  }
}
</style>
