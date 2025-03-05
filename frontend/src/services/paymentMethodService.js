import { apiRequest } from './apiService';

export async function getPaymentMethods(auth) {
  return apiRequest('/api/payment-methods', { auth });
}

export async function createPaymentMethod(auth, paymentMethod) {
  return apiRequest('/api/payment-methods', {
    auth,
    method: 'POST',
    body: JSON.stringify(paymentMethod)
  });
}

export async function deletePaymentMethod(auth, methodId) {
  return apiRequest(`/api/payment-methods/${methodId}`, {
    auth,
    method: 'DELETE'
  });
}
