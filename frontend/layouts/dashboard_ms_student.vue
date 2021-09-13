<template>
  <div class="main-bg">
    <div>
      <svg class="fixed pin-r pin-b z-negative sm:hidden" width="528" height="846" viewBox="0 0 528 846" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g clip-path="url(#clip0)">
          <path :fill="getColor" d="M169 928C301 1020.5 647 985 647 985L925 234.5C925 234.5 843.5 73.4998 677.5 12.9998C511.5 -47.5002 230 117 159 201C88 285 -9 534.5 1 644.5C11 754.5 37 835.5 169 928Z" class="opacity-25"/>
          <path :stroke="getColor" d="M642.415 954.411C642.415 954.411 278.701 966.724 147.01 863.784C15.3179 760.844 4.83617 669.429 30.9641 559.009C57.0921 448.589 281.994 385.878 361.74 305.483C441.487 225.087 424.178 -110.415 725.703 105.615C1027.23 321.645 982.754 211.687 982.754 211.687L642.415 954.411Z" stroke-width="0.5"/>
          <path :fill="getColor" d="M726.715 964.306C726.715 964.306 380.715 999.806 248.715 907.306C116.715 814.806 101 725.806 119 615.806C137 505.806 347.5 429.806 418.5 345.806C489.5 261.806 452 -66.1943 753 126.306C908.873 225.992 970.81 245.898 994.301 241.919L1004.71 213.805C1004.71 213.805 1016.17 238.214 994.301 241.919L726.715 964.306Z" class="opacity-50"/>
          <path :stroke="getColor" d="M113.616 884.647C231.997 994.037 579.63 1005.33 579.63 1005.33L955.906 298.963C955.906 298.963 896.768 128.476 740.397 46.23C584.026 -36.0164 282.984 89.187 201.345 162.89C119.707 236.594 -9.92288 470.806 -14.7869 581.153C-19.6508 691.499 -4.76493 775.257 113.616 884.647Z" stroke-width="0.5" stroke-dasharray="10 10"/>
          <path :fill="getColor" d="M1061.83 400.386C1061.83 400.386 715.831 364.886 583.831 457.386C451.831 549.886 436.116 638.886 454.116 748.886C472.116 858.886 682.616 934.886 753.616 1018.89C824.616 1102.89 787.116 1430.89 1088.12 1238.39C1243.99 1138.7 1305.93 1118.79 1329.42 1122.77L1339.83 1150.89C1339.83 1150.89 1351.29 1126.48 1329.42 1122.77L1061.83 400.386Z"/>
        </g>
        <defs>
          <clipPath id="clip0">
            <rect width="528" height="846" fill="white" transform="matrix(1 0 0 -1 0 846)"/>
          </clipPath>
        </defs>
      </svg>
    </div>
    <NavbarMs :menuList="menuList" :accountType="accountType" class="w-screen z-20"/>
    <div :class="{'w-full' : $route.name === 'dashboard-college-student'}" class="ms-container mb-32">
      <nuxt/>
    </div>
  </div>
</template>

<script>
  import NavbarMs from '../components/middleSchool/NavbarMs'
  export default {
    name: 'DashboardMs',
    components: { NavbarMs },
    data() {
      return {
        menuList: [
          { name: 'Mon parcours', slug : 'parcours' },
          { name: 'Mes résultats', slug: 'resultats' },
          // { name: 'Mes badges', slug: 'badges' },
          { name: 'Mon programme', slug: 'programme' },
          { name: 'Mes pistes', slug : 'choix' },
        ],
        accountType: 'student'
      }
    },
    computed: {
      getColor() {
        if(this.$route.name.startsWith('dashboard-college-student-choix')) {
          return '#1DD4B6'
        }
        if(this.$route.name.startsWith('dashboard-college-student-programme-module')) {
          return '#27AAFF'
        }
        if(this.$route.name.startsWith('dashboard-college-student-programme-exploration')) {
          return '#FF9C9C'
        }
        else {
          return '#432CB4'
        }
      }
    }
  }
</script>


<style lang="scss">
.main-bg {
  background-image: url(/img/middle-school/confetti1.png);
}
/* width */
::-webkit-scrollbar {
  width: 3px;
}

/* Track */
::-webkit-scrollbar-track {
  background: transparent;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #686676;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555;
}
  //fill current


</style>
