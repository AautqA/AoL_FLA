# DineReserve Workspace

Workspace ini sekarang dipisah menjadi dua folder utama:

- [frontend/](frontend/) untuk aplikasi React/Vite
- [backend/](backend/) untuk server Java `HttpServer`

## Jalankan Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend default mengarah ke backend lokal di `http://localhost:5001/api`.

## Jalankan Backend

```bash
cd backend
sh run.sh
```

Backend default berjalan di `http://localhost:5001`.

## Dokumentasi Backend

Lihat [backend/README.md](backend/README.md) untuk arsitektur, endpoint, dan dummy data backend.

## Catatan

- Root markdown lama yang duplikat sudah dibersihkan.
- File build seperti `.vite/` dan `backend/out/` dianggap artefak dan diabaikan lewat `.gitignore`.

## 🎨 Styling

- **CSS Modules**: Tidak digunakan, menggunakan plain CSS dengan global variables
- **Color Scheme**: Blue (#2563eb) as primary, dengan supporting colors untuk success, danger, warning
- **Responsive**: Mobile-first design dengan breakpoint di 768px

### CSS Variables (di `styles/global.css`):
```css
--primary-color: #2563eb
--secondary-color: #64748b
--danger-color: #dc2626
--success-color: #16a34a
--warning-color: #ea580c
--light-bg: #f8fafc
--border-color: #e2e8f0
--text-dark: #1e293b
--text-light: #64748b
```

## 🔑 Key Components Explanation

### AuthContext
- Manage global authentication state
- Store user data dan token di localStorage
- Provide `useAuth()` hook untuk akses auth state di component manapun

### PrivateRoute
- Wrapper untuk melindungi routes yang memerlukan authentication
- Check user role untuk admin-only routes
- Redirect ke login jika tidak authenticated

### Services (API Layer)
- Centralized API calls
- Error handling yang konsisten
- Automatic token injection ke headers

## ⚠️ Important Notes

1. **Token Management**: Token disimpan di localStorage dan di-attach otomatis ke setiap request via axios interceptor
2. **Role-based Access**: Routes di-protect berdasarkan user role (customer/admin)
3. **Date/Time Format**: Backend harus terima format ISO 8601 untuk dates dan HH:MM untuk times
4. **API Errors**: Semua error ditangani dan ditampilkan sebagai Alert component
5. **CORS**: Pastikan backend enable CORS untuk frontend URL

## 🛠️ Teknologi yang Digunakan

- **React 18** - UI library
- **React Router v6** - Client-side routing
- **Axios** - HTTP client
- **Vite** - Build tool & dev server
- **Context API** - State management (untuk auth)
- **Plain CSS** - Styling

## 📝 Next Steps

1. Setup backend server (Flask/Node.js/Django) dengan endpoints sesuai `src/services/`
2. Update `VITE_APP_API_URL` di `.env` sesuai backend URL
3. Test authentication flow
4. Test customer features (view tables, make reservations)
5. Test admin features (manage tables, view all reservations)

## 📝 Future Enhancements

- Add date/time picker library (react-datepicker)
- Add form validation library (react-hook-form, Zod)
- Add loading states untuk table & reservations
- Add pagination untuk reservation list
- Add search/filter functionality
- Add success/error toasts dengan library (react-hot-toast)
- Add chart untuk admin analytics
- Add unit tests dengan Vitest
- Add E2E tests dengan Cypress

## 📧 Support

Untuk pertanyaan atau issue, hubungi tim development.
