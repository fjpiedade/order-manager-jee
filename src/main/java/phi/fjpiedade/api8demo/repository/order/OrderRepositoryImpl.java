package phi.fjpiedade.api8demo.repository.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import phi.fjpiedade.api8demo.domain.order.OrderModel;

import java.util.List;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Override
    public List<OrderModel> findAll() {
        return em.createQuery("SELECT o FROM OrderModel o", OrderModel.class).getResultList();
    }

    @Override
    public OrderModel findById(Long id) {
        return em.find(OrderModel.class, id);
    }

    @Override
    @Transactional
    public void save(OrderModel order) {
        if (order.getId() == null) {
            em.persist(order);
        } else {
            em.merge(order);
        }
        em.flush();
    }

    @Override
    @Transactional
    public void delete(OrderModel order) {
        if (em.contains(order)) {
            em.remove(order);
        } else {
            em.remove(em.merge(order));
        }
    }
}
