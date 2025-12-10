<template>
  <div id="app">
    <AppHeader />
    <main>
      <router-view />
    </main>
  </div>
</template>

<script>
import AppHeader from './components/ui/AppHeader.vue'

export default {
  name: 'App',
  components: { AppHeader }
}
</script>

<style scoped>
#app { font-family: Arial, sans-serif; padding: 24px }
header { margin-bottom: 16px }
</style>
