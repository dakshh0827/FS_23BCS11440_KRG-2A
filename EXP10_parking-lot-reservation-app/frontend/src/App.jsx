import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer'; // Import Footer
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Dashboard from './components/user/Dashboard';
import BookSlot from './components/user/BookSlot';
import MyBookings from './components/user/MyBookings';
import QRScanner from './components/user/QRScanner';
import AdminDashboard from './components/admin/AdminDashboard';
import SlotManagement from './components/admin/SlotManagement';
import Analytics from './components/admin/Analytics';
import ProtectedRoute from './components/common/ProtectedRoute';
import useAuthStore from './store/authStore';

function App() {
  const { user } = useAuthStore();

  return (
    <Router>
      {/* Use flex-col and min-h-screen to make footer sticky */}
      <div className="flex flex-col min-h-screen bg-gray-50">
        <Navbar />
        <main className="flex-grow"> {/* Main content grows to fill space */}
          <Routes>
            <Route path="/login" element={user ? <Navigate to="/dashboard" /> : <Login />} />
            <Route path="/register" element={user ? <Navigate to="/dashboard" /> : <Register />} />
            
            {/* User Routes */}
            <Route path="/dashboard" element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } />
            
            <Route path="/book-slot" element={
              <ProtectedRoute>
                <BookSlot />
              </ProtectedRoute>
            } />
            
            <Route path="/my-bookings" element={
              <ProtectedRoute>
                <MyBookings />
              </ProtectedRoute>
            } />
            
            <Route path="/qr-scanner" element={
              <ProtectedRoute>
                <QRScanner />
              </ProtectedRoute>
            } />
            
            {/* Admin Routes */}
            <Route path="/admin" element={
              <ProtectedRoute adminOnly={true}>
                <AdminDashboard />
              </ProtectedRoute>
            } />
            
            <Route path="/admin/slots" element={
              <ProtectedRoute adminOnly={true}>
                <SlotManagement />
              </ProtectedRoute>
            } />
            
            <Route path="/admin/analytics" element={
              <ProtectedRoute adminOnly={true}>
                <Analytics />
              </ProtectedRoute>
            } />
            
            {/* Redirect root to dashboard if logged in, else to login */}
            <Route path="/" element={<Navigate to={user ? "/dashboard" : "/login"} />} />
          </Routes>
        </main>
        <Footer /> {/* Add Footer */}
      </div>
    </Router>
  );
}

export default App;