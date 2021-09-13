<template>
  <div class="confirm w-full flex flex-col items-center flexContainer my-12">

    <h1 class="text-blue text-4xl text-center title font-extrabold">
      Bienvenue parmi nous !
    </h1>
    <h2 class="text-blue text-2xl mt-8 text-center">
      Nous vous avons envoyé un <span class="font-semibold">e-mail de confirmation.</span>
    </h2>
    <h2 class="text-blue text-2xl mt-2 text-center">
      Veuillez cliquer sur le lien contenu dans cet email pour valider votre inscription.
    </h2>

    <picture>
      <source srcset="~/assets/img/welldone-register.webp"
              type="image/webp">
      <img src="~/assets/img/welldone-register.png"
           class="mt-12"
           alt="Confirmation d'inscription !">
    </picture>
    <nuxt-link to="/"
               class="uppercase text-blue flex items-center font-bold sm:mt-4 sm:self-start">
      <a class="button button--orange mt-8">Retour à l'accueil</a>
    </nuxt-link>

  </div>
</template>

<script>
export default {
  name: 'Confirm',
  layout: 'options'
}
</script>
