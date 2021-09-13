<template>
  <div class="bg-white mb-12 p-4 rounded-lg">

    <!-- Modal create -->
    <CollegeModal v-if="showModalEvent" :count="0" :modalFullWidth="true" @handleClose="closeModal()">
      <div class="w-3/4 h-auto mx-auto pb-12 px-12 flex flex-col justify-center text-center">
        <h1 class="mt-16 ms-title text-center">{{ eventPlanning.id ? 'Modification' : 'Création' }} d’un évènement</h1>
        <FormulateForm ref="eventForm" class="formulate-form-custom">
          <div class="flex flex-col">
            <div class="pt-12 flex justify-start items-center">
              <div class="dot mr-4 bg-black" />
              <div class="ms-subtitle text-xl">DATE</div>
              <div class="dot ml-4 bg-black" />
            </div>

            <div class="flex">
              <FormulateInput v-model="eventPlanning.startDate" :outerClass="['w-1/2 mr-6 my-8 text-left']"
                              :inputClass="['mt-4 py-5 px-6 select-none focus:border-purple text-ms-gray-dark']"
                              :validationMessages="{ required: 'La date est obligatoire' }"
                              :min="new Date().toISOString().slice(0, 10)"
                              validation="required" label="Jour *" name="startDate" 
                              type="date" placeholder="Titre" />
              <FormulateInput v-model="eventPlanning.hours" :outerClass="['w-1/4 mr-6 my-8 text-left']"
                              :inputClass="['mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                              :validationMessages="{ required: 'L\'heure est obligatoire' }"
                              validation="required" label="Heure *" name="hours" type="time" />
            </div>
          </div>
          <div class="flex flex-col">
            <div class="pt-12 flex justify-start items-center">
              <div class="dot mr-4 bg-black" />
              <div class="ms-subtitle text-xl">DESCRIPTION</div>
              <div class="dot ml-4 bg-black" />
            </div>
            <FormulateInput v-model="eventPlanning.title" :outerClass="['my-8 text-left']"
                            :inputClass="['mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                            :validationMessages="{ required: 'Le titre est obligatoire' }"
                            validation="required" label="Nom évenement *" name="title" 
                            type="text" placeholder="Titre de l'évenement ..." />
            <FormulateInput v-model="eventPlanning.description" :outerClass="['my-4 text-left']"
                            :inputClass="['h-48 mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                            label="Description" name="description" 
                            type="textarea" placeholder="Description de l'évenement ..." />
          </div>
          <div v-if="$store.state.auth.user.config.accountType == 'operator'" class="flex flex-col">
            <div class="pt-12 flex justify-start items-center">                                                                               
              <div class="dot mr-4 bg-black" />
              <div class="ms-subtitle text-xl">INVITATION</div>
              <div class="dot ml-4 bg-black" />
            </div>

            <multiselect v-model="eventPlanning.users"
                         :placeholder="''"
                         :multiple="true"
                         :close-on-select="false"
                         :clear-on-select="true"
                         :searchable="false"
                         :openDirection="'bottom'"
                         :options="teachersFilteredByBookings"
                         :selectLabel="'Cliquer pour sélectionner'"
                         :deselectLabel="'Cliquer pour désélectionner'"
                         :selectedLabel="''"
                         label="label"
                         track-by="value"
                         class="my-5 multiselect" />
          </div>
          <div class="flex justify-center">
            <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-ms-red focus:outline-none sm:ml-0" 
                    @click="closeModal()">
              Annuler
            </button>
            <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-blue focus:outline-none sm:ml-0 ml-6" 
                    @click="saveEvent()">
              {{ eventPlanning.id ? 'Modifier' : 'Créer' }} l'évenement
            </button>
          </div>
        </FormulateForm>
      </div>
    </CollegeModal>

    <!-- Calendar item modal -->
    <div v-click-outside="closePopupCalendarItem" v-show="showPopupCalendarItem" 
         :style="selectedCalendarItem.x ? `left: ${selectedCalendarItem.x}px; top: ${selectedCalendarItem.y}px;` : ''" 
         class="flex flex-col absolute w-100 p-5 z-10 bg-white shadow-lg rounded-lg">
      <div class="flex items-center justify-between">
        <h1 class="text-5xl">{{ selectedCalendarItem.title }}</h1>
        <img src="~/static/icons/times.png" class="w-4 h-4 cursor-pointer" @click="closePopupCalendarItem">
      </div>
      <div class="flex mt-4">
        <span class="text-ms-gray-dark">le <span class="text-black font-bold">{{ formatDate(selectedCalendarItem.startDate, 'D MMMM') }}</span></span>
        <span class="text-ms-gray-dark ml-2">à <span class="text-black font-bold">{{ selectedCalendarItem.hours }}</span></span>
      </div>
      <div class="flex flex-col mt-6">
        <div class="flex justify-start items-center mb-1">
          <div class="dot mr-2 bg-black" />
          <div class="ms-subtitle text-xl">Invités</div>
          <div class="dot ml-2 bg-black" />
        </div>

        <template v-for="booking in selectedCalendarItem.bookings">
          <div :key="booking.user_id" class="flex items-center mt-3">
            <img :src="currentUser.userID == booking.user_id ? currentUser.avatar_path : booking.user.avatar_path" 
                 class="w-7 h-7 rounded-lg">
            <span class="ml-3">
              {{ currentUser.userID == booking.user_id
                ? currentUser.firstName + ' ' + currentUser.lastName
              : booking.user.firstName + ' ' + booking.user.lastName }}
            </span>
            <span class="ml-4 text-ms-gray-dark">
              {{ statusSwitchName(booking.status) }}
            </span>
          </div>
        </template>

      </div>
      <div class="mt-5">{{ selectedCalendarItem.description }}</div>
      <div class="flex justify-end mt-10">
        <template v-if="verifyBookingStatusUser('host')">
          <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-blue focus:outline-none sm:ml-0" 
                  @click="openModalEvent('modify')">
            Modifier <img class="w-4 h-4 ml-3" src="~/assets/img/middle-school/edit_planning.svg">
          </button>
          <button class="flex items-center text-ms-red-crisp align-middle outline-none border-1 border-solid border-ms-red-crisp p-5 rounded-lg bg-white focus:outline-none sm:ml-0 ml-6" 
                  @click="removeEvent(selectedCalendarItem.id)">
            Supprimer <img class="w-4 h-4 ml-3" src="~/assets/img/middle-school/trash.svg">
          </button>
        </template>
        <template v-if="verifyBookingStatusUser('pending')">
          <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-blue focus:outline-none sm:ml-0" 
                  @click="changeBookingStatus('accepted')">
            Accepter <img class="w-4 h-4 ml-3" src="~/assets/img/middle-school/check.svg">
          </button>
          <button class="flex items-center text-ms-red-crisp align-middle outline-none border-1 border-solid border-ms-red-crisp p-5 rounded-lg bg-white focus:outline-none sm:ml-0 ml-6" 
                  @click="changeBookingStatus('refused')">
            Refuser <img class="w-4 h-4 ml-3" src="~/assets/img/middle-school/times_red.svg"> 
          </button>
        </template>
      </div>
      <div v-if="selectedCalendarItem.status === 'pending'" class="flex justify-end mt-10">
        <template v-if="verifyBookingStatusUser('host')">
          <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-ms-green focus:outline-none" 
                  @click="changeEventStatus('validate')">
            Valider la séquence <img class="w-4 h-4 ml-3" src="~/assets/img/middle-school/check.svg">
          </button>
        </template>
      </div>
    </div>

    <div class="flex items-center mb-6">
      <div class="flex items-center">
        <div class="w-10 h-10 bg-ms-purple-light-5 rounded-lg" /> <span class="ml-4">Entretien de mi-parcours</span>
      </div>
      <div class="flex items-center ml-8">
        <div class="w-10 h-10 bg-ms-blue-light rounded-lg" /> <span class="ml-4">Séquence en cours</span>
      </div>
      <div class="flex items-center ml-8">
        <div class="w-10 h-10 bg-ms-green-light rounded-lg" /> <span class="ml-4">Séquence terminée</span>
      </div>
    </div>

    <!-- Calendar component -->
    <calendar-view :showDate="showDate" :startingDayOfWeek="1" :items="events" :showTimes="true" :timeFormatOptions="timeFormat"
                   itemTop="2em" itemContentHeight="3.5em" weekdayNameFormat="long" currentPeriodLabel="aujourd'hui" 
                   class="calendar-wrapper relative" @click-item="openPopupCalendarItem">
      <template #header="{ headerProps }">
        <button class="absolute flex items-center text-blue align-middle outline-none p-5 rounded-lg bg-white focus:outline-none sm:ml-0 ml-6" 
                style="top: 10px; right: 10px;" @click="openModalEvent('create')">
          {{ monitoring ? 'Programmer un rendez-vous' : 'Nouvel évènement' }} <img class="w-4 h-4 ml-4" src="~/assets/img/middle-school/calendar.svg">
        </button>
        <calendar-view-header :headerProps="headerProps" @input="setShowDate" />
      </template>
    </calendar-view>

    <div v-if="monitoring && lastSequencesByDate.length" class="flex flex-col mt-8">
      <h1 class="text-6xl mb-6">
        {{ lastSequencesByDate.length > 1 ? 'Dernieres séquences' : 'Derniere séquence' }}
        de <span class="ms-title">{{ `${eventUser.firstName} ${eventUser.lastName}` }}</span>
      </h1>
      <div v-for="sequence in lastSequencesByDate.slice(0, 10)" :key="sequence.id" :style="`background-color: ${eventKindColor(sequence)}`"
           class="flex items-center text-blue p-5 rounded-lg mb-5">
        <div class="flex flex-col">
          <span class="text-4xl">
            <span class="font-medium">{{ sequence.title }}</span> 
            - {{ `${eventsConfig[sequence.kind][sequence.kind_object]} ${sequence.status === 'validate' ? 'terminée' : 'en cours'}` }}
          </span>
          <div v-if="sequence.description" class="mt-4 font-light">{{ sequence.description }}</div>
        </div>
        <div class="flex ml-auto">
          <span>le {{ `${formatDate(sequence.startDate, 'D MMMM')} à ${sequence.hours}` }}</span>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

