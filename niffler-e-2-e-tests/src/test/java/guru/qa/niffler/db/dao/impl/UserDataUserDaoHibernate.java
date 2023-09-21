package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public class UserDataUserDaoHibernate extends JpaService implements UserDataUserDAO {

    public UserDataUserDaoHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDb.USERDATA).createEntityManager());
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public void deleteUserInUserData(UserDataUserEntity user) {
        remove(user);
    }

    @Override
    public UserDataUserEntity getUserFromUserDataByUsername(String username) {
        return em.createQuery("select u from UserDataUserEntity u where u.username=:username", UserDataUserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public void updateUserInUserData(UserDataUserEntity user) {
        merge(user);
    }
}
