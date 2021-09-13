<template>
  <div id="how"
       :class="{'onView' : changed}"
       class="steps w-full bg-blue mt-32 flex flex-col sm:mt-16">

    <!-- Tabs -->
    <div class="steps__top bg-blue-darker flexContainer pt-8 w-full">
      <h3 class="text-6xl sm:text-5xl xsm:text-4xl title font-extrabold text-white mb-12">
        <span v-html="title" />
      </h3>
      <div class="xl:w-1/2 flex justify-between pt-4 appear">
        <div :class="{'step--active': currentStep == 1}"
             class="text-white text-center cursor-pointer flex flex-col items-center"
             @click="changeTab(1)">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="40"
               height="42"
               viewBox="0 0 40 42">
            <g fill="none"
               fill-rule="evenodd">
              <path d="M0 0h40v42H0z" />
              <path class="lines"
                    fill="#FFF"
                    d="M20 5.727c1.071 0 1.905.835 1.905 1.91a.93.93 0 0 0 0 .089v10.888a.954.954 0 0 0 1.696.596c.561-.7 1.242-1.55 2.113-1.55 1.072 0 1.905.834 1.905 1.908a.954.954 0 0 0 1.25.895c.804-.269 2.157-.788 3.14-.835.491-.024.816.056.982.194.167.138.342.402.342 1.178 0 .199.016.399.12.641.103.243.35.51.624.627.547.234.863.065 1.102-.015.477-.16.853-.298 1.011-.298 1.072 0 1.905.835 1.905 1.909v8.113c0 3.677-.797 5.546-1.964 6.593-1.16 1.038-2.912 1.411-5.179 1.52H20.476c-1.564 0-2.764-.532-3.869-1.4-1.105-.87-2.078-2.102-3.02-3.446-1.885-2.69-3.563-5.886-6.593-7.025-1.84-.692-2.656-1.27-2.946-1.596-.29-.327-.238-.373-.238-.85 0-.353.078-.489.282-.671.204-.184.59-.366 1.087-.492.991-.253 2.36-.27 3.392-.27 1.667 0 2.43.547 3.274 1.224.845.677 1.822 1.64 3.393 1.64.794 0 1.47-.463 1.86-.984.39-.52.592-1.105.73-1.655.274-1.1.267-2.133.267-2.133V7.636c0-1.074.834-1.909 1.905-1.909M34.286 1.91a3.8 3.8 0 0 1 3.81 3.818 3.8 3.8 0 0 1-3.81 3.818 3.8 3.8 0 0 1-3.81-3.818 3.8 3.8 0 0 1 3.81-3.818M20 1.91a3.8 3.8 0 0 1 3.81 3.818c0 .358-.057.71-.164 1.045C23.25 5.097 21.787 3.818 20 3.818a.924.924 0 0 0-.089 0 .95.95 0 0 0-.283.075c-1.58.164-2.894 1.27-3.304 2.759a3.4 3.4 0 0 1-.134-.925A3.8 3.8 0 0 1 20 1.91m-14.286 0a3.8 3.8 0 0 1 3.81 3.818 3.8 3.8 0 0 1-3.81 3.818 3.8 3.8 0 0 1-3.81-3.818 3.8 3.8 0 0 1 3.81-3.818M34.286 0C31.14 0 28.57 2.575 28.57 5.727s2.57 5.728 5.715 5.728C37.43 11.455 40 8.879 40 5.727 40 2.575 37.43 0 34.286 0M20 0c-3.145 0-5.714 2.575-5.714 5.727 0 1.588.706 2.984 1.696 3.997.061.064.132.12.208.165v12.543s-.006.86-.208 1.67c-.1.405-.256.775-.402.97-.146.194-.183.223-.342.223-.81 0-1.26-.468-2.202-1.223-.942-.754-2.322-1.64-4.465-1.64-1.031 0-2.519-.016-3.869.328-.674.172-1.33.422-1.875.91a2.861 2.861 0 0 0-.922 2.103c0 .477.052 1.37.714 2.118.662.746 1.75 1.377 3.72 2.118 2.069.777 3.725 3.54 5.685 6.338.98 1.4 2.05 2.78 3.407 3.848C16.79 41.264 18.47 42 20.476 42h10.521c2.43-.114 4.713-.475 6.414-1.998 1.7-1.525 2.589-4.066 2.589-8.025v-8.113c0-2.099-1.716-3.819-3.81-3.819-.446 0-.798.09-1.086.194-.132-.772-.394-1.455-.908-1.879-.681-.563-1.518-.678-2.276-.641-1.01.048-1.906.333-2.649.596-.523-1.485-1.902-2.565-3.557-2.565-.77 0-1.38.27-1.904.611V9.903a.947.947 0 0 0 .208-.179c.99-1.013 1.696-2.409 1.696-3.997C25.714 2.575 23.144 0 20 0M5.714 0C2.57 0 0 2.575 0 5.727s2.57 5.728 5.714 5.728c3.145 0 5.715-2.576 5.715-5.728C11.429 2.575 8.859 0 5.714 0" />
            </g>
          </svg>
          <h5 class="text-white font-extrabold mt-3 uppercase"><span v-html="step1.title" /></h5>
          <div class="icon mt-8 mx-0" />
        </div>
        <div :class="{'step--active': currentStep == 2}"
             class="text-white text-center cursor-pointer flex flex-col items-center"
             @click="changeTab(2)">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="57"
               height="42"
               viewBox="0 0 57 42">
            <g fill="none"
               fill-rule="evenodd">
              <path class="lines"
                    fill="#FFF"
                    d="M18.168 31.39C20.382 37.558 27.135 42 35.068 42c4.655 0 9.144-1.63 11.932-3.942l7.894 1.82c.86.198 1.508-.806 1-1.554l-4.233-6.224A12.515 12.515 0 0 0 52.731 27c0-6.926-5.486-12.698-12.914-14.452.261.718.462 1.462.594 2.222 6.073 1.816 10.386 6.636 10.386 12.23 0 1.658-.375 3.274-1.102 4.786a1.02 1.02 0 0 0 .075 1.02l3.097 4.554-5.879-1.358a.94.94 0 0 0-.814.196c-2.927 2.416-6.88 3.8-11.107 3.8-6.643 0-12.288-3.402-14.604-8.17-.78-.104-1.547-.25-2.295-.438" />
              <path d="M0 0h42.536v34H0z" />
              <path class="lines"
                    fill="#FFF"
                    d="M23.116 2c9.691 0 17.487 6.758 17.487 15s-7.796 15-17.487 15c-4.687 0-9.074-1.59-12.33-4.368a.946.946 0 0 0-.83-.202l-6.777 1.61 3.613-5.498c.194-.294.22-.674.072-.996A13.093 13.093 0 0 1 5.629 17c0-8.242 7.795-15 17.487-15M4.906 22.856L.17 30.036c-.495.752.158 1.744 1.017 1.538l8.75-2.082C13.498 32.372 18.162 34 23.116 34c10.693 0 19.42-7.568 19.42-17S33.809 0 23.116 0C12.424 0 3.696 7.568 3.696 17c0 2.226.49 4.376 1.21 5.856" />
              <path class="lines"
                    fill="#FFF"
                    d="M32.87 15c.534 0 .967-.448.967-1s-.433-1-.967-1H13.537c-.534 0-.967.448-.967 1s.433 1 .967 1H32.87zM27.07 21c.534 0 .967-.448.967-1s-.433-1-.967-1H13.537c-.534 0-.967.448-.967 1s.433 1 .967 1H27.07z" />
            </g>
          </svg>

          <h5 class="text-white font-extrabold mt-3 uppercase"><span v-html="step2.title" /></h5>
          <div class="icon mt-8 mx-0" />
        </div>
        <div :class="{'step--active': currentStep == 3}"
             class="text-white text-center cursor-pointer flex flex-col items-center"
             @click="changeTab(3)">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="41"
               height="42"
               viewBox="0 0 47 49">
            <g fill="none"
               fill-rule="evenodd">
              <path class="lines"
                    fill="#FFF"
                    d="M27.313 34.108l-5.372 5.348-1.682-1.675a.654.654 0 0 0-.923 0 .647.647 0 0 0 0 .92l2.143 2.134a.653.653 0 0 0 .923 0l5.833-5.808a.646.646 0 0 0 0-.92.652.652 0 0 0-.922.001" />
              <path d="M0 .65h47v46.795H0z" />
              <path class="lines stroke"
                    fill="#FFF"
                    stroke="#FFF"
                    stroke-width=".9"
                    d="M23.5 26.521c-4.471 0-8.11-3.622-8.11-8.074s3.639-8.074 8.11-8.074c4.472 0 8.11 3.622 8.11 8.074s-3.638 8.074-8.11 8.074m0-24.571c12.238 0 22.195 9.912 22.195 22.096 0 6.414-2.761 12.195-7.159 16.235-.54-6.41-5.135-11.689-11.22-13.272 3.294-1.461 5.6-4.746 5.6-8.562 0-5.168-4.224-9.374-9.416-9.374-5.191 0-9.415 4.206-9.415 9.374 0 3.816 2.305 7.1 5.599 8.562-6.084 1.583-10.68 6.862-11.22 13.273-4.397-4.04-7.158-9.822-7.158-16.236C1.306 11.862 11.262 1.95 23.5 1.95m10.6 41.506c-.069.037-.14.071-.21.107-.21.112-.423.221-.638.327-.162.078-.325.153-.488.228a21.506 21.506 0 0 1-1.214.513c-.242.094-.484.185-.73.27l-.07.025a22.34 22.34 0 0 1-3.392.876c-.07.012-.14.026-.21.037-.22.036-.44.067-.66.097a23.21 23.21 0 0 1-.956.11c-.188.017-.378.03-.568.044-.144.01-.287.02-.43.027-.342.015-.686.026-1.032.026H23.5c-.347 0-.692-.01-1.035-.026-.142-.007-.283-.018-.426-.027a19.577 19.577 0 0 1-1.535-.154c-.217-.03-.434-.06-.65-.097-.075-.012-.149-.026-.223-.04a22.303 22.303 0 0 1-3.365-.868l-.097-.032a24.335 24.335 0 0 1-.71-.262l-.25-.099a22.01 22.01 0 0 1-.6-.25c-.13-.057-.26-.117-.39-.176a17.839 17.839 0 0 1-1.132-.558c-.061-.033-.123-.063-.183-.096a22.193 22.193 0 0 1-3.188-2.112c.11-7.476 6.251-13.524 13.786-13.524 7.521 0 13.653 6.026 13.783 13.525a22.461 22.461 0 0 1-3.183 2.109M8.622 42.14c.011.01.016.026.029.037.846.687 1.739 1.31 2.666 1.869l.03.018c.282.17.568.333.857.49.1.055.2.108.3.161a25.385 25.385 0 0 0 1.07.532c.137.063.273.125.411.185.208.092.417.183.629.27l.224.088c1.434.568 2.936.999 4.491 1.277.087.016.173.032.26.046a22.974 22.974 0 0 0 1.132.162 20.565 20.565 0 0 0 1.21.111c.155.01.31.023.466.03.367.017.736.028 1.104.028.37 0 .737-.01 1.105-.028.156-.007.31-.02.466-.03.201-.014.402-.028.602-.046a23.104 23.104 0 0 0 1.028-.12c.238-.032.475-.068.71-.107a23.345 23.345 0 0 0 4.754-1.326l.218-.085a25.14 25.14 0 0 0 1.52-.684c.198-.097.394-.198.59-.3.101-.055.203-.107.302-.162.29-.158.575-.322.858-.491l.029-.017c.927-.56 1.82-1.182 2.666-1.87.017-.013.03-.032.045-.048C43.644 37.835 47 31.324 47 24.046 47 11.146 36.458.65 23.5.65S0 11.145 0 24.046c0 7.285 3.363 13.802 8.622 18.095" />
            </g>
          </svg>

          <h5 class="text-white font-extrabold mt-3 uppercase"><span v-html="step3.title" /></h5>
          <div class="icon mt-8 mx-0" />
        </div>
      </div>
    </div>
    <!-- End Tabs -->

    <!-- Content -->
    <div class="steps__top bg-blue flexContainer pt-8 w-full flex xl:justify-between sm:flex-col">

      <!-- Left part -->
      <div v-if="currentTitle && currentContent"
           class="xl:w-2/5 xl:mb-16 w-1/2 md:pr-8 sm:w-full">
        <transition name="fade">
          <div v-if="currentStep > 0">
            <h4 class="text-white text-2xl font-extrabold appear">
              <span v-html="currentTitle" />
            </h4>
            <div class="h-auto min-h-48">
              <p class="text-2xl text-white mt-6 appear text-justify">
                <span v-html="currentContent" />
              </p>
            </div>
          </div>
        </transition>
      </div>
      <!-- End Left part -->

      <!-- Right part - video -->
      <div class="xl:w-2/5 w-1/2 flex justify-end xl:items-center items-start xl:mb-0 mb-8 sm:w-full appear">
        <!--<videoComp src="https://archive.org/download/BigBuckBunny_124/Content/big_buck_bunny_720p_surround.mp4"-->
        <!--poster="https://peach.blender.org/wp-content/uploads/title_anouncement.jpg?x11217" />-->
      </div>
      <!-- End Right part - video -->

    </div>
    <!-- End Content -->

  </div>

