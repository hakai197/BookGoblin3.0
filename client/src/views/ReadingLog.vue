<template>
  <div class="reading-log">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">Reading Log</h1>
        <button class="btn btn-primary" @click="showAddLogModal = true">
          Add Reading Log
        </button>
      </div>

      <div class="log-filters">
        <div class="filter-group">
          <label>Filter by Book:</label>
          <select v-model="selectedBook" class="form-control">
            <option value="">All Books</option>
            <option v-for="book in userBooks" :key="book.userBookId" :value="book.userBookId">
              {{ book.bookTitle }}
            </option>
          </select>
        </div>
        <div class="filter-group">
          <label>Date Range:</label>
          <input type="date" v-model="startDate" class="form-control" />
          <span>to</span>
          <input type="date" v-model="endDate" class="form-control" />
        </div>
      </div>

      <div v-if="loading" class="loading-state">
        <LoadingSpinner message="Loading reading logs..." />
      </div>

      <div v-else>
        <div v-if="filteredLogs.length > 0" class="log-entries">
          <div v-for="log in filteredLogs" :key="log.logId" class="log-entry card">
            <div class="log-book">
              <img 
                :src="getBookCover(log)" 
                :alt="log.bookTitle"
                class="log-book-cover"
              />
            </div>
            <div class="log-details">
              <h3>{{ log.bookTitle }}</h3>
              <p class="book-author">{{ log.bookAuthor }}</p>
              <div class="log-dates">
                <span>Started: {{ formatDate(log.startDate) }}</span>
                <span v-if="log.endDate">Finished: {{ formatDate(log.endDate) }}</span>
              </div>
              <div v-if="log.rating" class="log-rating">
                Rating: {{ '★'.repeat(log.rating) }}{{ '☆'.repeat(5 - log.rating) }}
              </div>
              <p v-if="log.notes" class="log-notes">{{ log.notes }}</p>
            </div>
            <div class="log-actions">
              <button class="btn btn-outline btn-sm" @click="editLog(log)">
                Edit
              </button>
              <button class="btn btn-danger btn-sm" @click="deleteLog(log.logId)">
                Delete
              </button>
            </div>
          </div>
        </div>
        
        <div v-else class="empty-state">
          <p>No reading logs found.</p>
          <button class="btn btn-primary" @click="showAddLogModal = true">
            Add Your First Reading Log
          </button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Log Modal -->
    <div v-if="showAddLogModal" class="modal-overlay" @click="showAddLogModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ editingLog ? 'Edit' : 'Add' }} Reading Log</h3>
          <button class="btn-close" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveLog">
            <div class="form-group">
              <label>Book</label>
              <select v-model="currentLog.userBookId" class="form-control" required>
                <option v-for="book in userBooks" :key="book.userBookId" :value="book.userBookId">
                  {{ book.bookTitle }} by {{ book.bookAuthor }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>Start Date</label>
              <input type="date" v-model="currentLog.startDate" class="form-control" required />
            </div>
            <div class="form-group">
              <label>End Date</label>
              <input type="date" v-model="currentLog.endDate" class="form-control" />
            </div>
            <div class="form-group">
              <label>Rating (1-5)</label>
              <select v-model="currentLog.rating" class="form-control">
                <option value="">No Rating</option>
                <option v-for="n in 5" :key="n" :value="n">{{ n }} ★</option>
              </select>
            </div>
            <div class="form-group">
              <label>Notes</label>
              <textarea v-model="currentLog.notes" class="form-control" rows="4"></textarea>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-outline" @click="closeModal">
                Cancel
              </button>
              <button type="submit" class="btn btn-primary">
                {{ editingLog ? 'Update' : 'Add' }} Log
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LoadingSpinner from '../components/LoadingSpinner.vue'
import readingLogsService from '../services/readingLogs'
import userBooksService from '../services/userBooks'

export default {
  name: 'ReadingLog',
  components: {
    LoadingSpinner
  },
  data() {
    return {
      readingLogs: [],
      userBooks: [],
      loading: true,
      selectedBook: '',
      startDate: '',
      endDate: '',
      showAddLogModal: false,
      editingLog: null,
      currentLog: {
        userBookId: '',
        startDate: new Date().toISOString().split('T')[0],
        endDate: '',
        rating: null,
        notes: ''
      }
    }
  },
  async mounted() {
    await Promise.all([this.loadReadingLogs(), this.loadUserBooks()])
  },
  computed: {
    filteredLogs() {
      let filtered = this.readingLogs

      if (this.selectedBook) {
        filtered = filtered.filter(log => log.userBookId == this.selectedBook)
      }

      if (this.startDate) {
        filtered = filtered.filter(log => log.startDate >= this.startDate)
      }

      if (this.endDate) {
        filtered = filtered.filter(log => log.endDate <= this.endDate)
      }

      return filtered.sort((a, b) => new Date(b.startDate) - new Date(a.startDate))
    }
  },
  methods: {
    async loadReadingLogs() {
      try {
        // Since we need to get logs for all user books, we'll load user books first
        // and then get logs for each. In a real app, you might have a different endpoint.
        const userBooksRes = await userBooksService.getUserBooks()
        this.userBooks = userBooksRes.data
        
        // For demo, we'll create some mock logs
        // In a real app, you'd fetch these properly
        this.readingLogs = [
          {
            logId: 1,
            userBookId: 1,
            startDate: '2024-01-15',
            endDate: '2024-01-20',
            rating: 4,
            notes: 'Great book! Really enjoyed the character development.',
            bookTitle: 'Sample Book 1',
            bookAuthor: 'Author One'
          },
          {
            logId: 2,
            userBookId: 2,
            startDate: '2024-01-10',
            endDate: null,
            rating: null,
            notes: 'Currently reading this one.',
            bookTitle: 'Sample Book 2',
            bookAuthor: 'Author Two'
          }
        ]
      } catch (error) {
        console.error('Error loading reading logs:', error)
        alert('Failed to load reading logs')
      } finally {
        this.loading = false
      }
    },

    async loadUserBooks() {
      try {
        const response = await userBooksService.getUserBooks()
        this.userBooks = response.data
      } catch (error) {
        console.error('Error loading user books:', error)
      }
    },

    getBookCover(log) {
      return '/placeholder-book-cover.png'
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString()
    },

    editLog(log) {
      this.editingLog = log
      this.currentLog = { ...log }
      this.showAddLogModal = true
    },

    async deleteLog(logId) {
      if (confirm('Are you sure you want to delete this reading log?')) {
        try {
          await readingLogsService.deleteReadingLog(logId)
          this.readingLogs = this.readingLogs.filter(log => log.logId !== logId)
          alert('Reading log deleted successfully')
        } catch (error) {
          console.error('Error deleting reading log:', error)
          alert('Failed to delete reading log')
        }
      }
    },

    async saveLog() {
      try {
        if (this.editingLog) {
          await readingLogsService.updateReadingLog(this.editingLog.logId, this.currentLog)
          const index = this.readingLogs.findIndex(log => log.logId === this.editingLog.logId)
          this.readingLogs[index] = { ...this.currentLog, ...this.getLogBookInfo(this.currentLog.userBookId) }
        } else {
          const response = await readingLogsService.createReadingLog(this.currentLog)
          this.readingLogs.unshift({ 
            ...response.data, 
            ...this.getLogBookInfo(this.currentLog.userBookId) 
          })
        }
        this.closeModal()
        alert(`Reading log ${this.editingLog ? 'updated' : 'added'} successfully!`)
      } catch (error) {
        console.error('Error saving reading log:', error)
        alert(`Failed to ${this.editingLog ? 'update' : 'add'} reading log`)
      }
    },

    getLogBookInfo(userBookId) {
      const book = this.userBooks.find(b => b.userBookId == userBookId)
      return {
        bookTitle: book?.bookTitle || 'Unknown Book',
        bookAuthor: book?.bookAuthor || 'Unknown Author'
      }
    },

    closeModal() {
      this.showAddLogModal = false
      this.editingLog = null
      this.resetCurrentLog()
    },

    resetCurrentLog() {
      this.currentLog = {
        userBookId: '',
        startDate: new Date().toISOString().split('T')[0],
        endDate: '',
        rating: null,
        notes: ''
      }
    }
  }
}
</script>