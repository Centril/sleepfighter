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
package se.toxbee.sleepfighter.challenge.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * MinesweeperChallenge is a challenge where the user plays minesweeper.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 4, 2013
 */
public class MinesweeperChallenge extends BaseChallenge {
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.MINESWEEPER );
	}}

	public void start( Activity activity, ChallengeResolvedParams params ) {
		super.start( activity, params );
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state ) {
		super.start( activity, params );
	}

	@Override
	public Bundle savedState() {
		return null;
	}
}