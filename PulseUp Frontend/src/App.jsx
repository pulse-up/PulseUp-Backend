import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';

import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import StudentDashboard from './pages/StudentDashboard';
import StaffDashboard from './pages/StaffDashboard';
import AdminDashboard from './pages/AdminDashboard';
import AdminCreateAccountPage from './pages/AdminCreateAccountPage';

import './App.css';

function getStoredUser() {
  const storedUser = localStorage.getItem('pulseupUser');

  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser);
  } catch (error) {
    console.error('Could not read saved PulseUp user:', error);

    localStorage.removeItem('pulseupUser');

    return null;
  }
}

function normalizeRole(role) {
  return String(role || '')
    .trim()
    .toUpperCase()
    .replace('ROLE_', '');
}

function getToken(user) {
  return user?.token || user?.accessToken || user?.jwt || '';
}

function getDashboardPath(role) {
  switch (normalizeRole(role)) {
    case 'STUDENT':
      return '/student/dashboard';

    case 'STAFF':
      return '/staff/dashboard';

    case 'ADMIN':
      return '/admin/dashboard';

    default:
      return '/login';
  }
}

function PublicOnlyRoute({ children }) {
  const user = getStoredUser();

  const token = getToken(user);

  if (user && token) {
    return <Navigate to={getDashboardPath(user.role)} replace />;
  }

  return children;
}

function ProtectedRoute({ allowedRoles, children }) {
  const user = getStoredUser();

  const token = getToken(user);

  if (!user || !token) {
    return <Navigate to="/login" replace />;
  }

  const userRole = normalizeRole(user.role);

  const acceptedRoles = allowedRoles.map((role) => normalizeRole(role));

  if (!acceptedRoles.includes(userRole)) {
    return <Navigate to={getDashboardPath(userRole)} replace />;
  }

  return children;
}

function DashboardRedirect() {
  const user = getStoredUser();

  const token = getToken(user);

  if (!user || !token) {
    return <Navigate to="/login" replace />;
  }

  return <Navigate to={getDashboardPath(user.role)} replace />;
}

function NotFoundPage() {
  return (
    <main className="pulse-not-found">
      <section className="pulse-not-found-card">
        <p>404</p>

        <h1>Page not found</h1>

        <span>The page you requested does not exist.</span>

        <a href="/">Return to home</a>
      </section>
    </main>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />

        <Route
          path="/login"
          element={
            <PublicOnlyRoute>
              <LoginPage />
            </PublicOnlyRoute>
          }
        />

        <Route
          path="/register"
          element={
            <PublicOnlyRoute>
              <RegisterPage />
            </PublicOnlyRoute>
          }
        />

        <Route path="/dashboard" element={<DashboardRedirect />} />

        <Route
          path="/student/dashboard"
          element={
            <ProtectedRoute allowedRoles={['STUDENT']}>
              <StudentDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/staff/dashboard"
          element={
            <ProtectedRoute allowedRoles={['STAFF']}>
              <StaffDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/create-account"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <AdminCreateAccountPage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
