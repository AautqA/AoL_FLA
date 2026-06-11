import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

// Safe localStorage access
const getStorageItem = (key) => {
  try {
    return localStorage.getItem(key);
  } catch (e) {
    console.warn('localStorage not available');
    return null;
  }
};

const setStorageItem = (key, value) => {
  try {
    localStorage.setItem(key, value);
  } catch (e) {
    console.warn('localStorage not available');
  }
};

const removeStorageItem = (key) => {
  try {
    localStorage.removeItem(key);
  } catch (e) {
    console.warn('localStorage not available');
  }
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(getStorageItem('token'));

  useEffect(() => {
    // Check if user is already logged in
    const storedUser = getStorageItem('user');
    const storedToken = getStorageItem('token');
    
    if (storedUser && storedToken) {
      try {
        setUser(JSON.parse(storedUser));
        setToken(storedToken);
      } catch (e) {
        console.warn('Failed to parse stored user');
      }
    }
    setLoading(false);
  }, []);

  const login = (userData, authToken) => {
    setUser(userData);
    setToken(authToken);
    setStorageItem('user', JSON.stringify(userData));
    setStorageItem('token', authToken);
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    removeStorageItem('user');
    removeStorageItem('token');
  };

  const register = (userData, authToken) => {
    setUser(userData);
    setToken(authToken);
    setStorageItem('user', JSON.stringify(userData));
    setStorageItem('token', authToken);
  };

  return (
    <AuthContext.Provider value={{
      user,
      token,
      loading,
      login,
      logout,
      register,
      isAuthenticated: !!user
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
