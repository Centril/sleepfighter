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

package se.toxbee.sleepfighter.helper;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.commons.message.Message;
import se.toxbee.commons.message.MessageBus;

/**
 * AlarmTimeRefresher has the responsibility of asynchronously refreshing all alarms.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 17, 2013
 */
public class AlarmTimeRefresher {
	private static long REFRESH_INTERVAL = 1000;

	private Timer timer;
	private final AlarmList list;

	public class RefreshedEvent implements Message {
		public AlarmList getList() {
			return list;
		}

		public AlarmTimeRefresher getRefresher() {
			return AlarmTimeRefresher.this;
		}
	}

	/**
	 * Constructs the refresher given the list of alarms.
	 *
	 * @param list the list.
	 */
	public AlarmTimeRefresher( AlarmList list ) {
		this.list = list;
	}

	/**
	 * Starts the refresher.
	 */
	public void start() {
		if ( this.timer == null ) {
			this.timer = new Timer();
			synchronized ( this.timer ) {
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						refresh();
					}
				};

				// Refresh every second.
				this.timer.scheduleAtFixedRate( task, 0, REFRESH_INTERVAL );
			}
		}
	}

	/**
	 * Stops the refresher.
	 */
	public void stop() {
		if ( this.timer != null ) {
			synchronized ( this.timer ) {
				this.timer.cancel();
			}

			this.timer = null;
		}
	}

	private void refresh() {
		List<Alarm> l = Collections.synchronizedList( this.list );
		synchronized( l ) {
			if ( this.list.isEmpty() ) {
				return;
			}

			// Do refreshing.
			for ( Alarm a : l ) {
				synchronized( a ) {
					if ( this.timer == null ) {
						return;
					}

					a.getTime().refresh();
				}
			}

			if ( this.timer == null ) {
				return;
			}

			// Notify bus of refresh.
			MessageBus<Message> bus = this.list.getMessageBus();
			if ( bus != null ) {
				bus.publish( new RefreshedEvent() );
			}
		}
	}
}
