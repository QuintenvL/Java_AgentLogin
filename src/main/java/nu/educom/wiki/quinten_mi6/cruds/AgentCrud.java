package nu.educom.wiki.quinten_mi6.cruds;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import nu.educom.wiki.quinten_mi6.objects.Agent;
import nu.educom.wiki.quinten_mi6.views.ConsoleLogger;

public class AgentCrud {	
	Crud crud;
	ConsoleLogger logger = new ConsoleLogger();
	
	public AgentCrud (Crud c) {
		this.crud = c;
	}
	
	public Agent getSingleAgent(Short agentNumber) {
		Agent newAgent = null;
		String sql = "SELECT * FROM agents WHERE number = ?";
		try {
			crud.createStatement(sql);
			
			crud.stmt.setShort(1, agentNumber);
			
			crud.readOneRow();
			if (crud.result != null) {
				Short resultAgentNumber = crud.result.getShort("number");
				int agentId = crud.result.getInt("agent_id");
				String code = crud.result.getString("code");
				boolean active = crud.result.getBoolean("active");
				boolean license = crud.result.getBoolean("license_to_kill");
				Timestamp expireDate = crud.result.getTimestamp("expire_license");
				long coolOff = crud.result.getLong("cool_off");
				Timestamp latestCoolOff = crud.result.getTimestamp("date_latest_cool_off");
				
				newAgent = new Agent(agentId, resultAgentNumber, code,
						active, license, expireDate, coolOff, latestCoolOff);
			}
		}
		catch (SQLException e) {
			logger.debugLog("getSingleAgent "+e.getMessage());
		}
		finally {
			crud.sqlFinally();
		}
		return newAgent;
	}

	public void createLoginAttempt(Short agentNumber, boolean failed) {
		String sql = "INSERT INTO login_attempts (number, failed) VALUES (?,?)";
		try {
			crud.createStatement(sql);
			
			crud.stmt.setShort(1, agentNumber);
			crud.stmt.setBoolean(2, failed);
			
			crud.createRow();
			
			if (crud.result != null) {
				int insertId = crud.result.getInt(1);
				logger.debugLog("Successfully add login attempt with id: " + insertId);
			}
		}
		catch(SQLException e) {
			logger.debugLog("CreateFailedLoginAttempt "+e.getMessage());
		}
		finally {
			crud.sqlFinally();
		}
	}
	
	public void updateCoolOff(long coolOff, short agentNumber) {
		String sql = "UPDATE agents "
				+ "SET cool_off = ?, date_latest_cool_off = NOW()"
				+ "WHERE number = ?";
		
		try {
			crud.createStatement(sql);
			if (coolOff == 0) {
				crud.stmt.setNull(1, 0);
			}
			else {
				crud.stmt.setLong(1,coolOff);
			}
			crud.stmt.setShort(2, agentNumber);
			crud.updateRow();
			if (crud.result != null) {
				int affectedId = crud.result.getInt(1);
				logger.debugLog("Successfully updated row: " + affectedId);
			}
		}
		catch(SQLException e) {
			logger.debugLog("UpdateCoolOff " + e.getMessage());
		}
		finally {
			crud.sqlFinally();
		}
	}
	
	public List<LocalDateTime> readLoginAttempts(short agentNumber) {
		String sql = "SELECT date "
				+ "FROM login_attempts "
				+ "WHERE number = ? AND id >= "
				+ "(SELECT IFNULL(MAX(id), 0) FROM login_attempts WHERE number = ? AND failed = ?)"
				+ "ORDER BY date DESC";
		List<LocalDateTime> resultList = new ArrayList<LocalDateTime>();
		try {
			crud.createStatement(sql);
			
			crud.stmt.setShort(1,agentNumber);
			crud.stmt.setShort(2,agentNumber);
			crud.stmt.setBoolean(3,false);
			
			crud.readMultiRows();
			while (crud.result.next()) {
				resultList.add(crud.result.getTimestamp("date").toLocalDateTime());
			}
		}
		catch(SQLException e) {
			logger.debugLog("readLoginAttempts " + e.getMessage());
		}
		finally {
			crud.sqlFinally();
		}
		return resultList;
	}
}
