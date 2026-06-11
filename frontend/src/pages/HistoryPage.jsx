import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { reservationService } from '../services/reservationService';
import ReservationCard from '../components/ReservationCard';
import Alert from '../components/Alert';
import Loading from '../components/Loading';
import '../styles/HistoryPage.css';

const HistoryPage = () => {
  const { user } = useAuth();
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('all'); // all, confirmed, cancelled, completed

  useEffect(() => {
    fetchReservationHistory();
  }, []);

  const fetchReservationHistory = async () => {
    try {
      setLoading(true);
      const response = await reservationService.getReservationHistory(user?.user_id);
      setReservations(response.data || response);
      setError('');
    } catch (err) {
      setError('Failed to load reservation history');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelReservation = async (reservationId) => {
    if (!window.confirm('Are you sure you want to cancel this reservation?')) {
      return;
    }

    try {
      await reservationService.cancelReservation(reservationId);
      fetchReservationHistory();
      Alert('Reservation cancelled successfully', 'success');
    } catch (err) {
      setError('Failed to cancel reservation');
      console.error(err);
    }
  };

  const filteredReservations = filter === 'all' 
    ? reservations 
    : reservations.filter(r => r.status === filter);

  return (
    <div className="history-page">
      <div className="history-header">
        <h1>Your Reservations</h1>
      </div>

      {error && <Alert message={error} type="error" />}

      <div className="filter-section">
        <button 
          className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
          onClick={() => setFilter('all')}
        >
          All ({reservations.length})
        </button>
        <button 
          className={`filter-btn ${filter === 'confirmed' ? 'active' : ''}`}
          onClick={() => setFilter('confirmed')}
        >
          Confirmed
        </button>
        <button 
          className={`filter-btn ${filter === 'completed' ? 'active' : ''}`}
          onClick={() => setFilter('completed')}
        >
          Completed
        </button>
        <button 
          className={`filter-btn ${filter === 'cancelled' ? 'active' : ''}`}
          onClick={() => setFilter('cancelled')}
        >
          Cancelled
        </button>
      </div>

      <div className="reservations-container">
        {loading ? (
          <Loading />
        ) : filteredReservations.length === 0 ? (
          <p className="no-data">No reservations found</p>
        ) : (
          <div className="reservations-list">
            {filteredReservations.map((reservation) => (
              <ReservationCard
                key={reservation.reservation_id}
                reservation={reservation}
                onCancel={handleCancelReservation}
                isAdmin={false}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default HistoryPage;
