package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataUserEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    void deleteUserByUsernameInUserData(String username);
    UserDataUserEntity getUserFromUserDataByUsername(String username);
    void updateUserInUserData(UserDataUserEntity user);
}
