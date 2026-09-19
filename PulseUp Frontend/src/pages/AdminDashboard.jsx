import {
    useCallback,
    useEffect,
    useState,
} from "react";

import {
    Link,
    useNavigate,
} from "react-router-dom";

import api from "../api/api";
import ResponsiveHeader from "../components/ResponsiveHeader";

function AdminDashboard() {
    const navigate = useNavigate();

    const [user] = useState(() => {
        const storedUser =
            localStorage.getItem("pulseupUser");

        if (!storedUser) {
            return {};
        }

        try {
            return JSON.parse(storedUser);
        } catch (error) {
            console.error(
                "Could not read admin user:",
                error,
            );

            localStorage.removeItem("pulseupUser");
            return {};
        }
    });

    const userRole = String(
        user.role || "",
    ).toUpperCase();

    const [students, setStudents] = useState([]);
    const [staff, setStaff] = useState([]);
    const [admins, setAdmins] = useState([]);
    const [appointments, setAppointments] =
        useState([]);
    const [timeSlots, setTimeSlots] = useState([]);

    const [activeSection, setActiveSection] =
        useState("users");

    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const [workingItem, setWorkingItem] =
        useState("");

    const loadDashboardData = useCallback(
        async () => {
            try {
                setLoading(true);
                setMessage("");

                const [
                    studentsResponse,
                    staffResponse,
                    adminsResponse,
                    appointmentsResponse,
                    slotsResponse,
                ] = await Promise.all([
                    api.get("/students"),
                    api.get("/staff"),
                    api.get("/admins"),
                    api.get("/appointments"),
                    api.get("/time-slots"),
                ]);

                setStudents(
                    Array.isArray(studentsResponse.data)
                        ? studentsResponse.data
                        : [],
                );

                setStaff(
                    Array.isArray(staffResponse.data)
                        ? staffResponse.data
                        : [],
                );

                setAdmins(
                    Array.isArray(adminsResponse.data)
                        ? adminsResponse.data
                        : [],
                );

                setAppointments(
                    Array.isArray(appointmentsResponse.data)
                        ? appointmentsResponse.data
                        : [],
                );

                setTimeSlots(
                    Array.isArray(slotsResponse.data)
                        ? slotsResponse.data
                        : [],
                );
            } catch (error) {
                console.error(
                    "Admin dashboard loading failed:",
                    error,
                );

                if (error.response?.status === 403) {
                    setMessage(
                        "You do not have permission to view the administration records.",
                    );
                } else {
                    setMessage(
                        "Could not load the administration records.",
                    );
                }
            } finally {
                setLoading(false);
            }
        },
        [],
    );

    useEffect(() => {
        if (
            !user.userId ||
            !user.token ||
            userRole !== "ADMIN"
        ) {
            navigate("/login", {
                replace: true,
            });

            return;
        }

        let cancelled = false;

        window.queueMicrotask(() => {
            if (cancelled) {
                return;
            }

            loadDashboardData().catch((error) => {
                console.error(
                    "Admin dashboard initialisation failed:",
                    error,
                );
            });
        });

        return () => {
            cancelled = true;
        };
    }, [
        navigate,
        user.userId,
        user.token,
        userRole,
        loadDashboardData,
    ]);

    async function handleDeleteUser(
        endpoint,
        userId,
        userName,
    ) {
        const confirmed = window.confirm(
            `Are you sure you want to remove ${userName}?`,
        );

        if (!confirmed) {
            return;
        }

        const itemKey = `${endpoint}-${userId}`;

        try {
            setWorkingItem(itemKey);
            setMessage("");

            await api.delete(`/${endpoint}/${userId}`);
            await loadDashboardData();

            setMessage(
                `${userName} was removed successfully.`,
            );
        } catch (error) {
            console.error(
                "User deletion failed:",
                error,
            );

            if (error.response?.status === 403) {
                setMessage(
                    "You do not have permission to remove this account.",
                );
            } else {
                setMessage(
                    `${userName} could not be removed. The account may still be linked to other records.`,
                );
            }
        } finally {
            setWorkingItem("");
        }
    }

    async function handleAppointmentStatus(
        appointmentId,
        status,
    ) {
        const itemKey =
            `appointment-${appointmentId}`;

        try {
            setWorkingItem(itemKey);
            setMessage("");

            await api.patch(
                `/appointments/${appointmentId}/status`,
                {},
                {
                    params: {
                        status,
                    },
                },
            );

            await loadDashboardData();

            setMessage(
                `The appointment was changed to ${status}.`,
            );
        } catch (error) {
            console.error(
                "Appointment status update failed:",
                error.response?.status,
                error.response?.data || error.message,
            );

            setMessage(
                "The appointment status could not be updated.",
            );
        } finally {
            setWorkingItem("");
        }
    }

    async function handleDeleteAppointment(
        appointmentId,
    ) {
        const confirmed = window.confirm(
            "Are you sure you want to permanently remove this appointment?",
        );

        if (!confirmed) {
            return;
        }

        const itemKey =
            `appointment-${appointmentId}`;

        try {
            setWorkingItem(itemKey);
            setMessage("");

            await api.delete(
                `/appointments/${appointmentId}`,
            );

            await loadDashboardData();

            setMessage(
                "The appointment was removed.",
            );
        } catch (error) {
            console.error(
                "Appointment deletion failed:",
                error,
            );

            setMessage(
                "The appointment could not be removed.",
            );
        } finally {
            setWorkingItem("");
        }
    }

    async function handleDeleteSlot(slotId) {
        const confirmed = window.confirm(
            "Are you sure you want to delete this available time slot?",
        );

        if (!confirmed) {
            return;
        }

        const itemKey = `slot-${slotId}`;

        try {
            setWorkingItem(itemKey);
            setMessage("");

            await api.delete(`/time-slots/${slotId}`);
            await loadDashboardData();

            setMessage(
                "The time slot was deleted.",
            );
        } catch (error) {
            console.error(
                "Time-slot deletion failed:",
                error,
            );

            setMessage(
                "The time slot could not be deleted.",
            );
        } finally {
            setWorkingItem("");
        }
    }

    function handleLogout() {
        localStorage.removeItem("pulseupUser");

        navigate("/login", {
            replace: true,
        });
    }

    function renderUsers() {
        return (
            <div className="admin-record-groups">
                <section className="admin-record-group">
                    <h3>
                        Students ({students.length})
                    </h3>

                    {students.length === 0 ? (
                        <p className="empty-message">
                            No student accounts found.
                        </p>
                    ) : (
                        <div className="admin-record-list">
                            {students.map((student) => {
                                const itemKey =
                                    `students-${student.userId}`;

                                return (
                                    <article
                                        className="admin-record-card"
                                        key={student.userId}
                                    >
                                        <div>
                                            <strong>
                                                {student.firstName}{" "}
                                                {student.lastName}
                                            </strong>

                                            <span>
                        {student.studentNumber}
                      </span>

                                            <span>{student.email}</span>
                                            <span>{student.course}</span>
                                        </div>

                                        <button
                                            type="button"
                                            className="danger-button"
                                            disabled={
                                                workingItem === itemKey
                                            }
                                            onClick={() =>
                                                handleDeleteUser(
                                                    "students",
                                                    student.userId,
                                                    `${student.firstName} ${student.lastName}`,
                                                )
                                            }
                                        >
                                            {workingItem === itemKey
                                                ? "Removing..."
                                                : "Remove"}
                                        </button>
                                    </article>
                                );
                            })}
                        </div>
                    )}
                </section>

                <section className="admin-record-group">
                    <h3>
                        Staff ({staff.length})
                    </h3>

                    {staff.length === 0 ? (
                        <p className="empty-message">
                            No staff accounts found.
                        </p>
                    ) : (
                        <div className="admin-record-list">
                            {staff.map((member) => {
                                const itemKey =
                                    `staff-${member.userId}`;

                                return (
                                    <article
                                        className="admin-record-card"
                                        key={member.userId}
                                    >
                                        <div>
                                            <strong>
                                                {member.firstName}{" "}
                                                {member.lastName}
                                            </strong>

                                            <span>
                        {member.staffNumber}
                      </span>

                                            <span>{member.email}</span>

                                            <span>
                        {member.position}
                                                {" · "}
                                                {member.department}
                      </span>
                                        </div>

                                        <button
                                            type="button"
                                            className="danger-button"
                                            disabled={
                                                workingItem === itemKey
                                            }
                                            onClick={() =>
                                                handleDeleteUser(
                                                    "staff",
                                                    member.userId,
                                                    `${member.firstName} ${member.lastName}`,
                                                )
                                            }
                                        >
                                            {workingItem === itemKey
                                                ? "Removing..."
                                                : "Remove"}
                                        </button>
                                    </article>
                                );
                            })}
                        </div>
                    )}
                </section>

                <section className="admin-record-group">
                    <h3>
                        Administrators ({admins.length})
                    </h3>

                    {admins.length === 0 ? (
                        <p className="empty-message">
                            No administrator accounts found.
                        </p>
                    ) : (
                        <div className="admin-record-list">
                            {admins.map((admin) => {
                                const itemKey =
                                    `admins-${admin.userId}`;

                                return (
                                    <article
                                        className="admin-record-card"
                                        key={admin.userId}
                                    >
                                        <div>
                                            <strong>
                                                {admin.firstName}{" "}
                                                {admin.lastName}
                                            </strong>

                                            <span>
                        {admin.adminNumber}
                      </span>

                                            <span>{admin.email}</span>

                                            <span>
                        {admin.department}
                      </span>
                                        </div>

                                        {admin.userId !== user.userId && (
                                            <button
                                                type="button"
                                                className="danger-button"
                                                disabled={
                                                    workingItem === itemKey
                                                }
                                                onClick={() =>
                                                    handleDeleteUser(
                                                        "admins",
                                                        admin.userId,
                                                        `${admin.firstName} ${admin.lastName}`,
                                                    )
                                                }
                                            >
                                                {workingItem === itemKey
                                                    ? "Removing..."
                                                    : "Remove"}
                                            </button>
                                        )}
                                    </article>
                                );
                            })}
                        </div>
                    )}
                </section>
            </div>
        );
    }

    function renderAppointments() {
        if (appointments.length === 0) {
            return (
                <p className="empty-message">
                    No appointments found.
                </p>
            );
        }

        return (
            <div className="admin-record-list">
                {appointments.map((appointment) => {
                    const status =
                        appointment.status || "PENDING";

                    const student = appointment.student;
                    const slot = appointment.timeSlot;

                    const itemKey =
                        `appointment-${appointment.appointmentId}`;

                    return (
                        <article
                            className={
                                "admin-record-card " +
                                "appointment-admin-card"
                            }
                            key={appointment.appointmentId}
                        >
                            <div>
                                <strong>
                                    {appointment.appointmentType}
                                </strong>

                                <span>
                  {student?.firstName}{" "}
                                    {student?.lastName}
                </span>

                                <span>
                  {slot?.slotDate}
                                    {" · "}
                                    {slot?.startTime}
                                    {" – "}
                                    {slot?.endTime}
                </span>

                                <span
                                    className={
                                        `appointment-status ` +
                                        status.toLowerCase()
                                    }
                                >
                  {status}
                </span>
                            </div>

                            <div className="admin-card-actions">
                                {(status === "PENDING" ||
                                    status === "RESCHEDULED") && (
                                    <button
                                        type="button"
                                        className="primary-button"
                                        disabled={
                                            workingItem === itemKey
                                        }
                                        onClick={() =>
                                            handleAppointmentStatus(
                                                appointment.appointmentId,
                                                "CONFIRMED",
                                            )
                                        }
                                    >
                                        {workingItem === itemKey
                                            ? "Updating..."
                                            : "Confirm"}
                                    </button>
                                )}

                                {status === "CONFIRMED" && (
                                    <button
                                        type="button"
                                        className="complete-button"
                                        disabled={
                                            workingItem === itemKey
                                        }
                                        onClick={() =>
                                            handleAppointmentStatus(
                                                appointment.appointmentId,
                                                "COMPLETED",
                                            )
                                        }
                                    >
                                        {workingItem === itemKey
                                            ? "Updating..."
                                            : "Complete"}
                                    </button>
                                )}

                                {(status === "CANCELLED" ||
                                    status === "COMPLETED") && (
                                    <button
                                        type="button"
                                        className="danger-button"
                                        disabled={
                                            workingItem === itemKey
                                        }
                                        onClick={() =>
                                            handleDeleteAppointment(
                                                appointment.appointmentId,
                                            )
                                        }
                                    >
                                        {workingItem === itemKey
                                            ? "Removing..."
                                            : "Remove"}
                                    </button>
                                )}
                            </div>
                        </article>
                    );
                })}
            </div>
        );
    }

    function renderTimeSlots() {
        if (timeSlots.length === 0) {
            return (
                <p className="empty-message">
                    No time slots found.
                </p>
            );
        }

        return (
            <div className="admin-record-list">
                {timeSlots.map((slot) => {
                    const itemKey =
                        `slot-${slot.slotId}`;

                    return (
                        <article
                            className="admin-record-card"
                            key={slot.slotId}
                        >
                            <div>
                                <strong>{slot.slotDate}</strong>

                                <span>
                  {slot.startTime}
                                    {" – "}
                                    {slot.endTime}
                </span>

                                <span>
                  {slot.staff
                      ? `${slot.staff.firstName} ${slot.staff.lastName}`
                      : `Slot ID: ${slot.slotId}`}
                </span>

                                <span
                                    className={
                                        slot.available
                                            ? "slot-status available"
                                            : "slot-status booked"
                                    }
                                >
                  {slot.available
                      ? "Available"
                      : "Booked"}
                </span>
                            </div>

                            {slot.available && (
                                <button
                                    type="button"
                                    className="danger-button"
                                    disabled={
                                        workingItem === itemKey
                                    }
                                    onClick={() =>
                                        handleDeleteSlot(slot.slotId)
                                    }
                                >
                                    {workingItem === itemKey
                                        ? "Deleting..."
                                        : "Delete"}
                                </button>
                            )}
                        </article>
                    );
                })}
            </div>
        );
    }

    return (
        <main className="student-dashboard admin-dashboard">
            <ResponsiveHeader
                ariaLabel="Administrator dashboard navigation"
                identity={{
                    name: `${user.firstName || "Administrator"} ${user.lastName || ""}`.trim(),
                    detail: "Administrator",
                }}
                menuItems={[
                    { label: "Home", to: "/" },
                    {
                        label: "User records",
                        active: activeSection === "users",
                        onSelect: () => setActiveSection("users"),
                    },
                    {
                        label: "Appointments",
                        active: activeSection === "appointments",
                        onSelect: () => setActiveSection("appointments"),
                    },
                    {
                        label: "Time slots",
                        active: activeSection === "slots",
                        onSelect: () => setActiveSection("slots"),
                    },
                    {
                        label: "Create account",
                        to: "/admin/create-account",
                    },
                ]}
                onSignOut={handleLogout}
            />

            <section className="dashboard-introduction">
                <p className="eyebrow">
                    ADMIN DASHBOARD
                </p>

                <h1>PulseUp system management.</h1>

                <p>
                    Manage users, bookings, and clinic
                    availability.
                </p>
            </section>

            <section className="admin-account-action">
                <div>
                    <p className="eyebrow">
                        ACCOUNT MANAGEMENT
                    </p>

                    <h2>
                        Staff and administrator accounts
                    </h2>

                    <p className="muted-text">
                        Create secure accounts for healthcare staff
                        and system administrators.
                    </p>
                </div>

                <Link
                    className="admin-create-account-button"
                    to="/admin/create-account"
                >
                    Create account
                </Link>
            </section>

            <section className="admin-summary-grid">
                <article>
                    <span>Students</span>
                    <strong>{students.length}</strong>
                </article>

                <article>
                    <span>Staff</span>
                    <strong>{staff.length}</strong>
                </article>

                <article>
                    <span>Appointments</span>
                    <strong>{appointments.length}</strong>
                </article>

                <article>
                    <span>Time slots</span>
                    <strong>{timeSlots.length}</strong>
                </article>
            </section>

            {message && (
                <p className="dashboard-message">
                    {message}
                </p>
            )}

            <section className="admin-content">
                <nav className="admin-navigation">
                    <button
                        type="button"
                        className={
                            activeSection === "users"
                                ? "booking-tab active"
                                : "booking-tab"
                        }
                        onClick={() =>
                            setActiveSection("users")
                        }
                    >
                        User records
                    </button>

                    <button
                        type="button"
                        className={
                            activeSection === "appointments"
                                ? "booking-tab active"
                                : "booking-tab"
                        }
                        onClick={() =>
                            setActiveSection("appointments")
                        }
                    >
                        Appointments
                    </button>

                    <button
                        type="button"
                        className={
                            activeSection === "slots"
                                ? "booking-tab active"
                                : "booking-tab"
                        }
                        onClick={() =>
                            setActiveSection("slots")
                        }
                    >
                        Time slots
                    </button>
                </nav>

                <article className="dashboard-panel">
                    {loading ? (
                        <p>
                            Loading administration records...
                        </p>
                    ) : (
                        <>
                            {activeSection === "users" &&
                                renderUsers()}

                            {activeSection === "appointments" &&
                                renderAppointments()}

                            {activeSection === "slots" &&
                                renderTimeSlots()}
                        </>
                    )}
                </article>
            </section>
        </main>
    );
}

export default AdminDashboard;