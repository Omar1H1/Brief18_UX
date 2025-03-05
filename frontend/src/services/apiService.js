export const HOST = 'http://localhost:8080';

export function createHeaders(auth) {
  const headers = {
    'Content-Type': 'application/json'
  };

  if (auth?.accessToken) {
    headers['Authorization'] = `${auth.tokenType || 'Bearer'} ${auth.accessToken}`;
  }

  return headers;
};

export async function apiRequest(endpoint, options = {}) {
  const response = await fetch(`${HOST}${endpoint}`, {
    ...options,
    headers: {
      ...createHeaders(options.auth),
      ...options.headers
    }
  });

  if (!response.ok) {
    throw new Error(`API request failed: ${response.statusText}`);
  }

  return response.json();
};
