import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { slotAPI, bookingAPI } from '../../services/api';
import useAuthStore from '../../store/authStore';
import Loader from '../common/Loader';

const Dashboard = () => {
  const [slots, setSlots] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuthStore();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [slotsRes, bookingsRes] = await Promise.all([
        slotAPI.getAvailableSlots(),
        bookingAPI.getUserBookings(user.userId)
      ]);
      setSlots(slotsRes.data);
      setBookings(bookingsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader />;

  const activeBookings = bookings.filter(b => b.status === 'ACTIVE').length;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-8 text-gray-800">Welcome, {user.name}!</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-blue-500 text-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-2">Available Slots</h3>
          <p className="text-4xl font-bold">{slots.length}</p>
        </div>

        <div className="bg-green-500 text-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-2">Active Bookings</h3>
          <p className="text-4xl font-bold">{activeBookings}</p>
        </div>

        <div className="bg-purple-500 text-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-2">Total Bookings</h3>
          <p className="text-4xl font-bold">{bookings.length}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Link
          to="/book-slot"
          className="bg-white p-8 rounded-lg shadow-lg hover:shadow-xl transition duration-200 border-2 border-blue-500"
        >
          <h3 className="text-2xl font-bold mb-2 text-blue-600">Book a Slot</h3>
          <p className="text-gray-600">Find and reserve your parking spot</p>
        </Link>

        <Link
          to="/my-bookings"
          className="bg-white p-8 rounded-lg shadow-lg hover:shadow-xl transition duration-200 border-2 border-green-500"
        >
          <h3 className="text-2xl font-bold mb-2 text-green-600">My Bookings</h3>
          <p className="text-gray-600">View and manage your reservations</p>
        </Link>
      </div>
    </div>
  );
};

export default Dashboard;