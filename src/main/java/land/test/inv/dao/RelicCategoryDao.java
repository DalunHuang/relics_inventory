package land.test.inv.dao;

import land.test.inv.common.JdbcManager;
import land.test.inv.common.Utils;
import land.test.inv.domain.RelicCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RelicCategoryDao {
	
	public List<RelicCategory> select() {

        String sql = "SELECT * FROM RELIC_CATEGORY" +
                " WHERE 1 = 1 ORDER BY PRIMARY_CATEGORY, SECONDARY_CATEGORY";
		
		JdbcManager jdbcManager = new JdbcManager();
		List<Map<String,Object>> resultList = jdbcManager.select(sql);
		jdbcManager.finishCon();

        return resultList.stream()
                .map(map -> {
                    RelicCategory category = new RelicCategory();
                    category.setPrimaryCategory(Utils.toInt(map.get("PRIMARY_CATEGORY")));
                    category.setSecondaryCategory(Utils.toInt(map.get("SECONDARY_CATEGORY")));
                    category.setCategoryName(Utils.toString(map.get("CATEGORY_NAME")));
                    return category;
                }).collect(Collectors.toList());
	}

}
