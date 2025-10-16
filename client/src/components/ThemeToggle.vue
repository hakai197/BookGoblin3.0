<template>
  <button class="theme-toggle" @click="$emit('toggle-theme')" :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'">
    <font-awesome-icon :icon="isDark ? 'sun' : 'moon'" />
  </button>
</template>

<script>
export default {
  name: 'ThemeToggle',
  props: {
    isDark: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>
.theme-toggle {
  background: none;
  border: none;
  color: var(--text-color);
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  transition: var(--transition);
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle:hover {
  background-color: var(--border-color);
  transform: rotate(15deg);
}
</style>