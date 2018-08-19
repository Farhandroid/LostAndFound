package tanvir.lostandfound.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import tanvir.lostandfound.R;

/**
 * Created by USER on 20-Nov-17.
 */

public class SwipeAdapter extends PagerAdapter {


    private ArrayList<Uri> imagePath;
    private LayoutInflater inflater;
    String defaultEmpTyImage;
    private Context context;
    private int num_pages;


    public SwipeAdapter(Context context,ArrayList<Uri> imagePath) {
        this.context = context;
        this.imagePath=imagePath;
    }
    @Override
    public int getCount() {
        return imagePath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item_view = inflater.inflate(R.layout.swipelayout, view, false);
        assert item_view != null;
        final ImageView imageView = item_view
                .findViewById(R.id.image);
        Glide.with(context).load(imagePath.get(position)).into(imageView);

        view.addView(item_view, 0);
        return item_view;
    }
}
