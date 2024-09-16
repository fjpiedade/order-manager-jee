package phi.fjpiedade.apiorder.repository.order;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import phi.fjpiedade.apiorder.domain.order.OrderModel;

import java.util.List;

//@ApplicationScoped
@Stateless
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
//    @Transactional
    public OrderModel save(OrderModel order) {
        em.detach(order.getItem());
        em.detach(order.getUser());
        em.persist(order);
        em.flush();
        return order;
    }

    @Override
    public OrderModel update(OrderModel order) {
        em.detach(order.getItem());
        em.detach(order.getUser());
        em.merge(order);
        em.flush();
        return order;
    }

    @Override
//    @Transactional
    public void delete(OrderModel order) {
        if (em.contains(order)) {
            em.remove(order);
        } else {
            em.remove(em.merge(order));
        }
    }

    public List<OrderModel> findIncompleteOrdersForItem(Long itemId) {
        return em.createQuery("SELECT orderIncompleted  FROM OrderModel orderIncompleted WHERE orderIncompleted.item.id = :itemId AND orderIncompleted.quantity>orderIncompleted.fulfilledQuantity", OrderModel.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }

    @Override
    public List<OrderModel> findCompletedOrders() {
        return em.createQuery(
                "SELECT orders FROM OrderModel orders WHERE orders.quantity = orders.fulfilledQuantity",
                OrderModel.class
        ).getResultList();
    }
}
