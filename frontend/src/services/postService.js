import { apiRequest } from './apiService';

export function getTrendingPosts(auth) {
  return apiRequest('/api/posts/trending', { auth });
};

export function getNewestPosts(auth) {
  return apiRequest('/api/posts/newest', { auth });
};
