import api from './api'

export default {
  getCompanies(params) {
    return api.get('/companies', { params })
  },
  getCompany(id) {
    return api.get(`/companies/${id}`)
  },
  createCompany(payload) {
    return api.post('/companies', payload)
  },
  updateCompany(id, payload) {
    return api.put(`/companies/${id}`, payload)
  },
  deleteCompany(id) {
    return api.delete(`/companies/${id}`)
  }
}
