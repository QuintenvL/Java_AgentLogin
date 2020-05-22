package nu.educom.wiki.quinten_mi6.cruds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nu.educom.wiki.quinten_mi6.views.ConsoleLogger;

public class Crud implements ICrud{
	public Connection conn = null;
	public PreparedStatement stmt = null;
	public ResultSet result = null;
	private final String url = "jdbc:mysql://localhost/quinten_mi6";
	private final String user = "Admin";
	private final String password = "5pGcaSsZ7tSHI8bs";

	/**
	 * Set up a connection with the database.
	 * 
	 * @throws SQLException
	 */
	private void setupConnection() throws SQLException{
		conn = DriverManager.getConnection(url,user,password);
	}
	/**
	 * Try to close the result, statement and connection when it is not null.
	 */
	public void sqlFinally() {
		ConsoleLogger logger = new ConsoleLogger();
		try {
			if (result != null) {
				result.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		catch (SQLException ex) {
			logger.debugLog("sqlFinally " + ex.getMessage());
		}
	}
	/**
	 * Create a prepared statement with the given sql.
	 * 
	 * @param String sql the sql query.
	 * @throws SQLException
	 */
	public void createStatement(String sql) throws SQLException{
		setupConnection();
		stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	}
	
	/**
	 * Create a row in the table.
	 */
	public void createRow() throws SQLException{
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 1) {
			result = stmt.getGeneratedKeys();
			if (!result.next()) {
				result = null;
			}
		}
	}
	/**
	 * Read multiple rows.
	 */
	public void readMultiRows() throws SQLException{
		result = stmt.executeQuery();
	}
	/**
	 * Read a single row.
	 * Checks whether the result contains a row.
	 */
	public void readOneRow() throws SQLException {
		result = stmt.executeQuery();
		
		if (!result.next()) {
			result = null;
		}
		
	}
	
	/**
	 * Update a row in the table.
	 */
	public void updateRow() throws SQLException {
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 1) {
			result = stmt.getGeneratedKeys();
			if(!result.next()) {
				result = null;
			}
		}
	}
	/**
	 * Delete a row in the table. (NOT USED)
	 */
	public void deleteRow() throws SQLException{
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 1) {
			result = stmt.getGeneratedKeys();
			if(!result.next()) {
				result = null;
			}
		}
	}
}
