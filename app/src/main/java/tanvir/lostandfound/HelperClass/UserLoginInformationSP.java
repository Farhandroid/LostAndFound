package tanvir.lostandfound.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserLoginInformationSP {
    Context context;
    String name ,userName,emailAddress,phoneNumber,isUserUploadedProfilePicture;
    boolean isUserLoggedIn;

    public static final   String sharedPrefferenceName = "UserLoginInformation";
    public static final   String nameSP = "name";
    public static final   String userNameSP = "userName";
    public static final   String emailAddressSP = "emailAddress";
    public static final   String phoneNumberSP = "phoneNumber";
    public static final   String isUserUploadedProfilePictureSP = "isUserUploadedProfilePicture";

    public UserLoginInformationSP(Context context) {
        this.context=context;
    }

    public void putUserInformationToSP(String name ,String userName , String emailAddress ,String phoneNumber,String isUserUploadedProfilePicture)
    {
        Log.i("SharedPrefference","Enter");
        this.userName=userName;
        this.emailAddress=emailAddress;
        this.phoneNumber=phoneNumber;
        this.isUserUploadedProfilePicture=isUserUploadedProfilePicture;
        this.isUserLoggedIn =true;
        this.name=name;
        SharedPreferences.Editor editor=context.getSharedPreferences("UserLoginInformation",Context.MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.putString("userName",userName);
        editor.putString("emailAddress",emailAddress);
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("isUserUploadedProfilePicture",isUserUploadedProfilePicture);
        editor.apply();
        boolean b = editor.commit();
        Log.i("SharedPrefference", String.valueOf(b));
    }

    public void clearUserInformationSharedPrefference()
    {
        this.isUserLoggedIn =false;
        SharedPreferences.Editor editor=context.getSharedPreferences("UserLoginInformation",Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIsUserUploadedProfilePicture() {
        return isUserUploadedProfilePicture;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }
}
