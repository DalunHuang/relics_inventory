package land.test.inv.service;

import land.test.inv.dao.HistoricalRelicDao;
import land.test.inv.domain.HistoricalRelic;
import land.test.inv.dto.HistoricalRelicInfo;
import land.test.inv.dto.HistoricalRelicSelectDto;
import land.test.inv.dto.HistoricalRelicUpdateDto;

import java.util.List;


public class HistoricalRelicService {
	
	private final HistoricalRelicDao historicalRelicDao = new HistoricalRelicDao();

	public List<HistoricalRelicInfo> selectWithInfo(HistoricalRelicSelectDto params) {
//		System.out.println("relicId:" + params.relicId());
//		System.out.println("relicName:" + params.relicName());
//		System.out.println("primaryCategory:" + params.primaryCategory());
//		System.out.println("secondaryCategory:" + params.secondaryCategory());
//		System.out.println("era:" + params.era());
//		System.out.println("year:" + params.year());
//		System.out.println("obtained:" + params.obtained());
//		System.out.println("provider:" + params.provider());
        return historicalRelicDao.selectInfo(params);
	}
	
	public HistoricalRelic selectById(String id) {
		return historicalRelicDao.selectById(id);
	}

	public void update(HistoricalRelicUpdateDto updateParams) {
		historicalRelicDao.update(updateParams);
	}

}
