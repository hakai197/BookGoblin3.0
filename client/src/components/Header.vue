<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <router-link to="/" class="logo">
          <h1>Book Goblin</h1>
        </router-link>
        
        <nav class="nav" :class="{ 'nav-open': isMobileMenuOpen }">
          <router-link to="/" class="nav-link">Home</router-link>
          <router-link to="/dashboard" class="nav-link" v-if="$store.state.token">Dashboard</router-link>
          <router-link to="/library" class="nav-link" v-if="$store.state.token">My Library</router-link>
          <router-link to="/reading-log" class="nav-link" v-if="$store.state.token">Reading Log</router-link>
          <router-link to="/discover" class="nav-link" v-if="$store.state.token">Discover</router-link>
        </nav>

        <div class="header-actions">
          <ThemeToggle @toggle-theme="$emit('toggle-theme')" />
          
          <div class="auth-section" v-if="!$store.state.token">
            <router-link to="/login" class="btn btn-outline">Sign In</router-link>
            <router-link to="/register" class="btn btn-primary">Sign Up</router-link>
          </div>
          
          <div class="user-section" v-else>
            <router-link to="/account" class="user-info">
              <span>{{ $store.state.user.username }}</span>
            </router-link>
            <button @click="logout" class="btn btn-outline btn-sm">Logout</button>
          </div>

          <button class="mobile-menu-btn" @click="toggleMobileMenu">
            <span></span>
            <span></span>
            <span></span>
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import ThemeToggle from './ThemeToggle.vue'

export default {
  name: 'Header',
  components: {
    ThemeToggle
  },
  data() {
    return {
      isMobileMenuOpen: false
    }
  },
  methods: {
    logout() {
      this.$store.commit('LOGOUT')
      this.$router.push('/')
    },
    toggleMobileMenu() {
      this.isMobileMenuOpen = !this.isMobileMenuOpen
    }
  }
}
</script>

<style scoped>
.header {
  background-color: var(--card-bg);
  box-shadow: var(--box-shadow);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 0;
}

.logo {
  text-decoration: none;
  color: var(--primary-color);
}

.logo h1 {
  font-size: var(--font-size-2xl);
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 30px;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  color: var(--text-color);
  font-weight: 500;
  transition: var(--transition);
  padding: 8px 0;
  position: relative;
}

.nav-link:hover {
  color: var(--primary-color);
}

.nav-link.router-link-active {
  color: var(--primary-color);
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background-color: var(--primary-color);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.auth-section {
  display: flex;
  gap: 10px;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  text-decoration: none;
  color: var(--text-color);
  font-weight: 500;
}

.mobile-menu-btn {
  display: none;
  flex-direction: column;
  gap: 4px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
}

.mobile-menu-btn span {
  width: 20px;
  height: 2px;
  background-color: var(--text-color);
  transition: var(--transition);
}

@media (max-width: 768px) {
  .mobile-menu-btn {
    display: flex;
  }

  .nav {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background-color: var(--card-bg);
    flex-direction: column;
    padding: 20px;
    box-shadow: var(--box-shadow);
    transform: translateY(-100%);
    opacity: 0;
    visibility: hidden;
    transition: var(--transition);
  }

  .nav.nav-open {
    transform: translateY(0);
    opacity: 1;
    visibility: visible;
  }

  .auth-section {
    display: none;
  }

  .user-section .user-info {
    display: none;
  }
}
</style>