</template>

<script>
import videoComp from '~/components/Video'

export default {
  name: 'Steps',
  components: { videoComp },
  props: {
    title: {
      type: String,
      default: ''
    },
    step1 : {
      type: Object,
      default: () => { return {} }
    },
    step2 : {
      type: Object,
      default: () => { return {} }
    },
    step3 : {
      type: Object,
      default: () => { return {} }
    }
  },
  data() {
    return {
      currentStep: 1,
      changed: false
    }
  },
  computed: {
    currentTitle: function() {
      switch (this.currentStep) {
        case 1:
          return this.step1.title
        case 2:
          return this.step2.title
        case 3:
          return this.step3.title
      }
    },
    currentContent: function() {
      switch (this.currentStep) {
        case 1:
          return this.step1.text
        case 2:
          return this.step2.text
        case 3:
          return this.step3.text
      }
    }
  },
  methods: {
    changeTab: function(tab) {
      this.currentStep = tab
      this.changed = true
    }
  }
}
</script>

<style lang="scss" scoped>
.step--active svg .lines {
  fill: #ff8f5e;
}
.step--active svg .stroke {
  stroke: #ff8f5e;
}
.step--active h5 {
  @apply text-orange;
}

.step--active {
  .icon {
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 0 23.5px 23px 23.5px;
    border-color: transparent transparent #432cb4 transparent;
  }
}
.video {
  width: 100% !important;
  height: auto !important;
  display: flex;
  @apply rounded-lg;
  @screen xl {
    transform: translateY(-50%);
  }
}
.changed {
  h3 span {
    opacity: 1;
  }
  .appear--right {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
