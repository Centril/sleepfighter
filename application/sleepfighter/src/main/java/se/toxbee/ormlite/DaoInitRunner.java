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

package se.toxbee.ormlite;

/**
 * DaoInitRunner is a method run when a Dao is first initialized.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 12, 2013
 */
public interface DaoInitRunner<T> {
	/**
	 * Called when Dao is initialized.
	 *
	 * @param daoClazz the Class of the Dao.
	 * @param dao the Dao itself.
	 */
	public <D extends PersistenceExceptionDao<T, ?>> void daoInit( Class<T> daoClazz, D dao );
}
