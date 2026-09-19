import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import heroImage from '../assets/pulseup-student-clinic.png';
import './HomePage.css';

const values = [
  {
    icon: '✚',
    title: 'Professional',
    text: 'Structured healthcare support delivered through campus clinic services.',
  },
  {
    icon: '◇',
    title: 'Trustworthy',
    text: 'Clear appointment information and dependable communication.',
  },
  {
    icon: '▣',
    title: 'Confidential',
    text: 'Personal and healthcare information handled with appropriate care.',
  },
  {
    icon: '◉',
    title: 'Discreet',
    text: 'Private booking that helps students access care confidently.',
  },
  {
    icon: '◎',
    title: 'Holistic',
    text: 'Support that considers health, wellbeing and everyday student life.',
  },
  {
    icon: '◌',
    title: 'Culturally aware',
    text: 'Respectful care for the diverse CPUT student community.',
  },
];

const services = [
  {
    icon: '+',
    title: 'General Consultations',
    text: 'Everyday health assessments, medical advice and referrals.',
  },
  {
    icon: '♡',
    title: 'Reproductive Health',
    text: 'Private and respectful reproductive health support.',
  },
  {
    icon: '✓',
    title: 'HIV VCT',
    text: 'Confidential voluntary counselling and testing.',
  },
  {
    icon: '◫',
    title: 'TB DOTS',
    text: 'Screening, treatment support and observed therapy.',
  },
  {
    icon: '✦',
    title: 'Wound Dressings',
    text: 'Wound assessment, cleaning and follow-up dressings.',
  },
];

const steps = [
  {
    number: '01',
    title: 'Choose a service',
    text: 'Select the campus healthcare service you need.',
  },
  {
    number: '02',
    title: 'Select an available time',
    text: 'Choose a clinic slot that works with your schedule.',
  },
  {
    number: '03',
    title: 'Arrive informed',
    text: 'Track your clinician, room, status and digital queue.',
  },
];

