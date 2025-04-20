package land.test.inv.domain;

public class RelicCategory {
	
	private Integer primaryCategory;
	private Integer secondaryCategory;
    private String categoryName;
    
	public Integer getPrimaryCategory() {
		return primaryCategory;
	}
	public void setPrimaryCategory(Integer primaryCategory) {
		this.primaryCategory = primaryCategory;
	}
	public Integer getSecondaryCategory() {
		return secondaryCategory;
	}
	public void setSecondaryCategory(Integer secondaryCategory) {
		this.secondaryCategory = secondaryCategory;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
}
