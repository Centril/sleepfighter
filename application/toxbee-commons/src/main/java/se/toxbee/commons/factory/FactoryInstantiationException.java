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

package se.toxbee.commons.factory;

/**
 * FactoryInstantiationException is thrown when:
 * 1) A FactoryClassInstantiator throws ROJava6Exception.
 * 2) Other...
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-11
 * @version 1.0
 */
public class FactoryInstantiationException extends RuntimeException {
	private static final long serialVersionUID = -2285286855275253354L;

    /**
     * Constructs a new FactoryInstantiationException
     * and copies the cause of another exception.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public FactoryInstantiationException( Throwable cause ) {
        super( cause );
    }
}