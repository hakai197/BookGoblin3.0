import axios from 'axios';

export default {
  getTags() {
    return axios.get('/tags')
  },

  getTagById(id) {
    return axios.get(`/tags/${id}`)
  },

  getTagsByBookId(bookId) {
    return axios.get(`/tags/book/${bookId}`)
  },

  createTag(tag) {
    return axios.post('/tags', tag)
  },

  updateTag(id, tag) {
    return axios.put(`/tags/${id}`, tag)
  },

  deleteTag(id) {
    return axios.delete(`/tags/${id}`)
  },

  addTagToBook(tagId, bookId) {
    return axios.post(`/tags/${tagId}/book/${bookId}`)
  },

  removeTagFromBook(tagId, bookId) {
    return axios.delete(`/tags/${tagId}/book/${bookId}`)
  }
}