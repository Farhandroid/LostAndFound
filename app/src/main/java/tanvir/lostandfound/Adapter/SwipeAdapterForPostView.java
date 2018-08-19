package tanvir.lostandfound.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import tanvir.lostandfound.HelperClass.ImageOverlayView;
import tanvir.lostandfound.R;

/**
 * Created by USER on 20-Nov-17.
 */

public class SwipeAdapterForPostView extends PagerAdapter {


    private ArrayList<String> imagePath;
    private LayoutInflater inflater;
    String defaultEmpTyImage;
    private Context context;
    String cameFromWhere;
    private int num_pages;
    ImageViewer.Builder imageViewerBuilder;
    Fresco fresco;
    public SwipeAdapterForPostView()
    {

    }

    public SwipeAdapterForPostView(Context context, ArrayList<String> imagePath, String cameFromWhere) {
        this.context = context;
        this.imagePath=imagePath;
        this.cameFromWhere=cameFromWhere;

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
        final View item_view = inflater.inflate(R.layout.swipelayout2, view, false);
        assert item_view != null;
        final ImageView imageView = item_view
                .findViewById(R.id.image2);
        final String imagepath;
        if (cameFromWhere.contains("LostFragment"))
            imagepath ="http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserLostItemPostPic/"+imagePath.get(position)+".jpg";
        else
            imagepath ="http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserFoundItemPostPic/"+imagePath.get(position)+".jpg";
        Glide.with(context).load(imagepath).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> strings=new ArrayList<>();
                strings.add(imagepath);
                ImageOverlayView imageOverlayView=new ImageOverlayView(context);
                imageViewerBuilder= new ImageViewer.Builder(context, strings)
                        .setOverlayView(imageOverlayView)
                        .setOverlayView(imageOverlayView)
                        .setImageChangeListener(getImageChangeListener())
                        .setOnDismissListener(getDismissListener())
                        .setStartPosition(0);
                imageViewerBuilder.show();

            }
        });

        view.addView(item_view, 0);
        return item_view;
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
            }
        };
    }

    public ImageViewer.Builder getImageViewerBuilder()
    {
        ImageViewer.Builder imageViewerBuilde = imageViewerBuilder;
        return  imageViewerBuilde;
    }

    private ImageViewer.OnDismissListener getDismissListener() {
        return new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        };
    }
}
