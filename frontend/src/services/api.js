import axios from 'axios'

const BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/'

const api = axios.create({
  baseURL: BASE,
  timeout: 5000
})

export default api
