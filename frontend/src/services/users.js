import api from './api'

export default {
  getUsers(params) {
    return api.get('/users', { params })
  },
  getUser(id) {
    return api.get(`/users/${id}`)
  },
  createUser(payload) {
    return api.post('/users', payload)
  },
  updateUser(id, payload) {
    return api.put(`/users/${id}`, payload)
  },
  deleteUser(id) {
    return api.delete(`/users/${id}`)
  }
}
