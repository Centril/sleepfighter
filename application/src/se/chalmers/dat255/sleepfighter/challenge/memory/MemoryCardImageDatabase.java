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
package se.chalmers.dat255.sleepfighter.challenge.memory;

import java.util.Random;

import android.os.Parcel;
import android.os.Parcelable;

// strings are shown on the cards.
public class MemoryCardImageDatabase implements Parcelable{
	private String[] memoryCardImages;
	
	private Random rng = new Random();
	
	public String getImage(int cardType) {
		return memoryCardImages[cardType];
	}
	
	// assign each card type an image.
	public MemoryCardImageDatabase(final Memory mem) {
		
		memoryCardImages = new String[mem.getNumPairs()];
		
		for(int i = 0; i< memoryCardImages.length; ++i) {
			memoryCardImages[i] = "";
		}
		
		int cardI = 0;
		
		while(cardI != mem.getNumPairs()) {
			
			// find unassigned image
			boolean unassigned;
			String image;
			do {
		
				
				// get random memory card image from the database.
				image = memoryCardDatabase[rng.nextInt(memoryCardDatabase.length)];
				
				// ensure that it hasn't already been assigned.
				unassigned = true;
				for(int i = 0; i < memoryCardImages.length; ++i) {
					if(memoryCardImages[i].equals(image)) {
						// the image has already been assigned to another card type
						unassigned = false;
						break;
					}
				}
			
			
			}while(!unassigned);
			
			memoryCardImages[cardI] = image;
			
			++cardI;
		}
	}
	
    // list of all the usable memory cards. 
    private static String[] memoryCardDatabase = {
    		"A", "B",
    		"C", "D",
    		"E", "F",
    		"G", "H",
    		"H", "J",
    		"K", "L",
    		"M", "N",
    		"O", "P",
    		"Q", "R",
    		"S", "T",
    		"U", "V",
    		"W", "X",
    		"Y", "Z",
    };
    
    
    // Parcelable stuff

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeSerializable(memoryCardImages);
	}
	
 	public MemoryCardImageDatabase( Parcel in ) {
 		this.memoryCardImages = (String[]) in.readSerializable();
 	}
	
    public static final Parcelable.Creator<MemoryCardImageDatabase> CREATOR = new Parcelable.Creator<MemoryCardImageDatabase>() {
        public MemoryCardImageDatabase createFromParcel(Parcel in) {
            return new MemoryCardImageDatabase(in);
        }

        public MemoryCardImageDatabase[] newArray(int size) {
            return new MemoryCardImageDatabase[size];
        }
    };
}
