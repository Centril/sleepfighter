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
    		"a", "b",
    		"c", "d",
    		"e", "f",
    		"g", "h",
    		"h", "j",
    		"k", "l",
    		"m", "n",
    		"o", "p",
    		"q", "r",
    		"s", "t",
    		"u", "v",
    		"w", "x",
    		"y", "z",
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
