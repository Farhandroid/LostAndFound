package tanvir.lostandfound.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tanvir.lostandfound.Activity.UserPostViewActivity;
import tanvir.lostandfound.Activity.UserProfileActivity;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class RecyclerAdapterForUserProfile extends RecyclerView.Adapter<RecyclerAdapterForUserProfile.RecyclerViewHolder> {

    ArrayList<UserProfileItem> itemPostArrayList;
    Context context;
    Glide glide;
    public RecyclerAdapterForUserProfile(ArrayList<UserProfileItem> itemPostArrayList, Context context) {
        this.itemPostArrayList = itemPostArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_for_item_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, context, itemPostArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        if (itemPostArrayList.get(position).getItemCatagory().contains("FoundItem"))
            holder.itemRewardTV.setVisibility(View.GONE);
        else
        {
            holder.itemRewardTV.setVisibility(View.VISIBLE);
            holder.itemRewardTV.setText(itemPostArrayList.get(position).getItemReward());
        }

        holder.itemTypeTV.setText(itemPostArrayList.get(position).getItemType());
        holder.itemPlaceTV.setText(itemPostArrayList.get(position).getItemPlaceName());
        holder.postedByTV.setText(itemPostArrayList.get(position).getUserName());
        holder.itemDetailedDescriptionTV.setText(itemPostArrayList.get(position).getItemDetailedDescription());

        try {
            String postDateAndTime = itemPostArrayList.get(position).getItemPostDateAndTime();
            int dividePosition = postDateAndTime.indexOf(" ");
            holder.posDateTV.setText(postDateAndTime.substring(0,dividePosition));
            int lastIndexOfColon = postDateAndTime.lastIndexOf(":");
            int indexOfAmOrPm;
            if (postDateAndTime.contains("AM"))indexOfAmOrPm=postDateAndTime.indexOf('A');
            else indexOfAmOrPm=postDateAndTime.indexOf('P');
            holder.postTimeTV.setText(postDateAndTime.substring(dividePosition+1,lastIndexOfColon)+" "+postDateAndTime.substring(indexOfAmOrPm));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ErrorPostDateAndTimeRV",e.toString());
        }

        try {


            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKey(System.currentTimeMillis()));
            options.placeholder(R.drawable.image_blank_user_profile_icon);
            glide.with(context)
                    .load("http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserProfilePic/"+itemPostArrayList.get(position).getUserName().trim()+".jpg")
                    .apply(options)
                    .into(holder.circleImageView);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("profileImageExcptnLost",e.toString());
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        LinearLayout linearLayout;
        TextView itemTypeTV, itemPlaceTV, itemRewardTV, itemDetailedDescriptionTV,postedByTV , posDateTV , postTimeTV;
        ArrayList<UserProfileItem> foundItemPostArrayList;
        CircleImageView circleImageView;
        public RecyclerViewHolder(View view, final Context context, final ArrayList<UserProfileItem> foundItemPostArrayList1) {
            super(view);
            this.context=context;
            foundItemPostArrayList = foundItemPostArrayList1;
            itemTypeTV =view.findViewById(R.id.itemTypeTV);
            itemPlaceTV =view.findViewById(R.id.itemPlaceTV);
            itemRewardTV =view.findViewById(R.id.itemRewardTV);
            itemDetailedDescriptionTV =view.findViewById(R.id.itemDetailedDescriptionTV);
            postedByTV=view.findViewById(R.id.postedByTV);
            linearLayout=view.findViewById(R.id.lostItemRVLL);
            posDateTV=view.findViewById(R.id.postDateTV);
            postTimeTV=view.findViewById(R.id.postTimeTV);
            circleImageView=view.findViewById(R.id.imageViewInRecyclerView);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("foundItemType",foundItemPostArrayList.get(position).getItemType());
                    Activity activity= (Activity) context;
                    UserProfileActivity userProfileActivity = (UserProfileActivity) context;
                    Intent myIntent = new Intent(activity, UserPostViewActivity.class);
                    myIntent.putExtra("userAllPostArrayList",foundItemPostArrayList);
                    //myIntent.putExtra("scrollingPosition",position+1);
                    myIntent.putExtra("scrollingPosition",userProfileActivity.getScrollingPosition());
                    if (foundItemPostArrayList.get(position).getItemCatagory().contains("FoundItem"))
                    {
                        myIntent.putExtra("cameFromWhere","FoundFragmentUserProfileActivity");
                        myIntent.putExtra("userFoundItemPostData",getFoundItemPost(foundItemPostArrayList.get(position)));
                    }
                    else
                    {
                        myIntent.putExtra("cameFromWhere","LostFragmentUserProfileActivity");
                        myIntent.putExtra("userLostItemPostData",getLostItemPost(foundItemPostArrayList.get(position)));
                    }
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return itemPostArrayList.size();
    }

    static  public FoundItemPost getFoundItemPost(UserProfileItem userProfileItem)
    {
        Log.i("postDateAndTime","foundDate : "+userProfileItem.getItemPostDateAndTime());
        FoundItemPost foundItemPost=new FoundItemPost(userProfileItem.getUserName(),userProfileItem.getItemType(),userProfileItem.getItemPlaceName(),userProfileItem.getItemAdress(),userProfileItem.getItemDate(),userProfileItem.getItemTime(),userProfileItem.getItemDetailedDescription(),userProfileItem.getItemHowManyImage(),userProfileItem.getItemPostDateAndTime(),userProfileItem.getMessage());
        return foundItemPost;
    }

    static  public LostItemPost getLostItemPost(UserProfileItem userProfileItem)
    {
        Log.i("postDateAndTime","lostDate : "+userProfileItem.getItemPostDateAndTime());
        LostItemPost lostItemPost=new LostItemPost(userProfileItem.getUserName(),userProfileItem.getItemType(),userProfileItem.getItemPlaceName(),userProfileItem.getItemAdress(),userProfileItem.getItemDate(),userProfileItem.getItemTime(),userProfileItem.getItemReward(),userProfileItem.getItemDetailedDescription(),userProfileItem.getItemHowManyImage(),userProfileItem.getItemPostDateAndTime(),userProfileItem.getMessage());

        return lostItemPost;
    }
}
