<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal">
      <div class="modal-header">
        <div class="modal-title">{{ title }}</div>
        <button class="modal-close" @click="close">✕</button>
      </div>
      <div class="modal-body">
        <p v-if="error" class="error" style="margin-bottom:12px">{{ error }}</p>
        <template v-if="type === 'company'">
          <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.name" :disabled="!editing" /></div></div>

          <div class="detail-row"><div class="detail-key">ID</div><div class="detail-value">{{ form.id || form._id || data.id }}</div></div>

          <div class="detail-row"><div class="detail-key">CNPJ</div><div class="detail-value highlight"><input v-model="form.cnpj" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Website</div><div class="detail-value"><input v-model="form.website" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Status</div><div class="detail-value"><select v-model="form.status" :disabled="!editing"><option>ATIVO</option><option>INATIVO</option><option>SUSPENSO</option></select></div></div>
          <div class="detail-row"><div class="detail-key">Telefone</div><div class="detail-value"><input v-model="form.phone" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Tags</div><div class="detail-value"><input v-model="form.tags" placeholder="tag1, tag2" :disabled="!editing" /></div></div>
          <div v-if="form.address" class="detail-row"><div class="detail-key">Endereço</div><div class="detail-value"><input v-model="form.address.street" placeholder="Rua" :disabled="!editing" /></div></div>
        </template>

        <template v-else-if="type === 'contact'">
          <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.firstName" placeholder="Nome" :disabled="!editing" /> <input v-model="form.lastName" placeholder="Sobrenome" style="margin-left:8px" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Cargo</div><div class="detail-value highlight"><input v-model="form.jobTitle" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Email</div><div class="detail-value"><input v-model="form.email" type="email" class="highlight" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">LinkedIn</div><div class="detail-value"><input v-model="form.linkedinProfile" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Empresa</div><div class="detail-value"><input v-model="form.companyId" :disabled="!editing" /></div></div>
        </template>

        <template v-else-if="type === 'deal'">
          <div class="detail-row"><div class="detail-key">Título</div><div class="detail-value"><input v-model="form.title" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Stage</div><div class="detail-value"><select v-model="form.pipelineStage" :disabled="!editing"><option>PROSPECCAO</option><option>NEGOCIACAO</option><option>FECHADO_GANHO</option><option>FECHADO_PERDIDO</option></select></div></div>
          <div class="detail-row"><div class="detail-key">Valor</div><div class="detail-value highlight"><input v-model.number="form.dealValue" type="number" step="0.01" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Probabilidade</div><div class="detail-value"><input v-model.number="form.probability" type="number" step="0.01" min="0" max="1" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Contato</div><div class="detail-value"><input v-model="form.contactId" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Empresa</div><div class="detail-value"><input v-model="form.companyId" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Data Fechamento</div><div class="detail-value"><input v-model="form.expectedCloseDate" type="date" :disabled="!editing" /></div></div>
        </template>

        <template v-else-if="type === 'user'">
          <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.firstName" placeholder="Nome" :disabled="!editing" /> <input v-model="form.lastName" placeholder="Sobrenome" style="margin-left:8px" :disabled="!editing" /></div></div>
          <div class="detail-row"><div class="detail-key">Email</div><div class="detail-value"><input v-model="form.email" type="email" class="highlight" :disabled="!editing" /></div></div>
        </template>

        <template v-else>
          <pre>{{ data }}</pre>
        </template>
      </div>
      <div style="display:flex;gap:8px;justify-content:flex-end;margin-top:12px;padding-top:8px;border-top:1px solid rgba(16,24,40,0.04)">
        <template v-if="!editing">
          <button class="modal-close" @click="close">Fechar</button>
          <button class="refresh-btn" @click="startEdit">Editar</button>
        </template>
        <template v-else>
          <button class="modal-close" @click="cancelEdit">Cancelar</button>
          <button class="refresh-btn" :disabled="saving" @click="updateRecord">{{ saving ? 'Salvando...' : 'Atualizar' }}</button>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import companiesService from '../services/companies'
import contactsService from '../services/contacts'
import dealsService from '../services/deals'
import usersService from '../services/users'

export default {
  name: 'DetailModal',
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
