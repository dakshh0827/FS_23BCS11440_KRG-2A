import React, { useState, useEffect } from 'react';
import { slotAPI, bookingAPI } from '../../services/api';
import useAuthStore from '../../store/authStore';
import Loader from '../common/Loader';

const BookSlot = () => {
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [filterType, setFilterType] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');
  const { user } = useAuthStore();

  useEffect(() => {
    fetchAvailableSlots();
  }, []);

  const fetchAvailableSlots = async () => {
    setLoading(true);
    try {
      const res = await slotAPI.getAvailableSlots();
      setSlots(res.data);
    } catch (err) {
      setError('Failed to fetch slots.');
    } finally {
      setLoading(false);
    }
  };

  const getMinDateTime = () => {
    return new Date().toISOString().slice(0, 16);
  };

  const calculateDuration = () => {
    if (!startTime || !endTime) return null;
    const start = new Date(startTime);
    const end = new Date(endTime);
    const hours = Math.abs(end - start) / 36e5;
    return hours.toFixed(1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!selectedSlot) {
      setError('Please select a slot.');
      return;
    }
    if (!startTime || !endTime) {
      setError('Please select a start and end time.');
      return;
    }

    const bookingData = {
      userId: user.userId,
      slotId: selectedSlot.slotId,
      startTime: new Date(startTime).toISOString(),
      endTime: new Date(endTime).toISOString(),
    };

    setLoading(true);
    try {
      const res = await bookingAPI.createBooking(bookingData);
      if (res.data.success) {
        setSuccess('Booking successful! View in "My Bookings".');
        setSelectedSlot(null);
        setStartTime('');
        setEndTime('');
        fetchAvailableSlots();
      } else {
        setError(res.data.message || 'Booking failed.');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred during booking.');
    } finally {
      setLoading(false);
    }
  };

  const filteredSlots = slots.filter(slot => {
    const matchesType = filterType === 'ALL' || slot.type === filterType;
    const matchesSearch = slot.slotNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          slot.location.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesType && matchesSearch;
  });

  const slotTypes = ['ALL', 'REGULAR', 'VIP', 'HANDICAPPED'];
  const duration = calculateDuration();

  return (
    <div className="min-h-screen bg-slate-50 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-slate-900 mb-2">Book a Parking Slot</h1>
          <p className="text-slate-600">Select an available slot and choose your time</p>
        </div>

        {/* Alerts */}
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl flex items-start gap-3">
            <svg className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p className="text-sm text-red-800">{error}</p>
          </div>
        )}
        
        {success && (
          <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-xl flex items-start gap-3">
            <svg className="w-5 h-5 text-green-600 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p className="text-sm text-green-800">{success}</p>
          </div>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Slots Section */}
          <div className="lg:col-span-2">
            {/* Filters */}
            <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 mb-6">
              <div className="flex flex-col sm:flex-row gap-4">
                <div className="flex-1">
                  <div className="relative">
                    <svg className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                    <input
                      type="text"
                      placeholder="Search by slot number or location..."
                      className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>
                </div>
                <div className="flex gap-2">
                  {slotTypes.map(type => (
                    <button
                      key={type}
                      onClick={() => setFilterType(type)}
                      className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                        filterType === type
                          ? 'bg-indigo-600 text-white'
                          : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                      }`}
                    >
                      {type}
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Slots Grid */}
            <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-lg font-semibold text-slate-900">Available Slots</h2>
                <span className="text-sm text-slate-600">{filteredSlots.length} slots available</span>
              </div>
              
              {loading ? (
                <Loader />
              ) : filteredSlots.length === 0 ? (
                <div className="text-center py-12">
                  <svg className="w-16 h-16 text-slate-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                  </svg>
                  <p className="text-slate-600 font-medium">No slots available</p>
                  <p className="text-slate-500 text-sm mt-1">Try adjusting your filters</p>
                </div>
              ) : (
                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                  {filteredSlots.map((slot) => (
                    <button
                      key={slot.slotId}
                      onClick={() => setSelectedSlot(slot)}
                      className={`p-4 border-2 rounded-xl text-left transition-all ${
                        selectedSlot?.slotId === slot.slotId
                          ? 'bg-indigo-50 border-indigo-500 shadow-md scale-105'
                          : 'bg-white border-slate-200 hover:border-indigo-300 hover:shadow-sm'
                      }`}
                    >
                      <div className="flex items-start justify-between mb-2">
                        <p className="font-bold text-lg text-slate-900">{slot.slotNumber}</p>
                        {slot.type === 'VIP' && (
                          <span className="bg-amber-100 text-amber-700 text-xs px-2 py-0.5 rounded-full font-medium">
                            VIP
                          </span>
                        )}
                        {slot.type === 'HANDICAPPED' && (
                          <span className="bg-blue-100 text-blue-700 text-xs px-2 py-0.5 rounded-full font-medium">
                            ♿
                          </span>
                        )}
                      </div>
                      <p className="text-sm text-slate-600 mb-1">{slot.location}</p>
                      <p className="text-xs text-slate-500 capitalize">{slot.type.toLowerCase()}</p>
                    </button>
                  ))}
                </div>
              )}
            </div>
          </div>

          {/* Booking Form */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 sticky top-8">
              <h2 className="text-lg font-semibold text-slate-900 mb-6">Reservation Details</h2>
              
              {selectedSlot ? (
                <div className="mb-6 p-4 bg-indigo-50 border border-indigo-200 rounded-xl">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="w-12 h-12 bg-indigo-600 rounded-xl flex items-center justify-center text-white font-bold text-lg">
                      {/* --- THIS IS THE FIX --- */}
                      {selectedSlot.slotNumber}
                    </div>
                    <div>
                      <p className="font-semibold text-slate-900">{selectedSlot.slotNumber}</p>
                      <p className="text-sm text-slate-600">{selectedSlot.location}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2 text-sm">
                    <span className="px-3 py-1 bg-white rounded-lg text-slate-700 font-medium">
                      {selectedSlot.type}
                    </span>
                  </div>
                </div>
              ) : (
                <div className="mb-6 p-8 bg-slate-50 border-2 border-dashed border-slate-300 rounded-xl text-center">
                  <svg className="w-12 h-12 text-slate-400 mx-auto mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                  <p className="text-sm text-slate-600">Select a slot to continue</p>
                </div>
              )}

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-2">
                    Start Time
                  </label>
                  <input
                    type="datetime-local"
                    className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-slate-50 disabled:text-slate-500"
                    value={startTime}
                    min={getMinDateTime()}
                    onChange={(e) => setStartTime(e.target.value)}
                    disabled={!selectedSlot}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-2">
                    End Time
                  </label>
                  <input
                    type="datetime-local"
                    className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-slate-50 disabled:text-slate-500"
                    value={endTime}
                    min={startTime || getMinDateTime()}
                    onChange={(e) => setEndTime(e.target.value)}
                    disabled={!selectedSlot || !startTime}
                  />
                </div>

                {duration && (
                  <div className="p-4 bg-slate-50 rounded-lg">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-600">Duration</span>
                      <span className="font-semibold text-slate-900">{duration} hours</span>
                    </div>
                  </div>
                )}

                <button
                  onClick={handleSubmit}
                  disabled={loading || !selectedSlot || !startTime || !endTime}
                  className="w-full bg-indigo-600 text-white py-3 rounded-xl font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                >
                  {loading ? (
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                  ) : (
                    <>
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                      </svg>
                      Confirm Booking
                    </>
                  )}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookSlot;