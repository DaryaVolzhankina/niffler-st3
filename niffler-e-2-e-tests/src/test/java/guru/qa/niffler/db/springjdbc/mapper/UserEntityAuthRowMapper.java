package guru.qa.niffler.db.springjdbc.mapper;

import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserEntityAuthRowMapper implements RowMapper<AuthUserEntity> {
    public static final UserEntityAuthRowMapper INSTANCE = new UserEntityAuthRowMapper();

    @Override
    public AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUserEntity user = new AuthUserEntity();
        user.setId((UUID) rs.getObject("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

        List<AuthorityEntity> authorities = new ArrayList<AuthorityEntity>();
        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
        authorities.add(authority);
        while (rs.next()) {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(Authority.valueOf(rs.getString("authority")));
            authorities.add(ae);
        }
        user.setAuthorities(authorities);
        return user;
    }
}
