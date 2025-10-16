<template>
  <div class="book-card" @click="$emit('click', book)">
    <div class="book-cover">
      <img 
        :src="book.coverImageUrl || '/placeholder-book-cover.png'" 
        :alt="book.title"
        @error="handleImageError"
      />
      <div class="book-overlay">
        <button class="btn btn-primary btn-sm" @click.stop="addToLibrary">
          Add to Library
        </button>
      </div>
    </div>
    
    <div class="book-info">
      <h3 class="book-title">{{ book.title }}</h3>
      <p class="book-author">{{ book.author || 'Unknown Author' }}</p>
      <div class="book-meta">
        <span v-if="book.publicationYear" class="book-year">{{ book.publicationYear }}</span>
        <span v-if="book.isbn" class="book-isbn">ISBN: {{ book.isbn }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BookCard',
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  methods: {
    handleImageError(event) {
      event.target.src = '/placeholder-book-cover.png'
    },
    addToLibrary() {
      this.$emit('add-to-library', this.book)
    }
  }
}
</script>

<style scoped>
.book-card {
  background-color: var(--card-bg);
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  overflow: hidden;
  transition: var(--transition);
  cursor: pointer;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.book-cover {
  position: relative;
  aspect-ratio: 2/3;
  overflow: hidden;
}

.book-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: var(--transition);
}

.book-card:hover .book-cover img {
  transform: scale(1.05);
}

.book-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: var(--transition);
}

.book-card:hover .book-overlay {
  opacity: 1;
}

.book-info {
  padding: 15px;
}

.book-title {
  font-size: var(--font-size-base);
  font-weight: 600;
  margin-bottom: 5px;
  color: var(--text-color);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-author {
  color: var(--text-light);
  font-size: var(--font-size-sm);
  margin-bottom: 8px;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: var(--font-size-sm);
  color: var(--text-light);
}

.book-year {
  background-color: var(--primary-color);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.75rem;
}

@media (max-width: 768px) {
  .book-info {
    padding: 12px;
  }
  
  .book-title {
    font-size: 0.9rem;
  }
  
  .book-author {
    font-size: 0.8rem;
  }
}
</style>