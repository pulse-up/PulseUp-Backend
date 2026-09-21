package za.ac.cput.pulseup2026.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingListener {
    @EventListener
    public void handleAppointmentCreated(
            AppointmentCreatedEvent event) {

        System.out.println("===== LOGGING LISTENER =====");
//Will come back and check if this corresponds with the Appointment domain 
        System.out.println("New appointment received: " + event.getAppointmentId());
    }
}
