import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { tableService } from '../services/tableService';
import { reservationService } from '../services/reservationService';
import TableCard from '../components/TableCard';
import ReservationForm from '../components/ReservationForm';
import Alert from '../components/Alert';
import '../styles/DashboardPage.css';

const DashboardPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedTable, setSelectedTable] = useState(null);
  const [showReservationForm, setShowReservationForm] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchAvailableTables();
  }, []);

  const fetchAvailableTables = async () => {
    try {
      setLoading(true);
      const today = new Date().toISOString().split('T')[0];
      const response = await tableService.getAvailableTables(today, '19:00');
      setTables(response.data || response);
      setError('');
    } catch (err) {
      setError('Failed to load available tables');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleReserveTable = (table) => {
    setSelectedTable(table);
    setShowReservationForm(true);
  };

  const handleReservationSuccess = () => {
    setShowReservationForm(false);
    setSelectedTable(null);
    fetchAvailableTables();
    // Navigate to history or show success message
    alert('Reservation confirmed!');
    navigate('/history');
  };

  const filteredTables = tables.filter((table) => {
    const search = searchTerm.trim().toLowerCase();
    if (!search) {
      return true;
    }
    const restaurantName = (table.restaurant_name || '').toLowerCase();
    const tableNumber = String(table.table_number || '');
    return restaurantName.includes(search) || tableNumber.includes(search);
  });

  return (
    <div className="dashboard-page">
      <div className="dashboard-header">
        <h1>Welcome, {user?.name}!</h1>
        <p>Select a table to make your reservation</p>
        <div className="search-section">
          <input
            type="search"
            className="search-input"
            placeholder="Search restaurant or table number"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {error && <Alert message={error} type="error" />}

      {showReservationForm && selectedTable ? (
        <ReservationForm
          table={selectedTable}
          userId={user?.user_id}
          onSuccess={handleReservationSuccess}
          onCancel={() => {
            setShowReservationForm(false);
            setSelectedTable(null);
          }}
        />
      ) : (
        <div className="tables-container">
          {loading ? (
            <p>Loading available tables...</p>
          ) : filteredTables.length === 0 ? (
            <p>No available tables at the moment</p>
          ) : (
            <div className="tables-grid">
              {filteredTables.map((table) => (
                <TableCard
                  key={table.table_id}
                  table={table}
                  onReserve={handleReserveTable}
                  isAdmin={false}
                />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default DashboardPage;
