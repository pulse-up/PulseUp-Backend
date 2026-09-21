package za.ac.cput.pulseup2026.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingListener {
    @EventListener
    public void handleCourseCreated(
            CourseCreatedEvent event) {

        System.out.println("===== LOGGING LISTENER =====");

        System.out.println("New appointment received: " + event.getAppointment().getCode());
    }
}
