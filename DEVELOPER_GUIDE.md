# 🎯 Frontend Development Guide

## Quick Start untuk Developer

### 1. Setup
```bash
# Install dependencies
npm install

# Copy environment file
cp .env.example .env

# Update VITE_APP_API_URL sesuai backend URL Anda
```

### 2. Development
```bash
# Start dev server
npm run dev

# Aplikasi akan berjalan di http://localhost:5173
```

### 3. Build & Deploy
```bash
# Build untuk production
npm run build

# Preview build (local)
npm run preview
```

## 📁 Folder Structure Quick Reference

```
src/
├── components/        # Reusable React components
├── context/          # Global state (AuthContext)
├── pages/            # Page-level components
├── services/         # API integration
├── styles/           # CSS files
└── utils/            # Helper functions (for future use)
```

## 🔑 Key Files to Understand

1. **App.jsx** - Main routing setup
2. **src/context/AuthContext.jsx** - Authentication state
3. **src/services/api.js** - Axios configuration & interceptors
4. **src/components/PrivateRoute.jsx** - Protected routes

## 🎨 Adding New Components

1. Create component in `src/components/`
2. Import and use in pages
3. Create corresponding CSS in `src/styles/` if needed

Example:
```jsx
// src/components/MyComponent.jsx
import React from 'react';
import '../styles/MyComponent.css';

const MyComponent = () => {
  return <div>My Component</div>;
};

export default MyComponent;
```

## 📡 Adding New API Endpoints

1. Add new service method in `src/services/` (e.g., `userService.js`)
2. Export API methods
3. Use `api` instance for HTTP calls
4. Handle errors with try-catch

Example:
```javascript
// src/services/userService.js
import api from './api';

export const userService = {
  getProfile: async (userId) => {
    try {
      const response = await api.get(`/users/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }
};
```

## 🎯 Common Tasks

### Add New Page
1. Create file in `src/pages/MyPage.jsx`
2. Add route in `App.jsx`
3. Link in navbar or other components

### Add Authentication Check
Use `useAuth()` hook from AuthContext:
```jsx
import { useAuth } from '../context/AuthContext';

const MyComponent = () => {
  const { isAuthenticated, user, logout } = useAuth();
  
  return (
    <>
      {isAuthenticated && <p>Welcome {user.name}</p>}
    </>
  );
};
```

### Handle Loading State
Use Loading component:
```jsx
import Loading from '../components/Loading';

{loading ? <Loading /> : <Content />}
```

## 🧪 Testing Tips

1. **Test Auth Flow**: Register → Login → Check localStorage
2. **Test API Calls**: Use browser DevTools Network tab
3. **Test Protected Routes**: Try accessing admin route as customer
4. **Test Error Handling**: Check console for error messages

## ⚠️ Common Issues & Solutions

### Issue: Token not persisting
**Solution**: Check localStorage in DevTools, verify token is saved on login

### Issue: API 404 errors
**Solution**: Verify backend is running, check VITE_APP_API_URL, verify endpoint exists

### Issue: CORS errors
**Solution**: Backend must enable CORS, add `Access-Control-Allow-*` headers

### Issue: Routes not working
**Solution**: Make sure routes are defined in App.jsx before catch-all route

## 📚 Dependencies & Versions

- React 18+
- React Router v6+
- Axios 1.6+
- Vite 5+

Update with: `npm outdated`

## 🚀 Performance Tips

1. Use React DevTools Profiler
2. Implement code splitting for large pages
3. Lazy load components if needed: `React.lazy()`
4. Avoid unnecessary re-renders with `React.memo()` if needed

## 📝 Code Style Guidelines

- Use arrow functions for components
- Use destructuring for props
- Use meaningful variable names
- Keep components small and focused
- Add comments for complex logic

## 🔗 Useful Resources

- [React Documentation](https://react.dev)
- [React Router Documentation](https://reactrouter.com)
- [Axios Documentation](https://axios-http.com)
- [Vite Documentation](https://vitejs.dev)

## 📧 Questions?

Refer to README.md for general info or API_SPEC.md for backend integration details.
