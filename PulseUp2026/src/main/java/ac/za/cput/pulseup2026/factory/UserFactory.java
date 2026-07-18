package ac.za.cput.pulseup2026.factory;

public class UserFactory {
import ac.za.cput.pulseup2026.domain.User;
public class UserFactory {

    public static User createUser(
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String phoneNumber
    ) {

        return User.builder()
                .email(email)
                .passwordHash(passwordHash)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .isActive(true)
                .build();
    }
}
