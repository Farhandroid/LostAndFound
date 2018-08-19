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

import java.util.ArrayList;

import tanvir.lostandfound.Activity.UserPostViewActivity;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class RecyclerAdapterForUsersAllItem extends RecyclerView.Adapter<RecyclerAdapterForUsersAllItem.RecyclerViewHolder> {

    ArrayList<UserProfileItem> foundItemPostArrayList;
    Context context;

    public RecyclerAdapterForUsersAllItem(ArrayList<UserProfileItem> foundItemPostArrayList, Context context) {
        this.foundItemPostArrayList = foundItemPostArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_for_item_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, context, foundItemPostArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        if (foundItemPostArrayList.get(position).getItemCatagory().contains("FoundItem"))
            holder.itemRewardTV.setVisibility(View.GONE);
        else
        {
            holder.itemRewardTV.setVisibility(View.VISIBLE);
            holder.itemRewardTV.setText(foundItemPostArrayList.get(position).getItemReward());
        }


        holder.itemTypeTV.setText(foundItemPostArrayList.get(position).getItemType());
        holder.itemPlaceTV.setText(foundItemPostArrayList.get(position).getItemPlaceName());
        holder.postedByTV.setText(foundItemPostArrayList.get(position).getUserName());
        holder.itemDetailedDescriptionTV.setText(foundItemPostArrayList.get(position).getItemDetailedDescription());
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder

    {
        Context context;
        LinearLayout linearLayout;
        TextView itemTypeTV, itemPlaceTV, itemRewardTV, itemDetailedDescriptionTV,postedByTV;
        ArrayList<UserProfileItem> foundItemPostArrayList;
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


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.d("foundItemType",foundItemPostArrayList.get(position).getItemType());
                    Activity activity= (Activity) context;
                    Intent myIntent = new Intent(activity, UserPostViewActivity.class);
                    if (foundItemPostArrayList.get(position).getItemCatagory().contains("FoundItem"))
                    {
                        myIntent.putExtra("cameFromWhere","FoundFragment");
                        myIntent.putExtra("userFoundItemPostData",getFoundItemPost(foundItemPostArrayList.get(position)));
                    }
                    else
                    {
                        myIntent.putExtra("cameFromWhere","LostFragment");
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
        return foundItemPostArrayList.size();
    }

    static  public FoundItemPost getFoundItemPost(UserProfileItem userProfileItem)
    {
        FoundItemPost foundItemPost=new FoundItemPost(userProfileItem.getUserName(),userProfileItem.getItemType(),userProfileItem.getItemPlaceName(),userProfileItem.getItemAdress(),userProfileItem.getItemDate(),userProfileItem.getItemTime(),userProfileItem.getItemDetailedDescription(),userProfileItem.getItemHowManyImage(),userProfileItem.getItemPostDateAndTime(),userProfileItem.getMessage());

        return foundItemPost;
    }

    static  public LostItemPost getLostItemPost(UserProfileItem userProfileItem)
    {
        LostItemPost lostItemPost=new LostItemPost(userProfileItem.getUserName(),userProfileItem.getItemType(),userProfileItem.getItemPlaceName(),userProfileItem.getItemAdress(),userProfileItem.getItemDate(),userProfileItem.getItemTime(),userProfileItem.getItemReward(),userProfileItem.getItemDetailedDescription(),userProfileItem.getItemHowManyImage(),userProfileItem.getItemPostDateAndTime(),userProfileItem.getMessage());

        return lostItemPost;
    }
}
