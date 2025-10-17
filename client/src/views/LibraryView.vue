<template>
  <div class="library">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">My Library</h1>
        <div class="library-actions">
          <div class="search-bar">
            <input 
              type="text" 
              v-model="searchQuery" 
              placeholder="Search your library..." 
              class="form-control"
            />
          </div>
          <button class="btn btn-primary" @click="showAddBookModal = true">
            Add Book
          </button>
        </div>
      </div>

      <div class="shelf-tabs">
        <button 
          v-for="shelf in shelves" 
          :key="shelf.key"
          :class="['shelf-tab', { active: activeShelf === shelf.key }]"
          @click="activeShelf = shelf.key"
        >
          {{ shelf.label }} ({{ getShelfCount(shelf.key) }})
        </button>
      </div>

      <div v-if="loading" class="loading-state">
        <LoadingSpinner message="Loading your library..." />
      </div>

      <div v-else>
        <div v-if="filteredBooks.length > 0" class="book-grid">
          <BookCard 
            v-for="book in filteredBooks" 
            :key="book.userBookId" 
            :book="book"
            @click="viewBookDetails(book)"
          />
        </div>
        
        <div v-else class="empty-state">
          <p>No books found in your library.</p>
          <button class="btn btn-primary" @click="showAddBookModal = true">
            Add Your First Book
          </button>
        </div>
      </div>
    </div>

    <!-- Add Book Modal -->
    <div v-if="showAddBookModal" class="modal-overlay" @click="showAddBookModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Add Book to Library</h3>
          <button class="btn-close" @click="showAddBookModal = false">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="addBookToLibrary">
            <div class="form-group">
              <label>Book Title</label>
              <input v-model="newBook.title" class="form-control" required />
            </div>
            <div class="form-group">
              <label>Author</label>
              <input v-model="newBook.author" class="form-control" required />
            </div>
            <div class="form-group">
              <label>Status</label>
              <select v-model="newBook.currentStatus" class="form-control" required>
                <option value="unread">Unread</option>
                <option value="reading">Currently Reading</option>
                <option value="finished">Finished</option>
                <option value="dnf">Did Not Finish</option>
              </select>
            </div>
            <div class="form-group">
              <label>
                <input type="checkbox" v-model="newBook.isOwned" />
                I own this book
              </label>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-outline" @click="showAddBookModal = false">
                Cancel
              </button>
              <button type="submit" class="btn btn-primary">
                Add to Library
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BookCard from '../components/BookCard.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import userBooksService from '../services/userBooks'
import booksService from '../services/books'

export default {
  name: 'LibraryView',
  components: {
    BookCard,
    LoadingSpinner
  },
  data() {
    return {
      books: [],
      loading: true,
      activeShelf: 'all',
      searchQuery: '',
      showAddBookModal: false,
      newBook: {
        title: '',
        author: '',
        currentStatus: 'unread',
        isOwned: false,
        dateAdded: new Date().toISOString().split('T')[0]
      },
      shelves: [
        { key: 'all', label: 'All Books' },
        { key: 'unread', label: 'Unread' },
        { key: 'reading', label: 'Currently Reading' },
        { key: 'finished', label: 'Finished' },
        { key: 'dnf', label: 'Did Not Finish' }
      ]
    }
  },
  async mounted() {
    await this.loadUserBooks()
  },
  computed: {
    filteredBooks() {
      let filtered = this.books

      // Filter by shelf
      if (this.activeShelf !== 'all') {
        filtered = filtered.filter(book => book.currentStatus === this.activeShelf)
      }

      // Filter by search query
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(book => 
          book.bookTitle.toLowerCase().includes(query) ||
          book.bookAuthor.toLowerCase().includes(query)
        )
      }

      return filtered
    }
  },
  methods: {
    async loadUserBooks() {
      try {
        const response = await userBooksService.getUserBooks()
        this.books = response.data
      } catch (error) {
        console.error('Error loading user books:', error)
        alert('Failed to load your library')
      } finally {
        this.loading = false
      }
    },

    getShelfCount(shelfKey) {
      if (shelfKey === 'all') return this.books.length
      return this.books.filter(book => book.currentStatus === shelfKey).length
    },

    viewBookDetails(book) {
      // Navigate to book details or show modal
      console.log('View book details:', book)
    },

    async addBookToLibrary() {
      try {
        // First create the book in the system
        const bookResponse = await booksService.createBook({
          title: this.newBook.title,
          author: this.newBook.author
        })

        // Then add it to user's library
        await userBooksService.createUserBook({
          bookId: bookResponse.data.bookId,
          currentStatus: this.newBook.currentStatus,
          isOwned: this.newBook.isOwned,
          dateAdded: this.newBook.dateAdded
        })

        this.showAddBookModal = false
        this.resetNewBook()
        await this.loadUserBooks()
        alert('Book added to your library successfully!')
      } catch (error) {
        console.error('Error adding book:', error)
        alert('Failed to add book to library')
      }
    },

    resetNewBook() {
      this.newBook = {
        title: '',
        author: '',
        currentStatus: 'unread',
        isOwned: false,
        dateAdded: new Date().toISOString().split('T')[0]
      }
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: var(--card-bg);
  border-radius: var(--border-radius);
  padding: 0;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  color: var(--primary-color);
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-light);
}

.btn-close:hover {
  color: var(--text-color);
}

.modal-body {
  padding: 20px;
}

@media (max-width: 768px) {
  .modal-content {
    width: 95%;
    margin: 20px;
  }
}
</style>