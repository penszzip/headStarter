import { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(true);

  // Login function
  const login = async (username, password) => {
    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
      setLoading(false);
      if (response.ok) {
        const data = await response.json();
        setToken(data.token);
        setUser({ username: data.username });
        setIsAuthenticated(true);
        localStorage.setItem('token', data.token);
        return { success: true };
      } else {
        return { 
          success: false, 
          message: 'Invalid credentials'
        };
      }
    } catch (error) {
      console.error('Login failed:', error);
      return { 
        success: false, 
        message: 'Login failed. Please try again.' 
      };
    }
  };

  // Register function
  const register = async (username, password) => {
    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
      setLoading(false);
      if (response.ok) {
        const data = await response.json();
        setToken(data.token);
        setUser({ username: data.username });
        setIsAuthenticated(true);
        localStorage.setItem('token', data.token);
        return { success: true };
      } else {
        const errorData = await response.json();
        return { 
          success: false, 
          message: errorData || 'Registration failed' 
        };
      }
    } catch (error) {
      console.error('Registration failed:', error);
      return { 
        success: false, 
        message: 'Registration failed. Please try again.' 
      };
    }
  };

  // Logout function
  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ 
      isAuthenticated, 
      user, 
      token,
      loading,
      login, 
      register, 
      logout 
    }}>
      {children}
    </AuthContext.Provider>
  );
};