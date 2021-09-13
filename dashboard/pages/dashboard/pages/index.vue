<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Pages</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px" persistent>
          <template  v-slot:activator="{ on }">
            <v-btn v-show-numericite v-if="showSettings" color="primary" dark class="mb-2" v-on="on" @click="getFolder(), itemEdit = false">Créer</v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-form>
                <v-container grid-list-md>
                  <v-flex wrap>
                    <v-flex xs12>
                      <v-text-field
                        v-model="file.name"
                        v-validate="'required'"
                        label="Nom de la page *"
                        :error-messages="errors.collect('Nom de la page')"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="file.kind"
                        :items="fileKinds"
                        v-validate="'required'"
                        label="Type de fichier *"
                        :error-messages="errors.collect('Type de fichier')"
                        required>
                      </v-select>
                    </v-flex>
                    <v-flex xs12 >
                      <v-text-field
                        v-model="file.slug"
                        v-validate="'required'"
                        label="Slug *"
                        :error-messages="errors.collect('Slug')"
                        required>
                      </v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="file.parent_id"
                        v-validate="file.kind === 'file' ? 'required' : ''"
                        :items="folderList"
                        item-text="name"
                        item-value="id"
                        :label="file.kind === 'file' ? 'Parent *' : 'Parent'"
                        :error-messages="errors.collect('Parent')"
                        :required="file.kind === 'file'">
                      </v-select>
                    </v-flex>
                  </v-flex>
                </v-container>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" flat @click="close">Annuler</v-btn>
              <v-btn color="blue darken-1" flat @click="save">Sauvegarder</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
      <template>
        <v-flex class="treeContainer">
          <v-treeview v-for="item in items" :key="item.id" :item="item" @deleteItem="deleteItem" @editItem="editItem" @addItem="addItem"></v-treeview>
        </v-flex>
      </template>
    </v-app>
  </div>
</template>
<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import { FILE_KINDS } from '~/config'
  import VTreeview from "../../../components/VTreeview/VTreeview";

  export default {
    scrollToTop: true,
    name: 'DashboardPages',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    $_veeValidate: {
      validator: 'new'
    },
    data () {
      return {
        showSettings: true,
        usersList: {},
        parentId: '',
        fileKinds: FILE_KINDS,
        file: {
          name: '',
          kind: '',
          slug: '',
          parent_id: null,
        },
        itemEdit: false,
        folderList: [],
        dialog: false,
        items: [],
      }
    },

    computed: {
      formTitle () {
        return this.itemEdit ? 'Éditer une page' : 'Créer une page'
      }
    },
    methods: {
      async getFolder() {
        return new Promise(async (resolve, reject) => {
          try {
            let request = await this.$api.files.count()
            const count = request.data.res || 0

            request = await this.$api.files.getList({
              page: 1,
              numberPerPage: 1000,
              kind: 'folder',
            })
            this.folderList = request.data || []
            resolve({
              count,
            })
          } catch (e) {
            reject(e)
          }
        })
      },
      async getTree() {
        return new Promise(async (resolve, reject) => {
          try {
            let request = await this.$api.files.getTree()
            this.items = request.data || []

          } catch (e) {
            reject(e)
          }
        })
      },
      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {
            try {
              //UPDATE
              if (this.itemEdit === true) {
                await this.$api.files.update(this.file)
                this.close()
                this.getTree()
              }
              //CREATE
              else {
                await this.$api.files.create(this.file)
                this.close()
                this.getTree()
              }

            } catch (e) {
              console.log(e)
            }
          }
        })
      },
      resetFile() {
        this.file = {
          name: '',
            kind: '',
            slug: '',
            parent_id: null,
        }
      },
      close () {
        this.dialog = false
        this.resetFile()
      },
      editItem(item) {
            this.itemEdit = true
            this.dialog = true
            this.file = Object.assign({}, item)
            this.getFolder()
      },
      async deleteItem (item) {
        if (confirm('Êtes vous sur de vouloir supprimer cette page')) {
          try {
            await this.$api.files.drop([item.id])
            this.close()
            this.getTree()
          } catch (e) {

          }
        }
      },
      addItem(item) {
        this.itemEdit = false
        this.file.parent_id = item.id
        this.getFolder()
        this.dialog = true
      },
    },
    watch: {
      dialog (val) {
        val || this.close()
      }
    },
    components: {
      VTreeview
    },
    mounted() {
      this.getTree()
    }
  }
</script>
<style lang="scss">
</style>
