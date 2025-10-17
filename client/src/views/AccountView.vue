<template>
  <div class="account">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">Account Settings</h1>
      </div>

      <div class="account-content">
        <div class="account-sidebar">
          <div class="account-profile card">
            <div class="profile-avatar">
              <img src="/img/avatar.png" alt="User Avatar" />
            </div>
            <h2>{{ $store.state.user.username }}</h2>
            <p class="user-role">{{ $store.state.user.role }}</p>
          </div>

          <nav class="account-menu card">
            <a 
              v-for="tab in tabs" 
              :key="tab.key"
              :class="{ active: activeTab === tab.key }"
              @click="activeTab = tab.key"
            >
              {{ tab.label }}
            </a>
          </nav>
        </div>

        <div class="account-details">
          <div class="account-section card">
            <!-- Profile Tab -->
            <div v-if="activeTab === 'profile'" class="tab-content">
              <h2>Profile Information</h2>
              <form @submit.prevent="updateProfile" class="account-form">
                <div class="form-group">
                  <label>Username</label>
                  <input 
                    type="text" 
                    v-model="profile.username" 
                    class="form-control" 
                    required 
                  />
                </div>
                <div class="form-group">
                  <label>Email</label>
                  <input 
                    type="email" 
                    v-model="profile.email" 
                    class="form-control" 
                  />
                </div>
                <div class="form-group">
                  <label>Bio</label>
                  <textarea 
                    v-model="profile.bio" 
                    class="form-control" 
                    rows="4"
                    placeholder="Tell us about yourself..."
                  ></textarea>
                </div>
                <div class="form-actions">
                  <button type="submit" class="btn btn-primary">Update Profile</button>
                </div>
              </form>
            </div>

            <!-- Security Tab -->
            <div v-if="activeTab === 'security'" class="tab-content">
              <h2>Security Settings</h2>
              <form @submit.prevent="changePassword" class="account-form">
                <div class="form-group">
                  <label>Current Password</label>
                  <input 
                    type="password" 
                    v-model="password.current" 
                    class="form-control" 
                    required 
                  />
                </div>
                <div class="form-group">
                  <label>New Password</label>
                  <input 
                    type="password" 
                    v-model="password.new" 
                    class="form-control" 
                    required 
                  />
                </div>
                <div class="form-group">
                  <label>Confirm New Password</label>
                  <input 
                    type="password" 
                    v-model="password.confirm" 
                    class="form-control" 
                    required 
                  />
                </div>
                <div class="form-actions">
                  <button type="submit" class="btn btn-primary">Change Password</button>
                </div>
              </form>
            </div>

            <!-- Preferences Tab -->
            <div v-if="activeTab === 'preferences'" class="tab-content">
              <h2>Reading Preferences</h2>
              <form @submit.prevent="updatePreferences" class="account-form">
                <div class="form-group">
                  <label>Favorite Genres</label>
                  <div class="preferences-grid">
                    <label 
                      v-for="genre in allGenres" 
                      :key="genre"
                      class="preference-option"
                    >
                      <input 
                        type="checkbox" 
                        :value="genre" 
                        v-model="preferences.favoriteGenres" 
                      />
                      {{ genre }}
                    </label>
                  </div>
                </div>
                <div class="form-group">
                  <label>Reading Goal (books per year)</label>
                  <input 
                    type="number" 
                    v-model="preferences.readingGoal" 
                    class="form-control" 
                    min="1"
                    max="1000"
                  />
                </div>
                <div class="form-group">
                  <label>Default Reading Status</label>
                  <select v-model="preferences.defaultStatus" class="form-control">
                    <option value="unread">Unread</option>
                    <option value="reading">Currently Reading</option>
                    <option value="finished">Finished</option>
                  </select>
                </div>
                <div class="form-actions">
                  <button type="submit" class="btn btn-primary">Save Preferences</button>
                </div>
              </form>
            </div>

            <!-- Statistics Tab -->
            <div v-if="activeTab === 'statistics'" class="tab-content">
              <h2>Reading Statistics</h2>
              <div class="stats-cards">
                <div class="stat-card">
                  <div class="stat-number">{{ stats.totalBooks || 0 }}</div>
                  <div class="stat-label">Total Books</div>
                </div>
                <div class="stat-card">
                  <div class="stat-number">{{ stats.booksRead || 0 }}</div>
                  <div class="stat-label">Books Read</div>
                </div>
                <div class="stat-card">
                  <div class="stat-number">{{ stats.readingGoal || 0 }}</div>
                  <div class="stat-label">Reading Goal</div>
                </div>
                <div class="stat-card">
                  <div class="stat-number">{{ stats.goalProgress || 0 }}%</div>
                  <div class="stat-label">Goal Progress</div>
                </div>
              </div>

              <div class="reading-stats">
                <h3>Recent Activity</h3>
                <div v-if="recentActivity.length > 0" class="activity-list">
                  <div 
                    v-for="activity in recentActivity" 
                    :key="activity.id"
                    class="activity-item"
                  >
                    <div class="activity-icon">📖</div>
                    <div class="activity-details">
                      <strong>{{ activity.bookTitle }}</strong>
                      <span class="activity-date">{{ formatDate(activity.date) }}</span>
                    </div>
                    <div class="activity-status">{{ activity.status }}</div>
                  </div>
                </div>
                <div v-else class="empty-state">
                  <p>No recent reading activity</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userBooksService from '../services/userBooks'

