<template>
  <section class="list-section">
    <div class="list-header">
      <h3>Contacts</h3>
      <div>
        <button class="refresh-btn" @click="loadContacts">Atualizar</button>
        <button style="margin-left:8px" class="refresh-btn" @click="openCreate">Criar</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="loading" class="spinner"></div>

    <div v-else>
      <div v-if="items.length" class="list-grid">
        <article v-for="c in items" :key="c.id" class="item-card" :title="c.firstName + ' ' + c.lastName" @click="open(c)">
          <div class="item-title">{{ c.firstName }} {{ c.lastName }}</div>
          <div class="item-sub">{{ c.jobTitle || '—' }}</div>
          <div class="item-meta">Email: <span class="small-muted">{{ c.email || '—' }}</span></div>
          <div class="item-meta">Empresa: <span class="small-muted">{{ c.companyId || '—' }}</span></div>
          <div class="item-id">ID: {{ c.id }}</div>
        </article>
      </div>
      <pre v-else class="empty">{{ emptyMessage }}</pre>
    </div>
  </section>
  <DetailModal v-if="showModal" :title="selected.firstName + ' ' + selected.lastName" :data="selected" type="contact" @close="showModal=false" @updated="handleUpdated" />
  <CreateModal v-if="showCreate" type="contact" title="Criar Contato" @close="showCreate=false" @created="handleCreated" />
</template>

<script>
import contactsService from '../services/contacts'
import DetailModal from './DetailModal.vue'
import CreateModal from './CreateModal.vue'

export default {
  name: 'ContactsList',
  components: { DetailModal, CreateModal },
  data() {
    return {
      items: [],
      error: null,
      loading: true
      ,selected: null,
      showModal: false
      ,showCreate: false
    }
  },
  async mounted() {
    const id = this.$route?.query?.id
    if (id) this.fetchById(id)
    else await this.loadContacts()
  },
  watch: {
    '$route.query.id'(val) {
      if (val) this.fetchById(val)
      else this.loadContacts()
    }
  },
  methods: {
    async fetchById(id) {
      this.loading = true
      this.error = null
      try {
        const res = await contactsService.getContact(id)
        console.debug('ContactsList.fetchById response:', res)
        this.items = res.data ? [res.data] : []
      } catch (e) {
        this.items = []
        this.error = e?.response?.data || e.message || `Erro buscando contact ${id}`
      } finally {
        this.loading = false
      }
    }
    ,
    async loadContacts() {
      this.loading = true
      this.error = null
      try {
        const res = await contactsService.getContacts()
        this.items = res.data || []
      } catch (e) {
        this.error = e?.response?.data || e.message || 'Erro ao carregar contacts'
      } finally {
        this.loading = false
      }
    }
    ,open(item) {
      this.selected = item
      this.showModal = true
    }
    ,openCreate() {
      this.showCreate = true
    }
    ,handleCreated() {
      this.showCreate = false
      this.loadContacts()
    }
    ,handleUpdated() {
      this.showModal = false
      this.loadContacts()
    }
  },
  computed: {
    emptyMessage() {
      const q = this.$route?.query?.id
      return q ? `Nenhum contato encontrado para ID: ${q}` : 'No contacts loaded.'
    }
  }
}
</script>
