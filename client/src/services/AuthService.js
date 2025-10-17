import axios from 'axios';

// Create axios instance with base URL
const apiClient = axios.create({
  baseURL: 'http://localhost:9000', // Your Spring Boot server
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  login(user) {
    return apiClient.post('/login', user);
  },

  register(user) {
    return apiClient.post('/register', user);
  }
}