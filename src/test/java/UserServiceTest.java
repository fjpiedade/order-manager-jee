import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.repository.user.UserRepository;
import phi.fjpiedade.apiorder.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new UserModel();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("testuser@example.com");
    }

    @Test
    @Order(3)
    public void testGetAllUsers() {
        List<UserModel> users = Collections.singletonList(new UserModel());
        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userService.getAllUsers();
        assertEquals(users, result);

    }

    @Test
    @Order(1)
    void testCreateUserSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserModel createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testuser@example.com", createdUser.getEmail());
        assertEquals("Test User", createdUser.getName());
        assertNotNull(createdUser.getCreatedAt());
        assertNotNull(createdUser.getUpdatedAt());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @Order(4)
    void testCreateUserWithExistingEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserModel result = userService.createUser(user);

        assertNull(result);  // Since the method returns null if the email already exists
        verify(userRepository, times(0)).save(any(UserModel.class));  // Ensure save is never called
    }

    @Test
    @Order(2)
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(user);

        UserModel foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("Test User", foundUser.getName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @Order(5)
    public void testUpdateUserSuccess() {
        when(userRepository.findById(1L)).thenReturn(user);

        user.setName("Updated User");
        user.setEmail("updateduser@example.com");

        when(userRepository.update(any(UserModel.class))).thenReturn(user);

        UserModel updatedUser = userService.updateUser(user.getId(), user);

        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updateduser@example.com", updatedUser.getEmail());

        verify(userRepository, times(1)).update(user);
    }

    @Test
    @Order(6)
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(user);
        doNothing().when(userRepository).delete(user);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }
}
