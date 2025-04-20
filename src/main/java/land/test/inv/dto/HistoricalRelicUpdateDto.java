package land.test.inv.dto;

import java.time.LocalDate;

public record HistoricalRelicUpdateDto (
		String relicId,
		String relicName,
		Integer primaryCategory,
		Integer secondaryCategory,
		Integer era,
		Integer year,
		LocalDate obtained,
		Integer quantity,
		String provider,
		String memo
		){}