export default {
  name: 'AccountView',
  data() {
    return {
      activeTab: 'profile',
      tabs: [
        { key: 'profile', label: 'Profile' },
        { key: 'security', label: 'Security' },
        { key: 'preferences', label: 'Preferences' },
        { key: 'statistics', label: 'Statistics' }
      ],
      profile: {
        username: this.$store.state.user.username,
        email: '',
        bio: ''
      },
      password: {
        current: '',
        new: '',
        confirm: ''
      },
      preferences: {
        favoriteGenres: [],
        readingGoal: 52,
        defaultStatus: 'unread'
      },
      stats: {},
      recentActivity: [],
      allGenres: [
        'Fantasy', 'Science Fiction', 'Mystery', 'Romance', 
        'Thriller', 'Horror', 'Historical', 'Biography',
        'Self-Help', 'Business', 'Science', 'Technology'
      ]
    }
  },
  async mounted() {
    await this.loadUserData()
  },
  methods: {
    async loadUserData() {
      try {
        const userBooksRes = await userBooksService.getUserBooks()
        const userBooks = userBooksRes.data
        
        this.calculateStats(userBooks)
        this.loadRecentActivity(userBooks)
      } catch (error) {
        console.error('Error loading user data:', error)
      }
    },

    calculateStats(userBooks) {
      const totalBooks = userBooks.length
      const booksRead = userBooks.filter(book => book.currentStatus === 'finished').length
      const readingGoal = this.preferences.readingGoal || 52
      const goalProgress = readingGoal > 0 ? Math.min(Math.round((booksRead / readingGoal) * 100), 100) : 0

      this.stats = {
        totalBooks,
        booksRead,
        readingGoal,
        goalProgress
      }
    },

    loadRecentActivity(userBooks) {
      // For demo purposes, create some mock activity
      this.recentActivity = userBooks.slice(0, 5).map(book => ({
        id: book.userBookId,
        bookTitle: book.bookTitle,
        date: book.dateAdded,
        status: this.getStatusText(book.currentStatus)
      }))
    },

    getStatusText(status) {
      const statusMap = {
        'unread': 'Added to Library',
        'reading': 'Started Reading',
        'finished': 'Finished Reading',
        'dnf': 'Did Not Finish'
      }
      return statusMap[status] || status
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString()
    },

    updateProfile() {
      // In a real app, you'd make an API call to update the profile
      alert('Profile updated successfully!')
    },

    changePassword() {
      if (this.password.new !== this.password.confirm) {
        alert('New passwords do not match!')
        return
      }
      // In a real app, you'd make an API call to change the password
      alert('Password changed successfully!')
      this.password = { current: '', new: '', confirm: '' }
    },

    updatePreferences() {
      // In a real app, you'd make an API call to update preferences
      alert('Preferences updated successfully!')
    }
  }
}
</script>

<style scoped>
.account-content {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 30px;
}

.account-profile {
  text-align: center;
  padding: 30px 20px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  margin: 0 auto 15px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid var(--primary-color);
}

.profile-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.account-profile h2 {
  margin-bottom: 5px;
  color: var(--primary-color);
}

.user-role {
  color: var(--text-light);
  font-size: var(--font-size-sm);
  text-transform: capitalize;
}

.account-menu a {
  display: block;
  padding: 15px 20px;
  color: var(--text-color);
  text-decoration: none;
  cursor: pointer;
  transition: var(--transition);
  border-bottom: 1px solid var(--border-color);
}

.account-menu a:last-child {
  border-bottom: none;
}

.account-menu a:hover,
.account-menu a.active {
  background-color: var(--primary-color);
  color: white;
}

.tab-content h2 {
  margin-bottom: 20px;
  color: var(--primary-color);
}

.preferences-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 10px;
  margin-top: 10px;
}

.preference-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: var(--transition);
}

.preference-option:hover {
  background-color: var(--border-color);
}

.preference-option input[type="checkbox"] {
  margin: 0;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 20px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
}

.activity-icon {
  font-size: 1.2rem;
}

.activity-details {
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
  background-color: var(--primary-color);
  color: white;
  border-radius: 15px;
  font-size: var(--font-size-sm);
}

@media (max-width: 1024px) {
  .account-content {
    grid-template-columns: 1fr;
  }
  
  .account-sidebar {
    order: 2;
  }
  
  .account-details {
    order: 1;
  }
}

@media (max-width: 768px) {
  .preferences-grid {
    grid-template-columns: 1fr 1fr;
  }
  
  .activity-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .activity-status {
    align-self: flex-start;
  }
}
</style>