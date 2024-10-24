package adminstration;

import habitapp.services.adminstration.AdminBlocator;
import habitapp.entities.User;
import habitapp.services.in.Reader;
import habitapp.services.out.AdminUserChangerWriter;
import habitapp.repositories.HabitappRepository;
import habitapp.repositories.HabitappDAO;
import habitapp.services.validate.AdminRemoverValidator;
import habitapp.services.wait.Waiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class AdminBlocatorTest {

    @Mock
    private HabitappDAO habitappDAO;

    @Mock
    private Reader reader;

    @Mock
    private AdminUserChangerWriter writer;

    @Mock
    private Waiter waiter;

    @Mock
    private AdminRemoverValidator validator;

    @Mock
    private HabitappRepository habitappRepository;

    @InjectMocks
    private AdminBlocator adminBlocator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void blockUser_shouldCallWriterMethodToPromptUserEmailAndValidateIt_whenUserListIsNotEmpty() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User("User 1", "user1@example.com", "password1"));
        users.add(new User("User 2", "user2@example.com", "password2"));
        when(habitappDAO.getUsers()).thenReturn(users);
        when(reader.read()).thenReturn("user1@example.com");
        when(validator.isValid("user1@example.com")).thenReturn(true);

        // Act
        adminBlocator.blockUser();

        // Assert
        verify(writer, times(1)).writeUsersToChoose();
        verify(reader, times(1)).read();
        verify(validator, times(1)).isValid("user1@example.com");
        verify(habitappRepository, times(1)).blockUser("user1@example.com");
        verify(writer, times(1)).writeUserBlocked();
    }

}