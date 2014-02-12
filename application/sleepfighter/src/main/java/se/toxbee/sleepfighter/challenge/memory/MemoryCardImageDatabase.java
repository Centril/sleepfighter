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

package se.toxbee.sleepfighter.challenge.memory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

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
