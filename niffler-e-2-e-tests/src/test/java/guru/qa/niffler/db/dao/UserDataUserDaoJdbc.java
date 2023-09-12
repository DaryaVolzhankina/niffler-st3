package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.model.enums.CurrencyValues;
import guru.qa.niffler.db.model.entity.UserDataUserEntity;
import guru.qa.niffler.db.model.entity.AuthUserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataUserDaoJdbc implements UserDataUserDAO {
    private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDb.USERDATA);

    @Override
    public int createUserInUserData(AuthUserEntity user) {
        int createdRows = 0;
        try (Connection conn = userdataDs.getConnection()) {
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO users (username, currency) " +
                            "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

                usersPs.setString(1, user.getUsername());
                usersPs.setString(2, CurrencyValues.RUB.name());

                createdRows = usersPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return createdRows;
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        try (Connection conn = userdataDs.getConnection()) {
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {
                usersPs.setString(1, username);
                usersPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDataUserEntity getUserFromUserDataByUsername(String username) {
        UserDataUserEntity user = new UserDataUserEntity();
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM public.users WHERE username = ?")) {
            usersPs.setObject(1, username);
            ResultSet userResultSet = usersPs.executeQuery();

            if (userResultSet.next()) {
                user.setId((UUID) userResultSet.getObject("id"));
                user.setUsername(userResultSet.getString("username"));
                user.setCurrency(CurrencyValues.valueOf(userResultSet.getString("currency")));
                user.setFirstname(userResultSet.getString("firstname"));
                user.setSurname(userResultSet.getString("surname"));
                user.setPhoto(userResultSet.getBytes("photo"));
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void updateUserInUserData(UserDataUserEntity user) {
        int updatedRows = 0;
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("UPDATE users SET currency = ?, firstname = ?, " +
                     "surname = ?, photo = ? WHERE id = ?")) {
            usersPs.setObject(1, user.getCurrency().name());
            usersPs.setObject(2, user.getFirstname());
            usersPs.setObject(3, user.getSurname());
            usersPs.setObject(4, user.getPhoto());
            usersPs.setObject(5, user.getId());
            updatedRows = usersPs.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalArgumentException("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
