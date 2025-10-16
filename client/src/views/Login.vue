<template>
  <main class="auth-view">
    <div class="auth-card">
      <div class="card-header">Sign In</div>
      <div class="card-content">
        <form v-on:submit.prevent="login" class="auth-form">
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
          <button type="submit" class="submit-btn">Sign in</button>
        </form>
        <div class="auth-footer">
          <p>Need an account? <router-link v-bind:to="{ name: 'register' }">Register!</router-link></p>
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
      },
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 401) {
            alert("Invalid username and password!");
          } else {
            alert(response.message);
          }
        });
    },
  },
};
</script>