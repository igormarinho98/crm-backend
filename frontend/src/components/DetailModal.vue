<template>
  <Modal :title="title" @close="close">
    <div class="modal-body-content">
      <p v-if="error" class="error-message" style="margin-bottom:12px">🚨 {{ error }}</p>

      <div v-if="data.status && (type === 'company' || type === 'contact')" class="status-header">
        <div class="status-key">Status Atual:</div>
        <StatusTag :status="data.status" />
      </div>

      <template v-if="type === 'company'">
        
        <div class="detail-section">
          <h4 class="section-title">Informações Básicas</h4>
          <div class="detail-grid">
            <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.name" :disabled="!editing" /></div></div>
            <div class="detail-row"><div class="detail-key">CNPJ</div><div class="detail-value highlight"><input v-model="form.cnpj" :disabled="!editing" /></div></div>
            <div class="detail-row"><div class="detail-key">Website</div><div class="detail-value"><input v-model="form.website" :disabled="!editing" /></div></div>
            
            <div class="detail-row"><div class="detail-key">Mudar Status</div><div class="detail-value"><select v-model="form.status" :disabled="!editing">
              <option value="ATIVO">ATIVO</option><option value="INATIVO">INATIVO</option><option value="SUSPENSO">SUSPENSO</option><option value="CLIENTE">CLIENTE</option>
            </select></div></div>
            
            <div class="detail-row"><div class="detail-key">Telefone</div><div class="detail-value"><input v-model="form.phone" :disabled="!editing" /></div></div>
          </div>
        </div>

        <div v-if="form.address" class="detail-section">
          <h4 class="section-title">Endereço e Localização</h4>
          <div class="detail-grid">
            <div class="detail-row"><div class="detail-key">Rua</div><div class="detail-value"><input v-model="form.address.street" placeholder="Rua" :disabled="!editing" /></div></div>
            <div class="detail-row"><div class="detail-key">Cidade/Estado</div><div class="detail-value"><input v-model="form.address.city" placeholder="Cidade" :disabled="!editing" style="width:50%"/> <input v-model="form.address.state" placeholder="UF" :disabled="!editing" style="width:45%;margin-left:5%"/></div></div>
            <div class="detail-row"><div class="detail-key">CEP</div><div class="detail-value"><input v-model="form.address.postalCode" placeholder="CEP" :disabled="!editing" /></div></div>
            <div v-if="!editing" class="detail-row full-width"><div class="detail-key">Completo</div><div class="detail-value">{{ formatAddress(data.address) }}</div></div>
          </div>
        </div>
        
        <div class="detail-section">
          <h4 class="section-title">Metadados</h4>
          <div class="detail-row"><div class="detail-key">Tags</div><div class="detail-value"><input v-model="form.tags" placeholder="tag1, tag2" :disabled="!editing" /></div></div>
          <div v-if="!editing" class="detail-row id-info"><div class="detail-key">ID do Documento</div><div class="detail-value">{{ form.id || form._id || data.id }}</div></div>
        </div>
        
      </template>

      <template v-else-if="type === 'deal'">
        <div class="detail-section">
          <h4 class="section-title">Detalhes da Oportunidade</h4>
          <div class="detail-grid two-cols">
            
            <div class="column">
              <div class="detail-row"><div class="detail-key">Título</div><div class="detail-value"><input v-model="form.title" :disabled="!editing" /></div></div>
              <div class="detail-row"><div class="detail-key">Valor</div><div class="detail-value highlight">
                <input v-model.number="form.dealValue" type="number" step="0.01" :disabled="!editing" />
                <span v-if="!editing" class="currency-display">{{ formatCurrency(data.dealValue, 'BRL') }}</span>
              </div></div>
              <div class="detail-row"><div class="detail-key">Probabilidade (%)</div><div class="detail-value">
                <input v-model.number="form.probability" type="number" step="0.01" min="0" max="1" :disabled="!editing" />
                <span v-if="!editing">{{ data.probability != null ? (data.probability * 100).toFixed(0) + '%' : '—' }}</span>
              </div></div>
            </div>
            
            <div class="column">
              <div class="detail-row"><div class="detail-key">Stage</div><div class="detail-value"><select v-model="form.pipelineStage" :disabled="!editing">
                <option>PROSPECCAO</option><option>NEGOCIACAO</option><option>FECHADO_GANHO</option><option>FECHADO_PERDIDO</option>
              </select></div></div>
              <div class="detail-row"><div class="detail-key">Data Fechamento</div><div class="detail-value"><input v-model="form.expectedCloseDate" type="date" :disabled="!editing" /></div></div>
              <div class="detail-row"><div class="detail-key">Contato ID</div><div class="detail-value"><input v-model="form.contactId" :disabled="!editing" /></div></div>
              <div class="detail-row"><div class="detail-key">Empresa ID</div><div class="detail-value"><input v-model="form.companyId" :disabled="!editing" /></div></div>
            </div>
          </div>
        </div>
      </template>
      
      <template v-else>
        <pre>{{ data }}</pre>
      </template>
    </div>

    <template #footer>
      <div style="display:flex;gap:8px;justify-content:flex-end;margin-top:12px;padding-top:8px;border-top:1px solid rgba(16,24,40,0.04)">
        <template v-if="!editing">
          <button class="danger-btn" @click="confirmDelete">Excluir</button>
          <button class="modal-cancel" @click="close">Fechar</button>
          <button class="refresh-btn" @click="startEdit">Editar</button>
        </template>
        <template v-else>
          <button class="modal-cancel" @click="cancelEdit">Cancelar</button>
          <button class="refresh-btn" :disabled="saving" @click="updateRecord">{{ saving ? 'Salvando...' : 'Atualizar' }}</button>
        </template>
      </div>
    </template>
  </Modal>
