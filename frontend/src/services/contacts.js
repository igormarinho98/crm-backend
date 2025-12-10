import api from './api'

export default {
  getContacts(params) {
    return api.get('/contacts', { params })
  },
  getContact(id) {
    return api.get(`/contacts/${id}`)
  },
  createContact(payload) {
    return api.post('/contacts', payload)
  },
  updateContact(id, payload) {
    return api.put(`/contacts/${id}`, payload)
  },
  deleteContact(id) {
    return api.delete(`/contacts/${id}`)
  }
}
