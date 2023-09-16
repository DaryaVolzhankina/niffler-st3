package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.mapper.UserEntityUserDataRowMapper;
import guru.qa.niffler.db.model.enums.CurrencyValues;
import guru.qa.niffler.db.model.entity.UserDataUserEntity;
import guru.qa.niffler.db.model.entity.AuthUserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class UserDataUserDaoSpringJdbc implements UserDataUserDAO {
    private final TransactionTemplate userdataTtpl;
    private final JdbcTemplate userdataJdbcTemplate;

    public UserDataUserDaoSpringJdbc() {
        JdbcTransactionManager userdataTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.getDataSource(ServiceDb.USERDATA));

        this.userdataTtpl = new TransactionTemplate(userdataTm);
        this.userdataJdbcTemplate = new JdbcTemplate(userdataTm.getDataSource());
    }

    @Override
    public int createUserInUserData(AuthUserEntity user) {
        return userdataJdbcTemplate.update("INSERT INTO users (username, currency) VALUES (?, ?)",
                user.getUsername(), CurrencyValues.RUB.name());
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        userdataJdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
    }

    @Override
    public UserDataUserEntity getUserFromUserDataByUsername(String username) {
        return userdataJdbcTemplate.queryForObject("SELECT * FROM public.users WHERE username = ?",
                UserEntityUserDataRowMapper.INSTANCE, username);
    }

    @Override
    public void updateUserInUserData(UserDataUserEntity user) {
        userdataJdbcTemplate.update("UPDATE users SET currency = ?, firstname = ?, surname = ?, photo = ? WHERE id = ?",
                user.getCurrency().name(), user.getFirstname(), user.getSurname(), user.getPhoto(), user.getId());
    }
}
