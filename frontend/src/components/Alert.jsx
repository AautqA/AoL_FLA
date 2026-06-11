import React, { useState, useEffect } from 'react';
import '../styles/Alert.css';

const Alert = ({ message, type = 'info', duration = 5000 }) => {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
    }, duration);

    return () => clearTimeout(timer);
  }, [duration]);

  if (!visible) return null;

  return (
    <div className={`alert alert-${type}`}>
      <span>{message}</span>
      <button onClick={() => setVisible(false)} className="alert-close">×</button>
    </div>
  );
};

export default Alert;
