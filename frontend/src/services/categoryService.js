import { apiRequest } from './apiService';

export function getCategories(auth) {
  return apiRequest('/api/categories', { auth });
}

export function createCategory(auth, category) {
  return apiRequest('/api/categories', {
    auth,
    method: 'POST',
    body: JSON.stringify(category)
  });
}

export function deleteCategory(auth, categoryId) {
  return apiRequest(`/api/categories/${categoryId}`, {
    auth,
    method: 'DELETE'
  });
}
