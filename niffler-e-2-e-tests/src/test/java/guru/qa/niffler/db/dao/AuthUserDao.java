package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.entity.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDao {
    PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    int createUserInAuth(AuthUserEntity user);
    void deleteUserByIdInAuth(UUID userID);
    AuthUserEntity getUserFromAuthById(UUID userID);
    void updateUserInAuth(AuthUserEntity user);
}
