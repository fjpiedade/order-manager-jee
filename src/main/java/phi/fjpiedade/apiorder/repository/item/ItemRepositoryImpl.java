package phi.fjpiedade.apiorder.repository.item;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import phi.fjpiedade.apiorder.domain.item.ItemModel;

import java.util.List;

//@ApplicationScoped
@Stateless
public class ItemRepositoryImpl implements ItemRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Override
    public List<ItemModel> findAll() {
        return em.createQuery("SELECT o FROM ItemModel o", ItemModel.class).getResultList();
    }

    @Override
    public ItemModel findById(Long id) {
        return em.find(ItemModel.class, id);
    }

    @Override
//    @Transactional
    public ItemModel save(ItemModel item) {
        em.persist(item);
        em.flush();
        return item;
    }

    @Override
//    @Transactional
    public ItemModel update(ItemModel item) {
        em.merge(item);
        em.flush();
        return item;
    }

    @Override
//    @Transactional
    public void delete(ItemModel item) {
        if (em.contains(item)) {
            em.remove(item);
        } else {
            em.remove(em.merge(item));
        }
    }
}
