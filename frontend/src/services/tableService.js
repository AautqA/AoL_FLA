import api from './api';
import { mockTableService } from './mockData';

// Use mock data only when the backend is unavailable.
const USE_MOCK = false;

// Endpoints inferred from PDF - Table Management FR3, FR8-FR10
export const tableService = {
  // Get all available tables (FR3)
  getAvailableTables: async (date, time) => {
    try {
      if (USE_MOCK) {
        return await mockTableService.getAvailableTables(date, time);
      }
      const response = await api.get('/tables', {
        params: {
          date,
          time,
          status: 'Available',
        },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get all tables (for admin)
  getAllTables: async () => {
    try {
      if (USE_MOCK) {
        return await mockTableService.getAllTables();
      }
      const response = await api.get('/tables/all');
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Add table (FR8) - Admin only
  addTable: async (tableData) => {
    try {
      if (USE_MOCK) {
        return await mockTableService.addTable(tableData.restaurant_name, tableData.table_number, tableData.capacity);
      }
      const response = await api.post('/tables', tableData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Update table (FR9) - Admin only
  updateTable: async (tableId, tableData) => {
    try {
      if (USE_MOCK) {
        return await mockTableService.updateTable(tableId, tableData.restaurant_name, tableData.table_number, tableData.capacity, tableData.status);
      }
      const response = await api.put(`/tables/${tableId}`, tableData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Delete table (FR10) - Admin only
  deleteTable: async (tableId) => {
    try {
      if (USE_MOCK) {
        return await mockTableService.deleteTable(tableId);
      }
      const response = await api.delete(`/tables/${tableId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get table by ID
  getTableById: async (tableId) => {
    try {
      if (USE_MOCK) {
        return await mockTableService.getTableById(tableId);
      }
      const response = await api.get(`/tables/${tableId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};
