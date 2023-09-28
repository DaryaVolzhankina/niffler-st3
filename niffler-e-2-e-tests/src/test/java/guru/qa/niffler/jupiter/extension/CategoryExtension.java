package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.db.dao.SpendDao;
import guru.qa.niffler.db.dao.impl.SpendDaoHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback{
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Category annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            String username = annotation.username().isEmpty() ?
                    ((AuthUserEntity) context.getStore(DBUserExtension.NAMESPACE).get(context.getUniqueId())).getUsername() : annotation.username();
            category.setUsername(username);
            category.setCategory(annotation.category());
            CategoryJson createdCategory = categoryService.addCategory(category).execute().body();
            context.getStore(NAMESPACE).put(context.getUniqueId() + "_category", createdCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CategoryExtension.NAMESPACE)
                .get(extensionContext.getUniqueId() + "_category", CategoryJson.class);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        SpendDao spendCategoryDao = new SpendDaoHibernate();
        CategoryJson category = ((CategoryJson) context.getStore(NAMESPACE).get(context.getUniqueId() + "_category"));
        spendCategoryDao.deleteCategoryInSpend(spendCategoryDao.getCategoryByUUID(category.getId()));
    }
}
