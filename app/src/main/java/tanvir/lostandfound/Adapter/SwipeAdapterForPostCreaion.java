package tanvir.lostandfound.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tanvir.lostandfound.Activity.UserPostCreationActivity;
import tanvir.lostandfound.R;

/**
 * Created by USER on 20-Nov-17.
 */

public class SwipeAdapterForPostCreaion extends PagerAdapter {
    private ArrayList<Uri> imagePath;
    ArrayList<String> imageNameList;
    private LayoutInflater inflater;
    boolean isItForUpdatePost;
    String itemCategory;
    private Context context;

    public SwipeAdapterForPostCreaion(Context context, ArrayList<Uri> imagePath, boolean isItForUpdatePost) {
        this.context = context;
        this.imagePath=imagePath;
        this.isItForUpdatePost = isItForUpdatePost;
    }

    public SwipeAdapterForPostCreaion(ArrayList<String> imageNameList, Context context, boolean isItForUpdatePost, String itemCategory) {
        Log.i("imageNameListSizeAdpter",Integer.toString(imageNameList.size()));
        this.context = context;
        this.imageNameList=imageNameList;
        this.isItForUpdatePost = isItForUpdatePost;
        this.itemCategory=itemCategory;
    }
    @Override
    public int getCount() {

        if (isItForUpdatePost)
            return imageNameList.size();
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
        String imageUrl;
        if (isItForUpdatePost)
        {
            Log.i("isItForUpdatePost","isItForUpdatePost");
            if (itemCategory.contains("LostItem"))
                imageUrl ="http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserLostItemPostPic/"+imageNameList.get(position)+".jpg";
            else
                imageUrl ="http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserFoundItemPostPic/"+imageNameList.get(position)+".jpg";
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.delete_image);
                    Button yesBTN = dialog.findViewById(R.id.yesBTN);
                    yesBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Activity activity= (Activity) context;
                            UserPostCreationActivity userPostCreationActivity= (UserPostCreationActivity) context;
                            userPostCreationActivity.addImageToDeleteForPostUpdate(imageNameList.get(position));
                        }
                    });

                    Button noBTN = dialog.findViewById(R.id.noBTN);
                    noBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            Glide.with(context).load(imageUrl).into(imageView);
        }
        else
        {
            Glide.with(context).load(imagePath.get(position)).into(imageView);
        }

        view.addView(item_view, 0);
        return item_view;
    }
}
