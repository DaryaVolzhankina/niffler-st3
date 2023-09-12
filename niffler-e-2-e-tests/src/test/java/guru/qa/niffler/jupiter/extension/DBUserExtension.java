package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.dao.AuthUserDaoSpringJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDaoSpringJdbc;
import guru.qa.niffler.db.model.enums.Authority;
import guru.qa.niffler.db.model.entity.AuthorityEntity;
import guru.qa.niffler.db.model.entity.AuthUserEntity;
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
    private final AuthUserDao authUserDAO = new AuthUserDaoSpringJdbc();
    private final UserDataUserDAO userDataUserDAO = new UserDataUserDaoSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Faker faker = new Faker();
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            AuthUserEntity user = new AuthUserEntity();
            user.setUsername(annotation.username().isEmpty() ? faker.name().username() : annotation.username());
            user.setPassword(annotation.password().isEmpty() ? faker.internet().password() : annotation.password());
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        return ae;
                    }).toList());
            authUserDAO.createUserInAuth(user);
            userDataUserDAO.createUserInUserData(user);
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
        AuthUserEntity user = ((AuthUserEntity) context.getStore(NAMESPACE).get(context.getUniqueId()));
        userDataUserDAO.deleteUserByUsernameInUserData(user.getUsername());
        authUserDAO.deleteUserByIdInAuth(user.getId());
    }
}
