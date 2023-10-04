package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;

import java.util.List;
import java.util.UUID;

public interface SpendDao {
    void deleteCategoryInSpend(CategoryEntity category);

    CategoryEntity getCategoryByUUID(UUID uuid);
    CategoryEntity getCategoryByUsernameCategoryName(String username, String categoryName);

    void deleteAllSpends(List<SpendEntity> list);

    List<SpendEntity> getSpendsByCategory(CategoryEntity categoryEntity);

}