import React, { useState } from 'react';
import { reservationService } from '../services/reservationService';
import Alert from '../components/Alert';
import '../styles/ReservationForm.css';

const ReservationForm = ({ table, userId, onSuccess, onCancel }) => {
  const [date, setDate] = useState('');
  const [time, setTime] = useState('');
  const [peopleCount, setPeopleCount] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!date || !time) {
      setError('Please select date and time');
      return;
    }

    if (peopleCount < 1 || peopleCount > table.capacity) {
      setError(`People count must be between 1 and ${table.capacity}`);
      return;
    }

    setLoading(true);

    try {
      await reservationService.createReservation({
        tableId: table.table_id,
        userId,
        date,
        time,
        peopleCount,
      });
      onSuccess();
    } catch (err) {
      setError(err.message || 'Failed to create reservation');
    } finally {
      setLoading(false);
    }
  };

  const getTodayDate = () => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  };

  return (
    <div className="reservation-form-modal">
      <div className="modal-overlay" onClick={onCancel}></div>
      <div className="modal-content">
        <div className="modal-header">
          <h2>Reserve {table.restaurant_name || 'DineReserve Bistro'} - Table {table.table_number}</h2>
          <button className="close-btn" onClick={onCancel}>×</button>
        </div>

        {error && <Alert message={error} type="error" />}

        <form onSubmit={handleSubmit} className="reservation-form">
          <div className="form-group">
            <label htmlFor="date">Booking Date</label>
            <input
              id="date"
              type="date"
              min={getTodayDate()}
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="time">Booking Time</label>
            <input
              id="time"
              type="time"
              value={time}
              onChange={(e) => setTime(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="peopleCount">Number of People</label>
            <input
              id="peopleCount"
              type="number"
              min="1"
              max={table.capacity}
              value={peopleCount}
              onChange={(e) => setPeopleCount(parseInt(e.target.value))}
              required
            />
            <small>Maximum: {table.capacity} people</small>
          </div>

          <div className="form-actions">
            <button 
              type="button" 
              className="btn btn-secondary"
              onClick={onCancel}
              disabled={loading}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Reserving...' : 'Confirm Reservation'}
            </button>
          </div>
        </form>

        <div className="table-info">
          <h4>Table Details</h4>
          <p><strong>Restaurant:</strong> {table.restaurant_name || 'DineReserve Bistro'}</p>
          <p><strong>Capacity:</strong> {table.capacity} people</p>
          <p><strong>Status:</strong> {table.status}</p>
        </div>
      </div>
    </div>
  );
};

export default ReservationForm;
