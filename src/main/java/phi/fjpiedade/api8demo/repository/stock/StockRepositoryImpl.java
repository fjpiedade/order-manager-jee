package phi.fjpiedade.api8demo.repository.stock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import phi.fjpiedade.api8demo.domain.stock.StockModel;

import java.util.List;

@ApplicationScoped
public class StockRepositoryImpl implements StockRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Override
    public List<StockModel> findAll() {
        return em.createQuery("SELECT o FROM StockModel o", StockModel.class).getResultList();
    }

    @Override
    public StockModel findById(Long id) {
        return em.find(StockModel.class, id);
    }

    @Override
    public StockModel findByItemId(Long itemId) {
//        return em.createQuery("SELECT s FROM StockModel s WHERE s.item.id = :itemId", StockModel.class)
//                .setParameter("itemId", itemId)
//                .getSingleResult();
        List<StockModel> stocks = em.createQuery("SELECT s FROM StockModel s WHERE s.item.id = :itemId", StockModel.class)
                .setParameter("itemId", itemId)
                .getResultList();
        if (stocks.isEmpty()) {
            // Handle the case where no result is found
            return null;
        }
        return stocks.get(0);  // Or handle multiple results if possible
    }


    @Override
    @Transactional
    public void save(StockModel stock) {
        em.persist(stock);
        em.flush();
    }

    @Override
    @Transactional
    public void update(StockModel stock) {
        em.merge(stock);
        em.flush();
    }

    @Override
    @Transactional
    public void delete(StockModel stock) {
        if (em.contains(stock)) {
            em.remove(stock);
        } else {
            em.remove(em.merge(stock));
        }
    }
}
