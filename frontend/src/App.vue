<template>
  <div id="app">
    <AppHeader @toggle-theme="toggleDarkMode" /> 
    
    <main>
      <router-view />
    </main>
  </div>
</template>

<script>
import AppHeader from './components/ui/AppHeader.vue'

export default {
  name: 'App',
  components: { AppHeader },
  mounted() {
    this.initializeTheme();
  },methods:{
    initializeTheme() {
      const savedTheme = localStorage.getItem('theme');
      const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
      
      let themeToApply = 'light';
      
      if (savedTheme) {
        themeToApply = savedTheme;
      } else if (prefersDark) {
        themeToApply = 'dark';
      }

      if (themeToApply === 'dark') {
        document.body.classList.add('dark');
      } else {
        document.body.classList.remove('dark');
      }
    },

    /**
     * Alterna entre modo claro e escuro, e salva a preferência.
     */
    toggleDarkMode() {
      const isDark = document.body.classList.toggle('dark');
      if (isDark) {
        localStorage.setItem('theme', 'dark');
      } else {
        localStorage.setItem('theme', 'light');
      }
    }
  }

  }

</script>

<style scoped>
#app { font-family: Arial, sans-serif; padding: 24px }
header { margin-bottom: 16px }
</style>
