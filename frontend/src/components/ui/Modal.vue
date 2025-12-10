<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-surface">
      <header class="modal-head">
        <h3>{{ title }}</h3>
        <button class="modal-close" @click="$emit('close')">✕</button>
      </header>
      <div class="modal-content">
        <slot />
      </div>
    </div>
  </div>
</template>

<script>
export default { name: 'Modal', props: { title: { type: String, default: '' } } }
</script>

<style scoped>
.modal-backdrop{position:fixed;inset:0;background:rgba(2,6,23,0.45);display:flex;align-items:center;justify-content:center;z-index:60}
.modal-surface{background:var(--color-surface);border-radius:12px;padding:20px;max-width:720px;width:95%;box-shadow:var(--shadow-2)}
.modal-head{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
.modal-close{background:transparent;border:0;font-size:1.1rem;cursor:pointer}
.modal-content{max-height:60vh;overflow:auto}
</style>