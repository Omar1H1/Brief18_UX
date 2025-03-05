import React, { createContext, useState, useContext, useEffect } from 'react';
import * as loginService from '../services/loginService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const saveTokenToStorage = (data) => {
    if (data && data.accessToken) {
      sessionStorage.setItem('userToken', data.accessToken);
      sessionStorage.setItem('userData', JSON.stringify(data));
    }
  };

  const removeTokenFromStorage = () => {
    sessionStorage.removeItem('userToken');
    sessionStorage.removeItem('userData');
  };

  const login = async (username, password) => {
    try {
      const data = await loginService.login(username, password);
      setAuth(data);
      saveTokenToStorage(data);
      return data;
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  const register = async (username, password) => {
    try {
      const data = await loginService.register(username, password);
      setAuth(data);
      saveTokenToStorage(data);
      return true;
    } catch (error) {
      console.error('Registration error:', error);
      return false;
    }
  };

  const logout = () => {
    setAuth(null);
    removeTokenFromStorage();
  };

  useEffect(() => {
    const checkExistingSession = async () => {
      try {
        const storedToken = sessionStorage.getItem('userToken');

        if (storedToken) {
          const userData = await loginService.validateToken(storedToken);

          setAuth(userData);
        }
      } catch (error) {
        removeTokenFromStorage();
      } finally {
        setIsLoading(false);
      }
    };

    checkExistingSession();
  }, []);

  return (
      <AuthContext.Provider value={{
        auth,
        login,
        register,
        logout,
        isLoading
      }}>
        {children}
      </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);