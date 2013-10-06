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
package se.chalmers.dat255.sleepfighter.persist;

import java.sql.SQLException;

import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeParam;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Dao 
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class ChallengeParamDao extends BaseDaoImpl<ChallengeParam, Integer> {
	public CreateOrUpdateStatus createOrUpdate( ChallengeParam data ) throws SQLException {
		QueryBuilder<ChallengeParam, Integer> qb = queryBuilder();

		// NOTE: id here is not the identity field
		qb.where()
			.eq( ChallengeParam.CHALLENGE_ID_COLUMN, data.getId() ).and()
			.eq( "key", data.getKey() );

		ChallengeParam existing = qb.queryForFirst();
		if ( existing == null ) {
			int numRows = create( data );
			return new CreateOrUpdateStatus( true, false, numRows );
		} else {
			int numRows = update( data );
			return new CreateOrUpdateStatus( false, true, numRows );
		}
	}

	// this constructor must be defined
	public ChallengeParamDao( ConnectionSource connectionSource ) throws SQLException {
		super( connectionSource, ChallengeParam.class );
	}
}