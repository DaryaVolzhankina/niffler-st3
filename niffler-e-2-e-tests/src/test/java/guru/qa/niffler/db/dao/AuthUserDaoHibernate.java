package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.entity.AuthUserEntity;

import java.util.UUID;

public class AuthUserDaoHibernate implements AuthUserDao{
    @Override
    public int createUserInAuth(AuthUserEntity user) {
        return 0;
    }

    @Override
    public void deleteUserByIdInAuth(UUID userID) {

    }

    @Override
    public AuthUserEntity getUserFromAuthById(UUID userID) {
        return null;
    }

    @Override
    public void updateUserInAuth(AuthUserEntity user) {

    }
}
