import axios from 'axios';

export default {
  getUserBooks() {
    return axios.get('/user-books')
  },

  getUserBookById(id) {
    return axios.get(`/user-books/${id}`)
  },

  createUserBook(userBook) {
    return axios.post('/user-books', userBook)
  },

  updateUserBook(id, userBook) {
    return axios.put(`/user-books/${id}`, userBook)
  },

  deleteUserBook(id) {
    return axios.delete(`/user-books/${id}`)
  }
}