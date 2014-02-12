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

package se.toxbee.sleepfighter.android.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * <p>{@link AndroidSharable} is a mechanism for passing {@link Object}s<br/>
 * between android activities, services, etc. via intents without<br/>
 * resorting to implementing {@link Parcelable} for each object.</p>
 *
 * <p><strong>Note:</strong> You may not use this mechanism
 * for IPC (Inter-Process-Communication).</p>
 *
 * <p><strong>Example 1:</strong></p>
 * <p>In activity A: <pre>
 * MyObj obj = new MyObj();
 * Intent in = new Intent( this, ActivityB.class );
 * startActivity( AndroidSharable.put( in, EXTRA_KEY, obj ) );
 * </pre>
 * In activity B: <pre>
 * MyObj obj = AndroidSharable.take( EXTRA_KEY );
 * </pre>
 * </p>
 *
 * <p><strong>Example 2, fluid interface:</strong></p>
 * <p>In activity A: <pre>
 * MyObj obj1 = new MyObj();
 * MyObj obj2 = new MyObj();
 * MyObj obj3 = new MyObj();
 * Intent in = new Intent( this, ActivityB.class );
 * startActivity( new Accessor( in )
 * 	.put( EXTRA_KEY1, obj1 )
 * 	.put( EXTRA_KEY2, obj1 )
 * 	.put( EXTRA_KEY3, obj3 )
 *	.intent() );
 * </pre>
 * In activity B: <pre>
 * Binder f = new Accessor( in );
 * MyObj obj1 = f.take( EXTRA_KEY1 );
 * MyObj obj2 = f.take( EXTRA_KEY2 );
 * MyObj obj3 = f.take( EXTRA_KEY3 );
 * </pre>
 * </p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 16, 2013
 */
public final class AndroidSharable implements Parcelable {
	/* --------------------------------
	 * API: Fluid interface.
	 * --------------------------------
	 */

	/**
	 * {@link Accessor} allows for fluid accessors for {@link AndroidSharable}s.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 16, 2013
	 */
	public static class Accessor {
		private Intent intent;
		private Bundle bundle;

		/**
		 * Constructor, binds bundle to worker.
		 *
		 * @param bundle the bundle.
		 */
		public Accessor( Bundle bundle ) {
			this.bind( bundle );
		}

		/**
		 * Constructor, binds {@link Intent#getExtras()} to worker.
		 *
		 * @param intent the bundle.
		 */
		public Accessor( Intent intent ) {
			this.bind( intent );
		}

		/**
		 * Returns the bundle passed to {@link #bind(Bundle)}.
		 *
		 * @return the bundle
		 */
		public Bundle bundle() {
			return this.bundle;
		}

		/**
		 * Returns the intent passed to {@link #bind(Intent)}.
		 *
		 * @return the intent.
		 */
		public Intent intent() {
			return this.intent;
		}

		/**
		 * Binds bundle to worker.
		 *
		 * @param bundle the bundle to bind.
		 */
		public Accessor bind( Bundle bundle ) {
			this.bundle = Preconditions.checkNotNull( bundle );
			return this;
		}

		/**
		 * Binds {@link Intent#getExtras()} to worker.
		 *
		 * @param intent intent to bind.
		 */
		public Accessor bind( Intent intent ) {
			this.intent = Preconditions.checkNotNull( intent );
			return this.bind( intent.getExtras() );
		}

		/**
		 * {@link AndroidSharable#take(Bundle, String)}
		 */
		public <U> U take( String key ) {
			return AndroidSharable.take( this.bundle, key );
		}

		/**
		 * {@link AndroidSharable#put(Bundle, String, Object)
		 */
		public Accessor put( String key, Object value ) {
			AndroidSharable.put( this.bundle, key, value );
			return this;
		}
	}

	/* --------------------------------
	 * API: Static interface.
	 * --------------------------------
	 */

	/**
	 * Puts an {@link Object} value known as key in intent.<br/>
	 * The value should only be accessed by<br/>
	 * {@link #take(android.os.Bundle, String)} or {@link #take(android.content.Intent, String)}.
	 *
	 * @param intent the intent to store value in.
	 * @param key the key the value will be known as.
	 * @param value the value to store.
	 * @return the intent passed.
	 */
	public static Intent put( Intent intent, String key, Object value ) {
		return intent.putExtra( key, new AndroidSharable( value ) );
	}

	/**
	 * Puts an {@link Object} value known as key in bundle.<br/>
	 * The value should only be accessed by<br/>
	 * {@link #take(android.os.Bundle, String)} or {@link #take(android.content.Intent, String)}.
	 *
	 * @param bundle the bundle to store value in.
	 * @param key the key the value will be known as.
	 * @param value the value to store.
	 * @return the bundle passed.
	 */
	public static Bundle put( Bundle bundle, String key, Object value ) {
		bundle.putParcelable( key, new AndroidSharable( value ) );
		return bundle;
	}

	/**
	 * Takes an {@link Object} previously "stored" in bundle under key.<br/>
	 * This also removes the value from the bundle.
	 *
	 * @param bundle the bundle where key is stored.
	 * @param key the key the object is stored under.
	 * @return the object/value.
	 */
	public static <U> U take( Bundle bundle, String key ) {
		AndroidSharable v = bundle.getParcelable( key );

		if ( v == null ) {
			// Houston, we have a problem.
			throw new IllegalArgumentException( key + " is not bound to a " + AndroidSharable.class.getSimpleName() );
		}

		bundle.remove( key );
		return v.get();
	}

	/**
	 * Takes an {@link Object} previously "stored" in the intent extras under key.<br/>
	 * This also removes the value from the intent.
	 *
	 * @see #take(Bundle, String)
	 * @param intent the intent with extras where key is stored.
	 * @param key the key the object is stored under.
	 * @return the object/value.
	 */
	public static <U> U take( Intent intent, String key ) {
		return take( intent.getExtras(), key );
	}

	/* --------------------------------
	 * API: Non-static interface.
	 * --------------------------------
	 */
	private Object value;

	/**
	 * Returns the value stored in sharable.
	 *
	 * @return the value.
	 */
	@SuppressWarnings( "unchecked" )
	public <U> U get() {
		return (U) value;
	}

	/**
	 * Constructs the sharable storing the given value.
	 *
	 * @param value the value to store.
	 */
	public AndroidSharable( final Object value ) {
		this.value = value;
	}

	/* --------------------------------
	 * Parcelable implementation:
	 * --------------------------------
	 */
	/**
	 * Constructs the sharable from a parcel.
	 *
	 * @param in the parcel.
	 */
	private AndroidSharable( Parcel in ) {
		this.value = take( in.readLong() );
	}

	@Override
	public void writeToParcel( final Parcel out, int flags ) {
		final long val = SystemClock.elapsedRealtime();
		out.writeLong( val );
		put( val, value );
	}

	/* --------------------------------
	 * Data logic:
	 * --------------------------------
	 */
	private static final Map<Long, Object> entries = Maps.newHashMap();

	synchronized private static void put( long key, final Object obj ) {
		entries.put( key, obj );
	}

	synchronized private static Object take( long key ) {
		return entries.remove( key );
	}

	// Boilerplate:

	public static final Parcelable.Creator<AndroidSharable> CREATOR = new Parcelable.Creator<AndroidSharable>() {
		public AndroidSharable createFromParcel( Parcel in ) {
			return new AndroidSharable( in );
		}

		@Override
		public AndroidSharable[] newArray( int size ) {
			return new AndroidSharable[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}