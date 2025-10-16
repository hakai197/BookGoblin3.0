<template>
  <div class="sidebar-image">
    <img :src="imageUrl" :alt="altText" />
    <div class="sidebar-content" v-if="$slots.default">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SidebarImage',
  props: {
    imageUrl: {
      type: String,
      required: true
    },
    altText: {
      type: String,
      default: 'Sidebar image'
    }
  }
}
</script>

<style scoped>
.sidebar-image {
  position: relative;
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--box-shadow);
}

.sidebar-image img {
  width: 100%;
  height: auto;
  display: block;
}

.sidebar-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  color: white;
  padding: 20px;
}

@media (max-width: 768px) {
  .sidebar-image {
    margin-bottom: 20px;
  }
}
</style>