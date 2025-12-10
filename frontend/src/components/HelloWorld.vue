<template>
  <section>
    <h2>Exemplo de componente</h2>
    <p>Este é o frontend inicial do CRM. Clique para buscar empresas de exemplo do backend.</p>
    <button @click="loadCompanies">Carregar empresas (exemplo)</button>

    <ul v-if="companies.length">
      <li v-for="c in companies" :key="c.id">{{ c.name }} — {{ c.cnpj }}</li>
    </ul>

    <p v-if="error" style="color: red">{{ error }}</p>
  </section>
</template>

<script>
import companiesService from '../services/companies'

export default {
  name: 'HelloWorld',
  data() {
    return {
      companies: [],
      error: null
    }
  },
  methods: {
    async loadCompanies() {
      this.error = null
      try {
        const res = await companiesService.getCompanies()
        this.companies = res.data
      } catch (e) {
        this.error = e.message || 'Erro ao buscar empresas'
      }
    }
  }
}
</script>
