package land.test.inv.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import land.test.inv.common.JdbcManager;
import land.test.inv.common.Utils;
import land.test.inv.domain.HistoricalRelic;
import land.test.inv.dto.HistoricalRelicInfo;
import land.test.inv.dto.HistoricalRelicSelectDto;
import land.test.inv.dto.HistoricalRelicUpdateDto;

public class HistoricalRelicDao {
	
	private final String RELIC_ID_KEY = "RELIC_ID";
	private final String RELIC_NAME_KEY = "RELIC_NAME";
	private final String PRIMARY_CATEGORY_KEY = "PRIMARY_CATEGORY";
	private final String SECONDARY_CATEGORY_KEY = "SECONDARY_CATEGORY";
	private final String P_CATEGORY_NAME_KEY = "P_CATEGORY_NAME";
	private final String S_CATEGORY_NAME_KEY = "S_CATEGORY_NAME";
	private final String ERA_KEY = "ERA";
	private final String ERA_NAME_KEY = "ERA_NAME";
	private final String YEAR_KEY = "YEAR";
	private final String OBTAINED_KEY = "OBTAINED";
	private final String QUANTITY_KEY = "QUANTITY";
	private final String PROVIDER_KEY = "PROVIDER";
	private final String MEMO_KEY = "MEMO";
	private final String TOTAL = "TOTAL";
	
	public static final String RELIC_MINGUO_FORMATTER = "yy. MM. dd";

	public List<HistoricalRelicInfo> selectInfo(HistoricalRelicSelectDto queryParams) {

        StringBuilder sql = new StringBuilder();
		List<Object> pstmtParams = new ArrayList<Object>();

		sql.append("SELECT");
		sql.append(" R.RELIC_ID AS " + RELIC_ID_KEY + ",");
		sql.append(" R.RELIC_NAME AS " + RELIC_NAME_KEY + ",");
		sql.append(" R.ERA AS " + ERA_KEY + ",");
		sql.append(" (SELECT E.ERA_NAME FROM ERA AS E WHERE E.ERA_ID = R.ERA) AS " + ERA_NAME_KEY + ",");
		sql.append(" R.YEAR AS " + YEAR_KEY + ",");
		sql.append(" R.OBTAINED AS " + OBTAINED_KEY + ",");
		sql.append(" R.QUANTITY AS " + QUANTITY_KEY + ",");
		sql.append(" R.PROVIDER AS " + PROVIDER_KEY + ",");
		sql.append(" R.MEMO AS " + MEMO_KEY + ",");
		sql.append(" RC.PRIMARY_CATEGORY AS " + PRIMARY_CATEGORY_KEY + ",");
		sql.append(" RC.SECONDARY_CATEGORY AS " + SECONDARY_CATEGORY_KEY + ",");
		sql.append(" RC.CATEGORY_NAME AS " + S_CATEGORY_NAME_KEY + ",");
		sql.append(" (SELECT RCP.CATEGORY_NAME FROM RELIC_CATEGORY AS RCP");
		sql.append(" WHERE RCP.SECONDARY_CATEGORY = 0");
		sql.append(" AND RCP.PRIMARY_CATEGORY = RC.PRIMARY_CATEGORY) AS " + P_CATEGORY_NAME_KEY);

		sql.append(" FROM RELIC AS R");
		sql.append(" LEFT JOIN RELIC_CATEGORY AS RC");
		sql.append(" ON R.PRIMARY_CATEGORY = RC.PRIMARY_CATEGORY");
		sql.append(" AND R.SECONDARY_CATEGORY = RC.SECONDARY_CATEGORY");
		sql.append(" WHERE 1 = 1");
		selectConditional(sql, queryParams, pstmtParams);
		
		JdbcManager jdbcManager = new JdbcManager();
		List<Map<String, Object>> resultList = jdbcManager.select(sql.toString(), pstmtParams);
		jdbcManager.finishCon();

        return resultList.stream()
                .map(map -> {

                    String eraAndYear = "";
                    Optional<String> eStr = Optional.ofNullable(Utils.transform(map.get(ERA_NAME_KEY), String.class));
                    Optional<Integer> yStr = Optional.ofNullable(Utils.transform(map.get(YEAR_KEY), Integer.class));

                    if (eStr.isPresent()) {
                        eraAndYear += eStr.get();
                    }
                    if (yStr.isPresent()) {
                        eraAndYear += yStr.get();
                    }
                    if (yStr.isPresent() && !eraAndYear.isBlank()) {
                        eraAndYear += "年";
                    }

                    String obtained = Optional.ofNullable(Utils.transform(map.get(OBTAINED_KEY), String.class)).orElse("");
                    String provider = Optional.ofNullable(Utils.transform(map.get(PROVIDER_KEY), String.class)).orElse("");
                    String memo = Optional.ofNullable(Utils.transform(map.get(MEMO_KEY), String.class)).orElse("");

                    return new HistoricalRelicInfo(
                            Utils.toString(map.get(RELIC_ID_KEY)),
                            Utils.toString(map.get(RELIC_NAME_KEY)),
                            Utils.toString(map.get(P_CATEGORY_NAME_KEY)),
                            Utils.toString(map.get(S_CATEGORY_NAME_KEY)),
                            eraAndYear,
                            obtained,
                            Utils.toString(map.get(QUANTITY_KEY)),
                            provider,
                            memo);
                }).collect(Collectors.toList());
		
	}
	
