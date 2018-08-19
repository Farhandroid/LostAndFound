package tanvir.lostandfound.Networking;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import tanvir.lostandfound.HelperClass.ServerResponse;
import tanvir.lostandfound.HelperClass.ServerResponseOfRetrievePostImage;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LoggedUserInformation;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.PojoClass.UserProfileItem;

public interface ApiConfig {

    @GET("Lost&Found/ScriptRetrofit/UserLogin.php")
    Call<LoggedUserInformation> getLoggedUserInformation(
       @Query("UserName") String UserName ,
       @Query("Password") String Password
    );


    @GET("Lost&Found/ScriptRetrofit/RetriveUserPostDataOfLostItem.php")
    Call<ArrayList<LostItemPost>> getAllLostItemPost(
    );

    @GET("Lost&Found/ScriptRetrofit/RetriveUserPostDataOfFoundItem.php")
    Call<ArrayList<FoundItemPost>> getAllFoundItemPost(
    );



    @Multipart
    @POST("Lost&Found/ScriptRetrofit/InsertUserInformation.php")
    Call<ServerResponse> sendUserInformationRequestBody(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name,
            @Part("Name") RequestBody Name,
            @Part("UserName") RequestBody UserName,
            @Part("EmailAddress") RequestBody EmailAddress,
            @Part("PhoneNumber") RequestBody PhoneNumber,
            @Part("Password") RequestBody Password,
            @Part("IsUserUploadedProfilePicture") RequestBody IsUserUploadedProfilePicture

    );

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/InsertUserLostItemPostData.php")
    Call<ServerResponse> sendUserLostItemPostDataToServer(
            @Part ArrayList<MultipartBody.Part> file,
            @Part("ImageQuantity") Integer ImageQuantity,
            @Part("UserName") RequestBody  UserName,
            @Part("PostDateAndTime") RequestBody  PostDateAndTime,
            @Part("WhatIsLost") RequestBody  WhatIsLost,
            @Part("LostItemPlaceName") RequestBody  LostItemPlaceName,
            @Part("LostItemAdress") RequestBody  LostItemAdress,
            @Part("DayOfLost") RequestBody  DayOfLost,
            @Part("TimeOfLost") RequestBody  TimeOfLost,
            @Part("Reward") RequestBody  Reward,
            @Part("DetailedDescription") RequestBody  DetailedDescription
    );

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/InsertUserFoundItemPostData.php")
    Call<ServerResponse> sendUserFoundItemPostDataToServer(
            @Part ArrayList<MultipartBody.Part> file,
            @Part("ImageQuantity") Integer ImageQuantity,
            @Part("UserName") RequestBody  UserName,
            @Part("PostDateAndTime") RequestBody  PostDateAndTime,
            @Part("WhatIsFound") RequestBody  WhatIsFound,
            @Part("FoundItemPlaceName") RequestBody  FoundItemPlaceName,
            @Part("FoundItemAdress") RequestBody  FoundItemAdress,
            @Part("DayOfFound") RequestBody  DayOfFound,
            @Part("TimeOfFound") RequestBody  TimeOfFound,
            @Part("DetailedDescription") RequestBody  DetailedDescription
    );



    @GET("Lost&Found/ScriptRetrofit/RetrieveUserLostItemPostImage.php")
    Call<ArrayList<ServerResponseOfRetrievePostImage>> getLostItemPostImage(
            @Query("userName") String userName,
            @Query("postDateAndTime") String postDateAndTime
    );

    @GET("Lost&Found/ScriptRetrofit/RetrieveUserFoundItemPostImage.php")
    Call<ArrayList<ServerResponseOfRetrievePostImage>> getFoundItemPostImage(
            @Query("userName") String userName,
            @Query("postDateAndTime") String postDateAndTime
    );


    @GET("Lost&Found/ScriptRetrofit/RetriveUsersAllPostData.php")
    Call<ArrayList<UserProfileItem>> getUsersAllPost(
            @Query("UserName") String userName
    );


}
