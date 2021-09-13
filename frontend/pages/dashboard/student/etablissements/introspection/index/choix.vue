<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue pb-12">Mon choix d'établissements</h3>
    <div v-if="$store.state.auth.user.config.introspection_school_uais" class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mb-6 text-justify">
      <span>Tu as maintenant validé tes choix d'établissements ! Rendez-vous dans la partie "exploration" pour explorer ces établissements.</span>
    </div>
    <div class="card p-8 shadow rounded-xlg">
      <div v-for="(school, key) in schools" :key="key" class="flex my-8 flex-col">
        <div class="flex w-full">
          <div :class="{'text-blue': school.value, 'text-grey': !school.value}" class="w-full font-bold text-5xl">
            {{ school.school_name }}
          </div>
          <div class="float-right">
            <!-- checkbox -->
            <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
              <input :id="school.code_uai"
                     v-model="school.value"
                     :disabled="$store.state.auth.user.config.introspection_school_uais"
                     name="schoolValue"
                     type="checkbox"
                     @click="changeValue(school)">
              <label :for="school.code_uai" class="big"/>
            </div>
            <!-- End checkbox -->
          </div>
        </div>
      </div>
    </div>
    <div v-if="!$store.state.auth.user.config.introspection_school_uais" class="pt-20 flex flex-col">
      <div class="text-blue "> Quand tu es sûr(e) de tes choix, <span class="font-bold">clique sur valider mes choix d'établissements</span>.</div>
      <button :class="{'pointer-events-none opacity-25' : !school_selected.length}" class="button button--blue mt-5 w-1/3" @click="validateChoices">Valider mes choix d'établissements</button>
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'

  export default {
    name: 'IntrospectionSchool',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    async asyncData({app, store}) {
      try {
        let response = await app.$api.onisepFeedback.get({page: 1, numberPerPage: 1000, user_ids: [store.state.auth.user.userID]})
        const schools = response.data || []

        return { schools }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        schools: [],
        school_selected: []
      }
    },
    created() {
      const user_school_uais = this.$store.state.auth.user.config.introspection_school_uais
      this.schools.forEach(function(school) {
        school.value = _.includes(user_school_uais, school.code_uai)
      })
    },
    methods: {
      changeValue(school) {
        if (!this.$store.state.auth.user.config.introspection_school_uais) {
          school.value = !school.value
          if(!_.includes(this.school_selected, school.code_uai)) {
            this.school_selected.push(school.code_uai)
          }
          else {
            const index = this.school_selected.indexOf(school.code_uai);
            if (index > -1) {
              this.school_selected.splice(index, 1);
            }
          }
          this.$forceUpdate()
        }
      },
      async validateChoices() {
        try {
          const validated_school_uais = _.map(_.filter(this.schools, function(school) {
            return school.value
          }), 'code_uai')

          this.$store.commit('auth/setIntrospectionSchoolUais', validated_school_uais)
          await this.$api.user.updateUser(this.$store.state.auth.user)

          const experienceApi = this.$api.experiences
          const user_id = this.$store.state.auth.user.userID
          validated_school_uais.forEach(async function(code_uai) {
            const experience = {
              domain_id: code_uai,
              user_id: user_id,
              kind: 'school',
            };

            await experienceApi.create(experience)
          })

          this.$toast.success('Établissements validées', {
            position: 'bottom-right',
            duration: 3500
          })

          this.incrementCurrentStep()

          this.$router.go()
        } catch (e) {
          return 'error'
        }
      },
      async incrementCurrentStep() {
        const user = this.$store.state.auth.user
        if (this.stepCalculator(user.config.currentStep) < 10) {
          this.$store.commit('auth/setCurrentStep', 10)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .custom-textarea {
    min-height: 200px;
  }
</style>
