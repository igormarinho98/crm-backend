<template>
  <Modal :title="title" @close="close">
    <form id="createForm" @submit.prevent="submit">
      <template v-if="type === 'company'">
        <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.name" required /></div></div>
        <div class="detail-row"><div class="detail-key">CNPJ</div><div class="detail-value"><input v-model="form.cnpj" /></div></div>
        <div class="detail-row"><div class="detail-key">Telefone</div><div class="detail-value"><input v-model="form.phone" /></div></div>
        <div class="detail-row"><div class="detail-key">Website</div><div class="detail-value"><input v-model="form.website" /></div></div>
        <div class="detail-row"><div class="detail-key">Status</div><div class="detail-value"><select v-model="form.status"><option>ATIVO</option><option>INATIVO</option><option>SUSPENSO</option></select></div></div>
      </template>

      <template v-else-if="type === 'contact'">
        <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.firstName" placeholder="Nome" required /></div></div>
        <div class="detail-row"><div class="detail-key">Sobrenome</div><div class="detail-value"><input v-model="form.lastName" placeholder="Sobrenome" /></div></div>
        <div class="detail-row"><div class="detail-key">Email</div><div class="detail-value"><input v-model="form.email" type="email" /></div></div>
        <div class="detail-row"><div class="detail-key">Cargo</div><div class="detail-value"><input v-model="form.jobTitle" /></div></div>
        <div class="detail-row"><div class="detail-key">Company ID</div><div class="detail-value"><input v-model="form.companyId" /></div></div>
        <div class="detail-row"><div class="detail-key">LinkedIn</div><div class="detail-value"><input v-model="form.linkedinProfile" /></div></div>
      </template>

      <template v-else-if="type === 'deal'">
        <div class="detail-row"><div class="detail-key">Título</div><div class="detail-value"><input v-model="form.title" required /></div></div>
        <div class="detail-row"><div class="detail-key">Company ID</div><div class="detail-value"><input v-model="form.companyId" required /></div></div>
        <div class="detail-row"><div class="detail-key">Contact ID</div><div class="detail-value"><input v-model="form.contactId" /></div></div>
        <div class="detail-row"><div class="detail-key">Valor</div><div class="detail-value"><input v-model.number="form.dealValue" type="number" step="0.01" /></div></div>
        <div class="detail-row"><div class="detail-key">Moeda</div><div class="detail-value"><input v-model="form.currency" /></div></div>
        <div class="detail-row"><div class="detail-key">Stage</div><div class="detail-value"><select v-model="form.pipelineStage"><option>PROSPECCAO</option><option>NEGOCIACAO</option><option>FECHADO_GANHO</option><option>FECHADO_PERDIDO</option></select></div></div>
        <div class="detail-row"><div class="detail-key">Probabilidade</div><div class="detail-value"><input v-model.number="form.probability" type="number" step="0.01" min="0" max="1" /></div></div>
        <div class="detail-row"><div class="detail-key">Data Fechamento</div><div class="detail-value"><input v-model="form.expectedCloseDate" type="date" /></div></div>
      </template>

      <template v-else-if="type === 'user'">
        <div class="detail-row"><div class="detail-key">Nome</div><div class="detail-value"><input v-model="form.firstName" required /></div></div>
        <div class="detail-row"><div class="detail-key">Sobrenome</div><div class="detail-value"><input v-model="form.lastName" /></div></div>
        <div class="detail-row"><div class="detail-key">Email</div><div class="detail-value"><input v-model="form.email" type="email" /></div></div>
      </template>

    </form>
    <template #footer>
      <div style="display:flex; gap:8px; justify-content:flex-end">
        <button type="button" class="modal-cancel" @click="close">Cancelar</button>
        <button type="button" class="refresh-btn" @click.prevent="generateData">Gerar dados</button>
        <button type="submit" class="refresh-btn" form="createForm">Criar</button>
      </div>
    </template>
  
  </Modal>
</template>

<script>
import Modal from './ui/Modal.vue'
import companiesService from '../services/companies'
import contactsService from '../services/contacts'
import dealsService from '../services/deals'
import usersService from '../services/users'

