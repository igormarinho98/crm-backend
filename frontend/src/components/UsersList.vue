<template>
  <section class="list-section">
    <div class="list-header">
      <h3>Users</h3>
      <div>
        <button class="refresh-btn" @click="loadUsers">Atualizar</button>
        <button style="margin-left:8px" class="refresh-btn" @click="openCreate">Criar</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="loading" class="spinner"></div>

    <div v-else>
      <div v-if="items.length" class="list-grid">
        <article v-for="u in items" :key="u.id" class="item-card" :title="u.firstName + ' ' + u.lastName" @click="open(u)">
          <div class="item-title">{{ u.firstName }} {{ u.lastName }}</div>
          <div class="item-sub">{{ u.email || '—' }}</div>
          <div class="item-id">ID: {{ u.id }}</div>
        </article>
      </div>
      <pre v-else class="empty">No users loaded.</pre>
    </div>
  </section>
  <DetailModal v-if="showModal" :title="selected.firstName + ' ' + selected.lastName" :data="selected" type="user" @close="showModal=false" @updated="handleUpdated" />
  <CreateModal v-if="showCreate" type="user" title="Criar Usuário" @close="showCreate=false" @created="handleCreated" />
</template>

<script>
import usersService from '../services/users'
import DetailModal from './DetailModal.vue'
import CreateModal from './CreateModal.vue'

export default {
  name: 'UsersList',
  components: { DetailModal, CreateModal },
  data() {
    return {
      items: [],
      error: null,
      loading: true
      ,selected: null,
      showModal: false
      ,showCreate: false
    }
  },
  async mounted() {
    const id = this.$route?.query?.id
    if (id) this.fetchById(id)
    else await this.loadUsers()
  },
  watch: {
    '$route.query.id'(val) {
      if (val) this.fetchById(val)
      else this.loadUsers()
    }
  },
  methods: {
    async fetchById(id) {
      this.loading = true
      this.error = null
      try {
        const res = await usersService.getUser(id)
        this.items = res.data ? [res.data] : []
      } catch (e) {
        this.items = []
        this.error = e?.response?.data || e.message || `Erro buscando user ${id}`
      } finally {
        this.loading = false
      }
    }
    ,
    async loadUsers() {
      this.loading = true
      this.error = null
      try {
        const res = await usersService.getUsers()
        this.items = res.data || []
      } catch (e) {
        this.error = e?.response?.data || e.message || 'Erro ao carregar users'
      } finally {
        this.loading = false
      }
    }
    ,open(item) {
      this.selected = item
      this.showModal = true
    }
    ,openCreate() {
      this.showCreate = true
    }
    ,handleCreated() {
      this.showCreate = false
      this.loadUsers()
    }
    ,handleUpdated() {
      this.showModal = false
      this.loadUsers()
    }
  }
}
</script>