import CollegeModal from '~/components/middleSchool/CollegeModal'
import moment from 'moment'
import clickOutside from '~/directives/click-outside-directive.js'
import Multiselect from 'vue-multiselect'
import _ from 'lodash'
import { colors } from '~/tailwind.js'
import { EVENTS } from '~/config'

export default {
  name: 'Planning',
  components: { CollegeModal, Multiselect },
  props: {
    teachers: {
      type: Array,
      default: () => []
    },
    eventUser: {
      type: Object,
      default: () => {}
    },
    monitoring: {
      type: Boolean,
      default: () => false
    }
  },
  data: function() {
    return {
      showDate: new Date(),
      events: [],
      showModalEvent: false,
      showPopupCalendarItem: false,
      selectedCalendarItem: {},
      kind: 'planning',
      currentUser: this.$store.state.auth.user,
      timeFormat: { hour: '2-digit', minute: '2-digit' },
      calendarComponent: 1,
      eventPlanningBase: {
        startDate: null,
        title: null,
        description: null,
        users: [],
        kind: 'planning',
        kind_object: undefined,
        status: undefined
      },
      eventPlanningOriginal: {},
      eventPlanning: {
        startDate: null,
        title: null,
        description: null,
        users: [],
        kind: 'planning',
        kind_object: undefined,
        status: undefined
      },
      eventsConfig: EVENTS
    }
  },
  computed: {
    teachersFilteredByBookings() {
      return this.teachers.filter(teacher => !this.selectedCalendarItem?.users?.map(i => i.user_id).includes(teacher.userID))
    },
    lastSequencesByDate() {
      return [...this.events].filter(event => event.kind_object === 'sequence').filter(event => new Date(event.startDate).getTime() < Date.now())
        .sort((a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime())
    }
  },
  async mounted() {
    await this.getEvents()
  },
  methods: {
    setShowDate(d) {
      this.showDate = d;
    },

    formatDate(date, format) {
      return moment(date).locale('fr').format(format)
    },

    eventKindColor(event) {
      return event.kind_object === 'sequence' && event.status === 'validate' 
        ? colors['ms-green-light'] : event.kind_object === 'sequence' 
        ? colors['ms-blue-light']  : colors['ms-purple-light-5']
    },

    openModalEvent(type) {
      if (type === 'modify') {
        this.eventPlanning = _.clone(this.selectedCalendarItem)
        this.eventPlanning.bookings.map(booking => {
          booking.user.label = `${booking.user.firstName} ${booking.user.lastName}`
          booking.user.value = booking.user_id
          booking.user.booking_id = booking.id
        })
        this.eventPlanning.bookings = this.eventPlanning.bookings.filter(booking => booking.status !== 'host')
        this.eventPlanning.users = this.eventPlanning.bookings.map(booking => booking.user)
        this.eventPlanningOriginal = _.clone(this.eventPlanning)
      } else if (type === 'create' && this.monitoring) {
        this.eventPlanning.users.push(this.teachers.find(teacher => teacher.userID === this.eventUser.userID))
      }
      this.showModalEvent = true
    },

    closeModal() {
      if (this.eventPlanning.id) this.closePopupCalendarItem()
      this.eventPlanning = Object.assign({}, this.eventPlanningBase)
      this.showModalEvent = false
    },

    async getEvents() {
      let request = await this.$api.events.getByUserIds({
        user_ids: [this.eventUser.userID],
        kind: 'planning'
      })

      let events = request.data

      events.map(event => {
        event.startDate = `${moment(event.startDate).format('YYYY-MM-DD')} ${event.hours}`
        event.bookings = [event.bookings.find(booking => booking.status === 'host'), ...event.bookings.filter(booking => booking.status !== 'host')]
        event.users = event.bookings.map(booking => booking.user)
        event.classes = ['ml-3 cursor-pointer']
        event.style = `width: calc((100% / 7) - 1.25rem); background-color: ${this.eventKindColor(event)}`
      })

      this.events = events
    },

    async saveEvent() {

      const hasErrors = await this.$refs['eventForm'].hasValidationErrors()
      
      if (!hasErrors) {
        if (!this.eventPlanning.id) {
          // CREATE
          try {
            
            this.eventPlanning.kind_object = this.$store.state.auth.user.config.accountType === 'operator' ? 'interview' : 'sequence'

            if (this.eventPlanning.kind_object === 'sequence') {
              this.eventPlanning.status = 'pending'
            }

            const response = await this.$api.events.create(this.eventPlanning)
            const eventId = response.data.res.id 

            await this.$api.bookings.create({ 
              parent_id: eventId,
              user_id: this.currentUser.userID,
              status: 'host'
            })

            if (this.eventPlanning.users) {
              for (const user of this.eventPlanning.users) {
                await this.$api.bookings.create({ 
                  parent_id: eventId,
                  user_id: user.userID,
                  status: 'pending'
                })
              }
            }

            await this.getEvents()
            this.closeModal()
          } catch (e) {
            console.log(e.response)
          }
        } else {
          // UPDATE
          try {
            await this.$api.events.update(this.eventPlanning)

            const usersNew = _.differenceWith(this.eventPlanning.users, this.eventPlanningOriginal.users, _.isEqual)
            const usersRemove = _.differenceWith(this.eventPlanningOriginal.users, this.eventPlanning.users, _.isEqual)

            if (usersNew.length) {
              for (const user of usersNew) {
                await this.$api.bookings.create({ 
                  parent_id: this.eventPlanning.id,
                  user_id: user.userID,
                  status: 'pending'
                })
              }
            }

            if (usersRemove.length) {
              await this.$api.bookings.delete({ ids: usersRemove.map(user => user.booking_id) })
            }

            await this.getEvents()
            this.closeModal()
          } catch (e) {
            console.log(e)
          }
        }
      } else {
        const firstError = document.querySelector("*[data-has-errors=\"true\"] .formulate-input-element > *")
        firstError.focus()
      }

    },

    async removeEvent(eventPlanningId) {
      try {
        await this.$api.events.delete({ ids: [eventPlanningId] })
        await this.$api.bookings.removeByParentIds({ parent_ids: [eventPlanningId] })
        await this.getEvents()
        this.showPopupCalendarItem = false
      } catch (e) {
        console.log(e.response)
      }
    },

    openPopupCalendarItem(calendarItem, windowEvent) {
      this.selectedCalendarItem = Object.assign({}, calendarItem.originalItem)
      this.selectedCalendarItem.startDate = this.selectedCalendarItem.startDate.split(' ')[0]
      this.selectedCalendarItem.x = windowEvent.pageX
      this.selectedCalendarItem.y = windowEvent.pageY - (windowEvent.pageY > 350 ? (190 + ((this.selectedCalendarItem.users.length - 1) * 25)) : 0)
      this.$forceUpdate()
      this.showPopupCalendarItem = true
    },

    closePopupCalendarItem() {
      this.selectedCalendarItem = {}
      this.showPopupCalendarItem = false
    },

    statusSwitchName(status) {
      return status == 'host' 
        ? 'Hôte' : status == 'accepted' 
        ? 'Accepté' : status == 'pending'
        ? 'En attente' : ''
    },

    verifyBookingStatusUser(status) {
      let bookingUser = this.selectedCalendarItem?.bookings?.find(item => item.user_id == this.currentUser.userID)
      return bookingUser?.status == status
    },

    async changeEventStatus(status) {

      this.selectedCalendarItem.status = status

      try {
        await this.$api.events.update(this.selectedCalendarItem)
        await this.getEvents()
        this.closePopupCalendarItem()
      } catch (e) {
        console.log(e.response)
      }
    },

    async changeBookingStatus(status) {

      let bookingUser = this.selectedCalendarItem.bookings.find(item => item.user_id === this.currentUser.userID)
      bookingUser.status = status

      try {
        await this.$api.bookings.update(bookingUser)
      } catch (e) {
        console.log(e.response)
      }
    }

  }
}

</script>

<style lang="scss">

.multiselect__option--highlight, .multiselect__option--highlight::after, .multiselect__tag {
  @apply text-black bg-ms-orange #{!important};
}

.multiselect__option--highlight.multiselect__option--selected, .multiselect__option--selected.multiselect__option--highlight::after{
    background-color: #ff6a6a !important;
}

.multiselect__tag-icon:hover {
  @apply bg-dark-peach #{!important};
}

.multiselect__tag-icon::after {
  @apply text-black #{!important};
}


</style>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
