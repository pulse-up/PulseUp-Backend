import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import api from '../api/api';
import clinicImage from '../assets/pulseup-login-clinic.png';
import ResponsiveHeader from '../components/ResponsiveHeader';

import './RegisterPage.css';

const INITIAL_FORM = {
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  studentNumber: '',
  course: '',
  campus: '',
  residence: '',
  yearOfStudy: '',
  emergencyContactName: '',
  emergencyContactPhone: '',
  password: '',
  confirmPassword: '',
};

const CAMPUSES = [
  'District Six Campus',
  'Bellville Campus',
  'Mowbray Campus',
  'Wellington Campus',
];

const RESIDENCES = [
  'CPUT Student Residence',
  'Private Student Residence',
  'Living at Home',
  'Other',
];

const YEARS_OF_STUDY = [
  {
    value: 'FIRST_YEAR',
    label: '1st Year',
  },
  {
    value: 'SECOND_YEAR',
    label: '2nd Year',
  },
  {
    value: 'THIRD_YEAR',
    label: '3rd Year',
  },
  {
    value: 'FOURTH_YEAR',
    label: '4th Year',
  },
  {
    value: 'POSTGRADUATE',
    label: 'Postgraduate',
  },
  {
    value: 'MASTERS',
    label: 'Masters',
  },
  {
    value: 'PHD',
    label: 'PhD',
  },
];

function CheckIcon() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true">
      <path d="m6.5 12.5 3.2 3.2 7.8-8" />
    </svg>
  );
}

function DashboardIcon() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true">
      <rect x="3" y="3" width="7" height="7" rx="2" />
      <rect x="14" y="3" width="7" height="7" rx="2" />
      <rect x="3" y="14" width="7" height="7" rx="2" />
      <rect x="14" y="14" width="7" height="7" rx="2" />
    </svg>
  );
}

function getRegistrationError(error) {
  if (!error.response) {
    return (
      'Unable to connect to the PulseUp backend. ' +
      'Make sure Spring Boot is running on port 8080.'
    );
  }

  const responseData = error.response.data;

  const serverMessage =
    typeof responseData === 'string'
      ? responseData
      : responseData?.message || responseData?.error;

  if (serverMessage) {
    return serverMessage;
  }

  switch (error.response.status) {
    case 400:
      return 'The registration details were rejected by the server.';

    case 403:
      return 'Student registration is blocked by the security configuration.';

    case 409:
      return 'That email address or student number is already registered.';

    default:
      if (error.response.status >= 500) {
        return 'The server could not create the student account.';
      }

      return 'Registration could not be completed.';
  }
}

function RegisterPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState(INITIAL_FORM);
  const [message, setMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  function updateField(event) {
    const { name, value } = event.target;

    setForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));

    setMessage('');
  }

  function validateForm() {
    const requiredValues = Object.values(form);

    if (requiredValues.some((value) => !String(value).trim())) {
      return 'Please complete every required field.';
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim())) {
      return 'Enter a valid email address.';
    }

    if (!/^\d{8,12}$/.test(form.studentNumber.trim())) {
      return 'The student number must contain 8 to 12 digits.';
    }

    if (!/^0\d{9}$/.test(form.phoneNumber.trim())) {
      return 'Enter a valid 10-digit South African phone number.';
    }

    if (!/^0\d{9}$/.test(form.emergencyContactPhone.trim())) {
      return 'Enter a valid 10-digit emergency contact number.';
    }

    if (form.password.length < 8) {
      return 'Your password must contain at least 8 characters.';
    }

    if (form.password !== form.confirmPassword) {
      return 'The passwords do not match.';
    }

    return '';
  }

  async function handleSubmit(event) {
    event.preventDefault();

    const validationMessage = validateForm();

    if (validationMessage) {
      setMessage(validationMessage);
      return;
    }

    setMessage('');
    setIsSubmitting(true);

    try {
      const response = await api.post('/auth/register/student', {
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim(),
        email: form.email.trim().toLowerCase(),
        phoneNumber: form.phoneNumber.trim(),
        studentNumber: form.studentNumber.trim(),
        course: form.course.trim(),
        campus: form.campus,
        residence: form.residence,
        yearOfStudy: form.yearOfStudy,
        emergencyContactName: form.emergencyContactName.trim(),
        emergencyContactPhone: form.emergencyContactPhone.trim(),
        password: form.password,
      });

      const user = response.data || {};

      const token = user.token || user.accessToken || user.jwt || '';

      if (token) {
        localStorage.setItem(
          'pulseupUser',
          JSON.stringify({
            ...user,
            token,
            role: user.role || 'STUDENT',
          }),
        );

        navigate('/student/dashboard', {
          replace: true,
        });

        return;
      }

      navigate('/login', {
        replace: true,
      });
    } catch (error) {
      console.error(
        'Student registration failed:',
        error.response?.status,
        error.response?.data || error.message,
      );

      setMessage(getRegistrationError(error));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="pulse-register-page">
      <div
        className="pulse-register-pattern pulse-register-pattern-left"
        aria-hidden="true"
      />

      <div
        className="pulse-register-pattern pulse-register-pattern-right"
        aria-hidden="true"
      />

      <section
        className="pulse-register-shell"
        aria-label="PulseUp student registration"
      >
        <aside className="pulse-register-visual">
          <img
            src={clinicImage}
            alt="Student receiving help from a campus healthcare clinician"
          />

          <div className="pulse-register-image-overlay" aria-hidden="true" />

          <div className="pulse-register-visual-pattern" aria-hidden="true">
            <span />
            <span />
          </div>

          <div className="pulse-register-visual-heading">
            <p>YOUR HEALTHCARE DASHBOARD</p>

            <h2>One account for your campus healthcare.</h2>
          </div>

          <div className="pulse-register-dashboard">
            <header>
              <span className="pulse-register-dashboard-icon">
                <DashboardIcon />
              </span>

              <div>
                <small>STUDENT DASHBOARD</small>
                <strong>Healthcare at a glance</strong>
              </div>

              <span className="pulse-register-live">LIVE</span>
            </header>

            <div className="pulse-register-dashboard-grid">
              <article>
                <span>Next visit</span>
                <strong>General consultation</strong>
              </article>

              <article>
                <span>Digital queue</span>
                <strong>Live updates enabled</strong>
              </article>
            </div>

            <ul>
              <li>
                <i>
                  <CheckIcon />
                </i>
                Book and manage clinic appointments
              </li>

              <li>
                <i>
                  <CheckIcon />
                </i>
                View clinician and room information
              </li>
            </ul>
          </div>
        </aside>

        <section className="pulse-register-form-panel">
          <ResponsiveHeader
            variant="auth"
            ariaLabel="Registration page navigation"
            desktopAction={{ label: 'Back to home', to: '/' }}
            menuItems={[
              { label: 'Home', to: '/' },
              { label: 'Sign in', to: '/login' },
              { label: 'Create student account', to: '/register', active: true },
            ]}
          />

          <div className="pulse-register-content">
            <div className="pulse-register-heading">
              <p>STUDENT REGISTRATION</p>
              <h1>Create your account</h1>

              <span>
                Complete your details to enter the PulseUp healthcare dashboard.
              </span>
            </div>

            <form className="pulse-register-form" onSubmit={handleSubmit}>
              <label className="pulse-register-field">
                <span>First name</span>

                <input
                  name="firstName"
                  value={form.firstName}
                  onChange={updateField}
                  autoComplete="given-name"
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Last name</span>

                <input
                  name="lastName"
                  value={form.lastName}
                  onChange={updateField}
                  autoComplete="family-name"
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Email address</span>

                <input
                  name="email"
                  type="email"
                  value={form.email}
                  onChange={updateField}
                  autoComplete="email"
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Phone number</span>

                <input
                  name="phoneNumber"
                  type="tel"
                  value={form.phoneNumber}
                  onChange={updateField}
                  placeholder="0712345678"
                  maxLength={10}
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Student number</span>

                <input
                  name="studentNumber"
                  value={form.studentNumber}
                  onChange={updateField}
                  inputMode="numeric"
                  maxLength={12}
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Course</span>

                <input
                  name="course"
                  value={form.course}
                  onChange={updateField}
                  placeholder="Diploma in ICT Applications Development"
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Campus</span>

                <select
                  name="campus"
                  value={form.campus}
                  onChange={updateField}
                  disabled={isSubmitting}
                  required
                >
                  <option value="">Select campus</option>

                  {CAMPUSES.map((campus) => (
                    <option value={campus} key={campus}>
                      {campus}
                    </option>
                  ))}
                </select>
              </label>

              <label className="pulse-register-field">
                <span>Residence</span>

                <select
                  name="residence"
                  value={form.residence}
                  onChange={updateField}
                  disabled={isSubmitting}
                  required
                >
                  <option value="">Select residence</option>

                  {RESIDENCES.map((residence) => (
                    <option value={residence} key={residence}>
                      {residence}
                    </option>
                  ))}
                </select>
              </label>

              <label className="pulse-register-field">
                <span>Year of study</span>

                <select
                  name="yearOfStudy"
                  value={form.yearOfStudy}
                  onChange={updateField}
                  disabled={isSubmitting}
                  required
                >
                  <option value="">Select year</option>

                  {YEARS_OF_STUDY.map((year) => (
                    <option value={year.value} key={year.value}>
                      {year.label}
                    </option>
                  ))}
                </select>
              </label>

              <label className="pulse-register-field">
                <span>Emergency contact name</span>

                <input
                  name="emergencyContactName"
                  value={form.emergencyContactName}
                  onChange={updateField}
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Emergency contact phone</span>

                <input
                  name="emergencyContactPhone"
                  type="tel"
                  value={form.emergencyContactPhone}
                  onChange={updateField}
                  placeholder="0712345678"
                  maxLength={10}
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Password</span>

                <input
                  name="password"
                  type="password"
                  value={form.password}
                  onChange={updateField}
                  autoComplete="new-password"
                  minLength={8}
                  disabled={isSubmitting}
                  required
                />
              </label>

              <label className="pulse-register-field">
                <span>Confirm password</span>

                <input
                  name="confirmPassword"
                  type="password"
                  value={form.confirmPassword}
                  onChange={updateField}
                  autoComplete="new-password"
                  minLength={8}
                  disabled={isSubmitting}
                  required
                />
              </label>

              {message && (
                <p className="pulse-register-message" role="alert">
                  {message}
                </p>
              )}

              <button
                className="pulse-register-submit"
                type="submit"
                disabled={isSubmitting}
              >
                {isSubmitting
                  ? 'Creating account...'
                  : 'Create student account'}
              </button>
            </form>

            <p className="pulse-register-footer">
              Already registered? <Link to="/login">Sign in</Link>
            </p>
          </div>
        </section>
      </section>
    </main>
  );
}

export default RegisterPage;
