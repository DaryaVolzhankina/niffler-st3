package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.AuthUserDao;
import guru.qa.niffler.db.dao.AuthUserDaoHibernate;
import guru.qa.niffler.db.dao.AuthUserDaoJdbc;
import guru.qa.niffler.db.dao.AuthUserDaoSpringJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.jupiter.annotation.Dao;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class DaoExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if ((field.getType().isAssignableFrom(AuthUserDao.class) || field.getType().isAssignableFrom(UserDataUserDAO.class))
                    && field.isAnnotationPresent(Dao.class)) {
                field.setAccessible(true);

                AuthUserDao dao;

                if ("hibernate".equals(System.getProperty("db.impl"))) {
                    dao = new AuthUserDaoHibernate();
                } else if ("spring".equals(System.getProperty("db.impl"))) {
                    dao = new AuthUserDaoSpringJdbc();
                } else {
                    dao = new AuthUserDaoSpringJdbc();
                }

                field.set(testInstance, dao);
            }

        }

    }
}