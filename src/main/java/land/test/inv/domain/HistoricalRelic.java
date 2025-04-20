package land.test.inv.domain;

public class HistoricalRelic {

	//編號
	private String relicId;
	//文物名
	private String relicName;
	//主類別
    private Integer primaryCategory;
	//次類別
    private Integer secondaryCategory;
	//時代
	private Integer era;
	//年份
	private Integer year;
	//取得日期
	private String obtained;
	//取得數量
	private Integer quantity;
	//提供者
	private String provider;
	//備註
	private String memo;

	public String getRelicId() {
		return relicId;
	}

	public void setRelicId(String relicId) {
		this.relicId = relicId;
	}

	public String getRelicName() {
		return relicName;
	}

	public void setRelicName(String relicName) {
		this.relicName = relicName;
	}

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

	public Integer getEra() {
		return era;
	}

	public void setEra(Integer era) {
		this.era = era;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getObtained() {
		return obtained;
	}

	public void setObtained(String obtained) {
		this.obtained = obtained;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
