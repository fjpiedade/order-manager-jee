package phi.fjpiedade.api8demo.repository.user;

import phi.fjpiedade.api8demo.domain.user.UserModel;

import java.util.List;

public interface UserRepository {
    List<UserModel> findAll();
    UserModel findById(Long id);
    UserModel findByEmail(String email);
    void save(UserModel user);
    void update(UserModel user);
    void delete(UserModel user);
}
