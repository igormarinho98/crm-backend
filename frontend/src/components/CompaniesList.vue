<template>
  <section class="list-section">
    <div class="list-header">
      <h3>Companies</h3>
      <div>
        <button class="refresh-btn" @click="loadCompanies">Atualizar</button>
        <button style="margin-left:8px" class="refresh-btn" @click="openCreate">Criar</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="loading" class="spinner"></div>

    <div v-else>
      <div v-if="items.length" class="list-grid">
        <BaseCard v-for="c in items" :key="c.id" @click="open(c)">
          <div class="item-title">{{ c.name }}</div>
          <div class="item-sub">{{ c.cnpj || '—' }}</div>
          <div class="item-meta">Website: <span class="small-muted">{{ c.website || '—' }}</span></div>
          <div class="item-meta">Status: <span class="small-muted">{{ c.status || '—' }}</span></div>
          <div class="item-id">ID: {{ c.id }}</div>
        </BaseCard>
      </div>
      <pre v-else class="empty">No companies loaded.</pre>
    </div>
  </section>
  <DetailModal v-if="showModal" :title="selected.name || 'Company'" :data="selected" type="company" @close="showModal=false" @updated="handleUpdated" />
  <CreateModal v-if="showCreate" type="company" title="Criar Empresa" @close="showCreate=false" @created="handleCreated" />
</template>

<script>
import companiesService from '../services/companies'
import DetailModal from './DetailModal.vue'
import CreateModal from './CreateModal.vue'
import BaseCard from './ui/BaseCard.vue'
import BaseButton from './ui/BaseButton.vue'

export default {
  name: 'CompaniesList',
  components: { DetailModal, CreateModal, BaseCard, BaseButton },
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
    else await this.loadCompanies()
  },
  watch: {
    '$route.query.id'(val) {
      if (val) this.fetchById(val)
      else this.loadCompanies()
    }
  },
  methods: {
    async fetchById(id) {
      this.loading = true
      this.error = null
      try {
        const res = await companiesService.getCompany(id)
        this.items = res.data ? [res.data] : []
      } catch (e) {
        this.items = []
        this.error = e?.response?.data || e.message || `Erro buscando company ${id}`
      } finally {
        this.loading = false
      }
    }
    ,
    async loadCompanies() {
      this.loading = true
      this.error = null
      try {
        const res = await companiesService.getCompanies()
        this.items = res.data || []
      } catch (e) {
        this.error = e?.response?.data || e.message || 'Erro ao carregar companies'
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
      this.loadCompanies()
    }
    ,handleUpdated() {
      this.showModal = false
      this.loadCompanies()
    }
  }
}
</script>