export default {
  name: 'CreateModal',
  components: { Modal },
  props: {
    type: { type: String, required: true },
    title: { type: String, default: 'Criar' }
  },
  data() {
    return {
      form: this.defaultForm()
    }
  },
  methods: {
    defaultForm() {
      switch (this.type) {
        case 'company':
          return { name: '', cnpj: '', phone: '', website: '', status: 'ATIVO', tags: [] }
        case 'contact':
          return { firstName: '', lastName: '', email: '', jobTitle: '', companyId: '', linkedinProfile: '' }
        case 'deal':
          return { title: '', companyId: '', contactId: '', dealValue: null, currency: 'BRL', pipelineStage: 'PROSPECCAO', probability: 0.0, expectedCloseDate: '' }
        case 'user':
          return { firstName: '', lastName: '', email: '' }
        default:
          return {}
      }
    },
    async submit() {
      try {
        let res
        if (this.type === 'company') res = await companiesService.createCompany(this.form)
        else if (this.type === 'contact') res = await contactsService.createContact(this.form)
        else if (this.type === 'deal') res = await dealsService.createDeal(this.form)
        else if (this.type === 'user') res = await usersService.createUser(this.form)

        this.$emit('created', res?.data || res)
        this.close()
      } catch (e) {
        alert('Erro ao criar: ' + (e?.response?.data || e.message || e))
      }
    },
    close() {
      this.$emit('close')
    },
    generateData() {
      const randInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min
      const randChoice = (arr) => arr[Math.floor(Math.random() * arr.length)]
      const randFloat = (min, max, dec = 2) => parseFloat((Math.random() * (max - min) + min).toFixed(dec))
      const randomDateISO = (startYear = 2024, endYear = 2026) => {
        const start = new Date(startYear, 0, 1).getTime()
        const end = new Date(endYear, 11, 31).getTime()
        return new Date(randInt(start, end)).toISOString().slice(0, 10)
      }

      if (this.type === 'company') {
        const names = ['Acme Corp', 'Nexo Solutions', 'BluePeak', 'VerdeTech', 'Solara S.A.']
        this.form.name = randChoice(names)
        this.form.cnpj = String(randInt(10000000000000, 99999999999999))
        this.form.phone = '+55' + String(randInt(1100000000, 1199999999))
        this.form.website = this.form.name.toLowerCase().replace(/\s+/g, '') + '.com'
        this.form.status = randChoice(['ATIVO', 'INATIVO', 'SUSPENSO'])
      } else if (this.type === 'contact') {
        const first = ['Ana', 'Bruno', 'Carlos', 'Daniela', 'Eduardo', 'Fernanda']
        const last = ['Silva', 'Souza', 'Oliveira', 'Costa', 'Pereira']
        const jobs = ['Gerente', 'CTO', 'Analista', 'Head de Vendas', 'Desenvolvedor']
        const f = randChoice(first)
        const l = randChoice(last)
        this.form.firstName = f
        this.form.lastName = l
        this.form.email = `${f.toLowerCase()}.${l.toLowerCase()}@${randChoice(['example.com','empresa.com'])}`
        this.form.jobTitle = randChoice(jobs)
        this.form.companyId = ''
        this.form.linkedinProfile = `https://www.linkedin.com/in/${f.toLowerCase()}${l.toLowerCase()}`
      } else if (this.type === 'deal') {
        const titles = ['Venda Plataform X', 'Contrato Enterprise', 'Proposta Platinum', 'Renovação Anual']
        this.form.title = randChoice(titles)
        this.form.companyId = ''
        this.form.contactId = ''
        this.form.dealValue = randFloat(1000, 50000, 2)
        this.form.currency = 'BRL'
        this.form.pipelineStage = randChoice(['PROSPECCAO','NEGOCIACAO','FECHADO_GANHO','FECHADO_PERDIDO'])
        this.form.probability = parseFloat((Math.random()).toFixed(2))
        this.form.expectedCloseDate = randomDateISO()
      } else if (this.type === 'user') {
        const first = ['Miguel','Laura','João','Mariana','Luiz']
        const last = ['Alves','Martins','Ribeiro','Gomes']
        const f = randChoice(first)
        const l = randChoice(last)
        this.form.firstName = f
        this.form.lastName = l
        this.form.email = `${f.toLowerCase()}.${l.toLowerCase()}@empresa.com`
      }
    }
  }
}
</script>
