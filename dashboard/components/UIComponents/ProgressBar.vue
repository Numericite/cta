<template>
  <div class="ProgressBar">
    <div class="progress-container">

      <div class="step step1">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>{{ userSteps['0'] || 0 }} élèves</span>
        </v-tooltip>
        <span class="right">Connaissance de soi</span>
      </div>

      <div class="step step2">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>{{ userSteps['1'] || 0 }} élèves</span>
        </v-tooltip>
        <span class="right">Exploration</span>
      </div>

      <div class="step step0">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="bigCircle" />
          </template>
          <span>{{ userSteps['2'] || 0 }} élèves</span>
        </v-tooltip>
        <span class="category">DOMAINES</span>
      </div>

      <div class="step step4">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>{{ userSteps['2'] || 0}} élèves</span>
        </v-tooltip>
        <span class="right">Connaissance de soi</span>
      </div>

      <div class="step step5">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>{{ userSteps['4'] || 0}} élèves</span>
        </v-tooltip>
        <span class="right">Exploration</span>
      </div>

      <div class="step step3">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="bigCircle" />
          </template>
          <span>{{ userSteps['5'] || 0 }} élèves</span>
        </v-tooltip>
        <span class="category">FILIÈRES</span>
      </div>

      <div class="step step4">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>0 élèves</span>
        </v-tooltip>
        <span class="right">Connaissance de soi</span>
      </div>

      <div class="step step5">
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <div v-on="on" class="circle" />
          </template>
          <span>0 élèves</span>
        </v-tooltip>
        <span class="right">Exploration</span>
      </div>

      <div class="step step3">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <div class="bigCircle" />
          </template>
          <!--<span>Chiffre P3</span>-->
        </v-tooltip>
        <span class="category">Établissements</span>
      </div>

      <div class="step final-step">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <div class="bigCircle" />
          </template>
          <!--<span>Chiffre P4</span>-->
        </v-tooltip>
        <span class="category">Parcours Sup.</span>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ProgressBar',
    props: {
      userSteps: {
        type: Object,
        default: () => {
          return {}
        }
      }
    },
    created() {
      console.log(this.userSteps)
    }
  }
</script>


<style lang="scss" scoped>

  .progress-container {
    margin: 20px 0;
    display: flex;
    width: 100%;
    background-color: orange;
    height: 7px;
    border-radius: 10px;
    position: relative;
  }

  .step {
    height: 100%;
    border-radius: 50%;
    position: relative;
    z-index: 0;

    .circle {
      position: absolute;
      right: 0;
      width: 20px;
      height: 20px;
      border-radius: 50%;
      border: 1px solid orange;
      background-color: orange;
      cursor: pointer;
      transform: translate(25%, -30%);
    }
    .bigCircle {
      position: absolute;
      right: 0;
      width: 30px;
      height: 30px;
      border: 1px solid orange;
      background-color: orange;
      cursor: pointer;
      border-radius: 50%;
      transform: translate(25%, -40%);
    }
    span {
      transform: translateY(2rem);
    }
    .right {
      font-size: 8px;
      position: absolute;
      right: 0;
      text-transform: uppercase;
      text-align: center;
      font-weight: bold;
      z-index: 10;
      transform: translate(50%, 2rem);
    }
    .category {
      position: absolute;
      right: 0;
      font-size: 14px;
      font-weight: bold;
      text-transform: uppercase;
      text-align: center;
      z-index: 20;
      transform: translate(45%, -250%);
    }
  }

  .step0,
  .step1,
  .step2,
  .step3,
  .step4,
  .step5 {
    width: 8.5%;
  }

  .final-step {
    width: 14%;
  }

  .text-smallest {
    font-size: 10px;
  }

  #activity-0 {
    background-color: #ff8f5e;
  }

  #activity-1 {
    background: linear-gradient(to left, #fff 2%, #ff8f5e 98%);
  }

  #activity-2 {
    background: linear-gradient(to left, #fff 16.6%, #ff8f5e 83.4%);
  }

  #activity-3 {
    background: linear-gradient(to left, #fff 33.3%, #ff8f5e 66.6%);
  }
  #activity-4 {
    background: linear-gradient(to left, #fff 50%, #ff8f5e 50%);
  }
  #activity-5 {
    background: linear-gradient(to left, #fff 66.6%, #ff8f5e 33.3%);
  }
  #activity-6 {
    background: linear-gradient(to left, #fff 83.4%, #ff8f5e 16.6%);
  }
  #activity-7 {
    background: linear-gradient(to left, #fff 98%, #ff8f5e 2%);
  }

  .v-tooltip__content {
    transform: translateX(-260px);
  }
</style>
