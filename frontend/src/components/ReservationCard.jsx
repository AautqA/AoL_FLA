import React from 'react';
import '../styles/ReservationCard.css';

const ReservationCard = ({ reservation, onCancel, isAdmin = false }) => {
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'confirmed':
        return 'badge-confirmed';
      case 'cancelled':
        return 'badge-cancelled';
      case 'completed':
        return 'badge-completed';
      default:
        return 'badge-pending';
    }
  };

  return (
    <div className={`reservation-card ${getStatusBadgeClass(reservation.status)}`}>
      <div className="reservation-header">
        <h3>Reservation #{reservation.reservation_id}</h3>
        <span className={`status-badge ${getStatusBadgeClass(reservation.status)}`}>
          {reservation.status}
        </span>
      </div>

      <div className="reservation-details">
        <div className="detail-row">
          <span className="label">Restaurant:</span>
          <span className="value">{reservation.restaurant_name || 'DineReserve Bistro'}</span>
        </div>
        <div className="detail-row">
          <span className="label">Date:</span>
          <span className="value">{formatDate(reservation.booking_date)}</span>
        </div>
        <div className="detail-row">
          <span className="label">Time:</span>
          <span className="value">{reservation.booking_time}</span>
        </div>
        <div className="detail-row">
          <span className="label">People:</span>
          <span className="value">{reservation.people_count} people</span>
        </div>
        {isAdmin && (
          <div className="detail-row">
            <span className="label">Table:</span>
            <span className="value">Table {reservation.table_number}</span>
          </div>
        )}
        {isAdmin && (
          <div className="detail-row">
            <span className="label">Customer:</span>
            <span className="value">{reservation.user_id}</span>
          </div>
        )}
      </div>

      <div className="reservation-actions">
        {reservation.status === 'confirmed' && (
          <button 
            className="btn btn-danger" 
            onClick={() => onCancel(reservation.reservation_id)}
          >
            Cancel Reservation
          </button>
        )}
      </div>
    </div>
  );
};

export default ReservationCard;
