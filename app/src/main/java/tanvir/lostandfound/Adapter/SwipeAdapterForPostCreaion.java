package tanvir.lostandfound.Adapter;

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

import tanvir.lostandfound.Activity.UserPostCreateAndEditActivity;
import tanvir.lostandfound.R;

/**
 * Created by USER on 20-Nov-17.
 */

public class SwipeAdapterForPostCreaion extends PagerAdapter {
    private ArrayList<Uri> imagePath;
    ArrayList<String> imageNameList, imagePathOrNameInString;
    private LayoutInflater inflater;
    boolean isItForUpdatePost;
    String itemCategory;
    private Context context;

    /*public SwipeAdapterForPostCreaion(Context context, ArrayList<Uri> imagePath, boolean isItForUpdatePost) {
        this.context = context;
        this.imagePath=imagePath;
        this.isItForUpdatePost = isItForUpdatePost;
    }*/

    public SwipeAdapterForPostCreaion(Context context, ArrayList<String> imagePathOrNameInString, boolean isItForUpdatePost, String itemCategory) {
        this.context = context;
        this.imagePathOrNameInString = imagePathOrNameInString;
        this.isItForUpdatePost = isItForUpdatePost;
        this.itemCategory = itemCategory;
    }

    public SwipeAdapterForPostCreaion(ArrayList<String> imageNameList, Context context, boolean isItForUpdatePost, String itemCategory) {
        Log.i("imageNameListSizeAdpter", Integer.toString(imageNameList.size()));
        this.context = context;
        this.imageNameList = imageNameList;
        this.isItForUpdatePost = isItForUpdatePost;
        this.itemCategory = itemCategory;
    }

    @Override
    public int getCount() {

        /*if (isItForUpdatePost)
            return imageNameList.size();
        return imagePath.size();*/
        return imagePathOrNameInString.size();
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
        final ImageView imageView = item_view.findViewById(R.id.image);
        String imageUrl;
        if (isItForUpdatePost) {
            Log.i("isItForUpdatePost", "isItForUpdatePost");
            String path = imagePathOrNameInString.get(position);
            if (path.startsWith("path:")) {
                path = imagePathOrNameInString.get(position).substring(6);
                Uri uri = Uri.parse(path);
                Glide.with(context).load(uri).into(imageView);
            } else {
                if (itemCategory.contains("LostItem"))
                    imageUrl = "http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserLostItemPostPic/" + imagePathOrNameInString.get(position) + ".jpg";
                else
                    imageUrl = "http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserFoundItemPostPic/" + imagePathOrNameInString.get(position) + ".jpg";

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_delete_image);
                        Button yesBTN = dialog.findViewById(R.id.yesBTN);
                        yesBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog.isShowing()) dialog.dismiss();
                                UserPostCreateAndEditActivity userPostCreateAndEditActivity = (UserPostCreateAndEditActivity) context;
                                ///userPostCreateAndEditActivity.addImageToDeleteForPostUpdate(imageNameList.get(position));
                                userPostCreateAndEditActivity.addImageToDeleteForPostUpdate(imagePathOrNameInString.get(position), position);
                            }
                        });
                        Button noBTN = dialog.findViewById(R.id.noBTN);
                        noBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog.isShowing()) dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                Glide.with(context).load(imageUrl).into(imageView);
            }
        } else {
            String path = imagePathOrNameInString.get(position).substring(6);
            Uri uri = Uri.parse(path);
            Log.i("path", path);
            Glide.with(context).load(uri).into(imageView);
        }
        view.addView(item_view, 0);
        return item_view;
    }
}
