package phi.fjpiedade.apiorder.repository.user;

import phi.fjpiedade.apiorder.domain.user.UserModel;

import java.util.List;

public interface UserRepository {
    List<UserModel> findAll();
    UserModel findById(Long id);
    UserModel findByEmail(String email);
    UserModel save(UserModel user);
    UserModel update(UserModel user);
    void delete(UserModel user);
}
