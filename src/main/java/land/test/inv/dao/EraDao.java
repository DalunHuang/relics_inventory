package land.test.inv.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import land.test.inv.common.JdbcManager;
import land.test.inv.domain.Era;

public class EraDao {
	
	public List<Era> select() {
		return select(new Era());
	}
	
	public List<Era> select(Era queryParams) {
		
		Era params = queryParams;
		StringBuilder sql = new StringBuilder();
		List<Object> pstmtParams = new ArrayList<Object>();
		
		sql.append("SELECT * FROM ERA WHERE 1 = 1");
		if (Optional.ofNullable(params.getEraId()).orElse(Integer.valueOf(-1)) > -1) {
			sql.append(" AND ERA_ID = ?");
			pstmtParams.add(params.getEraId());
		}
		if (!Optional.ofNullable(params.getEraName()).orElse("").isBlank()) {
			sql.append(" AND ERA_NAME = ?");
			pstmtParams.add(params.getEraName());
		}
		
		JdbcManager jdbcManager = new JdbcManager();
		List<Map<String, Object>> resultList = jdbcManager.select(sql.toString(), pstmtParams);
		jdbcManager.finishCon();
		
		List<Era> items = resultList.stream()
				.map(map -> {
					Era item = new Era();
					item.setEraId(Integer.valueOf(map.get("ERA_ID").toString()));
					item.setEraName(map.get("ERA_NAME").toString());
					return item;
				})
				.collect(Collectors.toList());
		
		return items;
		
	}

}
