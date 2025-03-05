import { apiRequest } from './apiService';

export async function getTransactions(auth) {
  return apiRequest('/api/transactions', { auth });
}

export async function createTransaction(auth, transaction) {
  return apiRequest('/api/transactions', {
    auth,
    method: 'POST',
    body: JSON.stringify(transaction)
  });
}
