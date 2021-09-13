<template>
  <div>
    <div @click="launchFilePicker()">
      <slot name="activator" />
    </div>
    <input ref="file"
           :name="uploadFieldName"
           type="file"
           style="display:none"
           @change="onFileChange(
           $event.target.name, $event.target.files)">
  </div>
</template>
<script>
export default {
  name: 'UploadAvatar',
  props: {
    value: {
      type: Object,
      default: function() {
        return { value: null }
      }
    }
  },
  data: () => ({
    errorDialog: null,
    errorText: '',
    uploadFieldName: 'file',
    maxSize: 1024
  }),
  methods: {
    launchFilePicker() {
      this.$refs.file.click()
    },
    onFileChange(fieldName, file) {
      const { maxSize } = this
      let imageFile = file[0]

      //check if user actually selected a file
      if (file.length > 0) {
        let size = imageFile.size / maxSize / maxSize
        if (!imageFile.type.match('image.*')) {
          // check whether the upload is an image
          this.errorDialog = true
          this.errorText = 'Please choose an image file'
        } else if (size > 1) {
          // check whether the size is greater than the size limit
          this.errorDialog = true
          this.errorText =
            'Your file is too big! Please select an image under 1MB'
        } else {
          // Append file into FormData & turn file into image URL
          let formData = new FormData()
          let imageURL = URL.createObjectURL(imageFile)
          formData.append(fieldName, imageFile)
          // Emit FormData & image URL to the parent component
          this.$emit('input', { formData, imageURL })
        }
      }
    }
  }
}
</script>
