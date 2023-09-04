package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.dao.AuthUserDaoJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDaoJdbc;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
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
    private final AuthUserDao authUserDAO = new AuthUserDaoJdbc();
    private final UserDataUserDAO userDataUserDAO = new UserDataUserDaoJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            UserEntity user = new UserEntity();
            user.setUsername(annotation.username());
            user.setPassword(annotation.password());
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
            authUserDAO.createUser(user);
            userDataUserDAO.createUserInUserData(user);
            context.getStore(NAMESPACE).put(context.getUniqueId(), user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserEntity.class);
    }

    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(DBUserExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), UserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UserEntity user = ((UserEntity) context.getStore(NAMESPACE).get(context.getUniqueId()));
        userDataUserDAO.deleteUserByUsernameInUserData(user.getUsername());
        authUserDAO.deleteUserByIdInAuth(user.getId());
    }
}
