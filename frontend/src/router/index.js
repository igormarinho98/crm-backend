import { createRouter, createWebHistory } from 'vue-router'
import CompaniesList from '../components/CompaniesList.vue'
import ContactsList from '../components/ContactsList.vue'
import DealsList from '../components/DealsList.vue'
import UsersList from '../components/UsersList.vue'
import Dashboard from '../components/Dashboard.vue'

const routes = [
  { path: '/', redirect: '/companies' },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/companies', name: 'Companies', component: CompaniesList },
  { path: '/contacts', name: 'Contacts', component: ContactsList },
  { path: '/deals', name: 'Deals', component: DealsList },
  { path: '/users', name: 'Users', component: UsersList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
