<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>Book Goblin</h3>
          <p>Your personal library management system for tracking and discovering books.</p>
        </div>
        
        <div class="footer-section">
          <h4>Quick Links</h4>
          <router-link to="/" class="footer-link">Home</router-link>
          <router-link to="/discover" class="footer-link">Discover</router-link>
          <router-link to="/about" class="footer-link">About</router-link>
        </div>
        
        <div class="footer-section">
          <h4>Connect</h4>
          <a href="#" class="footer-link">GitHub</a>
          <a href="#" class="footer-link">Twitter</a>
          <a href="#" class="footer-link">Contact</a>
        </div>
      </div>
      
      <div class="footer-bottom">
        <p>&copy; 2024 Book Goblin. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer {
  background-color: var(--card-bg);
  border-top: 1px solid var(--border-color);
  margin-top: auto;
  padding: 40px 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h3,
.footer-section h4 {
  margin-bottom: 15px;
  color: var(--primary-color);
}

.footer-section p {
  color: var(--text-light);
  line-height: 1.6;
}

.footer-link {
  display: block;
  color: var(--text-light);
  text-decoration: none;
  margin-bottom: 8px;
  transition: var(--transition);
}

.footer-link:hover {
  color: var(--primary-color);
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
  color: var(--text-light);
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}
</style>