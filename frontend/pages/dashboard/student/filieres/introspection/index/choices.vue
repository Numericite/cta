<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <div v-if="!$store.state.auth.user.config.introspection_stream_ids" class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-8 mb-6 text-justify">
      <span v-html="texts[1]" />
    </div>
    <div v-else class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-8 mb-6">
      <span v-html="texts[2]" />
    </div>
    <div class="card p-8 shadow rounded-xlg">
      <div v-for="(domain, key) in domains" :key="key" class="flex my-8 flex-col">
        <div class="flex w-full">
          <div :class="{'text-blue': domain.value, 'text-grey': !domain.value}" class="w-full font-bold text-5xl">
            {{ domain.name }}
          </div>
          <div class="float-right">
            <!-- checkbox -->
            <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
              <input :id="domain.id"
                     v-model="domain.value"
                     :disabled="$store.state.auth.user.config.introspection_stream_ids"
                     name="domainValue"
                     type="checkbox"
                     @click="changeValue(domain)">
              <label :for="domain.id" class="big"/>
            </div>
            <!-- End checkbox -->
          </div>
        </div>
        <div class="w-full pr-16 pt-3">
          <span v-show="domain.value && !$store.state.auth.user.config.introspection_stream_ids" class="text-blue font-bold">Cite les raisons princiaples</span>
          <textarea v-show="domain.value && !$store.state.auth.user.config.introspection_stream_ids" v-model="domain.reason" class="custom-textarea mt-2 text-peach" placeholder="Durée courte / facilité d'accès / bonnes débouchées..."/>
        </div>
      </div>
    </div>
    <div v-if="!$store.state.auth.user.config.introspection_stream_ids">
      <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-12 mb-6">
        Reporte-toi maintenant à ta liste de domaines et tes pistes d’activités ou de métiers.<br>
        Les filières que tu as identifiées sont-elles pertinentes par rapport à ce que tu as envie de faire ?<br><br>
        Quelles seraient alors les spécialités que tu devrais choisir ?<br><br>
        Avant de te décider de façon définitive, prends le temps de te poser ces quelques questions pour
        chaque filière/spécialité identifiée :<br><br>
        <ul>
          <li><span class="font-bold text-yellow text-5xl">1.</span> Quel est l’objectif de la formation ?</li>
          <li><span class="font-bold text-topaze text-5xl">2.</span> Quels sont les débouchés pour cette spécialisation ?</li>
          <li><span class="font-bold text-peach text-5xl">3.</span> Le diplôme obtenu après cette formation est-il reconnu ?</li>
          <li><span class="font-bold text-emerald text-5xl">4.</span> Quelles sont les perspectives en termes de poursuite d’études ou d’insertion professionnelle ?</li>
        </ul>
        <br>
        Quand tu es sûr(e) de tes choix, <span class="font-bold">clique sur valider mes choix de filières</span>.
      </div>
      <button :class="{'pointer-events-none opacity-25' : !domain_selected.length}" class="button button--blue mt-8" @click="validateChoices">Valider mes choix de filières</button>
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'

  export default {
    name: 'IntrospectionEducation',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    async asyncData({app, store}) {
      try {
        const chapters = store.state.auth.user.course.chapters
        let streamsChapter = _.find(chapters, { slug: 'stream' })

        let response = await app.$api.domains.getByIds({ids: streamsChapter.domain_ids, page: 1, numberPerPage: 1000})
        const domains = response.data || []

        const domain_ids = _.map(domains, 'id')
        response = await app.$api.details.getList({domain_ids: domain_ids, page: 1, numberPerPage: 1000})
        const details = response.data || []

        domains.forEach(function(domain) {
          domain.reason = ''
          domain.details = _.filter(details, {domain_id: domain.id})
        })

        return { domains }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        domains: [],
        domain_selected: [],
        texts: this.getPageTexts(this.$route.name)
      }
    },
    created() {
      const user_detail_ids = this.$store.state.auth.user.config.detail_ids
      const user_instrospection_stream_ids = this.$store.state.auth.user.config.introspection_stream_ids
      this.domains.forEach(function(domain) {
        domain.value = _.includes(user_instrospection_stream_ids, domain.id)
        domain.details.forEach(function(detail) {
          if (_.includes(user_detail_ids, detail.id)) {
            domain.reason = domain.reason.length < 1 ? detail.short_answer : domain.reason + ' / ' + detail.short_answer
          }
        })
      })
    },
    methods: {
      changeValue(domain) {
        if (!this.$store.state.auth.user.config.introspection_stream_ids) {
          domain.value = !domain.value
          if(!_.includes(this.domain_selected, domain.id)) {
            this.domain_selected.push(domain.id)
          }
          else {
            const index = this.domain_selected.indexOf(domain.id);
            if (index > -1) {
              this.domain_selected.splice(index, 1);
            }
          }
          this.$forceUpdate()
        }
      },
      async validateChoices() {
        const validated_stream_ids = _.map(_.filter(this.domains, function(domain) {
          return domain.value
        }), 'id')

        try {
          this.$store.commit('auth/setUserIntrospectionStreams', validated_stream_ids)
          await this.$api.user.updateUser(this.$store.state.auth.user)


          const experienceApi = this.$api.experiences
          const user_id = this.$store.state.auth.user.userID
          validated_stream_ids.forEach(async function(stream_id) {
            const experience = {
              domain_id: stream_id,
              user_id: user_id,
              kind: 'stream',
            };

            await experienceApi.create(experience)
          })

          const user = this.$store.state.auth.user
          if (this.stepCalculator(user.config.currentStep) < 6) {
            this.$store.commit('auth/setCurrentStep', 6)
            await this.$api.user.updateUser(this.$store.state.auth.user)
          }

          this.$toast.success('Filières validées', {
            position: 'bottom-right',
            duration: 3500
          })

          this.$router.go()
        } catch (e) {
          return 'error'
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
