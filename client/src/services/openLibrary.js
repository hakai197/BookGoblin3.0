import axios from 'axios';

export default {
  searchBooks(query) {
    return axios.get(`/api/openlibrary/search?query=${encodeURIComponent(query)}`)
  }
}