package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.springjdbc.mapper.UserEntityAuthRowMapper;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private final TransactionTemplate authTtpl;
    private final JdbcTemplate authJdbcTemplate;

    public AuthUserDaoSpringJdbc() {
        JdbcTransactionManager authTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.getDataSource(ServiceDb.AUTH));

        this.authTtpl = new TransactionTemplate(authTm);
        this.authJdbcTemplate = new JdbcTemplate(authTm.getDataSource());
    }

    @Override
    public int createUserInAuth(AuthUserEntity user) {
        return authTtpl.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();

            authJdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, pe.encode(user.getPassword()));
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                return ps;
            }, kh);
            final UUID userId = (UUID) kh.getKeyList().get(0).get("id");
            user.setId(userId);
            authJdbcTemplate.batchUpdate("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, userId);
                    ps.setObject(2, Authority.values()[i].name());
                }

                @Override
                public int getBatchSize() {
                    return Authority.values().length;
                }
            });
            return 1;
        });
    }


    @Override
    public void deleteUserByIdInAuth(AuthUserEntity user) {
        authTtpl.executeWithoutResult(status -> {
            authJdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            authJdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
        });
    }

    @Override
    public AuthUserEntity getUserFromAuthById(UUID userID) {
        return authJdbcTemplate.queryForObject("SELECT * FROM users u " +
                "JOIN authorities a ON u.id = a.user_id " +
                "WHERE u.id = ?", UserEntityAuthRowMapper.INSTANCE, userID);
    }

    @Override
    public void updateUserInAuth(AuthUserEntity user) {
        authJdbcTemplate.update("UPDATE users SET password = ?, enabled = ?, account_non_expired = ?, " +
                        "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?", pe.encode(user.getPassword()),
                user.getEnabled(), user.getAccountNonExpired(), user.getAccountNonLocked(), user.getCredentialsNonExpired(),
                user.getId());
    }
}
