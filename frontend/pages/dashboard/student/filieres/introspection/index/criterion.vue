<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <span v-html="texts[1]" />
    </div>
    <div class="card rounded-lg mt-12 xl:w-5/6 xl:mx-6 mx-2 xl:p-8 p-4">


      <div class="flex mb-8 text-4xl text-blue-lighter font-bold sm:py-8">
        <div class="inline-block w-1/2">
          <span class="text-blue text-5xl font-bold"><span v-html="texts[2]" /></span>
        </div>
        <div class="inline-block w-1/2 text-right">
          <span class="text-blue text-6xl">{{ criteriaStep + 1 }}</span>
          <span>/</span>
          <span>3</span>
        </div>
      </div>

      <!-- Affirmations -->
      <div v-for="(criterion, key) in criteria.slice(criteriaStep * 10, (criteriaStep * 10) + 10)"
           :key="criterion.id">
        <div class="criterion w-full py-2 flex cursor-pointer"
             @click="changeValue(criterion)">

          <!-- checkbox -->
          <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
            <input :id="criterion.id"
                   v-model="criterion.value"
                   name="donnees"
                   type="checkbox"
                   @click="changeValue(criterion)">
            <label :for="criterion.id" />
          </div>
          <!-- End checkbox -->

          <!-- criterion -->
          <div class="content flex">
            <span class="font-bold xl:text-4xl text-2xl text-blue xl:mr-8 mr-3 sm:mt-1">{{ criterion.id + 1 }}</span>
            <p class="text-blue xl:text-lg text-base sm:mb-2">
              {{ criterion.text }}
            </p>
          </div>
          <!-- End criterion -->

        </div>
        <hr v-if="key !== 9"
            class="hr xl:mb-4 mb-2">
      </div>

      <div class="mt-3 text-right">
        <button v-scroll-to="'#actTitle'"
                :disabled="criteriaStep === 0"
                class="button button--blue mt-8 mx-3"
                @click="previousCriteriaStep">
          Précédents
        </button>
        <button v-scroll-to="'#actTitle'"
                :disabled="criteriaStep === 2"
                class="button button--blue mt-8 mx-3"
                @click="nextCriteriaStep">
          Suivants
        </button>
      </div>

      <!-- End criterion -->
    </div>
    <div id="printable-criteria">
      <div style="color: #432cb4;font-size:32px;margin-bottom:1em;font-weight:bold;">Mes critères de choix de filières</div>
      <div v-for="(criterion, key) in criteria" :key="key" class="">
        <div v-if="criterion.value" style="margin-bottom:15px;font-size:16px;">{{ key + 1 }} - {{ criterion.text }}</div>
      </div>
    </div>
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <span v-html="texts[3]" />
    </div>
    <nuxt-link to="/dashboard/student/filieres/introspection/details">
      <button :disabled="!sawAllCriteria" class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
    <button :disabled="!sawAllCriteria" class="button button--orange" @click="printCriteria()">Imprimer ma « fiche critères »</button>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import { CRITERIA } from '~/config'
  import _ from 'lodash'

  export default {
    name: 'IntrospectionCriterion',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    data() {
      return {
        criteria: CRITERIA,
        criteriaStep: 0,
        sawAllCriteria: false,
        texts: this.getPageTexts(this.$route.name)
      }
    },
    created() {
      const user_criterion_ids = this.$store.state.auth.user.config.criterion_ids
      if (user_criterion_ids) {
        this.criteria.forEach(function(criterion) {
          criterion.value = _.includes(user_criterion_ids, criterion.id)
        })
        this.sawAllCriteria  = true
      }
    },
    async destroyed() {
      const criterion_ids = _.map(_.filter(this.criteria, {value : true}), 'id')
      this.$store.commit('auth/setUserCriteria', criterion_ids)
      await this.$api.user.updateUser(this.$store.state.auth.user)
    },
    methods: {
      changeValue(criterion) {
        criterion.value = !criterion.value
      },
      nextCriteriaStep() {
        if (this.criteriaStep === 1 && !this.sawAllCriteria) {
          this.sawAllCriteria  = true
        }

        if (this.criteriaStep < 2) {
          this.criteriaStep++
        }
      },
      previousCriteriaStep() {
        if (this.criteriaStep > 0) {
          this.criteriaStep--;
        }
      },
      printCriteria() {
        let html="<html>";
        html+= document.getElementById('printable-criteria').innerHTML;

        html+="</html>";

        let printWin = window.open('','','left=0,top=0,toolbar=0,scrollbars=0,status  =0');
        printWin.document.write(html);
        printWin.document.close();
        printWin.focus();
        printWin.print();
        printWin.close();
      }
    }
  }
</script>

<style lang="scss" scoped>
  #printable-criteria {
     display: none;
  }

  @media print {
    #printable-criteria {
      display: block;
    }
  }
</style>
