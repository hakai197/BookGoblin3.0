import axios from 'axios';

export default {
  getReadingLogsByUserBook(userBookId) {
    return axios.get(`/reading-logs/user-book/${userBookId}`)
  },

  getReadingLogById(id) {
    return axios.get(`/reading-logs/${id}`)
  },

  createReadingLog(readingLog) {
    return axios.post('/reading-logs', readingLog)
  },

  updateReadingLog(id, readingLog) {
    return axios.put(`/reading-logs/${id}`, readingLog)
  },

  deleteReadingLog(id) {
    return axios.delete(`/reading-logs/${id}`)
  }
}