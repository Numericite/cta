<template>
  <v-dialog v-model="deleteDialog" persistent max-width="290">
    <template v-slot:activator="{ on }">
      <v-btn round depressed small
             @click.stop="deleteDialog = true">
        <v-icon
          small
        >
          delete
        </v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline">Confirmation</v-card-title>
      <v-card-text>{{ text }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn flat @click="deleteDialog = false">Annuler</v-btn>
        <v-btn color="error" flat @click="confirmClick()">Supprimer</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
  export default {
    name: 'confirm-delete',
    props: {
      text: {
        type: String,
        default: 'Êtes vous sûr de vouloir supprimer cet élément ?'
      }
    },
    data() {
      return {
        deleteDialog: false
      }
    },
    methods: {
      confirmClick() {
        this.$emit('confirmedClick')
      }
    }
  }
</script>
