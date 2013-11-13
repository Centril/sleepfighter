/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.persist.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.toxbee.sleepfighter.utils.string.StringUtils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * MigrationUtil provides utilities for database-migration.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public class MigrationUtil {
	public static List<String> getTableColumns( SQLiteDatabase db, String tableName ) {
		ArrayList<String> columns = new ArrayList<String>();
		String cmd = "pragma table_info(" + tableName + ");";
		Cursor cur = db.rawQuery( cmd, null );

		while ( cur.moveToNext() ) {
			columns.add( cur.getString( cur.getColumnIndex( "name" ) ) );
		}
		cur.close();

		return columns;
	}

	public static String makeCreateStmt( SQLiteDatabase db, String tableName ) {
		StringBuilder s = new StringBuilder();

		Cursor cur = db.rawQuery( "SELECT sql FROM sqlite_master WHERE name = '?'", new String[] { tableName } );
		while( cur.moveToNext() ) {
			s.append( cur.getString( 0 ) );
		}
		cur.close();

		return s.toString();
	}

	public static void dropColumns( SQLiteDatabase db,
			String tableName, String[] colsToRemove )
			throws java.sql.SQLException {

		List<String> updatedTableColumns = getTableColumns( db, tableName );

		// Remove the columns we don't want anymore from the table's list of columns
		updatedTableColumns.removeAll( Arrays.asList( colsToRemove ) );

		String columnsSeperated = StringUtils.COMMA_JOINER.join( ",", updatedTableColumns );

		// Fetch create statement.
		String createStmt = makeCreateStmt( db, tableName );

		db.execSQL( "ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;" );

		// Creating the table on its new format (no redundant columns)
		db.execSQL( createStmt );

		// Populating the table with the data
		db.execSQL( "INSERT INTO " + tableName + "(" + columnsSeperated
				+ ") SELECT " + columnsSeperated + " FROM " + tableName + "_old;" );
		db.execSQL( "DROP TABLE " + tableName + "_old;" );
	}

	public static void addColumn( SQLiteDatabase db, String tableName, String col, String definition )
			throws java.sql.SQLException {
		db.execSQL( String.format( "ALTER TABLE '%s' ADD COLUMN '%s' %s", tableName, col, definition ) );
	}

	public static void update( SQLiteDatabase db, String tableName, String set, String where )
		throws java.sql.SQLException {
		db.execSQL( String.format( "UPDATE '%s' SET %s", tableName, set + (where == null ? "" : " WHERE " + where ) ) );
	}
}
