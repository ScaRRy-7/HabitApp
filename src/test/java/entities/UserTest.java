package entities;

import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.services.enums.HabitFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john.doe@example.com", "password123");
    }

    @Test
    void testGetName() {
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    @Test
    void testSetName() {
        user.setName("Jane Doe");
        assertThat(user.getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testGetEmail() {
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testSetEmail() {
        user.setEmail("jane.doe@example.com");
        assertThat(user.getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void testGetPassword() {
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void testSetPassword() {
        user.setPassword("newpassword");
        assertThat(user.getPassword()).isEqualTo("newpassword");
    }



    @Test
    void testSetBlocked() {
        user.setBlocked();
        assertThat(user.isBlocked()).isTrue();
    }

    @Test
    void testIsBlockedInitiallyFalse() {
        assertThat(user.isBlocked()).isFalse();
    }
}