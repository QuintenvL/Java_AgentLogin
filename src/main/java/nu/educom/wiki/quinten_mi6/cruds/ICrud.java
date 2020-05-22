package nu.educom.wiki.quinten_mi6.cruds;


import java.sql.SQLException;

public interface ICrud {
	/**
	 * Create a row in the table.
	 */
	public void createRow() throws SQLException;
	/**
	 * Read multiple rows.
	 */
	public void readMultiRows() throws SQLException;
	/**
	 * Read a single row.
	 */
	public void readOneRow() throws SQLException;
	/**
	 * Update a row in the table.
	 */
	public void updateRow() throws SQLException;
	/**
	 * Delete a row in the table.
	 */
	public void deleteRow() throws SQLException;
}
