import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth APIs
export const authAPI = {
  login: (credentials) => api.post('/api/auth/login', credentials),
  signup: (userData) => api.post('/api/auth/signup', userData),
};

// Slot APIs
export const slotAPI = {
  getAllSlots: () => api.get('/api/slots'),
  getAvailableSlots: () => api.get('/api/slots/available'),
  getSlotById: (id) => api.get(`/api/slots/${id}`),
  createSlot: (slotData) => api.post('/api/slots', slotData),
  updateSlot: (id, slotData) => api.put(`/api/slots/${id}`, slotData),
  deleteSlot: (id) => api.delete(`/api/slots/${id}`),
};

// Booking APIs
export const bookingAPI = {
  createBooking: (bookingData) => api.post('/api/bookings/reserve', bookingData),
  getUserBookings: (userId) => api.get(`/api/bookings/user/${userId}`),
  cancelBooking: (bookingId) => api.post('/api/bookings/cancel', { bookingId }),
  getAllBookings: () => api.get('/api/bookings'),
};

// QR APIs
export const qrAPI = {
  generateQR: (bookingId) => api.post('/api/qr/generate', { bookingId }),
  validateQR: (code) => api.post('/api/qr/validate', { code }),
};

// Admin APIs
export const adminAPI = {
  getStatistics: () => api.get('/api/admin/stats'),
};

export default api;