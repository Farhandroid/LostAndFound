package tanvir.lostandfound.Networking;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.PojoClass.ServerResponseOfRetrievePostImage;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LoggedUserInformation;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.PojoClass.UserProfileItem;

public interface ApiConfig {

    @GET("Lost&Found/ScriptRetrofit/UserLogin.php")
    Call<LoggedUserInformation> getLoggedUserInformation(@Query("UserName") String UserName,
                                                         @Query("Password") String Password);


    @GET("Lost&Found/ScriptRetrofit/RetriveUserPostDataOfLostItem.php")
    Call<ArrayList<LostItemPost>> getAllLostItemPost();

    @GET("Lost&Found/ScriptRetrofit/RetriveUserPostDataOfFoundItem.php")
    Call<ArrayList<FoundItemPost>> getAllFoundItemPost();

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/InsertUserInformation.php")
    Call<ServerResponse> sendUserInformationRequestBody(@Part MultipartBody.Part file,
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
    Call<ServerResponse> sendUserLostItemPostDataToServer(@Part ArrayList<MultipartBody.Part> file,
                                                          @Part("ImageQuantity") Integer ImageQuantity,
                                                          @Part("UserName") RequestBody UserName,
                                                          @Part("PostDateAndTime") RequestBody PostDateAndTime,
                                                          @Part("WhatIsLost") RequestBody WhatIsLost,
                                                          @Part("LostItemPlaceName") RequestBody LostItemPlaceName,
                                                          @Part("LostItemAdress") RequestBody LostItemAdress,
                                                          @Part("DayOfLost") RequestBody DayOfLost,
                                                          @Part("TimeOfLost") RequestBody TimeOfLost,
                                                          @Part("Reward") RequestBody Reward,
                                                          @Part("DetailedDescription") RequestBody DetailedDescription);

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/InsertUserFoundItemPostData.php")
    Call<ServerResponse> sendUserFoundItemPostDataToServer(@Part ArrayList<MultipartBody.Part> file,
                                                           @Part("ImageQuantity") Integer ImageQuantity,
                                                           @Part("UserName") RequestBody UserName,
                                                           @Part("PostDateAndTime") RequestBody PostDateAndTime,
                                                           @Part("WhatIsFound") RequestBody WhatIsFound,
                                                           @Part("FoundItemPlaceName") RequestBody FoundItemPlaceName,
                                                           @Part("FoundItemAdress") RequestBody FoundItemAdress,
                                                           @Part("DayOfFound") RequestBody DayOfFound,
                                                           @Part("TimeOfFound") RequestBody TimeOfFound,
                                                           @Part("DetailedDescription") RequestBody DetailedDescription);


    @GET("Lost&Found/ScriptRetrofit/RetrieveUserLostItemPostImage.php")
    Call<ArrayList<ServerResponseOfRetrievePostImage>> getLostItemPostImage(@Query("userName") String userName,
                                                                            @Query("postDateAndTime") String postDateAndTime);

    @GET("Lost&Found/ScriptRetrofit/RetrieveUserFoundItemPostImage.php")
    Call<ArrayList<ServerResponseOfRetrievePostImage>> getFoundItemPostImage(@Query("userName") String userName,
                                                                             @Query("postDateAndTime") String postDateAndTime);


    @GET("Lost&Found/ScriptRetrofit/RetriveUsersAllPostData.php")
    Call<ArrayList<UserProfileItem>> getUsersAllPost(@Query("UserName") String userName);

    @FormUrlEncoded
    @POST("Lost&Found/ScriptRetrofit/testArrayList.php")
    Call<ServerResponse> testArrayList(@Field("arrayList") String testArrayList);

    @FormUrlEncoded
    @POST("Lost&Found/ScriptRetrofit/UserPostDelete.php")
    Call<ServerResponse> deleteUserPost(@Field("UserName") String userName,
                                        @Field("ItemCategory") String ItemCategory,
                                        @Field("IsThereAnyImage") String IsThereAnyImage,
                                        @Field("PostDateAndTime") String PostDateAndTime,
                                        @Field("ImageNameList") String ImageNameList);

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/UpdateUserPost.php")
    Call<ServerResponse> updateUserPost (@Part ArrayList<MultipartBody.Part> file,
                                         @Part("ItemCategory") RequestBody ItemCategory,
                                         @Part("UserName") RequestBody UserName,
                                         @Part("OldPostDateAndTime") RequestBody OldPostDateAndTime,
                                         @Part("NewPostDateAndTime") RequestBody NewPostDateAndTime,
                                         @Part("ItemType") RequestBody ItemType,
                                         @Part("ItemPlaceName") RequestBody ItemPlaceName,
                                         @Part("ItemAdress") RequestBody ItemAdress,
                                         @Part("ItemDate") RequestBody ItemDate,
                                         @Part("ItemTime") RequestBody ItemTime,
                                         @Part("ItemReward") RequestBody ItemReward,
                                         @Part("ItemDetailedDescription") RequestBody ItemDetailedDescription,
                                         @Part("HowManyImage") RequestBody HowManyImage,
                                         @Part("ImageToDelete") RequestBody ImageToDelete,
                                         @Part("NumberOfImageToUpload") RequestBody NumberOfImageToUpload

    );

    @FormUrlEncoded
    @POST("Lost&Found/ScriptRetrofit/ChangePassword.php")
    Call<ServerResponse> changePassword(@Field("UserName") String UserName,
                                        @Field("OldPassword") String OldPassword,
                                        @Field("NewPassword") String NewPassword);


    @GET("Lost&Found/ScriptRetrofit/SendEmailToUserUsingJavaMail.php")
    Call<ServerResponse> sendPasswordUsingEmail(@Query("Email") String Email);

    @Multipart
    @POST("Lost&Found/ScriptRetrofit/UpdateUserInformation.php")
    Call<ServerResponse> updateUserInformation(@Part MultipartBody.Part file,
                                        @Part("file") RequestBody name,
                                        @Part("OldUserName") RequestBody OldUserName,
                                        @Part("NewUserName") RequestBody NewUserName,
                                        @Part("Name") RequestBody Name,
                                        @Part("OldEmailAddress") RequestBody OldEmailAddress,
                                        @Part("NewEmailAddress") RequestBody NewEmailAddress,
                                        @Part("IsUserUploadedProfilePicture") RequestBody IsUserUploadedProfilePicture );
}
