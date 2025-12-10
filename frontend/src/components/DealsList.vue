<template>
  <section class="list-section">
    <div class="list-header">
      <h3>Deals</h3>
      <div>
        <button class="refresh-btn" @click="loadDeals">Atualizar</button>
        <button style="margin-left:8px" class="refresh-btn" @click="openCreate">Criar</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="loading" class="spinner"></div>

    <div v-else>
      <div v-if="items.length" class="list-grid">
        <article v-for="d in items" :key="d.id" class="item-card" :title="d.title" @click="open(d)">
          <div class="item-title">{{ d.title || '—' }}</div>
          <div class="item-sub">Stage: {{ d.pipelineStage || '—' }}</div>
          <div class="item-meta">Valor: <span class="small-muted">{{ d.dealValue || '—' }}</span></div>
          <div class="item-meta">Contato: <span class="small-muted">{{ d.contactId || '—' }}</span></div>
          <div class="item-id">ID: {{ d.id }}</div>
        </article>
      </div>
      <pre v-else class="empty">No deals loaded.</pre>
    </div>
  </section>
  <DetailModal v-if="showModal" :title="selected.title || 'Deal'" :data="selected" type="deal" @close="showModal=false" @updated="handleUpdated" />
  <CreateModal v-if="showCreate" type="deal" title="Criar Deal" @close="showCreate=false" @created="handleCreated" />
</template>

<script>
import dealsService from '../services/deals'
import DetailModal from './DetailModal.vue'
import CreateModal from './CreateModal.vue'

export default {
  name: 'DealsList',
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
    else await this.loadDeals()
  },
  watch: {
    '$route.query.id'(val) {
      if (val) this.fetchById(val)
      else this.loadDeals()
    }
  },
  methods: {
    async fetchById(id) {
      this.loading = true
      this.error = null
      try {
        const res = await dealsService.getDeal(id)
        this.items = res.data ? [res.data] : []
      } catch (e) {
        this.items = []
        this.error = e?.response?.data || e.message || `Erro buscando deal ${id}`
      } finally {
        this.loading = false
      }
    }
    ,
    async loadDeals() {
      this.loading = true
      this.error = null
      try {
        const res = await dealsService.getDeals()
        this.items = res.data || []
      } catch (e) {
        this.error = e?.response?.data || e.message || 'Erro ao carregar deals'
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
      this.loadDeals()
    }
    ,handleUpdated() {
      this.showModal = false
      this.loadDeals()
    }
  }
}

</script>
