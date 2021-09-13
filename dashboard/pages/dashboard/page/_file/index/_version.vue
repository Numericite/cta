<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title"><span><i @click="$router.go(-1)" class="ti-arrow-left arrow-icon"></i> </span>{{file.name}} ({{currentVersion.name}})</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
      </v-toolbar>
      <template @loadData="loadData">
        <no-ssr>
          <v-container style="flex: 1 1 auto;" class="grey lighten-5 container pageEditor">
            <v-layout
              v-for="n in 1"
              :key="n"
              :class="n === 1 ? 'mb-6' : ''"
              no-gutters
              class="card"
              :justify="justify"
              :alignment="alignment"
            >
              <v-flex xs2 class="card history">
                <v-flex class="history-version-btn">
                  <v-card
                    class="pa-2 history-button"
                    outlined
                    tile
                    @click="historydialog = true"
                  >
                    <v-layout class="align">
                      <v-dialog content-class="dialogBox"  v-model="historydialog" max-width="600px" persistent>
                        <template v-slot:activator="{ on }">
                          <i class="ti-timer icon"></i>
                          <div v-on="on">Historique</div>
                        </template>
                        <v-card class="dialog">
                          <div class="close-modal" @click="historydialog = false">
                            <div class="icon-close">
                              <i class="ti-close"></i>
                            </div>
                          </div>
                          <v-layout>
                            <v-flex class="icon-col">
                              <i class="ti-timer icon icon-modal"></i>
                            </v-flex>
                            <v-flex>
                              <v-card-title class="title-container">
                                <span class="headline modal_title">Historique</span>
                              </v-card-title>
                              <div class="subtitle-1">version: <span class="font-weight-bold">{{currentVersion.name}}</span></div>
                              <v-flex class="history-container" style="padding-right: 20px">
                                <v-layout v-for="(history, index) in currentVersion.pages" v-if="index <= 5" :key="index" class="history-row" align-center>
                                  <v-flex>{{ history.created_date | displayDate }}</v-flex>
                                  <v-flex class="date">à {{ history.created_date | displayTime }}</v-flex>
                                  <v-flex class="preview">
                                    <v-layout class="align">
                                      <a @click="openPreviewDialog(index, history)">Aperçu</a>
                                      <v-dialog v-if="previewDialog && activePreview === index" content-class="previewDialogBox" v-model="previewDialog" max-width="800px" persistent>
                                        <div class="close-modal close-modal-preview" @click="previewDialog = false">
                                          <div class="icon-close">
                                            <i class="ti-close"></i>
                                          </div>
                                        </div>
                                        <v-flex class="bloc-container">
                                          <v-card
                                            class="pa-2 card-top-title"
                                            outlined
                                            tile
                                          >
                                            Texte original
                                            <v-layout style="display: flex; justify-content: center;">{{ history.created_date | displayDate }} à {{ history.created_date | displayTime }} </v-layout>
                                          </v-card>
                                          <div v-html="history.texts[index]"
                                               v-for="(text, index) in history.texts"
                                               :key="index"
                                               :class="{'first': index === 0}"
                                               class="text-field big-text disabled-text" />
                                        </v-flex>
                                      </v-dialog>
                                    </v-layout>
                                  </v-flex>
                                  <v-btn v-if="history.status_name === 'open'" disabled color="gray" class="restore" >Défaut</v-btn>
                                  <v-btn v-else color="black" class="white--text restore" @click="restore(history), historydialog = false">Rétablir</v-btn>
                                </v-layout>
                              </v-flex>
                            </v-flex>
                          </v-layout>
                        </v-card>
                      </v-dialog>
                    </v-layout>
                  </v-card>
                  <v-card
                    class="pa-2 version-button"
                    outlined
                    tile
                    @click="versiondialog = true"
                  >
                    <v-layout class="align">
                      <v-dialog content-class="dialogBox" v-model="versiondialog" max-width="600px" persistent>
                        <template v-slot:activator="{ on }">
                          <i class="ti-view-list-alt icon"></i>
                          <div>Versions</div>
                        </template>
                        <v-card class="dialog">
                          <div class="close-modal" @click="versiondialog = false">
                            <div class="icon-close">
                              <i class="ti-close"></i>
                            </div>
                          </div>
                          <div class="new-btn-container">
                            <v-btn class="new-btn" @click="$router.push('/dashboard/page/' + $route.params.file + '/createVersion')">Nouveau</v-btn>
                          </div>
                          <v-layout>
                            <v-flex class="icon-col">
                              <i class="ti-view-list-alt icon icon-modal"></i>
                            </v-flex>
                            <v-flex>
                              <v-card-title class="title-container">
                                <span class="headline modal_title">Versions</span>
                              </v-card-title>
                              <v-flex class="history-container" style="padding-right: 20px">
                                <v-layout v-for="(version, index) in file.versions" v-if="index <= 5" :key="index" class="history-row" align-center>
                                  <v-tooltip left>
                                    <template v-slot:activator="{ on }">
                                      <i v-if="version.isDefault" v-on="on" class="ti-star star-icon"></i>
                                    </template>
                                    <span class="default-tooltip">Version par defaut</span>
                                  </v-tooltip>
                                  <v-flex class="version-name">{{version.name}}</v-flex>
                                  <v-flex class="date">{{version.created_date | displayDate}}</v-flex>
                                  <v-btn v-if="$route.params.version !== file.versions[index].id" @click="editVersion(version)" color="black" class="white--text restore">Modifier</v-btn>
                                  <v-btn v-else disabled color="gray" class="restore" >Modifier</v-btn>
                                </v-layout>
                              </v-flex>
                            </v-flex>
                          </v-layout>
                        </v-card>
                      </v-dialog>
                    </v-layout>
                  </v-card>
                </v-flex>
              </v-flex>
              <!--              SECOND COLUMN BEGIN -->
              <v-flex class="inputlist">
                <v-layout>
                  <v-flex class="bloc-container">
                    <v-card
                      class="pa-2 card-top-title"
                      outlined
                      tile
                    >
                      Texte original
                    </v-card>
                    <div v-html="currentPage.texts[index]"
                         v-for="(text, index) in currentPage.texts"
                         :key="index"
                         :class="{'first': index === 0}"
                         class="text-field big-text disabled-text" />
                  </v-flex>
                  <v-flex class="bloc-container">
                    <v-card
                      class="pa-2 card-top-title"
                      outlined
                      tile
                    >
                      Texte modifié
                    </v-card>
                    <v-layout v-for="(text, index) in currentText" :key="index" class="textarea-container">
                      <v-flex>
                        <text-page v-model="currentText[index]"></text-page>
                      </v-flex>
                      <v-flex class="delete-icon">
                        <v-btn v-show-numericite round depressed small @click.stop="deleteText(index)">
                          <v-icon
                            small
                          >
                            delete
                          </v-icon>
                        </v-btn>
                      </v-flex>
                    </v-layout>
                    <v-layout>
                      <v-btn @click="addText" class="btn-add">
                        <i class="ti-plus"></i>
                      </v-btn>
                    </v-layout>
                  </v-flex>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-container>
          <v-layout row wrap align-end>
            <v-spacer></v-spacer>
            <v-btn class="save-btn" :class="{'save-btn-disabled' : !activeBtn }" @click="saveModification">
              Enregistrer
            </v-btn>
          </v-layout>
          <v-layout v-if="textChanged" class="no-change" row wrap align-end>
            <v-spacer></v-spacer>
            Aucun changement n'a été effectué
          </v-layout>
        </no-ssr>
      </template>
    </v-app>
  </div>
