import { apiRequest } from './apiService';

export async function login(username, password) {
  return apiRequest('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password })
  });
};

export async function register(username, password) {
  return apiRequest('/api/auth/register', {
    method: 'POST',
    body: JSON.stringify({ username, password })
  });
};

export async function validateToken(token) {
  return apiRequest('/api/auth/validate', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
};
