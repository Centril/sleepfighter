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

package se.toxbee.sleepfighter.persist.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;

import se.toxbee.ormlite.PersistenceException;
import se.toxbee.sleepfighter.model.challenge.ChallengeParam;

/**
 * ChallengeParamDao is the DAO for ChallengeParam. 
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class ChallengeParamDao extends BaseDaoImpl<ChallengeParam, Integer> {
	public CreateOrUpdateStatus createOrUpdate( ChallengeParam data ) throws SQLException {
		QueryBuilder<ChallengeParam, Integer> qb = queryBuilder();

		Where<ChallengeParam, Integer> where = qb.where()
			.eq( ChallengeParam.CHALLENGE_ID_COLUMN, data.getId() ).and()
			.eq( "key", data.getKey() );

		ChallengeParam existing = qb.queryForFirst();

		if ( existing == null ) {
			int numRows = create( data );
			return new CreateOrUpdateStatus( true, false, numRows );
		} else {
			UpdateBuilder<ChallengeParam, Integer> ub = this.updateBuilder();
			ub.setWhere( where );
			ub.updateColumnValue( ChallengeParam.CHALLENGE_VALUE_COLUMN, data.getValue() );
			int numRows = ub.update();
			return new CreateOrUpdateStatus( false, true, numRows );
		}
	}

	@Override
	public int deleteIds( Collection<Integer> ids ) throws SQLException {
		DeleteBuilder<ChallengeParam, Integer> db = this.deleteBuilder();

		try {
			db.where().in( ChallengeParam.CHALLENGE_ID_COLUMN, ids );
			return db.delete();
		} catch ( SQLException e ) {
			throw new PersistenceException( e );
		}
	}

	// this constructor must be defined
	public ChallengeParamDao( ConnectionSource connectionSource ) throws SQLException {
		super( connectionSource, ChallengeParam.class );
	}
}