	public Integer infoTotal(HistoricalRelicSelectDto queryParams) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> pstmtParams = new ArrayList<Object>();

		sql.append("SELECT COUNT(R.*) AS " + TOTAL +" FROM RELIC AS R WHERE 1 = 1");
		selectConditional(sql, queryParams, pstmtParams);
		
		JdbcManager jdbcManager = new JdbcManager();
		List<Map<String, Object>> resultList = jdbcManager.select(sql.toString(), pstmtParams);
		jdbcManager.finishCon();

        return resultList.stream().findFirst()
                .map(map -> Utils.toInt(map.get(TOTAL))).orElse(0);
		
	}
	
	public HistoricalRelic selectById(String id) {

        HistoricalRelic historicalRelic = null;
		StringBuilder sql = new StringBuilder();
		List<Object> pstmtParams = new ArrayList<Object>();
		
		HistoricalRelicSelectDto params = new HistoricalRelicSelectDto(
                id, null, null, null, null, null, null, null, null, null);

		selectRelicTable(sql);
		selectConditional(sql, params, pstmtParams);
		
		JdbcManager jdbcManager = new JdbcManager();
		List<Map<String, Object>> resultList = jdbcManager.select(sql.toString(), pstmtParams);
		jdbcManager.finishCon();
		
		List<HistoricalRelic> historicalRelicList = resultList.stream()
				.map(map -> {
					
					HistoricalRelic item = new HistoricalRelic();
					item.setRelicId(Utils.toString(map.get(RELIC_ID_KEY)));
					item.setRelicName(Utils.toString(map.get(RELIC_NAME_KEY)));
					item.setPrimaryCategory(Utils.toInt(map.get(PRIMARY_CATEGORY_KEY)));
					item.setSecondaryCategory(Utils.toInt(map.get(SECONDARY_CATEGORY_KEY)));
					item.setEra(Utils.toInt(map.get(ERA_KEY)));
					item.setYear(Utils.toInt(map.get(YEAR_KEY)));
					
					String obtained = Optional.ofNullable(Utils.transform(map.get(OBTAINED_KEY), String.class)).orElse("");
					String provider = Optional.ofNullable(Utils.transform(map.get(PROVIDER_KEY), String.class)).orElse("");
					String memo = Optional.ofNullable(Utils.transform(map.get(MEMO_KEY), String.class)).orElse("");
					
					item.setObtained(obtained);
					item.setQuantity(Utils.toInt(map.get(QUANTITY_KEY)));
					item.setProvider(provider);
					item.setMemo(memo);
					
					return item;
					
				})
				.toList();
		if (resultList.size() == 1) {
			historicalRelic = historicalRelicList.get(0);
		}
		
		return historicalRelic;
		
	}
	
	
	private void selectRelicTable(StringBuilder sql) {
		
		sql.append("SELECT");
		sql.append(" R.RELIC_ID AS " + RELIC_ID_KEY + ",");
		sql.append(" R.RELIC_NAME AS " + RELIC_NAME_KEY + ",");
		sql.append(" R.PRIMARY_CATEGORY AS " + PRIMARY_CATEGORY_KEY + ",");
		sql.append(" R.SECONDARY_CATEGORY AS " + SECONDARY_CATEGORY_KEY + ",");
		sql.append(" R.ERA AS " + ERA_KEY + ",");
		sql.append(" R.YEAR AS " + YEAR_KEY + ",");
		sql.append(" R.OBTAINED AS " + OBTAINED_KEY + ",");
		sql.append(" R.QUANTITY AS " + QUANTITY_KEY + ",");
		sql.append(" R.PROVIDER AS " + PROVIDER_KEY + ",");
		sql.append(" R.MEMO AS " + MEMO_KEY);
		sql.append(" FROM RELIC AS R WHERE 1 = 1");
		
	}
	
	
	private void selectConditional(StringBuilder sql, HistoricalRelicSelectDto queryParams, List<Object> pstmtParams) {
		
		if (!Optional.ofNullable(queryParams.relicId()).orElse("").isBlank()) {
			sql.append(" AND R.RELIC_ID = ?");
			pstmtParams.add(queryParams.relicId());
		}
		if (!Optional.ofNullable(queryParams.relicName()).orElse("").isBlank()) {
			sql.append(" AND R.RELIC_NAME LIKE ?");
			pstmtParams.add("%" + queryParams.relicName() + "%");
		}
		if (Optional.ofNullable(queryParams.primaryCategory()).orElse(0) > 0) {
			sql.append(" AND R.PRIMARY_CATEGORY = ?");
			pstmtParams.add(queryParams.primaryCategory());
		}
		if (Optional.ofNullable(queryParams.secondaryCategory()).orElse(0) > 0) {
			sql.append(" AND R.SECONDARY_CATEGORY = ?");
			pstmtParams.add(queryParams.secondaryCategory());
		}
		if (Optional.ofNullable(queryParams.era()).orElse(0) > 0) {
			sql.append(" AND R.ERA = ?");
			pstmtParams.add(queryParams.era());
		}
		if (Optional.ofNullable(queryParams.year()).orElse(0) > 0) {
			sql.append(" AND R.YEAR = ?");
			pstmtParams.add(queryParams.year());
		}
		if (Optional.ofNullable(queryParams.obtained()).isPresent()) {
			sql.append(" AND R.OBTAINED = ?");
			String minguoDateStr = Utils.minguoDateStr(queryParams.obtained(), RELIC_MINGUO_FORMATTER);
			pstmtParams.add(minguoDateStr);
		}
		if (!Optional.ofNullable(queryParams.provider()).orElse("").isBlank()) {
			sql.append(" AND R.PROVIDER LIKE ?");
			pstmtParams.add("%" + queryParams.provider() + "%");
		}
		
		sql.append(" ORDER BY");
		if(Optional.ofNullable(queryParams.sortPrimary()).orElse(false)) {
			sql.append(" R.PRIMARY_CATEGORY,");
		}
		if(Optional.ofNullable(queryParams.sortSecondary()).orElse(false)) {
			sql.append(" R.SECONDARY_CATEGORY,");
		}
		sql.append(" R.RELIC_ID");
		sql.append(" ASC");
		
	}
	
	
	public void update(HistoricalRelicUpdateDto updateParams) {

        StringBuilder sql = new StringBuilder();
		List<Object> pstmtParams = new ArrayList<Object>();
		
		sql.append("UPDATE RELIC SET");
		sql.append(" RELIC_NAME = ? ,");
		pstmtParams.add(updateParams.relicName());
		sql.append(" PRIMARY_CATEGORY = ? ,");
		pstmtParams.add(updateParams.primaryCategory());
		sql.append(" SECONDARY_CATEGORY = ? ,");
		pstmtParams.add(updateParams.secondaryCategory());
		sql.append(" ERA = ? ,");
		pstmtParams.add(updateParams.era());
		sql.append(" YEAR = ? ,");
		pstmtParams.add(updateParams.year());
		sql.append(" OBTAINED = ? ,");
		if (updateParams.obtained() != null) {
			pstmtParams.add(Utils.minguoDateStr(updateParams.obtained(), RELIC_MINGUO_FORMATTER));
		} else {
			pstmtParams.add(null);
		}
		sql.append(" QUANTITY = ? ,");
		pstmtParams.add(updateParams.quantity());
		sql.append(" PROVIDER = ? ,");
		pstmtParams.add(updateParams.provider());
		sql.append(" MEMO = ? ");
		pstmtParams.add(updateParams.memo());
		sql.append(" WHERE RELIC_ID = ?");
		pstmtParams.add(updateParams.relicId());
		
		JdbcManager jdbcManager = new JdbcManager();
		jdbcManager.autoCommit(false);
		int succse = jdbcManager.update(sql.toString(), pstmtParams);
		if (succse == 1) {
			jdbcManager.commit();		
		} else {
			jdbcManager.rollback();
		}
		jdbcManager.finishCon();

	}

}
