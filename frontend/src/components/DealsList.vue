<template>
  <section class="list-section">
    
    <div class="list-header">
      <h3 class="title-main">Oportunidades (Deals)</h3>
      <div class="header-actions">
        <BaseButton icon="sync" class="refresh-btn" @click="loadDeals" :disabled="loading">
          Atualizar
        </BaseButton>
        <BaseButton icon="plus" type="primary" style="margin-left:8px" @click="openCreate">
          Criar Deal
        </BaseButton>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>
    <div v-if="loading" class="spinner"></div>

    <div v-else>
      <div v-if="items.length" class="list-grid">
        <BaseCard v-for="d in items" :key="d.id" @click="open(d)" class="deal-card">
          
          <div class="card-header">
            <div class="item-title" :title="d.title">{{ d.title || 'Novo Negócio' }}</div>
            <StatusTag :status="d.pipelineStage" class="stage-tag" />
          </div>

          <div class="item-sub item-value">
            <span class="currency-value">{{ formatCurrency(d.dealValue, 'BRL') }}</span>
          </div>

          <div class="item-meta">
            Probabilidade: <span class="small-muted">{{ formatProbability(d.probability) }}</span>
          </div>
          
          <div class="item-meta">
            Contato ID: <span class="small-muted">{{ d.contactId || '—' }}</span>
          </div>
          <div class="item-meta">
            Empresa ID: <span class="small-muted">{{ d.companyId || '—' }}</span>
          </div>
          
          <div class="item-id">ID: {{ d.id }}</div>
        </BaseCard>
      </div>
      <pre v-else class="empty">Nenhuma oportunidade de venda carregada.</pre>
    </div>
  </section>
  <DetailModal v-if="showModal" :title="selected.title || 'Deal'" :data="selected" type="deal" @close="showModal=false" @updated="handleUpdated" />
  <CreateModal v-if="showCreate" type="deal" title="Criar Deal" @close="showCreate=false" @created="handleCreated" />
</template>

<script>

import dealsService from '../services/deals'
import DetailModal from './DetailModal.vue'
import CreateModal from './CreateModal.vue'
// Importação dos novos/melhorados componentes
import BaseCard from './ui/BaseCard.vue' 
import BaseButton from './ui/BaseButton.vue'
import StatusTag from './ui/StatusTag.vue' 

export default {
  name: 'DealsList',
  // Inclusão dos componentes UI
  components: { DetailModal, CreateModal, BaseCard, BaseButton, StatusTag }, 
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
  // ... (mounted e watch permanecem iguais)
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
    // ... (fetchById e loadDeals permanecem iguais)
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
    },
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
    },
    // Métodos utilitários de formatação
    formatCurrency(value, currency = 'BRL') {
        if (value == null) return 'R$ 0,00'
        try {
            const v = Number(value)
            // Formato BRL para Real Brasileiro
            return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: currency }).format(v)
        } catch (e) {
            return value
        }
    },
    formatProbability(value) {
        if (value == null || value === '') return '0%'
        // Assume que o valor está entre 0 e 1 (0.5 = 50%)
        return `${Math.round(value * 100)}%`
    },
    
    // ... (open, openCreate, handleCreated, handleUpdated permanecem iguais)
    open(item) {
      this.selected = item
      this.showModal = true
    },
    openCreate() {
      this.showCreate = true
    },
    handleCreated() {
      this.showCreate = false
      this.loadDeals()
    },
    handleUpdated() {
      this.showModal = false
      this.loadDeals()
    }
  }
}
</script>
