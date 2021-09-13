<template>
  <v-flex class="treeContainer">
      <div class="label-wrapper"
           :class="{
            'depth-0' : item.depth === 0,
            'depth-1' : item.depth === 1,
            'depth-2' : item.depth === 2,
            'depth-3': item.depth === 3 ,
            'depth-4': item.depth === 4 ,
            'depth-5': item.depth === 5 ,
            'depth-6': item.depth === 6 || item.kind === 'file',
             'link' : item.kind === 'file'}" @click="toggleChildren">
        <div class="edit">
          <v-btn v-if="item.kind === 'folder'" v-show-numericite round depressed small @click.stop="addItem(item)">
            <v-icon
              small
            >
              ti-plus
            </v-icon>
          </v-btn>
          <v-btn v-show-numericite round depressed small @click.stop="editItem(item)">
            <v-icon
              small
            >
              edit
            </v-icon>
          </v-btn>
          <v-btn v-show-numericite round depressed small @click.stop="deleteItem(item)">
            <v-icon
              small
            >
              delete
            </v-icon>
          </v-btn>
        </div>
        <div :style="indent" :class="labelClasses" class="tree-root">
          <i v-if="item.kind === 'folder' && !showChildren" class="ti-angle-down icon"></i>
          <i v-if="showChildren" class="ti-angle-up icon"></i>
          <div>{{ item.name }}</div>
        </div>
      </div>
      <v-treeview
        class="treeview"
        v-if="showChildren"
        v-for="child in children"
        :key="child.id"
        :item="child"
        @editItem="editItem"
        @deleteItem="deleteItem"
        @addItem="addItem"
      >
      </v-treeview>
  </v-flex>
</template>


<script>
  export default{
    scrollToTop: true,
    props: {
      item: {
        type: Object,
        default: () => { return {} }
      }
    },
    name: 'v-treeview',
    data () {
      return {
        showChildren: false,
      }
    },
    methods: {
      toggleChildren() {
        if(this.item.kind === 'file') {
          this.$router.push('/dashboard/page/' + this.item.id)
        }
        else {
          this.showChildren = !this.showChildren
        }
      },
      editItem(item) {
        this.$emit('editItem', item)
      },
      async deleteItem (item) {
        this.$emit('deleteItem', item)
      },
      addItem(item) {
        this.$emit('addItem', item)
      },
    },
    computed:{
      labelClasses() {
        return { 'has-children': this.item.children }
      },
      indent() {
        return { transform: `translate(${this.item.depth * 50}px)` }
      },
      children() {
        const orderChildren = _.orderBy(this.item.children, ['kind', 'name'])
        return orderChildren
      }
    },
    mounted() {
    }
  }
</script>
<style lang="scss">
  .treeContainer {
    font-size: 16px;
    position: relative;
    .label-wrapper {
      padding: 22px;
      border-bottom: 1px solid #ccc;
      background: #a3a3a3;
      cursor: pointer;
      .has-children {
        cursor: pointer;
      }
    }
    .icon {
      position: relative;
      font-size: 16px;
      margin-right: 12px;
      color: black !important;
    }
    .link:hover {
      text-decoration: underline;
    }
    .tree-root {
      display: flex;
      align-content: center;
      align-items: center;
      margin-left: 10px;
    }
    .depth-0 {
      background: #888887;
    }
    .depth-1 {
      background: #9F9F9C;
    }
    .depth-2 {
      background: #BBBABA;
    }
    .depth-3 {
      background: #C8C9CB;
    }
    .depth-4 {
      background: #D4D7D6;
    }
    .depth-5 {
      background: #E0E2E4;
    }
    .depth-6 {
      background: #f0f0f0;
    }

    .edit {
      position: absolute;
      right: 20px;
      top: 14px;
      .v-btn {
        margin: 15px 10px;
        z-index: 100;
      }
    }
  }
</style>
