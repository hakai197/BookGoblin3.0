<template>
  <main class="auth-view">
    <div class="auth-card">
      <div class="card-header">Create Account</div>
      <div class="card-content">
        <form v-on:submit.prevent="register" class="auth-form">
          <div class="form-group">
            <label for="username">Username</label>
            <input
              type="text"
              id="username"
              placeholder="Username"
              v-model="user.username"
              required
              autofocus
            />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              placeholder="Password"
              v-model="user.password"
              required
            />
          </div>
          <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              placeholder="Confirm Password"
              v-model="user.confirmPassword"
              required
            />
          </div>
          <div class="form-group">
            <label for="role">Role</label>
            <select id="role" v-model="user.role" required>
              <option value="ROLE_USER">User</option>
              <option value="ROLE_ADMIN">Admin</option>
            </select>
          </div>
          <button type="submit" class="submit-btn">Register</button>
        </form>
        <div class="auth-footer">
          <p>Already have an account? <router-link v-bind:to="{ name: 'login' }">Sign In!</router-link></p>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "ROLE_USER",
      },
    };
  },
  methods: {
    register() {
      if (this.user.password !== this.user.confirmPassword) {
        alert("Passwords do not match!");
        return;
      }
      authService
        .register(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.$router.push("/login");
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 400) {
            alert("Validation error: " + response.data.message);
          } else {
            alert(response.message);
          }
        });
    },
  },
};
</script>