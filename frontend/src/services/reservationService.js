import api from './api';
import { mockReservationService } from './mockData';

// Use mock data only when the backend is unavailable.
const USE_MOCK = false;

// Endpoints inferred from PDF - Reservation Management FR4-FR7, FR11
export const reservationService = {
  // Reserve a table (FR5)
  createReservation: async (reservationData) => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.createReservation(
          reservationData.userId,
          reservationData.tableId,
          reservationData.date,
          reservationData.time,
          reservationData.peopleCount
        );
      }
      const response = await api.post('/reservations', {
        table_id: reservationData.tableId,
        user_id: reservationData.userId,
        booking_date: reservationData.date,
        booking_time: reservationData.time,
        people_count: reservationData.peopleCount,
        status: 'confirmed',
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Cancel reservation (FR6)
  cancelReservation: async (reservationId) => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.cancelReservation(reservationId);
      }
      const response = await api.put(`/reservations/${reservationId}/cancel`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // View reservation history (FR7)
  getReservationHistory: async (userId) => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.getReservationHistory(userId);
      }
      const response = await api.get(`/reservations/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get all reservations (FR11) - Admin only
  getAllReservations: async () => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.getAllReservations();
      }
      const response = await api.get('/reservations');
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get specific reservation
  getReservationById: async (reservationId) => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.getReservationById(reservationId);
      }
      const response = await api.get(`/reservations/${reservationId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Check table availability (FR4)
  checkAvailability: async (tableId, date, time) => {
    try {
      if (USE_MOCK) {
        return await mockReservationService.checkAvailability(tableId, date, time);
      }
      const response = await api.get(`/tables/${tableId}/availability`, {
        params: {
          date,
          time,
        },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};
