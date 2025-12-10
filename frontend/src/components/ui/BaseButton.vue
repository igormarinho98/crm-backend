<template>
  <button :class="['base-button', variant]" @click="$emit('click')">
    <slot />
  </button>
</template>

<script>
export default {
  name: 'BaseButton',
  props: { variant: { type: String, default: 'primary' } }
}
</script>

<style scoped>
.base-button{padding:8px 12px;border-radius:10px;border:none;cursor:pointer;font-weight:600}
.base-button.primary{background:var(--color-primary);color:#fff}
.base-button.ghost{background:transparent;color:var(--color-text);border:1px solid rgba(16,24,40,0.06)}
.base-button.success{background:var(--color-success);color:#fff}
</style>