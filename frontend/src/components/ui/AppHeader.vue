<template>
  <header class="app-header">
    <div class="brand">CRM</div>

    <button class="menu-toggle" @click="toggleMenu" aria-label="Toggle menu">☰</button>

    <nav :class="['nav', { open: menuOpen }]">
      <router-link to="/companies">Companies</router-link>
      <router-link to="/contacts">Contacts</router-link>
      <router-link to="/deals">Deals</router-link>
      <router-link to="/users">Users</router-link>
    </nav>

    <div class="actions">
      <input class="search" v-model="query" @keyup.enter="submitSearch" placeholder="Pesquisar por ID e pressionar Enter" />
      <button class="cta" @click="submitSearch">Buscar</button>
      <button class="cta" style="margin-left:8px">Nova</button>
    </div>
  </header>
</template>

<script>
export default {
  name: 'AppHeader',
  data() {
    return { menuOpen: false, query: '' }
  },
  methods: {
    toggleMenu() { this.menuOpen = !this.menuOpen },
    submitSearch() {
      const q = (this.query || '').toString().trim()
      const query = q ? { id: q } : {}
      this.$router.push({ path: this.$route.path, query })
    }
  }
}
</script>

<style scoped>
.app-header{display:flex;align-items:center;justify-content:space-between;padding:12px 20px;background:transparent;gap:12px}
.brand{font-weight:800;color:var(--color-text);font-size:1.25rem}
.menu-toggle{display:none;background:transparent;border:0;font-size:1.25rem;cursor:pointer}
.nav{display:flex;gap:16px;align-items:center}
.nav a{color:var(--color-subtext);text-decoration:none;padding:6px 8px;border-radius:8px;display:inline-flex;align-items:center}
.nav a:hover{background:rgba(16,24,40,0.03)}
.nav a.router-link-active,.nav a.router-link-exact-active{color:var(--color-primary);font-weight:700}
.actions{display:flex;align-items:center;gap:12px}
.search{padding:8px;border-radius:8px;border:1px solid rgba(16,24,40,0.06)}
.cta{background:var(--color-primary);color:#fff;border-radius:8px;padding:8px 12px;border:none}

/* Responsive: collapse nav into menu on small screens */
@media (max-width: 720px) {
  .menu-toggle{display:block}
  .nav{position:absolute;top:64px;left:0;right:0;background:var(--color-surface);flex-direction:column;padding:12px 16px;gap:8px;border-bottom:1px solid rgba(16,24,40,0.04);display:none}
  .nav.open{display:flex}
  .actions{display:none}
}
</style>