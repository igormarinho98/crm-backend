<template>
  <section class="dashboard">
    <h1>Dashboard</h1>
    <p>Visão geral rápida do CRM com gráficos. Filtre por empresa para análises específicas.</p>

    <div style="display:flex;gap:12px;align-items:center;margin-top:8px">
      <label for="companySelect">Empresa:</label>
      <select id="companySelect" v-model="selectedCompanyId">
        <option value="">Todas</option>
        <option v-for="c in companies" :key="c._id || c.id" :value="c._id || c.id">{{ c.name }}</option>
      </select>
      <button class="refresh-btn" @click="renderCharts">Atualizar</button>
    </div>

    <div class="charts">
      <div class="chart-card">
        <h3>Deals por Pipeline</h3>
        <canvas id="dealsPipelineChart" width="400" height="200"></canvas>
      </div>

      <div class="chart-card">
        <h3>Atividades por Tipo</h3>
        <canvas id="activitiesChart" width="400" height="200"></canvas>
      </div>
    </div>

    <div class="cards">
      <div class="card">
        <h3>Companies</h3>
        <router-link to="/companies" class="link">Ver empresas</router-link>
      </div>

      <div class="card">
        <h3>Contacts</h3>
        <router-link to="/contacts" class="link">Ver contatos</router-link>
      </div>
    </div>

    <!-- Funnel dashboard inserted below existing cards -->
    <FunnelDashboard :companyId="selectedCompanyId" />
  </section>
</template>

<script>
import { onMounted, ref, watch } from 'vue'
import { Chart, registerables } from 'chart.js'
import dealsService from '../services/deals'
import activitiesService from '../services/activities'
import companiesService from '../services/companies'
import FunnelDashboard from './FunnelDashboard.vue'

Chart.register(...registerables)

export default {
  name: 'Dashboard',
  setup() {
    const companies = ref([])
    const selectedCompanyId = ref('')
    let dealsChart = null
    let activitiesChart = null

    const fetchCompanies = async () => {
      try {
        const res = await companiesService.getCompanies()
        companies.value = res.data || []
      } catch (e) {
        console.error('Failed to load companies', e)
        companies.value = []
      }
    }

    const fetchDeals = async (companyId) => {
      try {
        const params = companyId ? { companyId } : {}
        const res = await dealsService.getDeals(params)
        return res.data
      } catch (e) {
        console.error('Failed to load deals', e)
        return []
      }
    }

    const fetchActivities = async (companyId) => {
      try {
        const params = companyId ? { companyId } : {}
        const res = await activitiesService.getActivities(params)
        return res.data
      } catch (e) {
        console.error('Failed to load activities', e)
        return []
      }
    }

    const destroyCharts = () => {
      try { if (dealsChart) { dealsChart.destroy(); dealsChart = null } } catch(e){}
      try { if (activitiesChart) { activitiesChart.destroy(); activitiesChart = null } } catch(e){}
    }

    const renderCharts = async () => {
      try {
        const companyId = selectedCompanyId.value || null
        const dealsResp = await fetchDeals(companyId)
        const activitiesResp = await fetchActivities(companyId)

        // prepare deals by pipeline stage
        const stageCounts = {}
        const stagesOrder = []
        if (Array.isArray(dealsResp)) {
          dealsResp.forEach(d => {
            const stage = d.pipelineStage || 'UNKNOWN'
            if (!stageCounts[stage]) {
              stageCounts[stage] = 0
              stagesOrder.push(stage)
            }
            stageCounts[stage] += 1
          })
        }

        const dealsLabels = stagesOrder
        const dealsData = dealsLabels.map(s => stageCounts[s] || 0)

        const ctx1 = document.getElementById('dealsPipelineChart')
        if (ctx1) {
          destroyCharts()
          dealsChart = new Chart(ctx1, {
            type: 'bar',
            data: {
              labels: dealsLabels,
              datasets: [{ label: 'Deals', data: dealsData, backgroundColor: '#4F46E5' }]
            },
            options: { responsive: true, maintainAspectRatio: false }
          })
        }

        // activities by type
        const typeCounts = { CALL: 0, TASK: 0, EMAIL: 0 }
        if (Array.isArray(activitiesResp)) {
          activitiesResp.forEach(a => {
            const t = (a.type || '').toString()
            if (typeCounts.hasOwnProperty(t)) typeCounts[t] += 1
            else typeCounts[t] = (typeCounts[t] || 0) + 1
          })
        }

        const activitiesLabels = Object.keys(typeCounts)
        const activitiesData = activitiesLabels.map(k => typeCounts[k])

        const ctx2 = document.getElementById('activitiesChart')
        if (ctx2) {
          activitiesChart = new Chart(ctx2, {
            type: 'doughnut',
            data: {
              labels: activitiesLabels,
              datasets: [{ data: activitiesData, backgroundColor: ['#10B981', '#F59E0B', '#3B82F6'] }]
            },
            options: { responsive: true, maintainAspectRatio: false }
          })
        }
      } catch (err) {
        console.error('Error rendering charts', err)
      }
    }

    onMounted(() => {
      fetchCompanies();
      renderCharts()
    })

    // re-render when company selection changes
    watch(selectedCompanyId, () => {
      renderCharts()
    })

    return { companies, selectedCompanyId, renderCharts }
  }
}
</script>

<style scoped>
.dashboard{padding:24px}
.charts{display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-top:18px}
.chart-card{background:var(--color-surface);padding:12px;border-radius:10px;box-shadow:var(--shadow);height:260px}
.cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:16px;margin-top:18px}
.card{background:var(--color-surface);padding:16px;border-radius:10px;box-shadow:var(--shadow);display:flex;flex-direction:column;gap:8px}
.link{margin-top:auto;color:var(--color-primary);text-decoration:none;font-weight:600}
</style>
