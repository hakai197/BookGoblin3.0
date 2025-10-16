<template>
  <div class="dashboard">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">Dashboard</h1>
        <p>Welcome back, {{ $store.state.user.username }}!</p>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-number">{{ stats.totalBooks || 0 }}</div>
          <div class="stat-label">Total Books</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ stats.booksRead || 0 }}</div>
          <div class="stat-label">Books Read</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ stats.currentlyReading || 0 }}</div>
          <div class="stat-label">Currently Reading</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ stats.pagesRead || 0 }}</div>
          <div class="stat-label">Pages Read</div>
        </div>
      </div>

      <div class="dashboard-content">
        <div class="recent-activity card">
          <h2>Recent Activity</h2>
          <div v-if="recentLogs.length > 0" class="activity-list">
            <div v-for="log in recentLogs" :key="log.logId" class="activity-item">
              <div class="activity-info">
                <strong>{{ log.bookTitle }}</strong> by {{ log.bookAuthor }}
                <span class="activity-date">{{ formatDate(log.startDate) }}</span>
              </div>
              <div class="activity-status" :class="getStatusClass(log)">
                {{ getStatusText(log) }}
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <p>No recent reading activity</p>
            <router-link to="/reading-log" class="btn btn-primary">Start Logging</router-link>
          </div>
        </div>

        <div class="quick-actions card">
          <h2>Quick Actions</h2>
          <div class="actions-grid">
            <router-link to="/library" class="action-card">
              <div class="action-icon">📚</div>
              <h3>My Library</h3>
              <p>View and manage your book collection</p>
            </router-link>
            <router-link to="/discover" class="action-card">
              <div class="action-icon">🔍</div>
              <h3>Discover</h3>
              <p>Find new books to read</p>
            </router-link>
            <router-link to="/reading-log" class="action-card">
              <div class="action-icon">📖</div>
              <h3>Reading Log</h3>
              <p>Track your reading progress</p>
            </router-link>
            <router-link to="/account" class="action-card">
              <div class="action-icon">👤</div>
              <h3>Account</h3>
              <p>Manage your profile and settings</p>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userBooksService from '../services/userBooks'
import readingLogsService from '../services/readingLogs'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      recentLogs: [],
      loading: true
    }
  },
  async mounted() {
    await this.loadDashboardData()
  },
  methods: {
    async loadDashboardData() {
      try {
        const [userBooksRes, readingLogsRes] = await Promise.all([
          userBooksService.getUserBooks(),
          this.getRecentLogs()
        ])
        
        const userBooks = userBooksRes.data
        this.calculateStats(userBooks)
      } catch (error) {
        console.error('Error loading dashboard data:', error)
      } finally {
        this.loading = false
      }
    },
    
    calculateStats(userBooks) {
      this.stats = {
        totalBooks: userBooks.length,
        booksRead: userBooks.filter(book => book.currentStatus === 'finished').length,
        currentlyReading: userBooks.filter(book => book.currentStatus === 'reading').length,
        pagesRead: 0 // This would need additional data
      }
    },
    
    async getRecentLogs() {
      try {
        // For demo purposes, we'll create some mock recent logs
        // In a real app, you'd fetch these from your API
        this.recentLogs = [
          {
            logId: 1,
            bookTitle: 'Sample Book 1',
            bookAuthor: 'Author One',
            startDate: '2024-01-15',
            endDate: null,
            rating: 4
          },
          {
            logId: 2,
            bookTitle: 'Sample Book 2',
            bookAuthor: 'Author Two',
            startDate: '2024-01-10',
            endDate: '2024-01-14',
            rating: 5
          }
        ]
      } catch (error) {
        console.error('Error loading recent logs:', error)
      }
    },
    
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString()
    },
    
    getStatusClass(log) {
      return log.endDate ? 'finished' : 'reading'
    },
    
    getStatusText(log) {
      return log.endDate ? 'Finished' : 'Currently Reading'
    }
  }
}
</script>

<style scoped>
.dashboard-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
  margin-top: 30px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.activity-item:hover {
  background-color: var(--border-color);
}

.activity-info {
  flex: 1;
}

.activity-date {
  display: block;
  font-size: var(--font-size-sm);
  color: var(--text-light);
  margin-top: 5px;
}

.activity-status {
  padding: 5px 10px;
  border-radius: 20px;
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.activity-status.reading {
  background-color: #fff3cd;
  color: #856404;
}

.activity-status.finished {
  background-color: #d1edff;
  color: #0c5460;
}

.actions-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.action-card {
  display: block;
  padding: 20px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
  text-decoration: none;
  color: var(--text-color);
  transition: var(--transition);
  text-align: center;
}

.action-card:hover {
  background-color: var(--primary-color);
  color: white;
  transform: translateY(-3px);
}

.action-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.action-card h3 {
  margin-bottom: 8px;
  font-size: var(--font-size-base);
}

.action-card p {
  font-size: var(--font-size-sm);
  opacity: 0.8;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
}

@media (max-width: 768px) {
  .dashboard-content {
    grid-template-columns: 1fr;
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
  
  .activity-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>