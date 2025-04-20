package land.test.inv.service;

import land.test.inv.dao.EraDao;
import land.test.inv.domain.Era;

import java.util.List;


public class EraService {
	
	private EraDao eraDao = new EraDao();

	public List<Era> select() {
		return eraDao.select();
	}

}
