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
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.R;

public class RecyclerAdapterForLostItemFragment extends RecyclerView.Adapter<RecyclerAdapterForLostItemFragment.RecyclerViewHolder> {

    ArrayList<LostItemPost> lostItemPostArrayList;
    Context context;

    public RecyclerAdapterForLostItemFragment(ArrayList<LostItemPost> lostItemPostArrayList, Context context) {
        this.lostItemPostArrayList = lostItemPostArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_for_item_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, context, lostItemPostArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.lostItemTypeTV.setText(lostItemPostArrayList.get(position).getWhatIsLost());
        holder.lostItemPlaceTV.setText(lostItemPostArrayList.get(position).getLostItemPlaceName());
        holder.lostItemRewardTV.setText(lostItemPostArrayList.get(position).getReward());
        holder.postedByTV.setText(lostItemPostArrayList.get(position).getUserName());
        holder.lostItemDetailedDescriptionTV.setText(lostItemPostArrayList.get(position).getDetailedDescription());
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder

    {
        Context context;
        LinearLayout linearLayout;
        TextView lostItemTypeTV,lostItemPlaceTV,lostItemRewardTV,lostItemDetailedDescriptionTV,postedByTV;
        ArrayList<LostItemPost> lostItemPostArrayList;
        public RecyclerViewHolder(View view, final Context context, final ArrayList<LostItemPost> lostItemPostArrayList1) {
            super(view);
            this.context=context;
            lostItemPostArrayList = lostItemPostArrayList1;
            lostItemTypeTV=view.findViewById(R.id.itemTypeTV);
            lostItemPlaceTV=view.findViewById(R.id.itemPlaceTV);
            lostItemRewardTV=view.findViewById(R.id.itemRewardTV);
            lostItemDetailedDescriptionTV=view.findViewById(R.id.itemDetailedDescriptionTV);
            postedByTV=view.findViewById(R.id.postedByTV);
            linearLayout=view.findViewById(R.id.lostItemRVLL);


            ///Log.d("ArrayListPosition",Integer.toString(position));

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("rewardRV",lostItemPostArrayList.get(position).getReward());
                    Activity activity= (Activity) context;
                    Intent myIntent = new Intent(activity, UserPostViewActivity.class);
                    myIntent.putExtra("cameFromWhere","LostFragment");
                    myIntent.putExtra("userLostItemPostData",lostItemPostArrayList.get(position));
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return lostItemPostArrayList.size();
    }
}
