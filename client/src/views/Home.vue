<template>
  <div class="home">
    <section class="hero">
      <div class="container">
        <div class="hero-content">
          <div class="hero-text">
            <h1>Welcome to Book Goblin</h1>
            <p>Your personal library management system. Track your reading, discover new books, and organize your collection.</p>
            <div class="hero-buttons">
              <router-link v-if="!$store.state.token" to="/register" class="btn btn-primary btn-lg">
                Get Started
              </router-link>
              <router-link v-else to="/library" class="btn btn-primary btn-lg">
                View My Library
              </router-link>
              <router-link to="/discover" class="btn btn-outline btn-lg">
                Discover Books
              </router-link>
            </div>
          </div>
          <div class="hero-image">
            <SidebarImage imageUrl="/book-hero.jpg" alt="Books collection">
              <h3>Start Your Reading Journey</h3>
            </SidebarImage>
          </div>
        </div>
      </div>
    </section>

    <section class="features">
      <div class="container">
        <h2 class="text-center">Why Choose Book Goblin?</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📚</div>
            <h3>Track Your Reading</h3>
            <p>Keep detailed logs of your reading progress, ratings, and notes for every book.</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔍</div>
            <h3>Discover New Books</h3>
            <p>Search through thousands of books and find your next favorite read.</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>Reading Statistics</h3>
            <p>Get insights into your reading habits with comprehensive statistics.</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🏷️</div>
            <h3>Organize with Tags</h3>
            <p>Categorize your books with custom tags for easy organization.</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import SidebarImage from '../components/SidebarImage.vue'

export default {
  name: 'Home',
  components: {
    SidebarImage
  }
}
</script>

<style scoped>
.home {
  padding: 0;
}

.hero {
  padding: 80px 0;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--accent-color) 100%);
  color: white;
}

.hero-content {
  display: flex;
  align-items: center;
  gap: 60px;
}

.hero-text {
  flex: 1;
}

.hero-text h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  color: white;
}

.hero-text p {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
  max-width: 500px;
}

.hero-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.hero-image {
  flex: 1;
}

.features {
  padding: 80px 0;
}

.features h2 {
  font-size: 2.5rem;
  margin-bottom: 50px;
  color: var(--primary-color);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: var(--primary-color);
}

.feature-card p {
  color: var(--text-light);
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 40px;
  }

  .hero-text h1 {
    font-size: 2.5rem;
  }

  .hero-buttons {
    justify-content: center;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>