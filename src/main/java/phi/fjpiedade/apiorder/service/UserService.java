package phi.fjpiedade.apiorder.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.repository.user.UserRepository;

import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class UserService {
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService() {
    }

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        logger.info("Fetching user(s)");
        return userRepository.findAll();
    }

    public UserModel createUser(UserModel user) {
        UserModel userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            logger.error("Email of User already Exist!");
            return null;
            //throw new IllegalArgumentException("Email of User already Exist!");
        }

        user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(user);
        logger.info("Saved new User: {}", user.getId());
        return user;
    }

    public UserModel getUserById(Long id) {
        logger.info("Fetching User By ID: {}", id);
        return userRepository.findById(id);
    }

    public UserModel updateUser(Long id, UserModel updatedUser) {
        UserModel user = userRepository.findById(id);
        if (user == null) {
            logger.error("User not Found to update");
            //throw new IllegalArgumentException("User not Found!");
            return null;
        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.update(user);
        logger.info("Updating User: {}", user.getId());
        return user;
    }

    public boolean deleteUser(Long id) {
        UserModel user = userRepository.findById(id);
        if (user == null) {
            logger.error("User not Found to delete!");
            return false;
        }
        userRepository.delete(user);
        logger.info("User deleted!");
        return true;
    }
}
