import { createContext, useContext, useEffect, useMemo, useState } from 'react';

const ThemeContext = createContext(null);

export function ThemeProvider({ children }) {
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem('hospital_theme') !== 'light');

  useEffect(() => {
    document.documentElement.classList.toggle('dark', darkMode);
    document.documentElement.dataset.theme = darkMode ? 'dark' : 'light';
    localStorage.setItem('hospital_theme', darkMode ? 'dark' : 'light');
  }, [darkMode]);

  const value = useMemo(
    () => ({ darkMode, setDarkMode, toggleTheme: () => setDarkMode((current) => !current) }),
    [darkMode],
  );

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

export function useTheme() {
  return useContext(ThemeContext);
}