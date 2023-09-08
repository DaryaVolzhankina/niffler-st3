package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public class AuthUserDaoHibernate implements AuthUserDao{
    @Override
    public int createUser(UserEntity user) {
        return 0;
    }

    @Override
    public void deleteUserByIdInAuth(UUID userID) {

    }

    @Override
    public UserEntity getUserFromAuthById(UUID userID) {
        return null;
    }

    @Override
    public void updateUserInAuth(UserEntity user) {

    }
}