</template>
<script>
  import _ from 'lodash'
  import TextPage from '~/components/TextPage'
  export default {
    scrollToTop: true,
    name: 'DashboardPages',
    data () {
      return {
        activePreview : 0,
        textChanged: false,
        previewDialog: false,
        activeBtn: true,
        currentText: '',
        currentVersion: {},
        currentPage: {},
        newItem: '',
        file: {},
        version: {
          id: '',
          name: '',
          isDefault: '',
          parent_id: '',
        },
        page: {
          name: '',
          version_id: '',
          status_name: '',
          texts: []
        },
        pagination: {
          page: 1,
          rowsPerPage: 4
        },
        total: 0,
        rowsPerItems: [10,25,50],
        historydialog: false,
        versiondialog: false,
        edited: '',
        alignment: 'end',
        justify: 'center',
      }
    },

    computed: {
    },
    methods: {
      init() {
        this.currentVersion = _.find(this.file.versions, {'id' : this.$route.params.version})
        this.currentPage = _.find(this.currentVersion.pages, {'status_name' : 'open'})
        this.currentText = _.clone(this.currentPage.texts)
      },
      updatePagesList(id, texts, version_id) {
        this.currentVersion.pages.unshift({
          id: id,
          texts: texts,
          status_name: 'open',
          version_id: version_id,
          created_date: new Date().getTime()
        })
        this.currentVersion.pages.forEach((page) => {
          if(page.id !== id) {
            page.status_name = 'closed'
          }
        })
      },
      openPreviewDialog(index, history) {
          this.activePreview = index
          this.previewDialog = true
      },
      close() {
        this.dialog = false
      },
      addText() {
        this.currentText.push(this.newItem)
      },
      deleteText(index) {
        this.currentText.splice(index, 1)
      },
      async saveModification() {
            if(!_.isEqual(this.currentText, this.currentPage.texts)) {
              this.textChanged = false
            try {
              //CREATE
              this.page.name = this.file.name
              this.page.version_id = this.currentVersion.id
              this.page.texts = this.currentText
              const request = await this.$api.pages.create(this.page)
              const id = _.get(request.data,'res.id', '')
              this.updatePagesList(id, this.page.texts, this.page.version_id)
              this.init()
            }
            catch (e) {
              console.log(e)
            }
           }
            else {
              this.textChanged = true
            }
      },
      async restore(history) {
        try {
          //CREATE
          const request = await this.$api.pages.create(history)
          const id = _.get(request.data,'res.id', '')
          this.updatePagesList(id, history.texts, history.version_id)
          this.init()
        }
        catch (e) {
          console.log(e)
        }
      },
       editVersion(version) {
        this.versiondialog = false
         this.$router.push('/dashboard/page/' + this.$route.params.file + '/' + version.id )
      },
    },
     created() {
       this.$emit('loadData', (file) => {
         this.file = file
         this.init()
      })
    },
    components: {
      TextPage
    },
    watch: {
      dialog(val) {
        val || this.close()
      },
    }
  }
