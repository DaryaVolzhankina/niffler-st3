package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDb;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.springjdbc.mapper.UserEntityUserDataRowMapper;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
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
    public int createUserInUserData(UserDataUserEntity user) {
        return userdataJdbcTemplate.update("INSERT INTO users (username, currency) VALUES (?, ?)",
                user.getUsername(), CurrencyValues.RUB.name());
    }

    @Override
    public void deleteUserInUserData(UserDataUserEntity user) {
        userdataJdbcTemplate.update("DELETE FROM users WHERE username = ?", user.getUsername());
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
