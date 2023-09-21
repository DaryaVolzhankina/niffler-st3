package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDb.AUTH);

    @Override
    public int createUserInAuth(AuthUserEntity user) {
        int createdRows = 0;
        try (Connection conn = authDs.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                usersPs.setString(1, user.getUsername());
                usersPs.setString(2, pe.encode(user.getPassword()));
                usersPs.setBoolean(3, user.getEnabled());
                usersPs.setBoolean(4, user.getAccountNonExpired());
                usersPs.setBoolean(5, user.getAccountNonLocked());
                usersPs.setBoolean(6, user.getCredentialsNonExpired());

                createdRows = usersPs.executeUpdate();
                UUID generatedUserId;

                try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t obtain id from given ResultSet");
                    }
                }

                for (Authority authority : Authority.values()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, authority.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
                user.setId(generatedUserId);
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return createdRows;
    }

    @Override
    public void deleteUserByIdInAuth(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement authorityPs = conn.prepareStatement(
                    "DELETE FROM authorities WHERE user_id=?");
                 PreparedStatement usersPs = conn.prepareStatement(
                         "DELETE FROM users WHERE id=?")) {
                authorityPs.setObject(1, user.getId());
                usersPs.setObject(1, user.getId());
                authorityPs.executeUpdate();
                usersPs.executeUpdate();
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity getUserFromAuthById(UUID userID) {
        AuthUserEntity user = new AuthUserEntity();
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users u " +
                     "JOIN authorities a ON u.id = a.user_id " +
                     "WHERE u.id = ?")) {
            usersPs.setObject(1, userID);
            ResultSet userResultSet = usersPs.executeQuery();

            if (userResultSet.next()) {
                user.setId((UUID) userResultSet.getObject("id"));
                user.setUsername(userResultSet.getString("username"));
                user.setPassword(userResultSet.getString("password"));
                user.setEnabled(userResultSet.getBoolean("enabled"));
                user.setAccountNonExpired(userResultSet.getBoolean("account_non_expired"));
                user.setAccountNonLocked(userResultSet.getBoolean("account_non_locked"));
                user.setCredentialsNonExpired(userResultSet.getBoolean("credentials_non_expired"));

                List<AuthorityEntity> authorities = new ArrayList<AuthorityEntity>();
                AuthorityEntity authority = new AuthorityEntity();
                authority.setAuthority(Authority.valueOf(userResultSet.getString("authority")));
                authorities.add(authority);
                while (userResultSet.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(Authority.valueOf(userResultSet.getString("authority")));
                    authorities.add(ae);
                }
                user.setAuthorities(authorities);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void updateUserInAuth(AuthUserEntity user) {
        int updatedRows = 0;
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("UPDATE users SET password = ?, enabled = ?, account_non_expired = ?, " +
                     "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?")) {
            usersPs.setObject(1, pe.encode(user.getPassword()));
            usersPs.setObject(2, user.getEnabled());
            usersPs.setObject(3, user.getAccountNonExpired());
            usersPs.setObject(4, user.getAccountNonLocked());
            usersPs.setObject(5, user.getCredentialsNonExpired());
            usersPs.setObject(6, user.getId());
            updatedRows = usersPs.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalArgumentException("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
