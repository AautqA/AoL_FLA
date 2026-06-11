import api from './api';
import { mockAuthService } from './mockData';

// Use mock data only when the backend is unavailable.
const USE_MOCK = false;

// Endpoints inferred from PDF - Authentication FR1 & FR2
export const authService = {
  // Register account (FR1)
  register: async (name, email, password) => {
    try {
      if (USE_MOCK) {
        return await mockAuthService.register(name, email, password);
      }
      const response = await api.post('/auth/register', {
        name,
        email,
        password,
        role: 'customer',
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Login (FR2)
  login: async (email, password) => {
    try {
      if (USE_MOCK) {
        return await mockAuthService.login(email, password);
      }
      const response = await api.post('/auth/login', {
        email,
        password,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Logout
  logout: () => {
    try {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    } catch (e) {
      console.warn('localStorage not available');
    }
  },
};
