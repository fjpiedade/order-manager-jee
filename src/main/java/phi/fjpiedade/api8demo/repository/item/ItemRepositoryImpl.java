package phi.fjpiedade.api8demo.repository.item;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import phi.fjpiedade.api8demo.domain.item.ItemModel;

import java.util.List;

@ApplicationScoped
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
    @Transactional
    public void save(ItemModel item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
        em.flush();
    }

    @Override
    @Transactional
    public void delete(ItemModel item) {
        if (em.contains(item)) {
            em.remove(item);
        } else {
            em.remove(em.merge(item));
        }
    }
}
