<template>
  <div class="discover">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">Discover Books</h1>
        <div class="search-bar">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Search for books, authors..." 
            class="form-control"
            @keyup.enter="searchBooks"
          />
          <button class="btn btn-primary" @click="searchBooks">
            <font-awesome-icon icon="magnifying-glass" />
            Search
          </button>
        </div>
      </div>

      <div v-if="searchResults.length > 0" class="search-results">
        <h2>Search Results ({{ searchResults.length }})</h2>
        <div class="book-grid">
          <BookCard 
            v-for="book in searchResults" 
            :key="book.bookId || book.key" 
            :book="book"
            @add-to-library="addToLibrary"
          />
        </div>
      </div>

      <div v-else class="discover-sections">
        <section class="featured-books">
          <h2>Featured Books</h2>
          <div class="book-carousel">
            <BookCard 
              v-for="book in featuredBooks" 
              :key="book.bookId" 
              :book="book"
              @add-to-library="addToLibrary"
            />
          </div>
        </section>

        <section class="popular-genres">
          <h2>Popular Genres</h2>
          <div class="genre-grid">
            <div 
              v-for="genre in genres" 
              :key="genre.name"
              :class="['genre-card', genre.class]"
              @click="searchGenre(genre.name)"
            >
              <h3>{{ genre.name }}</h3>
              <p>{{ genre.description }}</p>
            </div>
          </div>
        </section>
      </div>

      <div v-if="loading" class="loading-state">
        <LoadingSpinner message="Searching books..." />
      </div>
    </div>
  </div>
</template>

<script>
import BookCard from '../components/BookCard.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import openLibraryService from '../services/openLibrary'
import booksService from '../services/books'
import userBooksService from '../services/userBooks'

export default {
  name: 'DiscoverView',
  components: {
    BookCard,
    LoadingSpinner
  },
  data() {
    return {
      searchQuery: '',
      searchResults: [],
      featuredBooks: [],
      loading: false,
      genres: [
        { 
          name: 'Fantasy', 
          class: 'fantasy',
          description: 'Magical worlds and epic adventures'
        },
        { 
          name: 'Science Fiction', 
          class: 'scifi',
          description: 'Future technologies and space exploration'
        },
        { 
          name: 'Mystery', 
          class: 'mystery',
          description: 'Crime solving and suspenseful plots'
        },
        { 
          name: 'Romance', 
          class: 'romance',
          description: 'Love stories and emotional journeys'
        },
        { 
          name: 'Thriller', 
          class: 'thriller',
          description: 'Fast-paced and suspenseful stories'
        },
        { 
          name: 'Non-Fiction', 
          class: 'nonfiction',
          description: 'Real stories and factual information'
        }
      ]
    }
  },
  async mounted() {
    await this.loadFeaturedBooks()
  },
  methods: {
    async searchBooks() {
      if (!this.searchQuery.trim()) return

      this.loading = true
      try {
        const response = await openLibraryService.searchBooks(this.searchQuery)
        this.searchResults = response.data
      } catch (error) {
        console.error('Error searching books:', error)
        alert('Failed to search books. Please try again.')
      } finally {
        this.loading = false
      }
    },

    async loadFeaturedBooks() {
      try {
        const response = await booksService.getBooks()
        this.featuredBooks = response.data.slice(0, 6) // Show first 6 books as featured
      } catch (error) {
        console.error('Error loading featured books:', error)
      }
    },

    searchGenre(genreName) {
      this.searchQuery = genreName
      this.searchBooks()
    },

    async addToLibrary(book) {
      try {
        // First, check if book exists in our database
        let bookId = book.bookId
        
        if (!bookId) {
          // If it's from Open Library, create the book in our system first
          const bookResponse = await booksService.createBook({
            title: book.title,
            author: book.author,
            isbn: book.isbn,
            coverImageUrl: book.coverImageUrl,
            publicationYear: book.publicationYear
          })
          bookId = bookResponse.data.bookId
        }

        // Add to user's library
        await userBooksService.createUserBook({
          bookId: bookId,
          currentStatus: 'unread',
          isOwned: false,
          dateAdded: new Date().toISOString().split('T')[0]
        })

        alert('Book added to your library successfully!')
      } catch (error) {
        console.error('Error adding book to library:', error)
        alert('Failed to add book to library. Please try again.')
      }
    }
  }
}
</script>

<style scoped>
.search-results {
  margin-top: 30px;
}

.book-carousel {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding: 20px 0;
  margin: 0 -20px;
  padding-left: 20px;
}

.book-carousel .book-card {
  flex: 0 0 200px;
}

.genre-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.genre-card {
  padding: 30px 20px;
  border-radius: var(--border-radius);
  color: white;
  cursor: pointer;
  transition: var(--transition);
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.genre-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.genre-card h3 {
  margin-bottom: 10px;
  font-size: 1.3rem;
  color: white;
}

.genre-card p {
  opacity: 0.9;
  margin: 0;
}

.genre-card.fantasy {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
}

.genre-card.scifi {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.genre-card.mystery {
  background: linear-gradient(135deg, #8e2de2 0%, #4a00e0 100%);
}

.genre-card.romance {
  background: linear-gradient(135deg, #f80759 0%, #bc4e9c 100%);
}

.genre-card.thriller {
  background: linear-gradient(135deg, #ff6b6b 0%, #ffa726 100%);
}

.genre-card.nonfiction {
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
}

.featured-books,
.popular-genres {
  margin-bottom: 50px;
}

.featured-books h2,
.popular-genres h2 {
  margin-bottom: 20px;
  color: var(--primary-color);
}

@media (max-width: 768px) {
  .book-carousel .book-card {
    flex: 0 0 150px;
  }
  
  .genre-grid {
    grid-template-columns: 1fr;
  }
  
  .search-bar {
    flex-direction: column;
  }
}
</style>