function HomePage() {
  const [headerExpanded, setHeaderExpanded] = useState(false);
  const [headerScrolled, setHeaderScrolled] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    let lastScrollY = window.scrollY;
    let animationFrameId = null;

    function updateNavigation() {
      const currentScrollY = window.scrollY;
      const scrollDifference = currentScrollY - lastScrollY;

      setHeaderScrolled(currentScrollY > 12);

      if (currentScrollY < 40) {
        setHeaderExpanded(false);
      } else if (scrollDifference > 6) {
        setHeaderExpanded(true);
      } else if (scrollDifference < -6) {
        setHeaderExpanded(false);
      }

      lastScrollY = currentScrollY;
      animationFrameId = null;
    }

    function handleScroll() {
      if (animationFrameId !== null) {
        return;
      }

      animationFrameId = window.requestAnimationFrame(updateNavigation);
    }

    const revealTargets = document.querySelectorAll(
      [
        '.home-stat-strip',
        '.home-heading',
        '.home-values',
        '.home-purpose',
        '.home-service-grid',
        '.home-service-action',
        '.home-steps',
        '.home-queue-card',
        '.home-emergency-card',
        '.home-contact-cards',
      ].join(','),
    );

    revealTargets.forEach((element) => {
      element.classList.add('home-reveal');
    });

    let revealObserver = null;

    if ('IntersectionObserver' in window) {
      revealObserver = new IntersectionObserver(
        (entries, observer) => {
          entries.forEach((entry) => {
            if (!entry.isIntersecting) {
              return;
            }

            entry.target.classList.add('is-visible');
            observer.unobserve(entry.target);
          });
        },
        {
          threshold: 0.12,
          rootMargin: '0px 0px -50px 0px',
        },
      );

      revealTargets.forEach((element) => {
        revealObserver.observe(element);
      });
    } else {
      revealTargets.forEach((element) => {
        element.classList.add('is-visible');
      });
    }

    window.addEventListener('scroll', handleScroll, {
      passive: true,
    });

    return () => {
      window.removeEventListener('scroll', handleScroll);

      if (animationFrameId !== null) {
        window.cancelAnimationFrame(animationFrameId);
      }

      if (revealObserver) {
        revealObserver.disconnect();
      }
    };
  }, []);

  useEffect(() => {
    if (!mobileMenuOpen) {
      return undefined;
    }

    const previousOverflow = document.body.style.overflow;

    function handleKeyDown(event) {
      if (event.key === 'Escape') {
        setMobileMenuOpen(false);
      }
    }

    function handleResize() {
      if (window.innerWidth > 980) {
        setMobileMenuOpen(false);
      }
    }

    document.body.style.overflow = 'hidden';

    window.addEventListener('keydown', handleKeyDown);
    window.addEventListener('resize', handleResize);

    return () => {
      document.body.style.overflow = previousOverflow;

      window.removeEventListener('keydown', handleKeyDown);

      window.removeEventListener('resize', handleResize);
    };
  }, [mobileMenuOpen]);

  function closeMobileMenu() {
    setMobileMenuOpen(false);
  }

  const headerClassName = [
    'home-header',
    headerScrolled ? 'home-header-scrolled' : '',
    headerExpanded && !mobileMenuOpen ? 'home-header-expanded' : '',
    mobileMenuOpen ? 'home-header-menu-open' : '',
  ]
    .filter(Boolean)
    .join(' ');

  const navigationClassName = [
    'home-links',
    mobileMenuOpen ? 'home-links-open' : '',
  ]
    .filter(Boolean)
    .join(' ');

  const menuButtonClassName = [
    'home-menu-button',
    mobileMenuOpen ? 'home-menu-button-open' : '',
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className="home-page">
      <header className={headerClassName}>
        <nav className="home-container home-nav" aria-label="Main navigation">
          <a className="home-logo" href="#home" onClick={closeMobileMenu}>
            PULSE<span>UP</span>
          </a>

          <div id="home-navigation-menu" className={navigationClassName}>
            <a href="#home" onClick={closeMobileMenu}>
              Home
            </a>

            <a href="#about" onClick={closeMobileMenu}>
              About us
            </a>

            <a href="#services" onClick={closeMobileMenu}>
              Services
            </a>

            <a href="#how-it-works" onClick={closeMobileMenu}>
              How it works
            </a>

            <a href="#contact" onClick={closeMobileMenu}>
              Contact
            </a>

            <div className="home-mobile-actions">
              <Link
                className="home-button home-button-outline"
                to="/login"
                onClick={closeMobileMenu}
              >
                Student sign in
              </Link>

              <Link
                className="home-button"
                to="/register"
                onClick={closeMobileMenu}
              >
                Create account
              </Link>
            </div>
          </div>

          <div className="home-nav-actions">
            <Link className="home-login-link" to="/login">
              Sign in
            </Link>

            <Link className="home-button home-button-small" to="/register">
              Create account
            </Link>
          </div>

          <button
            className={menuButtonClassName}
            type="button"
            aria-label={
              mobileMenuOpen ? 'Close navigation menu' : 'Open navigation menu'
            }
            aria-expanded={mobileMenuOpen}
            aria-controls="home-navigation-menu"
            onClick={() => {
              setMobileMenuOpen((currentState) => !currentState);
            }}
          >
            <span />
            <span />
            <span />
          </button>
        </nav>
      </header>

      <main>
        <section className="home-hero" id="home">
          <div className="home-pattern home-pattern-left" aria-hidden="true" />

          <div className="home-container home-hero-grid">
            <div className="home-hero-media">
              <div className="home-photo-frame">
                <img
                  src={heroImage}
                  alt="Students speaking with a campus healthcare professional"
                />

                <div className="home-online-card home-glass">
                  <span className="home-online-dot" />

                  <div>
                    <small>Clinic booking</small>
                    <strong>Available online</strong>
                  </div>
                </div>
              </div>
            </div>

            <div className="home-hero-copy">
              <span className="home-clinical-badge">
                CPUT clinical excellence
              </span>

              <h1>
                PulseUp
                <span>Campus Health Clinic</span>
              </h1>

              <p className="home-hero-slogan">
                Campus healthcare, without the uncertainty.
              </p>

              <p className="home-hero-text">
                A simpler student clinic experience for booking care, receiving
                appointment updates and following your digital queue.
              </p>

              <ul className="home-hero-points">
                <li>
                  <span>✓</span>
                  Book in under 60 seconds from your phone.
                </li>

                <li>
                  <span>✓</span>
                  Receive live updates on your appointment status.
                </li>

                <li>
                  <span>✓</span>
                  Focus on your studies while PulseUp handles the queue.
                </li>
              </ul>

              <div className="home-hero-metrics" aria-label="PulseUp benefits">
                <article>
                  <strong>60 sec</strong>
                  <span>Simple booking</span>
                </article>

                <article>
                  <strong>Live</strong>
                  <span>Status updates</span>
                </article>

                <article>
                  <strong>Secure</strong>
                  <span>Student access</span>
                </article>
              </div>

              <div className="home-hero-actions">
                <Link className="home-button" to="/register">
                  Book a clinic visit
                  <span>→</span>
                </Link>

                <Link className="home-button home-button-outline" to="/login">
                  Student sign in
                </Link>
              </div>
            </div>
          </div>
        </section>

        <section className="home-stat-strip">
          <div className="home-container home-stats">
            <article>
              <strong>5</strong>
              <span>Essential health services</span>
            </article>

            <article>
              <strong>One</strong>
              <span>Secure booking platform</span>
            </article>

            <article>
              <strong>Live</strong>
              <span>Digital queue information</span>
            </article>

            <article>
              <strong>Student-first</strong>
              <span>Designed around campus life</span>
            </article>
          </div>
        </section>

        <section className="home-section home-about" id="about">
          <div className="home-pattern home-pattern-right" aria-hidden="true" />

          <div className="home-container">
            <div className="home-heading home-heading-centred">
              <span>About PulseUp</span>

              <h2>Care that respects your time and privacy.</h2>

              <p>
                PulseUp connects CPUT students with campus health services
                through a simple, informed and confidential appointment
                experience.
              </p>
            </div>

            <div className="home-values">
              {values.map((value) => (
                <article
                  className="home-value-card home-glass"
                  key={value.title}
                >
                  <span className="home-value-icon">{value.icon}</span>

                  <h3>{value.title}</h3>
                  <p>{value.text}</p>
                </article>
              ))}
            </div>

            <div className="home-purpose">
              <article>
                <span className="home-purpose-number">01</span>

                <div>
                  <small>Our vision</small>

                  <h3>A calmer campus healthcare experience.</h3>

                  <p>
                    To make essential student healthcare easier to discover,
                    book and follow across the CPUT community.
                  </p>
                </div>
              </article>

              <article className="home-purpose-dark">
                <span className="home-purpose-number">02</span>

                <div>
                  <small>Our mission</small>

                  <h3>Give every student clarity before their visit.</h3>

                  <p>
                    To provide accessible booking, meaningful updates and a
                    transparent digital queue for campus clinic visits.
                  </p>
                </div>
              </article>
            </div>
          </div>
        </section>

        <section className="home-section home-services" id="services">
          <div className="home-container">
            <div className="home-heading home-heading-light">
              <span>Student health services</span>
              <h2>Choose the care you need.</h2>

              <p>
                Five essential clinic services available through one
                student-focused booking journey.
              </p>
            </div>

            <div className="home-service-grid">
              {services.map((service) => (
                <article key={service.title}>
                  <span className="home-service-icon">{service.icon}</span>

                  <h3>{service.title}</h3>
                  <p>{service.text}</p>
                </article>
              ))}
            </div>

            <div className="home-service-action">
              <p>
                Already registered? Sign in to see the latest appointment
                availability.
              </p>

              <Link className="home-button home-button-orange" to="/login">
                View appointments
                <span>→</span>
              </Link>
            </div>
          </div>
        </section>

        <section className="home-section home-process" id="how-it-works">
          <div className="home-container home-process-grid">
            <div>
              <div className="home-heading">
                <span>How PulseUp works</span>
                <h2>Know what comes next.</h2>

                <p>
                  A short booking journey that gives you useful information
                  before you enter the clinic.
                </p>
              </div>

              <div className="home-steps">
                {steps.map((step) => (
                  <article key={step.number}>
                    <span>{step.number}</span>

                    <div>
                      <h3>{step.title}</h3>
                      <p>{step.text}</p>
                    </div>
                  </article>
                ))}
              </div>
            </div>

            <div className="home-queue-card">
              <div className="home-queue-pattern" aria-hidden="true" />

              <header>
                <div>
                  <small>Live digital queue</small>
                  <h3>General Consultation</h3>
                </div>

                <span className="home-live-label">
                  <i />
                  Live now
                </span>
              </header>

              <div className="home-queue-body">
                <div className="home-queue-number">
                  <strong>#4</strong>
                  <span>in queue</span>
                </div>

                <dl>
                  <div>
                    <dt>Assisting clinician</dt>
                    <dd>Assigned after confirmation</dd>
                  </div>

                  <div>
                    <dt>Estimated wait</dt>
                    <dd>18 minutes</dd>
                  </div>

                  <div>
                    <dt>Clinic room</dt>
                    <dd>Unit A1</dd>
                  </div>

                  <div>
                    <dt>Status</dt>
                    <dd>Confirmed</dd>
                  </div>
                </dl>
              </div>

              <p className="home-queue-footnote">
                Your live appointment information appears after you sign in.
              </p>
            </div>
          </div>
        </section>

        <section className="home-emergency">
          <div className="home-container home-emergency-card">
            <div>
              <span>Urgent support</span>
              <h2>Medical emergency?</h2>

              <p>
                Do not wait for an online appointment. Contact campus protection
                or go directly to the nearest emergency facility.
              </p>
            </div>

            <div className="home-emergency-contacts">
              <article>
                <small>District Six Campus Protection</small>

                <a href="tel:+27214603122">021 460 3122</a>

                <span>Alternative: 021 460 3631</span>
              </article>

              <article>
                <small>Bellville Campus Protection</small>

                <a href="tel:+27219596301">021 959 6301</a>

                <span>Alternative: 021 959 6550</span>
              </article>
            </div>
          </div>
        </section>

        <section className="home-section home-contact" id="contact">
          <div
            className="home-pattern home-contact-pattern"
            aria-hidden="true"
          />

          <div className="home-container home-contact-grid">
            <div className="home-heading">
              <span>Contact us</span>
              <h2>Speak to your campus clinic.</h2>

              <p>
                For clinic-specific questions, use the contact information for
                the campus you attend.
              </p>

              <div className="home-notice home-glass">
                <strong>!</strong>

                <p>
                  PulseUp manages appointments. It does not replace professional
                  medical advice or emergency care.
                </p>
              </div>
            </div>

            <div className="home-contact-cards">
              <article className="home-glass">
                <span>Bellville Campus</span>
                <h3>Campus Health Clinic</h3>

                <p>New Library Extension, Ground Floor</p>

                <a href="tel:+27219596403">021 959 6403</a>

                <small>Consultation hours: 08:00–16:30</small>
              </article>

              <article className="home-glass">
                <span>District Six Campus</span>
                <h3>Campus Health Clinic</h3>

                <p>Contact the clinic before visiting</p>

                <a href="tel:+27214603405">021 460 3405</a>

                <small>Consultation hours: 08:00–16:30</small>
              </article>
            </div>
          </div>
        </section>
      </main>

      <footer className="home-footer">
        <div className="home-container home-footer-grid">
          <div>
            <a className="home-logo home-logo-light" href="#home">
              PULSE<span>UP</span>
            </a>

            <p>
              Student-focused appointment booking and digital queue information
              for CPUT campus healthcare.
            </p>
          </div>

          <div className="home-footer-links">
            <strong>Explore</strong>
            <a href="#about">About us</a>
            <a href="#services">Student services</a>
            <a href="#how-it-works">How it works</a>
            <a href="#contact">Contact us</a>
          </div>

          <div className="home-footer-links">
            <strong>Student access</strong>
            <Link to="/login">Sign in</Link>
            <Link to="/register">Create account</Link>
          </div>

          <div className="home-footer-alert">
            <strong>Need urgent help?</strong>

            <p>
              Online booking is not intended for life-threatening emergencies.
            </p>
          </div>
        </div>

        <div className="home-container home-footer-bottom">
          <span>© 2026 PulseUp. Student project.</span>

          <a
            href="https://www.cput.ac.za/student/support-services/dsa/campus-health-clinics"
            target="_blank"
            rel="noreferrer"
          >
            Official CPUT Campus Health information
          </a>
        </div>
      </footer>
    </div>
  );
}

export default HomePage;
