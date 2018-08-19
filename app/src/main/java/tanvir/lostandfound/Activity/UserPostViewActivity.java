package tanvir.lostandfound.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import retrofit2.Callback;
import retrofit2.Response;
import tanvir.lostandfound.Adapter.SwipeAdapterForPostView;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.ServerResponseOfRetrievePostImage;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.R;

public class UserPostViewActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SwipeAdapterForPostView customSwipeAdapter;
    ViewPager viewPager;
    ArrayList<String> imagePath;
    TextView itemTypeTV, itemLocationTV , itemDateTV , itemTimeTV , itemRewardTV , itemDescriptionTV , userNameTV;
    String cameFromWhere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_view);
        progressDialog = new ProgressDialog(this);

        Fresco.initialize(this);
        itemTypeTV =findViewById(R.id.itemTypeInPOSTview);
        itemDateTV=findViewById(R.id.itemDateInPOSTview);
        itemTimeTV=findViewById(R.id.itemTimeInPOSTview);
        itemRewardTV=findViewById(R.id.itemRewardInPOSTview);
        itemDescriptionTV=findViewById(R.id.itemtDescriptionInPOSTview);
        itemLocationTV=findViewById(R.id.itemLocationInPOSTview);
        userNameTV=findViewById(R.id.userNameInPOSTview);
        viewPager=findViewById(R.id.viewPagerInUserPostView);
        imagePath = new ArrayList<>();

        retriveCameFromWhereFromIntent();
        initialView();
        retriveDataFromIntent();
    }

    public void getItemPostImageInformationFromServer(String userName, String postDateAndTime)
    {
        progressDialog.showProgressDialog("Please wait...");
        ApiConfig getDataFromServer = AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ArrayList<ServerResponseOfRetrievePostImage>> imageInformationRetriavationCall;

        if (cameFromWhere.contains("LostFragment"))
                imageInformationRetriavationCall=getDataFromServer.getLostItemPostImage(userName,postDateAndTime);
        else
            imageInformationRetriavationCall=getDataFromServer.getFoundItemPostImage(userName,postDateAndTime);

        imageInformationRetriavationCall.enqueue(new Callback<ArrayList<ServerResponseOfRetrievePostImage>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<ServerResponseOfRetrievePostImage>> call, Response<ArrayList<ServerResponseOfRetrievePostImage>> response) {
                ArrayList<ServerResponseOfRetrievePostImage> serverResponseOfRetrievePostImageArrayList=response.body();
                Log.d("arrayListSize",Integer.toString(serverResponseOfRetrievePostImageArrayList.size()));
                progressDialog.hideProgressDialog();
                if (serverResponseOfRetrievePostImageArrayList.size()>0)
                {
                    for (int i=0;i<serverResponseOfRetrievePostImageArrayList.size();i++)
                    {
                        imagePath.add(serverResponseOfRetrievePostImageArrayList.get(i).getImageName());
                    }
                    Log.d("retrofitResponse",serverResponseOfRetrievePostImageArrayList.get(0).getMessage());
                    //setSwaipeAdapter();
                    customSwipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<ServerResponseOfRetrievePostImage>> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitErrror",t.getMessage());

            }
        });
    }
    
    public void retriveDataFromIntent()
    {
        try {
            if (cameFromWhere.contains("LostFragment"))
            {
                LostItemPost lostItemPost;
                lostItemPost= (LostItemPost) getIntent().getSerializableExtra("userLostItemPostData");
                setLostItemData(lostItemPost);
            }
            else
            {
                FoundItemPost foundItemPost;
                foundItemPost= (FoundItemPost) getIntent().getSerializableExtra("userFoundItemPostData");
                setFoundItemData(foundItemPost);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("postDateAndTimeError",e.toString());
        }
    }

    public void retriveCameFromWhereFromIntent()
    {

        try {
            cameFromWhere=getIntent().getStringExtra("cameFromWhere");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("errorcameFromWhere",e.toString());
        }
    }

    public void setLostItemData(LostItemPost lostItemData)
    {
        Log.d("postDateAbdTime",lostItemData.getPostDateAndTime());
        Log.d("userName",lostItemData.getUserName());

        itemTypeTV.setText(lostItemData.getWhatIsLost());
        itemLocationTV.setText(lostItemData.getLostItemPlaceName()+"\n"+lostItemData.getLostItemAdress());
        itemRewardTV.setText(lostItemData.getReward());
        itemTimeTV.setText(lostItemData.getTimeOfLost());
        itemDateTV.setText(lostItemData.getDayOfLost());
        itemDescriptionTV.setText(lostItemData.getDetailedDescription());
        Log.d("HowManyImage",""+lostItemData.getHowManyImage());
        if (!lostItemData.getHowManyImage().equals("0"))
        {
            getItemPostImageInformationFromServer(lostItemData.getUserName(),lostItemData.getPostDateAndTime());
        }
    }

    public void initialView() {
        customSwipeAdapter = new SwipeAdapterForPostView(this, imagePath,cameFromWhere);
        viewPager.setAdapter(customSwipeAdapter);
        CirclePageIndicator indicator = findViewById(R.id.CirclePageIndicatorInUserPostView);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
    }


    public void setFoundItemData(FoundItemPost foundItemData) {
        itemRewardTV.setVisibility(View.GONE);
        itemTypeTV.setText(foundItemData.getWhatIsFound());
        itemLocationTV.setText(foundItemData.getFoundItemPlaceName()+"\n"+foundItemData.getFoundItemAdress());
        itemTimeTV.setText(foundItemData.getTimeOfFound());
        itemDateTV.setText(foundItemData.getDayOfFound());
        itemDescriptionTV.setText(foundItemData.getDetailedDescription());
        Log.d("HowManyImage",""+foundItemData.getHowManyImage());
        if (!foundItemData.getHowManyImage().equals("0"))
        {
            getItemPostImageInformationFromServer(foundItemData.getUserName(),foundItemData.getPostDateAndTime());
        }

    }
}
