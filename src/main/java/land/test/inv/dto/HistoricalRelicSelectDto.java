package land.test.inv.dto;

import java.time.LocalDate;

public record HistoricalRelicSelectDto (
		String relicId, 
		String relicName, 
		Integer primaryCategory,
		Integer secondaryCategory, 
		Integer era, 
		Integer year,
		LocalDate obtained, 
		String provider,
		Boolean sortPrimary,
		Boolean sortSecondary
		) {}
