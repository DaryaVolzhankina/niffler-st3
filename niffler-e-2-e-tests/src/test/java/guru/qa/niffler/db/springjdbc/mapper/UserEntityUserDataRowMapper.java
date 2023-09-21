package guru.qa.niffler.db.springjdbc.mapper;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserEntityUserDataRowMapper implements RowMapper<UserDataUserEntity> {

    public static final UserEntityUserDataRowMapper INSTANCE = new UserEntityUserDataRowMapper();

    @Override
    public UserDataUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDataUserEntity user = new UserDataUserEntity();
        if (rs.next()) {
            user.setId((UUID) rs.getObject("id"));
            user.setUsername(rs.getString("username"));
            user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
            user.setFirstname(rs.getString("firstname"));
            user.setSurname(rs.getString("surname"));
            user.setPhoto(rs.getBytes("photo"));
            return user;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
