import { create } from 'zustand';

const useAuthStore = create((set) => ({
  user: JSON.parse(localStorage.getItem('user')) || null,
  token: localStorage.getItem('token') || null,
  
  login: (userData, token) => {
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('token', token);
    set({ user: userData, token });
  },
  
  logout: () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    set({ user: null, token: null });
  },
  
  isAuthenticated: () => {
    const token = localStorage.getItem('token');
    return !!token;
  },
  
  isAdmin: () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return user?.role === 'ADMIN';
  },
}));

export default useAuthStore;