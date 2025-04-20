package land.test.inv.service;

import land.test.inv.dao.RelicCategoryDao;
import land.test.inv.domain.RelicCategory;
import land.test.inv.domain.RelicCategorys;

import java.util.List;

public class RelicCategoryService {
	
	public static RelicCategorys getCategorys() {
		
		final List<RelicCategory> categorys = new RelicCategoryDao().select();
		
		RelicCategorys relicCategorys = new RelicCategorys();
		RelicCategorys pCategorys = new RelicCategorys();
		RelicCategorys sCategorys = new RelicCategorys();
		
		for (RelicCategory category : categorys) {
			
			Integer primary = category.getPrimaryCategory();
			Integer secondary = category.getSecondaryCategory();
			String categoryName = category.getCategoryName();
			
			if (!primary.equals(pCategorys.getCatgoryCode())) {
				pCategorys = new RelicCategorys();
				pCategorys.setCatgoryCode(primary);
				pCategorys.setCatgoryName(categoryName);
				relicCategorys.addChildNode(pCategorys);
			}
			if (!secondary.equals(sCategorys.getCatgoryCode())) {
				sCategorys = new RelicCategorys();
				sCategorys.setCatgoryCode(secondary);
				sCategorys.setCatgoryName(categoryName);
				pCategorys.addChildNode(sCategorys);
			}
			
		};
		
		return relicCategorys;
		
	}

}
