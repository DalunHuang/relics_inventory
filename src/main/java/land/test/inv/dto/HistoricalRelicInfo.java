package land.test.inv.dto;

public record HistoricalRelicInfo (
		String relicId,
		String relicName,
		String pCategoryName,
		String sCategoryName,
		String eraAndYear,
		String obtained,
		String quantity,
		String provider,
		String memo
		){}
