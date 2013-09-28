package se.chalmers.dat255.sleepfighter.adapter;

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
	
    public MemoryAdapter(final Context c, final Memory mem) {
        context = c;
        this.mem = mem;
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

        imageView.setImageResource(mThumbIds[position]);
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
    private Integer[] memoryCardDatabse = {
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