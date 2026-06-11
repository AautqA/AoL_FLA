import axios from 'axios';

// Inference dari PDF: API endpoints akan berisi endpoints untuk auth, reservations, dan tables
// Backend berjalan di http://localhost:5001 saat testing lokal
// Untuk Vite, gunakan import.meta.env
const API_BASE_URL = import.meta.env.VITE_APP_API_URL || 'http://localhost:5001/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  try {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  } catch (e) {
    console.warn('localStorage not available');
  }
  return config;
});

export default api;
