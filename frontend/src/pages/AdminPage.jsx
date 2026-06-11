import React, { useState, useEffect } from 'react';
import { tableService } from '../services/tableService';
import { reservationService } from '../services/reservationService';
import TableCard from '../components/TableCard';
import ReservationCard from '../components/ReservationCard';
import Alert from '../components/Alert';
import Loading from '../components/Loading';
import '../styles/AdminPage.css';

const AdminPage = () => {
  const [activeTab, setActiveTab] = useState('tables'); // tables or reservations
  const [tables, setTables] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAddTableForm, setShowAddTableForm] = useState(false);
  const [formData, setFormData] = useState({ table_number: '', capacity: '' });

  useEffect(() => {
    if (activeTab === 'tables') {
      fetchTables();
    } else {
      fetchReservations();
    }
  }, [activeTab]);

  const fetchTables = async () => {
    try {
      setLoading(true);
      const response = await tableService.getAllTables();
      setTables(response.data || response);
      setError('');
    } catch (err) {
      setError('Failed to load tables');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchReservations = async () => {
    try {
      setLoading(true);
      const response = await reservationService.getAllReservations();
      setReservations(response.data || response);
      setError('');
    } catch (err) {
      setError('Failed to load reservations');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddTable = async (e) => {
    e.preventDefault();
    try {
      await tableService.addTable({
        table_number: parseInt(formData.table_number),
        capacity: parseInt(formData.capacity),
        status: 'Available',
      });
      setFormData({ table_number: '', capacity: '' });
      setShowAddTableForm(false);
      fetchTables();
      alert('Table added successfully!');
    } catch (err) {
      setError('Failed to add table');
      console.error(err);
    }
  };

  const handleDeleteTable = async (tableId) => {
    if (!window.confirm('Are you sure you want to delete this table?')) {
      return;
    }

    try {
      await tableService.deleteTable(tableId);
      fetchTables();
      alert('Table deleted successfully!');
    } catch (err) {
      setError('Failed to delete table');
      console.error(err);
    }
  };

  return (
    <div className="admin-page">
      <div className="admin-header">
        <h1>Admin Panel</h1>
        <p>Manage restaurant tables and reservations</p>
      </div>

      {error && <Alert message={error} type="error" />}

      <div className="admin-tabs">
        <button 
          className={`tab-btn ${activeTab === 'tables' ? 'active' : ''}`}
          onClick={() => setActiveTab('tables')}
        >
          🍽️ Manage Tables
        </button>
        <button 
          className={`tab-btn ${activeTab === 'reservations' ? 'active' : ''}`}
          onClick={() => setActiveTab('reservations')}
        >
          📋 View Reservations
        </button>
      </div>

      <div className="admin-content">
        {activeTab === 'tables' && (
          <div className="tables-management">
            <div className="tables-header">
              <h2>Restaurant Tables</h2>
              <button 
                className="btn btn-primary"
                onClick={() => setShowAddTableForm(!showAddTableForm)}
              >
                {showAddTableForm ? 'Cancel' : '+ Add Table'}
              </button>
            </div>

            {showAddTableForm && (
              <form className="add-table-form" onSubmit={handleAddTable}>
                <div className="form-group">
                  <label htmlFor="tableNumber">Table Number</label>
                  <input
                    id="tableNumber"
                    type="number"
                    placeholder="e.g., 1"
                    value={formData.table_number}
                    onChange={(e) => setFormData({ ...formData, table_number: e.target.value })}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="capacity">Capacity</label>
                  <input
                    id="capacity"
                    type="number"
                    placeholder="e.g., 4"
                    value={formData.capacity}
                    onChange={(e) => setFormData({ ...formData, capacity: e.target.value })}
                    required
                  />
                </div>

                <button type="submit" className="btn btn-primary">
                  Add Table
                </button>
              </form>
            )}

            {loading ? (
              <Loading />
            ) : tables.length === 0 ? (
              <p>No tables found</p>
            ) : (
              <div className="tables-grid">
                {tables.map((table) => (
                  <TableCard
                    key={table.table_id}
                    table={table}
                    isAdmin={true}
                    onDelete={handleDeleteTable}
                  />
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'reservations' && (
          <div className="reservations-management">
            <h2>All Reservations</h2>
            {loading ? (
              <Loading />
            ) : reservations.length === 0 ? (
              <p>No reservations found</p>
            ) : (
              <div className="reservations-grid">
                {reservations.map((reservation) => (
                  <ReservationCard
                    key={reservation.reservation_id}
                    reservation={reservation}
                    isAdmin={true}
                  />
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminPage;
