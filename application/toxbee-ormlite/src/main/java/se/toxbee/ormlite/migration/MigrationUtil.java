/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.ormlite.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.toxbee.commons.string.StringUtils;

/**
 * MigrationUtil provides utilities for database-migration.<br/>
 * These operations are all SQLite specific.<br/>
 * Provides a fluid interface.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public class MigrationUtil {
	private final SQLiteDatabase db;
	private final ConnectionSource cs;

	private String tableName;

	/**
	 * Constructs the utility object given the DB and connection source.
	 *
	 * @param db the DB.
	 * @param cs the connection source.
	 */
	public MigrationUtil( SQLiteDatabase db, ConnectionSource cs ) {
		this.db = db;
		this.cs = cs;
	}

	/**
	 * Returns the SQLiteDatabase the utility is operating on.
	 *
	 * @return the db.
	 */
	public SQLiteDatabase getDB() {
		return this.db;
	}

	/**
	 * Returns the ConnectionSource the utility is operating on.
	 *
	 * @return the cs.
	 */
	public ConnectionSource getConnectionSource() {
		return this.cs;
	}

	/**
	 * Returns the names of the columns in a table.
	 *
	 * @param tableName the table name.
	 * @return the list of columns.
	 */
	public List<String> getTableColumns() {
		ArrayList<String> columns = new ArrayList<String>();
		String cmd = "pragma table_info(" + tableName + ");";
		Cursor cur = db.rawQuery( cmd, null );

		while ( cur.moveToNext() ) {
			columns.add( cur.getString( cur.getColumnIndex( "name" ) ) );
		}
		cur.close();

		return columns;
	}

	/**
	 * Returns the CREATE TABLE statement that can be used to the table with tableName.
	 *
	 * @param tableName the name of the table.
	 * @return the SQL statement string.
	 */
	public String fetchCreateStmt() {
		StringBuilder s = new StringBuilder();

		Cursor cur = db.rawQuery( "SELECT sql FROM sqlite_master WHERE name = ?", new String[] { tableName } );
		while( cur.moveToNext() ) {
			String stmt = cur.getString( 0 );
			s.append( stmt );
		}
		cur.close();

		return s.toString();
	}

	/**
	 * Drops the columns given by colsToRemove in current table.<br/>
	 * This is an expensive operation as it requires a temporary table.
	 *
	 * @param colsToRemove the column names to remove.
	 * @throws java.sql.SQLException
	 */
	public MigrationUtil dropColumns( String[] colsToRemove ) throws java.sql.SQLException {
		// Fetch create statement.
		String createStmt = fetchCreateStmt();

		// Assemble columns.
		String columnsSeperated;
		{
			List<String> updatedTableColumns = getTableColumns();

			// Remove the columns we don't want anymore from the table's list of columns
			updatedTableColumns.removeAll( Arrays.asList( colsToRemove ) );

			columnsSeperated = StringUtils.COMMA_JOINER.join( updatedTableColumns );
		}

		// Rename the table.
		db.execSQL( "ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;" );

		// Creating the table on its new format (no redundant columns)
		db.execSQL( createStmt );

		// Populating the table with the data
		db.execSQL( "INSERT INTO " + tableName + "(" + columnsSeperated
				+ ") SELECT " + columnsSeperated + " FROM " + tableName + "_old;" );

		// Drop the old table.
		db.execSQL( "DROP TABLE " + tableName + "_old;" );

		return this;
	}

	/**
	 * Adds a column to current table.
	 *
	 * @param col the column name.
	 * @param definition the definition of the column (e.g: INTEGER DEFAULT 0).
	 * @throws java.sql.SQLException
	 */
	public MigrationUtil addColumn( String col, String definition ) throws java.sql.SQLException {
		db.execSQL( String.format( "ALTER TABLE '%s' ADD COLUMN '%s' %s", tableName, col, definition ) );
		return this;
	}

	/**
	 * Updates the table with tableName.
	 *
	 * @param tableName the table name.
	 * @param set the SET part of the statement without SET string in it.
	 * @param where the WHERE part of the statement without WHERE string in it.
	 * @throws java.sql.SQLException
	 */
	public MigrationUtil update( String set, String where ) throws java.sql.SQLException {
		db.execSQL( String.format( "UPDATE '%s' SET %s", tableName, set + (where == null ? "" : " WHERE " + where ) ) );
		return this;
	}

	/**
	 * Instructs the utility to work on the the given table for the time being.
	 *
	 * @param tableName the name of the table.
	 */
	public MigrationUtil table( String name ) {
		this.tableName = name;
		return this;
	}
}
