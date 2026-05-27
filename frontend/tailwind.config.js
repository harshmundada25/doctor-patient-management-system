export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      boxShadow: {
        soft: '0 20px 45px rgba(15, 23, 42, 0.12)',
      },
      backgroundImage: {
        'hospital-radial': 'radial-gradient(circle at top left, rgba(14,165,233,0.25), transparent 35%), radial-gradient(circle at bottom right, rgba(16,185,129,0.18), transparent 30%)',
      },
    },
  },
  plugins: [],
};