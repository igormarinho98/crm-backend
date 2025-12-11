<template>
  <div class="chart-card">
    <h3>Funil de Vendas (por estágio)</h3>
    <div style="display:flex;gap:12px">
      <canvas id="funnelValueChart" width="400" height="180"></canvas>
      <canvas id="funnelCountChart" width="400" height="180"></canvas>
    </div>
  </div>
</template>

<script>
import { onMounted, watch } from 'vue'
import { Chart, registerables } from 'chart.js'
import dealsService from '../services/deals'

Chart.register(...registerables)

export default {
  name: 'FunnelDashboard',
  props: {
    companyId: { type: String, default: '' }
  },
  setup(props) {
    let valueChart = null
    let countChart = null

    const destroy = () => {
      try { if (valueChart) { valueChart.destroy(); valueChart = null } } catch(e){}
      try { if (countChart) { countChart.destroy(); countChart = null } } catch(e){}
    }

    const render = async () => {
      try {
        const params = props.companyId ? { companyId: props.companyId } : {}
        const res = await dealsService.getFunnel(params)
        const data = res.data || []
        // sort by pipelineStage for stable order
        data.sort((a,b) => (a.pipelineStage||'').localeCompare(b.pipelineStage||''))
        const labels = data.map(d => d.pipelineStage || 'UNKNOWN')
        const totals = data.map(d => d.totalValue || 0)
        const counts = data.map(d => d.count || 0)

        destroy()

        const ctxV = document.getElementById('funnelValueChart')
        if (ctxV) {
          valueChart = new Chart(ctxV, {
            type: 'bar',
            data: { labels, datasets: [{ label: 'Valor total (R$)', data: totals, backgroundColor: '#4F46E5' }] },
            options: { responsive: true, maintainAspectRatio: false }
          })
        }

        const ctxC = document.getElementById('funnelCountChart')
        if (ctxC) {
          countChart = new Chart(ctxC, {
            type: 'bar',
            data: { labels, datasets: [{ label: 'Contagem de Deals', data: counts, backgroundColor: '#10B981' }] },
            options: { responsive: true, maintainAspectRatio: false }
          })
        }
      } catch (e) {
        console.error('Failed to load funnel stats', e)
      }
    }

    onMounted(() => { render() })
    watch(() => props.companyId, () => { render() })

    return {}
  }
}
</script>

<style scoped>
.chart-card{background:var(--color-surface);padding:12px;border-radius:10px;box-shadow:var(--shadow);}
</style>
