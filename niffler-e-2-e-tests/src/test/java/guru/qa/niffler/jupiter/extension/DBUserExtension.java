package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.dao.impl.AuthUserDaoHibernate;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.UserDataUserDaoHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Arrays;


public class DBUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);
    private final AuthUserDao authUserDAO = new AuthUserDaoHibernate();
    private final UserDataUserDAO userDataUserDAO = new UserDataUserDaoHibernate();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Faker faker = new Faker();
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            AuthUserEntity user = new AuthUserEntity();
            String username = annotation.username().isEmpty() ? faker.name().username() : annotation.username();
            String password = annotation.password().isEmpty() ? faker.internet().password() : annotation.password();
            user.setUsername(username);
            user.setPassword(password);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(user);
                        return ae;
                    }).toList());
            authUserDAO.createUserInAuth(user);
            user.setPassword(password);

            UserDataUserEntity userdataUser = new UserDataUserEntity();
            userdataUser.setUsername(user.getUsername());
            userdataUser.setCurrency(CurrencyValues.RUB);
            userDataUserDAO.createUserInUserData(userdataUser);

            context.getStore(NAMESPACE).put(context.getUniqueId(), user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(DBUserExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), AuthUserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        AuthUserEntity authUser = ((AuthUserEntity) context.getStore(NAMESPACE).get(context.getUniqueId()));
        userDataUserDAO.deleteUserInUserData(userDataUserDAO.getUserFromUserDataByUsername(authUser.getUsername()));
        authUserDAO.deleteUserByIdInAuth(authUser);
    }
}
