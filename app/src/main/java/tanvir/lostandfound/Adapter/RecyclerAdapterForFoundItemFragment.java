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
import tanvir.lostandfound.R;

public class RecyclerAdapterForFoundItemFragment extends RecyclerView.Adapter<RecyclerAdapterForFoundItemFragment.RecyclerViewHolder> {

    ArrayList<FoundItemPost> foundItemPostArrayList;
    Context context;

    public RecyclerAdapterForFoundItemFragment(ArrayList<FoundItemPost> foundItemPostArrayList, Context context) {
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
        holder.foundItemTypeTV.setText(foundItemPostArrayList.get(position).getWhatIsFound());
        holder.foundItemPlaceTV.setText(foundItemPostArrayList.get(position).getFoundItemPlaceName());
        holder.postedByTV.setText(foundItemPostArrayList.get(position).getUserName());
        holder.foundItemDetailedDescriptionTV.setText(foundItemPostArrayList.get(position).getDetailedDescription());
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder

    {
        Context context;
        LinearLayout linearLayout;
        TextView foundItemTypeTV,foundItemPlaceTV,foundItemRewardTV,foundItemDetailedDescriptionTV,postedByTV;
        ArrayList<FoundItemPost> foundItemPostArrayList;
        public RecyclerViewHolder(View view, final Context context, final ArrayList<FoundItemPost> foundItemPostArrayList1) {
            super(view);
            this.context=context;
            foundItemPostArrayList = foundItemPostArrayList1;
            foundItemTypeTV=view.findViewById(R.id.itemTypeTV);
            foundItemPlaceTV=view.findViewById(R.id.itemPlaceTV);
            foundItemRewardTV=view.findViewById(R.id.itemRewardTV);
            foundItemRewardTV.setVisibility(View.GONE);
            foundItemDetailedDescriptionTV=view.findViewById(R.id.itemDetailedDescriptionTV);
            postedByTV=view.findViewById(R.id.postedByTV);
            linearLayout=view.findViewById(R.id.lostItemRVLL);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.d("foundItemType",foundItemPostArrayList.get(position).getWhatIsFound());
                    Activity activity= (Activity) context;
                    Intent myIntent = new Intent(activity, UserPostViewActivity.class);
                    myIntent.putExtra("cameFromWhere","FoundFragment");
                    myIntent.putExtra("userFoundItemPostData",foundItemPostArrayList.get(position));
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
}
