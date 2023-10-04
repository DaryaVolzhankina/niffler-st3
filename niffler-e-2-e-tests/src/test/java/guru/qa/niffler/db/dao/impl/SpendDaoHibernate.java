package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.SpendDao;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;

import java.util.List;
import java.util.UUID;

public class SpendDaoHibernate extends JpaService implements SpendDao {

    public SpendDaoHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDb.SPEND).createEntityManager());
    }

    @Override
    public void deleteCategoryInSpend(CategoryEntity category) {
        remove(category);
    }

    @Override
    public CategoryEntity getCategoryByUUID(UUID uuid) {
        return em.createQuery("select u from CategoryEntity u where u.id=:id", CategoryEntity.class)
                .setParameter("id", uuid)
                .getSingleResult();
    }

    @Override
    public CategoryEntity getCategoryByUsernameCategoryName(String username, String categoryName) {
        return em.createQuery("select u from CategoryEntity u where u.username=:username and u.category=:category", CategoryEntity.class)
                .setParameter("username", username)
                .setParameter("category", categoryName)
                .getSingleResult();
    }

    @Override
    public void deleteAllSpends(List<SpendEntity> list) {
        list.forEach(this::remove);
    }

    @Override
    public List<SpendEntity> getSpendsByCategory(CategoryEntity categoryEntity) {
        return em.createQuery("select u from SpendEntity u where u.category=:category", SpendEntity.class)
                .setParameter("category", categoryEntity)
                .getResultList();
    }
}
