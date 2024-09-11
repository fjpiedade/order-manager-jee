package phi.fjpiedade.api8demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.api8demo.domain.user.UserModel;
import phi.fjpiedade.api8demo.repository.user.UserRepository;

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
        return userRepository.findAll();
    }

    public UserModel createUser(UserModel user) {
        UserModel userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            logger.error("Email of User already Exist!");
            throw new IllegalArgumentException("Email of User already Exist!");
        }

        user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(user);
        return user;
    }

    public UserModel getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel updateUser(Long id, UserModel updatedUser) {
        UserModel user = userRepository.findById(id);
        if (user != null) {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.update(user);
        }
        return user;
    }

    public boolean deleteUser(Long id) {
        UserModel user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
