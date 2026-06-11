import React from 'react';
import '../styles/TableCard.css';

const TableCard = ({ table, onReserve, isAdmin, onEdit, onDelete }) => {
  const getStatusColor = (status) => {
    switch (status) {
      case 'Available':
        return 'status-available';
      case 'Reserved':
        return 'status-reserved';
      case 'Occupied':
        return 'status-occupied';
      default:
        return '';
    }
  };

  return (
    <div className={`table-card ${getStatusColor(table.status)}`}>
      <div className="table-header">
        <h3>{table.restaurant_name || 'DineReserve Bistro'} - Table {table.table_number}</h3>
        <span className={`status-badge ${getStatusColor(table.status)}`}>
          {table.status}
        </span>
      </div>
      
      <div className="table-info">
        <p><strong>Restaurant:</strong> {table.restaurant_name || 'DineReserve Bistro'}</p>
        <p><strong>Capacity:</strong> {table.capacity} people</p>
        {table.status && <p><strong>Status:</strong> {table.status}</p>}
      </div>

      <div className="table-actions">
        {!isAdmin && table.status === 'Available' && (
          <button className="btn btn-primary" onClick={() => onReserve(table)}>
            Reserve Now
          </button>
        )}
        
        {isAdmin && (
          <>
            <button className="btn btn-secondary" onClick={() => onEdit(table)}>
              Edit
            </button>
            <button className="btn btn-danger" onClick={() => onDelete(table.table_id)}>
              Delete
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default TableCard;
