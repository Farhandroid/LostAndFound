package tanvir.lostandfound.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tanvir.lostandfound.Adapter.SwipeAdapterForPostView;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.PojoClass.ServerResponseOfRetrievePostImage;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.R;

public class UserPostViewActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SwipeAdapterForPostView customSwipeAdapter;
    ViewPager viewPager;
    ArrayList<String> imageNameList;
    TextView itemTypeTV, itemLocationTV , itemDateTV , itemTimeTV , itemRewardTV , itemDescriptionTV , userNameTV;
    String cameFromWhere;
    FloatingActionButton userPoatUpdateFAB;
    String postDateAndTime , userName ,itemCategory,howManyImage;
    private LostItemPost lostItemPost;
    private FoundItemPost foundItemPost;
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
        userPoatUpdateFAB=findViewById(R.id.postUpdateFAB);
        imageNameList = new ArrayList<>();

        retriveCameFromWhereFromIntent();
        initialView();
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
                        imageNameList.add(serverResponseOfRetrievePostImageArrayList.get(i).getImageName());
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

    public void retrieveLostItemData()
    {
        LostItemPost lostItemPost;
        lostItemPost= (LostItemPost) getIntent().getSerializableExtra("userLostItemPostData");
        this.lostItemPost=lostItemPost;
        setLostItemData(lostItemPost);
    }

    public void retrieveFoundItemData()
    {
        FoundItemPost foundItemPost;
        foundItemPost= (FoundItemPost) getIntent().getSerializableExtra("userFoundItemPostData");
        this.foundItemPost=foundItemPost;
        setFoundItemData(foundItemPost);
    }

    public void retriveCameFromWhereFromIntent()
    {
        try {
            cameFromWhere=getIntent().getStringExtra("cameFromWhere");

            if (cameFromWhere.contains("FoundFragmentUserProfileActivity") ||cameFromWhere.contains("LostFragmentUserProfileActivity"))
                userPoatUpdateFAB.setVisibility(View.VISIBLE);
            else
                userPoatUpdateFAB.setVisibility(View.GONE);

            if (cameFromWhere.contains("LostFragment"))
            {
                retrieveLostItemData();
            }
            else
            {
                retrieveFoundItemData();
            }

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

        itemCategory="LostItem";
        howManyImage=lostItemData.getHowManyImage();
        userName=lostItemData.getUserName();
        postDateAndTime= lostItemData.getPostDateAndTime();
    }

    public void initialView() {
        customSwipeAdapter = new SwipeAdapterForPostView(this, imageNameList,cameFromWhere);
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


        itemCategory="FoundItem";
        howManyImage=foundItemData.getHowManyImage();
        userName=foundItemData.getUserName();
        postDateAndTime= foundItemData.getPostDateAndTime();

    }

    public void userPostEdit(View view) {
        showPostEditDialog();
    }

    public void showPostEditDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_post_update_delete);
        dialog.setCancelable(true);
        LinearLayout updatePost = dialog.findViewById(R.id.updatePost);
        LinearLayout deletePost = dialog.findViewById(R.id.deletePost);
        updatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserPostViewActivity.this,UserPostCreationActivity.class);
                if (cameFromWhere.contains("FoundFragmentUserProfileActivity"))
                {
                    intent.putExtra("cameFromWhere","FoundFragmentUserProfileActivity");
                    intent.putExtra("FoundItemData",foundItemPost);
                }
                else if (cameFromWhere.contains("LostFragmentUserProfileActivity"))
                {
                    intent.putExtra("cameFromWhere","LostFragmentUserProfileActivity");
                    intent.putExtra("LostItemData",lostItemPost);
                }
                if (imageNameList.size()>0)
                {
                    intent.putStringArrayListExtra("imageNameList",imageNameList);
                }
                UserPostViewActivity.this.startActivity(intent);
                UserPostViewActivity.this.overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserPostFromServer();
            }
        });
        dialog.show();
    }

    public void deleteUserPostFromServer()
    {

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> serverResponseCall = apiConfig.deleteUserPost(userName,itemCategory,howManyImage,postDateAndTime, new Gson().toJson(imageNameList));
        Log.i("userNameToDelete",userName);
        Log.i("itemCategoryToDelete",itemCategory);
        Log.i("howManyImageToDelete",howManyImage);
        Log.i("postDateAndTimeToDelete",postDateAndTime);
        Log.i("imagePathToDelete",Integer.toString(imageNameList.size()));
        Log.i("imagePathStrToDelete",new Gson().toJson(imageNameList));
        serverResponseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse=response.body();
                String s=serverResponse.getMessage();
                Log.i("retrofitPostDelete",s);
                Toast.makeText(UserPostViewActivity.this, "delete : "+s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.i("retrofitPostDeleteError",t.getMessage());
                Toast.makeText(UserPostViewActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void startUserProfileActivity()
    {
        EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
        enterOrBackFromActivity.startUserProfileActivity(this);


    }
}
