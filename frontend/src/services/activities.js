import api from './api'

export default {
  getActivities(params) {
    return api.get('/activities', { params })
  },
  getActivity(id) {
    return api.get(`/activities/${id}`)
  },
  createActivity(payload) {
    return api.post('/activities', payload)
  },
  updateActivity(id, payload) {
    return api.put(`/activities/${id}`, payload)
  },
  deleteActivity(id) {
    return api.delete(`/activities/${id}`)
  }
}
