<template>
  <header class="app-header">
    <div class="brand">🚀 CRM</div>

    <button class="menu-toggle" @click="toggleMenu" aria-label="Toggle menu">☰</button>

    <nav :class="['nav', { open: menuOpen }]">
      <router-link to="/dashboard">Dashboard</router-link>
      <router-link to="/companies">Empresas</router-link>
      <router-link to="/contacts">Contatos</router-link>
      <router-link to="/deals">Negócios</router-link>
      <router-link to="/users">Usuários</router-link>
    </nav>
    
    <div class="actions">
      <div class="search-container">
        <input 
          class="search-input" 
          v-model="query" 
          @keyup.enter="submitSearch" 
          placeholder="Pesquisar por ID..." 
          aria-label="Pesquisar por ID"
        />
        <button class="search-btn" @click="submitSearch">🔎</button>
      </div>

      <button @click="$emit('toggle-theme')" class="theme-toggle-btn" aria-label="Alternar modo claro/escuro">
        <span class="theme-icon">🌙 / 💡</span>
      </button>

      <button class="cta-new">➕ Novo</button>
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
/* ... (Estilos anteriores) ... */

/* ========================================================= */
/* BASE HEADER & THEME SWITCH */
/* ========================================================= */
.app-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: var(--space-sm) var(--space-xl);
    background: var(--color-surface);
    box-shadow: var(--shadow-1);
    position: sticky;
    top: 0;
    z-index: 100;
}
/* ... (brand e menu-toggle permanecem iguais) ... */

/* ========================================================= */
/* NAVEGAÇÃO (NAV) - CHAVE PARA LIBERAR ESPAÇO */
/* ========================================================= */
.nav {
    display: flex;
    gap: var(--space-md);
    align-items: center;
    flex-grow: 1; 
    margin-left: var(--space-lg);
    /* NOVO: Garante que a barra de navegação ocupe o máximo de espaço 
       e empurre as ações para a direita */
    margin-right: auto; 
}

.nav a {
    /* Deve usar a cor do texto secundário ou principal */
    color: var(--color-subtext); 
    /* ... */
}
.nav a.router-link-active, .nav a.router-link-exact-active {
    /* A cor primária deve ter contraste suficiente no modo escuro */
    color: var(--color-primary); 
    /* ... */
}
/* ... (estilos de nav a permanecem iguais) ... */


/* ========================================================= */
/* BOTÃO DE TEMA E AÇÕES - AJUSTADO */
/* ========================================================= */
.actions {
    display: flex;
    align-items: center;
    /* Mantém o espaçamento entre os itens de ação */
    gap: var(--space-sm); 
    /* NOVO: Garante que as ações não sejam forçadas a quebrar */
    flex-shrink: 0; 
}

/* 1. Botão Alternar Tema (Sutil) */
/* ... (theme-toggle-btn permanece igual) ... */

/* 2. Campo de Busca Consolidado */
.search-container {
    display: flex;
    align-items: center;
    border: 1px solid var(--color-border);
    border-radius: var(--radius-sm);
    overflow: hidden;
    background: var(--color-surface);
    /* NOVO: Define uma largura para que não ocupe espaço demais */
    width: 280px; 
}
.search-input {
    padding: 8px 10px;
    border: none;
    outline: none;
    background: transparent;
    color: var(--color-text);
    font-size: var(--fs-sm);
    width: 100%; /* Garante que o input preencha o container */
}
/* ... (search-btn e cta-new permanecem iguais) ... */

/* ========================================================= */
/* RESPONSIVE & MOBILE MENU */
/* ... (A seção @media (max-width: 720px) permanece a mesma) ... */
</style>