package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserDataUserDAO {

    int createUserInUserData(UserDataUserEntity user);

    void deleteUserInUserData(UserDataUserEntity user);
    UserDataUserEntity getUserFromUserDataByUsername(String username);
    void updateUserInUserData(UserDataUserEntity user);
}
