<template>
  <div class="login fixed pin-t pin-l w-screen h-screen max-w-full h-screen flex justify-center items-center z-10">

    <div class="bg-black w-full h-screen z-10 opacity-50 absolute pin-t pin-l"
         @click="$emit('close')" />
    <!-- Modal -->
    <div class="login__modal relative w-1/3 sm:w-full bg-white rounded-lg px-10 py-16 z-20 sm:mx-4">

      <img class="absolute cursor-pointer pin-t pin-r m-6"
           src="~/static/icons/close-blue.svg"
           alt="fermer la fenêtre de connexion"
           @click="$emit('close')">

      <h3 class="text-blue font-extrabold text-4xl">Ravis de vous revoir</h3>

      <div class="form flex flex-col items-center justify-center mt-6">

        <!-- Email -->
        <div :class="{'field--active': user.email.length > 0}"
             class="field w-full">
          <label for="email"
                 class="pointer-events-none">Adresse email</label>
          <input id="email"
                 v-model="user.email"
                 type="text"
                 class="w-full">
        </div>
        <!-- End Email -->

        <!-- Password -->
        <div :class="{'field--active': user.password.length > 0}"
             class="field w-full relative">
          <label for="pswd"
                 class="pointer-events-none">Mot de passe</label>
          <input id="pswd"
                 v-model="user.password"
                 type="password"
                 class="w-full"
                 @keyup.enter="login">
        </div>
        <!-- End Password -->
        <nuxt-link to="/mot-de-passe-oublie"
                   class="cursor-pointer w-full text-right text-grey text-xs mt-6">Mot de passe oublié ?</nuxt-link>

        <!-- Button -->
        <button class="button button--orange mt-16"
                @click="login">
          Je me connecte
        </button>
        <!-- End Button -->

      </div>

    </div>
    <!-- End Modal -->

  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      user: {
        email: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      try {
        let request = await this.$api.user.login(this.user)
        const user = request.data.res.user
        this.$store.commit('auth/setUser', user)
        this.$store.commit('auth/setToken', request.data.res.token)

        if (user.config.accountType === 'student') {
          request = await this.$api.schools.getClassroomsByIds([user.config.classroom_id])
          const classroom = request.data[0]

          request = await this.$api.schools.getSchoolsByIds([classroom.school_id])
          const school = request.data[0]

          const course_id = classroom.course_id ? classroom.course_id : school.course_id ? school.course_id : ''

          if (course_id !== '') {
            request = await this.$api.courses.getByIds([course_id])
          } else {
            request = await this.$api.courses.getCourses({page: 1, numberPerPage: 1000})
          }

          const course = request.data[0]

          request = await this.$api.files.getFront({version_name: course.text_version_name})
          const pages = request.data

          this.$store.commit('setPages', pages)
          this.$store.commit('auth/setUser', user)
          this.$store.commit('auth/setSchool', school)
          this.$store.commit('auth/setClassroom', classroom)
          this.$store.commit('auth/setCourse', course)
        } else if (user.config.accountType === 'teacher' || user.config.accountType === 'operator') {

          // Matrix auth
          let userMatrix = await this.authUserToMatrix(user)

          this.$store.commit('auth/setUserMatrix', userMatrix)
          this.$store.commit('auth/setInitialSyncMatrix', [])
          this.$store.commit('auth/setSyncMatrix', [])

          request = await this.$api.schools.getSchoolsByIds([user.config.school_id])
          const school = request.data[0]
          this.$store.commit('auth/setSchool', school)
        } else if (user.config.accountType === 'partner') {
          request = await this.$api.partners.getPartnersByIds([user.config.partner_id])
          const partner = request.data[0]
          this.$store.commit('auth/setPartner', partner)
        }

        this.$api.loginLog.create({ user_id : user.userID })

        this.$router.push('/dashboard/student')

        this.$toast.success('Connexion réussie', {
          position: 'bottom-right',
          duration: 3500
        })

        return 'success'
      } catch (e) {
        console.log(e)
        this.$toast.error('Erreur : identitiants incorrects', {
          position: 'bottom-right',
          duration: 3500
        })
        return e
      }
    }

  }
}
</script>

<style>
.login {
  transition: opacity 0.3s ease;
}
.login__modal {
  box-shadow: 0 10px 30px 0 rgba(19, 6, 85, 0.5);
}
</style>
