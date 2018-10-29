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
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.R;

public class RecyclerAdapterForLostItemFragment extends RecyclerView.Adapter<RecyclerAdapterForLostItemFragment.RecyclerViewHolder> {

    ArrayList<LostItemPost> lostItemPostArrayList;
    Context context;
    Glide glide;
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

        try {
            String postDateAndTime = lostItemPostArrayList.get(position).getPostDateAndTime();
            int dividePosition = postDateAndTime.indexOf(" ");
            holder.postDateTV.setText(postDateAndTime.substring(0,dividePosition));
            int lastIndexOfColon = postDateAndTime.lastIndexOf(":");
            int indexOfAmOrPm=postDateAndTime.length();
            if (postDateAndTime.contains("AM"))indexOfAmOrPm=postDateAndTime.indexOf('A');
            else indexOfAmOrPm=postDateAndTime.indexOf('P');
            holder.postTimeTV.setText(postDateAndTime.substring(dividePosition+1,lastIndexOfColon)+" "+postDateAndTime.substring(indexOfAmOrPm));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ErrorPostDateAndTimeRV",e.toString());
        }

     try {

           /* Log.i("userNameLostAdapter",lostItemPostArrayList.get(position).getUserName());
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.image_add_button))
                    .into(holder.circleImageView);*/
            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKey(System.currentTimeMillis()));
            options.placeholder(R.drawable.image_blank_user_profile_icon);
            glide.with(context)
                    .load("http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserProfilePic/"+lostItemPostArrayList.get(position).getUserName().trim()+".jpg")
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
        TextView lostItemTypeTV,lostItemPlaceTV,lostItemRewardTV,lostItemDetailedDescriptionTV,postedByTV,postDateTV,postTimeTV;
        CircleImageView circleImageView;
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
            postDateTV=view.findViewById(R.id.postDateTV);
            postTimeTV=view.findViewById(R.id.postTimeTV);
            circleImageView=view.findViewById(R.id.imageViewInRecyclerView);

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
