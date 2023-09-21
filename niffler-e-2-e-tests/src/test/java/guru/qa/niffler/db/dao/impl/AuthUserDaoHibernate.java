package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import java.util.UUID;

public class AuthUserDaoHibernate extends JpaService implements AuthUserDao {
    public AuthUserDaoHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDb.AUTH).createEntityManager());
    }

    @Override
    public int createUserInAuth(AuthUserEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        persist(user);
        return 0;
    }

    @Override 
    public void deleteUserByIdInAuth(AuthUserEntity user) {
        remove(user);
    }

    @Override
    public AuthUserEntity getUserFromAuthById(UUID userID) {
        return em.createQuery("select u from AuthUserEntity u where u.id=:userId", AuthUserEntity.class)
                .setParameter("id", userID)
                .getSingleResult();
    }

    @Override
    public void updateUserInAuth(AuthUserEntity user) {
        merge(user);
    }
}
