<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <span v-html="texts[1]" />
    </div>
    <div v-for="(domain, key) in domains" :key="key" class="card rounded-xlg my-8 shadow-lg">
      <div :class="{'rounded-b-xlg': domain.collapse,'bg-orange': key % 3 === 2, 'bg-green': key % 3 === 1, 'bg-blue-sky': key % 3 === 0}" class="py-6 px-8 rounded-t-xlg text-3xl font-bold text-white text">
        {{ domain.name }}
        <span v-if="!domain.collapse"
              class="custom-collapser float-right w-10 h-10 border-white border-solid border-1 text-white rounded-full text-center text-4xl cursor-pointer pt-1 pr-1 minus-custom"
              @click="collapseDomain(domain, true)">
          ---
        </span>
        <span v-if="domain.collapse"
              class="custom-collapser float-right w-10 h-10 border-white border-solid border-1 text-white rounded-full text-center text-4xl cursor-pointer"
              @click="collapseDomain(domain, false)">
          ...
        </span>
      </div>
      <div v-show="!domain.collapse" class="p-6">
        <div v-for="(detail, key) in domain.details" :key="key" class="px-8 py-6">
          <div class="flex">
            <div class="w-1/3 text-blue font-bold">
              {{ detail.question }}
            </div>
            <div class="px-8 w-full">
              <div v-html="linkify(detail.answer)" />
            </div>
            <div class="w-1/6 float-right">
              <!-- checkbox -->
              <div class="checkbox w-8 h-8 xl:mr-10 mr-3 float-right">
                <input :id="detail.id"
                       v-model="detail.value"
                       name="detailValue"
                       type="checkbox"
                       @click="changeValue(detail)">
                <label :for="detail.id" class="big"/>
              </div>
              <!-- End checkbox -->
            </div>
          </div>
          <div v-if="key !== (domain.details.length - 1)"
               class="w-full h-px bg-blue-lighter mt-12 opacity-50"/>
        </div>
      </div>
    </div>
    <nuxt-link to="/dashboard/student/filieres/introspection/choices">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'


  export default {
    name: 'IntrospectionDetails',
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

        details.forEach(function(detail) {
          detail.answer = detail.answer.replace(/(?:\r\n|\r|\n)/g, '<br>')
        })

        domains.forEach(function(domain) {
          domain.collapse = true
          domain.details = _.filter(details, {domain_id: domain.id})
        })

        return { domains }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name)
      }
    },
    created() {
      const user_detail_ids = this.$store.state.auth.user.config.detail_ids
      if (user_detail_ids) {
        this.domains.forEach(function(domain) {
          domain.details.forEach(function(detail) {
            detail.value = _.includes(user_detail_ids, detail.id)
          })
        })
      }
    },
    async destroyed() {
      const detail_ids = []

      this.domains.forEach(function(domain) {
        domain.details.forEach(function(detail) {
          if (detail.value) {
            detail_ids.push(detail.id)
          }
        })
      })

      this.$store.commit('auth/setUserDetails', detail_ids)
      await this.$api.user.updateUser(this.$store.state.auth.user)
    },
    methods: {
      changeValue(detail) {
        detail.value = !detail.value
      },
      collapseDomain( domain, value ) {
        domain.collapse = value
        this.$forceUpdate()
      },
      linkify(inputText) {
        var replacedText, replacePattern1, replacePattern2, replacePattern3;

        //URLs starting with http://, https://, or ftp://
        replacePattern1 = /(\b(https?|ftp):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
        replacedText = inputText.replace(replacePattern1, '<a href="$1" target="_blank">$1</a>');

        //URLs starting with "www." (without // before it, or it'd re-link the ones done above).
        replacePattern2 = /(^|[^\/])(www\.[\S]+(\b|$))/gim;
        replacedText = replacedText.replace(replacePattern2, '$1<a href="http://$2" target="_blank">$2</a>');

        //Change email addresses to mailto:: links.
        replacePattern3 = /(([a-zA-Z0-9\-\_\.])+@[a-zA-Z\_]+?(\.[a-zA-Z]{2,6})+)/gim;
        replacedText = replacedText.replace(replacePattern3, '<a href="mailto:$1">$1</a>');

        return replacedText;
      }
    }
  }
</script>

<style lang="scss" scoped>
  .minus-custom {
    letter-spacing: -3px;
    padding-right: 1px;
  }

  .custom-collapser {
    position: relative;
    bottom: 9px;
  }

  .checkbox {
    position: relative;
    bottom: 10px;
  }
</style>
