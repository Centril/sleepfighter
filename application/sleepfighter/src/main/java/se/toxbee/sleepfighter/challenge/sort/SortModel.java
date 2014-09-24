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

package se.toxbee.sleepfighter.challenge.sort;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Random;

import static se.toxbee.commons.collect.PrimitiveArrays.reverseOrder;
import static se.toxbee.commons.collect.PrimitiveArrays.shuffle;

/**
 * SortModel is the model for SortChallenge.<br/>
 * <strong>NOTE:</strong> the implementation is <strong>NOT</strong> thread safe.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class SortModel implements Parcelable {

	/**
	 * Enumeration of sorting orders<br/>
	 * There are two, {@link #ASCENDING} and {@link #DESCENDING}.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 30, 2013
	 */
	public enum Order {
		ASCENDING, DESCENDING;

		/**
		 * Returns an Order given a boolean b.
		 *
		 * @param b the boolean value.
		 * @return the order it corresponds to.
		 */
		public static Order fromBool( boolean b ) {
			return b ? ASCENDING : DESCENDING;
		}

		/**
		 * Returns a boolean given an Order.
		 *
		 * @param order the order value.
		 * @return the boolean it corresponds to.
		 */
		public static boolean toBool( Order order ) {
			return order == ASCENDING;
		}
	}

	/* --------------------------------
	 * Private fields.
	 * --------------------------------
	 */
	private int[] generatedList;

	private int stepIndex = 0;

	private Order sortOrder;

	private int size;

	private NumberListGenerator generator;

	/* --------------------------------
	 * Public interface.
	 * --------------------------------
	 */

	/**
	 * Default constructor.
	 */
	public SortModel() {
	}

	/**
	 * Sets the size of the generated numbers list.<br/>
	 *
	 * @param size
	 */
	public void setSize( int size ) {
		this.size = size;
	}

	/**
	 * Sets the generator to use to use numbers.
	 *
	 * @param generator
	 */
	public void setGenerator( NumberListGenerator generator ) {
		this.generator = generator;
	}

	/**
	 * <p>Generates the list of numbers, sets sort order.<br/>
	 * This resets the model.</p>
	 *
	 * <p>Before generating the list, you must call {@link #setSize(int)}.</p>
	 */
	public void generateList( Random rng ) {
		this.generator.setNumDigits( 3 );
		int[] numbers = this.generator.generateList( rng, size );

		this.stepIndex = 0;

		// Finally, sort list.
		this.sortOrder = Order.fromBool( rng.nextBoolean() );
		Arrays.sort( numbers );
		if ( this.sortOrder == Order.DESCENDING ) {
			reverseOrder( numbers );
		}

		this.generatedList = numbers;
	}

	/**
	 * Returns a copy of the generated list.
	 *
	 * @return a copy of the list.
	 */
	public int[] getListCopy() {
		return this.generatedList.clone();
	}

	/**
	 * Returns a shuffled representation of the generated list.
	 *
	 * @return the shuffled list.
	 */
	public int[] getShuffledList() {
		int[] list = this.generatedList.clone();
		shuffle( list, new Random() );
		return list;
	}

	/**
	 * Advances one step or throws an exception {@link #isNextNumber(int)} does not pass for given number.
	 *
	 * @param number the number to advance with.
	 */
	public void advanceStep( int number ) {
		if ( !this.isNextNumber( number ) ) {
			throw new IllegalArgumentException( "Can not advance to next step, given number: " + number + ", is not correct." );
		}

		++this.stepIndex;
	}

	/**
	 * Returns true if the given number is the next step in list.
	 *
	 * @param number the number.
	 * @return true if it's the next step.
	 */
	public boolean isNextNumber( int number ) {
		return this.generatedList[this.stepIndex] == number;
	}

	/**
	 * Returns true if there is no next step, all have been passed.
	 *
	 * @return true if we're finished.
	 */
	public boolean isFinished() {
		return this.stepIndex == this.generatedList.length;
	}

	/**
	 * Returns the sort order currently used.
	 *
	 * @return the sort order.
	 */
	public Order getSortOrder() {
		return this.sortOrder;
	}

	/* --------------------------------
	 * Parcelable stuff.
	 * --------------------------------
	 */

	public  SortModel( Parcel in ) {
		this.size = in.readInt();
		this.stepIndex = in.readInt();
		in.readIntArray( this.generatedList );
		this.sortOrder = (Order) in.readSerializable();
	}

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt( this.size );
        out.writeInt( this.stepIndex );
        out.writeIntArray( this.generatedList );
        out.writeSerializable( this.sortOrder );
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SortModel> CREATOR = new Parcelable.Creator<SortModel>() {
        public SortModel createFromParcel(Parcel in) {
            return new SortModel(in);
        }

        public SortModel[] newArray(int size) {
            return new SortModel[size];
        }
    };
}