package phi.fjpiedade.api8demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import phi.fjpiedade.api8demo.domain.user.UserModel;
import phi.fjpiedade.api8demo.repository.user.UserRepository;

import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class UserService {
    private UserRepository userRepository;

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
            userRepository.save(user);
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
