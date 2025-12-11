import api from './api'

export default {
  getDeals(params) {
    return api.get('/deals', { params })
  },
  getDeal(id) {
    return api.get(`/deals/${id}`)
  },
  createDeal(payload) {
    return api.post('/deals', payload)
  },
  updateDeal(id, payload) {
    return api.put(`/deals/${id}`, payload)
  },
  changeStage(id, partial) {
    return api.patch(`/deals/${id}/stage`, partial)
  },
  deleteDeal(id) {
    return api.delete(`/deals/${id}`)
  }
  ,getFunnel(params) {
    return api.get('/deals/funnel', { params })
  }
}
