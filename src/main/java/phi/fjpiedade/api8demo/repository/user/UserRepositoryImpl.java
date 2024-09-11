package phi.fjpiedade.api8demo.repository.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import phi.fjpiedade.api8demo.domain.user.UserModel;

import java.util.List;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Override
    public List<UserModel> findAll() {
        return em.createQuery("SELECT o FROM UserModel o", UserModel.class).getResultList();
    }

    @Override
    public UserModel findById(Long id) {
        return em.find(UserModel.class, id);
    }

    @Override
    public UserModel findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM UserModel u WHERE u.email = :email", UserModel.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;  // No user found with this email
        }
    }

    @Override
    @Transactional
    public void save(UserModel user) {
        em.persist(user);
        em.flush();
    }

    @Override
    @Transactional
    public void update(UserModel user) {
        em.merge(user);
        em.flush();
    }

    @Override
    @Transactional
    public void delete(UserModel user) {
        if (em.contains(user)) {
            em.remove(user);
        } else {
            em.remove(em.merge(user));
        }
    }
}
