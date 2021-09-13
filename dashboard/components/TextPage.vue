<template>
  <div class="editor">
    <editor-menu-bubble class="menububble" :editor="editor" @hide="hideLinkMenu" v-slot="{ commands, isActive, getMarkAttrs, menu }">
      <div
        class="menububble"
        :class="{ 'is-active': menu.isActive }"
        :style="`left: ${menu.left}px; bottom: ${menu.bottom}px;`"
      >

        <form class="menububble__form" v-if="linkMenuIsActive" @submit.prevent="setLinkUrl(commands.link, linkUrl)">
          <input class="menububble__input" type="text" v-model="linkUrl" placeholder="https://" ref="linkInput" @keydown.esc="hideLinkMenu"/>
        </form>
        <template v-else>
          <button
            class="menububble__button"
            @click="showLinkMenu(getMarkAttrs('link'))"
            :class="{ 'is-active': isActive.link() }"
          >
            <span class="menububble__icon ti-link" />
          </button>
        </template>

        <template v-if="linkMenuIsActive">
          <button class="menububble__button" @click="setLinkUrl(commands.link, null)">
            <span class="menububble__icon ti-unlink" />
          </button>
        </template>
        <template v-else>
          <button
            class="menububble__button"
            :class="{ 'is-active': isActive.bold() }"
            @click="commands.bold"
          >
            <span class="menububble__icon custom-bold">B</span>
          </button>

          <button
            class="menububble__button"
            :class="{ 'is-active': isActive.italic() }"
            @click="commands.italic"
          >
            <span class="menububble__icon ti-Italic" />
          </button>
        </template>

      </div>
    </editor-menu-bubble>

    <editor-content class="editor__content" :editor="editor" />
  </div>
</template>

<script>
  import Icon from '~/components/UIComponents/Tiptap/Icon'
  import { Editor, EditorContent, EditorMenuBar, EditorMenuBubble } from 'tiptap'
  import {
    Bold,
    Italic,
    Link
  } from 'tiptap-extensions'

  export default {
    props: {
      value: {
        type: String,
        default: () => { return '' }
      },
      isDisabled: {
        type: Boolean,
        default: () => { return true }
      }
    },
    components: {
      EditorContent,
      EditorMenuBar,
      EditorMenuBubble,
      Icon,
    },
    data() {
      return {
        editor: new Editor({
          extensions: [
            new Link(),
            new Bold(),
            new Italic(),
          ],
          content: this.value,
          onUpdate: ({ getHTML }) => {
            this.$emit('input', getHTML())
          }
        }),
        linkUrl: null,
        linkMenuIsActive: false,
      }
    },
    beforeDestroy() {
      this.editor.destroy()
    },
    methods: {
      showLinkMenu(attrs) {
        this.linkUrl = attrs.href
        this.linkMenuIsActive = true
        this.$nextTick(() => {
          this.$refs.linkInput.focus()
        })
      },
      hideLinkMenu() {
        this.linkUrl = null
        this.linkMenuIsActive = false
      },
      setLinkUrl(command, url) {
        command({ href: url })
        this.hideLinkMenu()
      },
    }
  }

</script>

<style lang="scss">
  .menububble {
    position: absolute;
    display: flex;
    z-index: 20;
    background: #000;
    border-radius: 5px;
    padding: 0.3rem;
    margin-bottom: 0.5rem;
    transform: translateX(-50%);
    visibility: hidden;
    opacity: 0;
    transition: opacity 0.2s, visibility 0.2s;
    bottom: 0;

    &.is-active {
      opacity: 1;
      visibility: visible;
    }

    &__button {
      display: inline-flex;
      background: transparent;
      border: 0;
      color: #fff;
      padding: 0.2rem 0.5rem;
      margin-right: 0.2rem;
      border-radius: 3px;
      cursor: pointer;

      &:last-child {
        margin-right: 0;
      }

      &:hover {
        background-color: rgba(#fff, 0.1);
      }

      &.is-active {
        background-color: rgba(#fff, 0.2);
      }
    }

    &__form {
      display: flex;
      align-items: center;
    }

    &__input {
      font: inherit;
      border: none;
      background: transparent;
      color: #fff;
    }

    &__icon {
      font-size: 1.3em;

      &.custom-bold {
        font-size: 1.4em;
        width: 20px;
      }
    }
  }

  .ProseMirror {
    margin: 20px;
    border: 1px solid grey;
    padding: 15px;
    text-align: left;
    max-height: 300px;
    overflow-y: auto;

    * {
      font-size: 14px !important;
      margin: 0;
    }
  }
</style>
