<template>
  <div id="app" :class="{ 'dark-mode': isDarkMode }">
    <Header @toggle-theme="toggleTheme" />
    <div class="main-content">
      <router-view />
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

export default {
  name: 'App',
  components: {
    Header,
    Footer
  },
  data() {
    return {
      isDarkMode: false
    }
  },
  methods: {
    toggleTheme() {
      this.isDarkMode = !this.isDarkMode
      document.body.classList.toggle('dark-mode', this.isDarkMode)
    }
  },
  mounted() {
    // Check for saved theme preference
    const savedTheme = localStorage.getItem('darkMode')
    if (savedTheme) {
      this.isDarkMode = JSON.parse(savedTheme)
      document.body.classList.toggle('dark-mode', this.isDarkMode)
    }
  },
  watch: {
    isDarkMode(newVal) {
      localStorage.setItem('darkMode', JSON.stringify(newVal))
    }
  }
}
</script>

<style>
:root {
  --primary-color: #3f51b5;
  --secondary-color: #ff4081;
  --accent-color: #673ab7;
  --success-color: #4caf50;
  --warning-color: #ff9800;
  --danger-color: #f44336;
  --text-color: #333;
  --text-light: #666;
  --bg-color: #f5f5f5;
  --card-bg: #fff;
  --border-color: #ddd;
  --border-radius: 8px;
  --box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  --transition: all 0.3s ease;
  --font-size-sm: 0.875rem;
  --font-size-base: 1rem;
  --font-size-lg: 1.125rem;
  --font-size-xl: 1.25rem;
  --font-size-2xl: 1.5rem;
  --font-size-3xl: 2rem;
}

.dark-mode {
  --text-color: #f5f5f5;
  --text-light: #b0b0b0;
  --bg-color: #121212;
  --card-bg: #1e1e1e;
  --border-color: #333;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: var(--bg-color);
  color: var(--text-color);
  transition: var(--transition);
  line-height: 1.6;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Common Component Styles */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: var(--border-radius);
  cursor: pointer;
  font-size: var(--font-size-base);
  transition: var(--transition);
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: #303f9f;
  transform: translateY(-2px);
}

.btn-secondary {
  background-color: var(--secondary-color);
  color: white;
}

.btn-secondary:hover {
  background-color: #e91e63;
  transform: translateY(-2px);
}

.btn-outline {
  background-color: transparent;
  border: 2px solid var(--primary-color);
  color: var(--primary-color);
}

.btn-outline:hover {
  background-color: var(--primary-color);
  color: white;
  transform: translateY(-2px);
}

.btn-danger {
  background-color: var(--danger-color);
  color: white;
}

.btn-danger:hover {
  background-color: #d32f2f;
  transform: translateY(-2px);
}

.btn-sm {
  padding: 6px 12px;
  font-size: var(--font-size-sm);
}

.btn-lg {
  padding: 12px 24px;
  font-size: var(--font-size-lg);
}

.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: var(--font-size-base);
  transition: var(--transition);
  background-color: var(--card-bg);
  color: var(--text-color);
}

.form-control:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(63, 81, 181, 0.2);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: var(--text-color);
}

.card {
  background-color: var(--card-bg);
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 20px;
  transition: var(--transition);
}

.card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.text-center {
  text-align: center;
}

.mb-1 { margin-bottom: 10px; }
.mb-2 { margin-bottom: 20px; }
.mb-3 { margin-bottom: 30px; }
.mt-1 { margin-top: 10px; }
.mt-2 { margin-top: 20px; }
.mt-3 { margin-top: 30px; }

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.align-center {
  align-items: center;
}

.gap-1 { gap: 10px; }
.gap-2 { gap: 20px; }
.gap-3 { gap: 30px; }
</style>