import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/HomePage.css';

const HomePage = () => {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="home-page">
      <div className="hero-section">
        <h1>Welcome to DineReserve</h1>
        <p>Reserve your favorite restaurant table with just a few clicks</p>
        
        <div className="hero-buttons">
          {!isAuthenticated ? (
            <>
              <button className="btn btn-primary btn-lg" onClick={() => navigate('/login')}>
                Login
              </button>
              <button className="btn btn-secondary btn-lg" onClick={() => navigate('/register')}>
                Register
              </button>
            </>
          ) : (
            <>
              {user?.role === 'customer' && (
                <button className="btn btn-primary btn-lg" onClick={() => navigate('/dashboard')}>
                  View Available Tables
                </button>
              )}
              {user?.role === 'admin' && (
                <button className="btn btn-primary btn-lg" onClick={() => navigate('/admin')}>
                  Admin Panel
                </button>
              )}
            </>
          )}
        </div>
      </div>

      <div className="features-section">
        <div className="feature-card">
          <h3>📋 Easy Booking</h3>
          <p>Reserve tables quickly and easily with our intuitive booking system</p>
        </div>
        <div className="feature-card">
          <h3>✅ Real-time Availability</h3>
          <p>See available tables in real-time and pick your preferred time slot</p>
        </div>
        <div className="feature-card">
          <h3>🛡️ Secure</h3>
          <p>Your reservation is secure and you can manage it anytime</p>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
