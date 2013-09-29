package se.chalmers.dat255.sleepfighter.adapter;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Memory;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MemoryAdapter extends BaseAdapter {
   
	private Context context;
	private final Memory mem;
	
	private int[] memoryCardImages;
	
	private Random rng = new Random();

	// assign each card type an image.
	private void assignCards() {
		memoryCardImages = new int[mem.getNumPairs()];
		
		for(int i = 0; i< memoryCardImages.length; ++i) {
			// so far unassigned ones are given the value 0.
			// (0 is not a valid resource ID)
			// http://developer.android.com/reference/android/content/res/Resources.html#getIdentifier%28java.lang.String,%20java.lang.String,%20java.lang.String%29
			memoryCardImages[i] = 0;
		}
		
		int cardI = 0;
		
		while(cardI != mem.getNumPairs()) {
			
			// find unassigned image
			boolean unassigned;
			int image;
			do {
		
				
				// get random memory card image from the database.
				image = memoryCardDatabase[rng.nextInt(memoryCardDatabase.length)];
				
				// ensure that it hasn't already been assigned.
				unassigned = true;
				for(int i = 0; i < memoryCardImages.length; ++i) {
					if(memoryCardImages[i] == image) {
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
	
    public MemoryAdapter(final Context c, final Memory mem) {
        context = c;
        this.mem = mem;       
       
        assignCards();
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(final int position) {
        return null;
    }

    public long getItemId(final int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        
      //  int image = memoryCardImages[mem.getCard(position)];
        imageView.setImageResource(/*image*/ R.drawable.a);
        return imageView;
     
    }

    // references to our images
    private Integer[] mThumbIds = {
    		R.drawable.a, R.drawable.b,
    		R.drawable.c, R.drawable.d,
    		R.drawable.e, R.drawable.f,
    		R.drawable.g, R.drawable.h,
    };
    
    // list of all the usable memory cards. 
    private int[] memoryCardDatabase = {
    		R.drawable.a, R.drawable.b,
    		R.drawable.c, R.drawable.d,
    		R.drawable.e, R.drawable.f,
    		R.drawable.g, R.drawable.h,
    		R.drawable.h, R.drawable.j,
    		R.drawable.k, R.drawable.l,
    		R.drawable.m, R.drawable.n,
    		R.drawable.o, R.drawable.p,
    		R.drawable.q, R.drawable.r,
    		R.drawable.s, R.drawable.t,
    		R.drawable.u, R.drawable.v,
    		R.drawable.w, R.drawable.x,
    		R.drawable.y, R.drawable.z,
    };

}