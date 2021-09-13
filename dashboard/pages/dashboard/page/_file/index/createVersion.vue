<template>
  <div>
    <v-app>
      <v-toolbar flat color="black" class="version-editor">
        <v-toolbar-title class="numericite-toolbar-title"><span><i @click="$router.go(-1)" class="ti-arrow-left arrow-icon"></i> </span>{{file.name}}</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
      </v-toolbar>
      <template>
        <no-ssr>
          <v-container class="grey lighten-5 container version-container">
            <v-flex
              no-gutters
              class="container-content"
            >
            <v-flex class="bigContainer">
              <div class="main-title" > Nom de la version </div>
              <v-combobox
                placeholder="Nom de la version"
                v-model="version.name"
                :items="versionNames"
                :class="{'error-input textarea-no-padding' : inputError}"
                attach
                hide-no-data
                outline
              ></v-combobox>
              <div v-if="inputError" class="error-msg">Veuillez renseigner un nom de version.</div>
            </v-flex>
              <v-flex bigContainer>
                <div class="main-title" > Modèles </div>
                <v-select
                  :items="file.versions"
                  item-value="id"
                  item-text="name"
                  v-model="version.model_id"
                  label=""
                  attach
                  outline
                ></v-select>
              </v-flex>
            </v-flex>
          </v-container>
          <v-layout class="btnContainer" row wrap align-end>
            <v-spacer></v-spacer>
            <v-btn @click="createVersion" class="save-btn">
              Créer
            </v-btn>
          </v-layout>
        </no-ssr>
      </template>
    </v-app>
  </div>
</template>
<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  export default {
    scrollToTop: true,
    name: 'DashboardPages',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    async asyncData({ app }) {
      try {
        let request = await app.$api.versions.getAvailableNames()
        const versionNames = request.data || []

        return  { versionNames }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        file: {},
        inputError: false,
        version: {
          name: '',
          isDefault: false,
          kind: 'file',
          parent_id: '',
          model_id: ''
        },
      }
    },

    computed: {
    },
    methods: {
      async createVersion() {
        if(this.version.name) {
          try {
            //CREATE VERSION
            let request = await this.$api.versions.create(this.version)
            const id = _.get(request.data, 'res.id', '')
            var pageTexts = ['']

            if (this.version.model_id && this.version.model_id !== '') {
              const modelVersion = _.find(this.file.versions, {id: this.version.model_id})
              pageTexts = _.get(_.find(modelVersion.pages, {status_name: 'open'}), 'texts', pageTexts)
            }
            // CREATE PAGE
            const page = {
              name: this.file.name,
              version_id: id,
              status_name: 'open',
              texts: pageTexts
            }
            request = await this.$api.pages.create(page)
            this.$router.push('/dashboard/page/' + this.$route.params.file + '/' + id)
          } catch (e) {
            console.log(e)
          }
        }
        else {
          this.inputError = true
        }
      },
    },
    created() {
      this.$emit('loadData', (file) => {
        this.file = file
        this.versionNames = _.difference(this.versionNames, _.map(this.file.versions, 'name'))
      })
      this.version.parent_id = this.$route.params.file
    }
  }
</script>
<style lang="scss">

.main-title {
  font-size: 26px;
  font-weight: 500;
  margin-bottom: 20px;
}
.nav>li>a:hover {
  background: transparent !important;
}
.bigContainer {
  .v-input__slot {
    border: 1px solid rgba(0,0,0,0.54) !important;
    border-radius: 0 !important;
    max-width: 600px;
    margin-bottom: 20px;
  }
  .textarea-no-padding {
    .v-input__slot {
      margin-bottom: 0 !important;
    }
  }
  input {
    margin-top: 12px !important;
    margin-bottom: 12px !important;
  }
}
  .container-content {
    padding: 50px;
    min-width: 100%;
  }
   .version-container {
    padding: 0 !important;
    margin: 0 !important;
    flex: 0 !important;
  }
  .v-select__selections {
    padding-top: 0 !important;
  }
  .btnContainer {
    flex: 0 !important;
    .save-btn {
      color: white !important;
      background: black !important;
      margin: 25px !important;
      min-width: 250px !important;
      cursor: pointer !important;
      pointer-events: initial !important;
    }
    .save-btn:hover {
      opacity: 0.75;
    }
  }
  .card {
    text-align: left !important;
  }
 .version-editor {
   .arrow-icon {
     margin-right: 12px;
     margin-top: 8px !important;
     border-radius: 50px;
     padding: 3px;
     cursor: pointer;
     border: 2px solid white !important;
     z-index: 20 !important;
   }
 }
  .error-msg {
    color: red !important;
    margin-bottom: 22px !important;
  }
  .error-input {
    .v-input__slot {
    border: 1px solid red !important;
    }
  }
</style>
