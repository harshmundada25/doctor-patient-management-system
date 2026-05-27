import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);

function parseJwt(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload;
  } catch {
    return null;
  }
}

function isTokenExpired(token) {
  const payload = parseJwt(token);
  if (!payload?.exp) {
    return true;
  }
  return payload.exp * 1000 < Date.now();
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('hospital_token'));
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('hospital_user');
    return raw ? JSON.parse(raw) : null;
  });

  useEffect(() => {
    if (token && isTokenExpired(token)) {
      logout();
    }
  }, [token]);

  const login = (authResponse) => {
    const nextUser = {
      userId: authResponse.userId,
      fullName: authResponse.fullName,
      email: authResponse.email,
      role: authResponse.role,
    };
    localStorage.setItem('hospital_token', authResponse.token);
    localStorage.setItem('hospital_user', JSON.stringify(nextUser));
    setToken(authResponse.token);
    setUser(nextUser);
  };

  const logout = () => {
    localStorage.removeItem('hospital_token');
    localStorage.removeItem('hospital_user');
    setToken(null);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token && user),
      login,
      logout,
    }),
    [token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}

export async function fetchMe() {
  const { data } = await api.get('/auth/me');
  return data;
}