</template>

<script>
import Modal from './ui/Modal.vue'
import StatusTag from './ui/StatusTag.vue'  
import companiesService from '../services/companies'
import contactsService from '../services/contacts'
import dealsService from '../services/deals'
import usersService from '../services/users'

export default {
  name: 'DetailModal',
  components: { Modal, StatusTag },
  props: {
    title: { type: String, default: 'Detalhes' },
    data: { type: Object, required: true },
    type: { type: String, required: true }
  },
  data() {
    return {
      form: JSON.parse(JSON.stringify(this.data || {})),
      saving: false,
      error: null,
      editing: false,
      _backup: null
    }
  },
  watch: {
    data: {
      handler(n) {
        this.form = JSON.parse(JSON.stringify(n || {}))
        if (this.form && Array.isArray(this.form.tags)) this.form.tags = this.form.tags.join(', ')
      },
      immediate: true
    }
  },
  methods: {
    startEdit() {
      this._backup = JSON.stringify(this.form)
      this.editing = true
    },
    cancelEdit() {
      if (this._backup) this.form = JSON.parse(this._backup)
      else this.form = JSON.parse(JSON.stringify(this.data || {}))
      this.editing = false
    },
    close() {
      this.$emit('close')
    },
    confirmDelete() {
      if (!confirm('Tem certeza que deseja excluir este registro?')) return
      this.deleteRecord()
    },
    async deleteRecord() {
      this.saving = true
      this.error = null
      try {
        const id = this.form.id || this.form._id || this.data.id
        console.debug('[DetailModal] deleteRecord id=', id)
        if (!id) throw new Error('ID não encontrado para exclusão')
        if (this.type === 'company') await companiesService.deleteCompany(id)
        else if (this.type === 'contact') await contactsService.deleteContact(id)
        else if (this.type === 'deal') await dealsService.deleteDeal(id)
        else if (this.type === 'user') await usersService.deleteUser(id)

        this.$emit('deleted', id)
        this.close()
      } catch (e) {
        console.error('DetailModal.deleteRecord error', e)
        // Provide richer debug info to help diagnose 404s
        let msg = 'Erro ao excluir.'
        try {
          const resp = e?.response
          if (resp) {
            const url = resp.config?.url || resp.request?.responseURL || 'unknown'
            msg = `Status ${resp.status} calling ${url}: ` + (resp.data ? (typeof resp.data === 'string' ? resp.data : JSON.stringify(resp.data)) : resp.statusText)
          } else if (e?.message) msg = e.message
        } catch (ex) {
          msg = e?.message || 'Erro desconhecido'
        }
        this.error = msg
      } finally {
        this.saving = false
      }
    },
    async updateRecord() {
      this.saving = true
      this.error = null
      try {
        let res
        const id = this.form.id || this.form._id || this.data.id
        if (this.type === 'company') {
          // build a clean payload matching backend Company model
          const raw = { ...this.form }
          const payload = {
            name: raw.name || null,
            cnpj: raw.cnpj ? String(raw.cnpj).replace(/\D/g, '') : null,
            phone: raw.phone || null,
            website: raw.website || null,
            status: raw.status || null,
            address: raw.address ? {
              street: raw.address.street || null,
              number: raw.address.number || null,
              complement: raw.address.complement || null,
              neighborhood: raw.address.neighborhood || null,
              city: raw.address.city || null,
              state: raw.address.state || null,
              country: raw.address.country || null,
              postalCode: raw.address.postalCode || null
            } : null,
            tags: []
          }
          if (raw.tags) {
            if (Array.isArray(raw.tags)) payload.tags = raw.tags.map(String)
            else if (typeof raw.tags === 'string') payload.tags = raw.tags.split(',').map(t => t.trim()).filter(Boolean)
          }
          res = await companiesService.updateCompany(id, payload)
        } else if (this.type === 'contact') {
          const payload = { ...this.form }
          if (payload.email) payload.email = String(payload.email).trim().toLowerCase()
          res = await contactsService.updateContact(id, payload)
        } else if (this.type === 'deal') {
          const payload = { ...this.form }
          // ensure numeric types and proper date format
          if (payload.dealValue != null && payload.dealValue !== '') payload.dealValue = Number(payload.dealValue)
          if (payload.probability != null && payload.probability !== '') payload.probability = Number(payload.probability)
          if (payload.expectedCloseDate === '') payload.expectedCloseDate = null
          res = await dealsService.updateDeal(id, payload)
        } else if (this.type === 'user') {
          const payload = { ...this.form }
          if (payload.email) payload.email = String(payload.email).trim().toLowerCase()
          res = await usersService.updateUser(id, payload)
        }

        this.$emit('updated', res?.data || res)
        this.close()
      } catch (e) {
        console.error('DetailModal.updateRecord error', e)
        // prefer server message if present
        let msg = 'Erro ao atualizar.'
        if (e?.response?.data) {
          msg = typeof e.response.data === 'string' ? e.response.data : JSON.stringify(e.response.data)
        } else if (e?.message) msg = e.message
        this.error = msg
      } finally {
        this.saving = false
      }
    },
    formatAddress(a) {
      if (!a) return '—'
      const parts = [a.street, a.number, a.complement, a.neighborhood, a.city, a.state, a.postalCode].filter(Boolean)
      return parts.join(', ')
    },
    formatCurrency(value, currency) {
      if (value == null) return '—'
      try {
        const v = Number(value)
        return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: currency || 'BRL' }).format(v)
      } catch (e) {
        return value
      }
    }
  }
}
</script>
