package tanvir.lostandfound.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class UserPostViewActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SwipeAdapterForPostView customSwipeAdapter;
    ViewPager viewPager;
    ArrayList<String> imageNameList;
    TextView itemTypeTV, itemLocationTV , itemDateTV , itemTimeTV , itemRewardTV , itemDescriptionTV , userNameTV;
    String cameFromWhere="";
    FloatingActionButton userPoatUpdateFAB;
    String postDateAndTime , userName ,itemCategory,howManyImage;
    private LostItemPost lostItemPost;
    private FoundItemPost foundItemPost;
    private Context context;
    private RelativeLayout relativeLayoutShouldHideIfNoImageContains;
    private CardView userPostInformationCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_post_view);
        setContentView(R.layout.activity_user_post_view_another_layout);
        progressDialog = new ProgressDialog(this);
        context=UserPostViewActivity.this;
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
        relativeLayoutShouldHideIfNoImageContains=findViewById(R.id.RLpostView);
        userPostInformationCardView=findViewById(R.id.cardViewInUserPostActivity);
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
                retrieveLostItemData();
            else
                retrieveFoundItemData();
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
            getItemPostImageInformationFromServer(lostItemData.getUserName(),lostItemData.getPostDateAndTime());
        else

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
            getItemPostImageInformationFromServer(foundItemData.getUserName(),foundItemData.getPostDateAndTime());
        else
        {
            relativeLayoutShouldHideIfNoImageContains.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_post_update_delete);
        dialog.setCancelable(true);
        LinearLayout updatePost = dialog.findViewById(R.id.updatePost);
        LinearLayout deletePost = dialog.findViewById(R.id.deletePost);
        updatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())dialog.dismiss();
                Intent intent= new Intent(UserPostViewActivity.this,UserPostCreateAndEditActivity.class);
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
                if (dialog.isShowing())dialog.dismiss();
                deleteUserPostFromServer();
            }
        });
        dialog.show();
    }

    public void deleteUserPostFromServer()
    {
        progressDialog.showProgressDialog("Deteting post...");
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
                progressDialog.hideProgressDialog();
                Log.i("RspnseInPostDelete",s);
                if (s.contains("success in post delete"))
                {
                    Toast.makeText(UserPostViewActivity.this, "Post deleted successfully ", Toast.LENGTH_LONG).show();
                    EnterOrBackFromActivity enterOrBackFromActivity=new EnterOrBackFromActivity();
                    enterOrBackFromActivity.backToUserProfileActivity(UserPostViewActivity.this);
                }
                else
                    Toast.makeText(context, "Error in post delete\nPlease try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.i("ErrorInPostDelete",t.getMessage());
                Toast.makeText(context, "Error in post delete\nPlease try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (cameFromWhere.equals("FoundFragmentUserProfileActivity") || cameFromWhere.contains("LostFragmentUserProfileActivity"))
        {
            try {
                ArrayList<UserProfileItem> userAllPostArrayList= (ArrayList<UserProfileItem>) getIntent().getSerializableExtra("userAllPostArrayList");
                int scrollingPosition = getIntent().getIntExtra("scrollingPosition",0);
                Intent intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra("userAllPostArrayList",userAllPostArrayList);
                intent.putExtra("cameFromWhere","UserPostViewActivity");
                intent.putExtra("scrollingPosition",scrollingPosition);
                this.startActivity(intent);
                this.overridePendingTransition(R.anim.right_in,R.anim.right_out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else
        {
            EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
            enterOrBackFromActivity.backToHomePageActivity(UserPostViewActivity.this);
        }

    }
}
