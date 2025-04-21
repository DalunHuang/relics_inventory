package land.test.inv.common;

import java.sql.*;
import java.util.*;

public class JdbcManager {

	private java.sql.Connection con;
	
	private boolean transationSafe = true;
	
	private boolean autoCommitNow = true;
	

	public JdbcManager() {
		try {
			Properties properties = PropertiesLoader.getProperties();
			Class.forName(properties.getProperty("datasource.driver-class-name"));
			this.con = DriverManager.getConnection(properties.getProperty("datasource.url"));
		} catch (ClassNotFoundException | SQLException | NullPointerException e) {
			e.printStackTrace();
			this.con = null;
		}
	}

	public void commit() {
		try {
			if (transationSafe) {
				con.commit();				
			} else {
				rollback();
			}
		} catch (SQLException e) {
			transationSafe = false;
		}
	}

	public void rollback() {
		try {
			con.rollback();
		} catch (SQLException e) {
			transationSafe = false;
		}
	}
	
	public void autoCommit(boolean bool) {
		try {
			con.setAutoCommit(bool);
			autoCommitNow = bool;
		} catch (SQLException e) {
			transationSafe = false;
		}
	}
	
	public void finishCon() {
		if (!autoCommitNow) {
			if (transationSafe) {
				commit();
			} else {
				rollback();
			}
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con = null;
	}
	
	public List<Map<String, Object>> select(String sql){
		return select(sql, null);
	}
	
	public List<Map<String, Object>> select(String sql, List<Object> params) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			setPreparedStatement(ps, params);
			try (ResultSet rs = ps.executeQuery()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				while (rs.next()) {
					Map<String, Object> columnData = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						String columnName = rsmd.getColumnName(i);
						columnData.put(columnName, rs.getObject(columnName));
					}
					result.add(columnData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			transationSafe = false;
			if (!result.isEmpty()) result.clear();
		}
		return result;
	}
	
	public int update(String sql, List<Object> params) {
		int[] result = batchUpdate(sql, List.of(params));
		return result != null ? result[0] : PreparedStatement.EXECUTE_FAILED;
	}
	
	public int[] batchUpdate(String sql, List<List<Object>> paramsList) {
		int[] result = null;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			for (List<Object> params : paramsList) {
				setPreparedStatement(ps, params);
				ps.addBatch();
			}
			result = ps.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			transationSafe = false;
		}
		return result;
	}
	
	public static synchronized boolean preparedStatementUpdateErrorCheck(int[] updaterows) {
		boolean flag = false;
		for (int updaterow : updaterows) {
			switch (updaterow) {
			case PreparedStatement.SUCCESS_NO_INFO:
			case PreparedStatement.EXECUTE_FAILED:
				flag = true;
				break;
			default:
				flag = false;
				break;
			}
			if (flag) {
				break;
			}
		}
		return flag;
	}
	
	private void setPreparedStatement(PreparedStatement ps, List<Object> params) 
		throws SQLException, Exception
	{
		if (params != null && ps != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				int psIndex = i + 1;
				Object param = params.get(i);
				if (param != null) {
					ps.setObject(psIndex, param);
				} else {
					ps.setNull(psIndex, Types.NULL);
				}
			}
		}
	}
	
	

}
