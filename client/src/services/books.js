import axios from 'axios';

export default {
  getBooks() {
    return axios.get('/books')
  },

  getBookById(id) {
    return axios.get(`/books/${id}`)
  },

  searchBooks(title, author) {
    const params = {}
    if (title) params.title = title
    if (author) params.author = author
    return axios.get('/books/search', { params })
  },

  createBook(book) {
    return axios.post('/books', book)
  },

  updateBook(id, book) {
    return axios.put(`/books/${id}`, book)
  },

  deleteBook(id) {
    return axios.delete(`/books/${id}`)
  }
}