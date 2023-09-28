package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.SpendDao;
import guru.qa.niffler.db.dao.impl.SpendDaoHibernate;
import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.DeleteSpendings;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.UUID;

public class DeleteSpendingExtension implements AfterEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DeleteSpendingExtension.class);

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        DeleteSpendings annotation = context.getRequiredTestMethod().getAnnotation(DeleteSpendings.class);
        if (annotation != null) {
            SpendDao spendDao = new SpendDaoHibernate();
            UUID categoryId = annotation.username().isEmpty() && annotation.category().isEmpty() ?
                    ((CategoryJson) context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId() + "_category")).getId() :
                    spendDao.getCategoryByUsernameCategoryName(annotation.username(), annotation.category()).getId();
            CategoryEntity categoryEntity = spendDao.getCategoryByUUID(categoryId);
            spendDao.deleteAllSpends(spendDao.getSpendsByCategory(categoryEntity));
        }
    }
}
