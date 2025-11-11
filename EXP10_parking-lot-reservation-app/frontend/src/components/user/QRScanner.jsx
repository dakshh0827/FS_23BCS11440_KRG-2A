import React, { useState } from 'react';
import QrScanner from 'react-qr-scanner';
import { qrAPI } from '../../services/api';
import Loader from '../common/Loader';

const QRScanner = () => {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleScan = async (data) => {
    if (data && !loading) {
      setLoading(true);
      setResult(null);
      setError('');
      
      try {
        const res = await qrAPI.validateQR(data.text);
        setResult(res.data);
      } catch (err) {
        setError(err.response?.data?.message || 'Invalid QR Code or scan error.');
        setResult({ valid: false, message: 'Invalid QR Code or scan error.' });
      } finally {
        setLoading(false);
        // Stop scanning after first attempt by setting a delay or state
        // For this example, we'll allow re-scanning
      }
    }
  };

  const handleError = (err) => {
    console.error(err);
    setError('Failed to start QR scanner. Please check camera permissions.');
  };

  const previewStyle = {
    height: 300,
    width: '100%',
    maxWidth: 400,
    margin: '0 auto',
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-lg">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 text-center">Validate QR Code</h1>
      
      <div className="bg-white p-6 rounded-lg shadow-lg border">
        <QrScanner
          delay={300}
          style={previewStyle}
          onError={handleError}
          onScan={handleScan}
          constraints={{ video: { facingMode: 'environment' } }}
        />
        <p className="text-center text-gray-500 mt-4">Point your camera at a QR code</p>
      </div>

      {loading && <Loader />}
      {error && <div className="mt-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">{error}</div>}
      
      {result && (
        <div className={`mt-6 p-6 rounded-lg shadow-lg border-2 ${
          result.valid ? 'bg-green-100 border-green-500' : 'bg-red-100 border-red-500'
        }`}>
          <h2 className="text-2xl font-bold text-center mb-4">Validation Result</h2>
          {result.valid ? (
            <div className="text-green-800">
              <p className="text-lg text-center font-semibold">Success: {result.message}</p>
              <p className="text-center">Booking ID: {result.bookingId}</p>
            </div>
          ) : (
            <p className="text-lg text-center font-semibold text-red-800">Error: {result.message}</p>
          )}
          <button
            onClick={() => { setResult(null); setError(''); }}
            className="w-full mt-6 bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition duration-200"
          >
            Scan Another
          </button>
        </div>
      )}
    </div>
  );
};

export default QRScanner;