package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.entity.UserDataUserEntity;
import guru.qa.niffler.db.model.entity.AuthUserEntity;

public interface UserDataUserDAO {

    int createUserInUserData(AuthUserEntity user);

    void deleteUserByUsernameInUserData(String username);
    UserDataUserEntity getUserFromUserDataByUsername(String username);
    void updateUserInUserData(UserDataUserEntity user);
}
