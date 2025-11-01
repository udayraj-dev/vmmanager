/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.html", "./src/**/*.jsx"
  ],
  theme: {
    extend: {
      colors: {
        'brand-aqua': '#0E3E58',
        'brand-aqua-lt': '#1D7979',
        'brand-beige': '#EAD9CA',
        'brand-beige-lt': '#F8F4F0',
        'brand-orange': '#D86A22',
        'brand-orange-lt': '#F8A345',
      }
    },
  },
  plugins: [],
}