</script>
<style lang="scss">
  .container {
    padding: 0 !important;
    min-width: 100%;
    height: 100%;
    flex: 1 1 auto !important;
  }
  .star-icon {
    margin-right: 12px;
    position: absolute;
    left: 24px;
    cursor: pointer;
  }
  .close-modal, .close-modal-preview {
    width: 50px;
    height: 50px;
    background: black;
    color: black;
    /*border-radius: 50px !important;*/
    border-top-left-radius: 50px !important;
    border-bottom-left-radius: 50px !important;
    border-top-right-radius: 50px !important;
    border-bottom-right-radius: 50px !important;
    border: 3px solid white;
    position: absolute;
    right: -10px;
    top: -10px;
    z-index: 10;
  }
  .close-modal-preview {
    right: 0px !important;
    top: 0 !important;
  }

  .no-change {
    color: red;
    margin-right: 12px !important;
    margin-bottom: 12px;
  }
  .new-btn-container {
    background: black;
    position: absolute;
    top: 0;
    right: 0px;
    min-width: 150px;
    border-bottom-left-radius: 10px;
  }
  .new-btn {
    background: black !important;
    color: white !important;
  }
  .date {
    color: gray;
  }
  .icon-close {
    color: white;
    font-size: 20px;
    position: relative;
    text-align: center;
    margin-top: 8px;
    cursor: pointer;
  }
  .save-btn {
    color: white !important;
    background: black !important;
    margin: 25px !important;
    min-width: 250px !important;
    cursor: pointer !important;
  }
  .save-btn:hover {
    opacity: 0.65;
  }
  .save-btn-disabled {
    pointer-events: none !important;
    background: lightgray !important;
  }
  .inputlist textarea {
    border: 1px solid gray !important;
    min-width: 95%;
    margin: 20px !important;
    text-align: left !important;
  }
  .inputlist .textarea-container {
    align-items: center;
    align-content: center;
  }
  .inputlist .delete-icon {
    flex: 0 !important;
  }
  .disabled-text {
    max-height: 300px;
    overflow-y: auto;
    border: 1px solid grey;
    margin: 40px 44px;
    padding: 15px;
    text-align: left;
    color: grey;

    &.first {
      margin-top: 20px;
    }

    * {
      font-size: 14px !important;
      margin: 0;
    }

    strong {
      color: grey;
    }
  }
  .editable-text ::placeholder{
    color: black !important;
  }
  .card {
    min-height: 100%;
    text-align: center;
    margin-bottom: 0;
  }
  .arrow-icon {
    margin-right: 12px;
    margin-top: 8px !important;
    border-radius: 50px;
    padding: 3px;
    cursor: pointer;
    border: 2px solid white !important;
    z-index: 20 !important;
  }
  .pageEditor {
    flex: 1 1 auto !important;
    .v-input__slot {
      border: 1px solid rgba(0,0,0,0.54) !important;
      border-radius: initial !important;
    }
    .btn-add {
      background: white !important;
      color: black !important;
      border-radius: 50px;
      font-size: 14px;
      width: 40px;
      height: 40px;
      margin: 10px 0 20px 4px;
    }
    .bloc-container {
      flex: 1 1 0 !important;
      position: relative;
    }
  }
  .preview a {
    color: black !important;
  }
  .preview a:hover {
    text-decoration: underline;
  }
  .version-name {
    min-width: 150px;
    max-width: 200px;
  }
  .card-top-title {
    width: 100%;
    padding: 30px !important;
    background: #b1b1b1 !important;
    color: #2e2e2d !important;
    font-size: 20px;
    font-weight: 500;
    border: none !important;
    border-radius: 0 !important;
    text-align: center;
  }
  .history {
    background: lightgray !important;
    border-radius: 0;
    max-width: 100%;
    min-width: 200px;
  }
  .history-button {
    min-width: 100%;
    color: white !important;
    font-size: 20px;
    font-weight: bold;
    background: #2e2e2d !important;
    border-top-right-radius: 5px !important;
    border-bottom: 1px solid gray !important;
  }
  .version-button {
    min-width: 100%;
    color: white !important;
    font-size: 20px;
    font-weight: bold;
    background: #2e2e2d !important;
    border-bottom-left-radius: 5px !important;
  }

  .icon {
    margin-right: 12px;
    background: transparent !important;
    color: white !important;
  }
  .icon-modal {
    font-size: 22px;
  }
  .align {
    align-items: baseline;
    padding: 0 10px;
  }
  .history-version-btn {
    position: absolute;
    bottom: 0;
    min-width: 100%;
    cursor: pointer;
  }
  .history-button:hover, .version-button:hover {
    opacity: 0.7;
  }
  .dialog {
    padding: 20px !important;
    border: 20px solid black !important;
    /*border-radius: 50px !important;*/
    border-top-left-radius: 50px !important;
    border-bottom-left-radius: 50px !important;
    border-top-right-radius: 50px !important;
    border-bottom-right-radius: 50px !important;
  }
  .modal_title {
    font-size: 32px;
    font-weight: 500;
  }
  .title-container {
    padding: 0 !important;
  }
  .icon-col {
    margin-right: 12px;
    flex: 0 !important;
    margin-top: 4px;
  }
  .history-container {
    margin-top: 30px;
    font-size: 18px;
  }
  .restore {
    border-radius: 6px;
    min-width: 100px !important;
  }
  .history-row {
    border-bottom: 1px solid gray;
    width: 100%;
    margin-bottom: 20px;
  }
  .dialogBox {
    border-radius: 48px !important;
    position: relative !important;
  }
  .previewDialogBox {
    background: white !important;
    box-shadow: #60773d;
    position: relative;
    border: 10px solid black;
    border-top-left-radius: 32px;
    border-bottom-left-radius: 32px;
    border-top-right-radius: 32px;
    border-bottom-right-radius: 32px;
    overflow-x: hidden;
    max-width: 600px;
    min-height: 80%;
    z-index: 10;
  }
  .previewDialogBox textarea {
    border: 1px solid gray !important;
    margin: 20px !important;
    text-align: left !important;
  }
  .default-tooltip{
    font-size: 18px !important;
  }
</style>
