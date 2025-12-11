<template>
  <span :class="['status-tag', statusClass]">{{ status }}</span>
</template>

<script>
export default {
  props: {
    status: {
      type: String,
      required: true
    }
  },
  computed: {
    statusClass() {
      switch (this.status.toUpperCase()) {
        case 'CLIENTE':
        case 'ATIVO':
          return 'status-success';
        case 'PROSPECCAO':
          return 'status-warning';
        case 'INATIVO':
          return 'status-danger';
        default:
          return 'status-default';
      }
    }
  }
}
</script>

<style scoped>
.status-tag {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  color: white;
  white-space: nowrap;
}
.status-success { background-color: var(--color-success); }
.status-warning { background-color: var(--color-warning); color: #333; }
.status-danger { background-color: var(--color-danger); }
.status-default { background-color: var(--color-text-muted); }
</style>