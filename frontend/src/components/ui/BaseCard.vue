<template>
  <article class="base-card" @click="$emit('click')">
    <slot />
  </article>
</template>

<script>
export default { name: 'BaseCard' }
</script>

<style scoped>
.base-card{background:var(--color-surface);border-radius:12px;padding:16px;box-shadow:var(--shadow-1);border:1px solid rgba(16,24,40,0.04);transition:transform .18s ease,box-shadow .18s ease}
.base-card:hover{transform:translateY(-6px);box-shadow:var(--shadow-2)}
</style>