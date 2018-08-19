package tanvir.lostandfound.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginInformationSP {
    Context context;
    String userName,emailAddress,phoneNumber,isUserUploadedProfilePicture;
    boolean isUserLoggedIn;

    public UserLoginInformationSP(Context context) {
        this.context=context;
    }

    public void putUserInformationToSP(String userName , String emailAddress ,String phoneNumber,String isUserUploadedProfilePicture)
    {
        this.userName=userName;
        this.emailAddress=emailAddress;
        this.phoneNumber=phoneNumber;
        this.isUserUploadedProfilePicture=isUserUploadedProfilePicture;
        this.isUserLoggedIn =true;
        SharedPreferences.Editor editor=context.getSharedPreferences("UserLoginInformation",Context.MODE_PRIVATE).edit();
        editor.putString("userName",userName);
        editor.putString("emailAddress",emailAddress);
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("isUserUploadedProfilePicture",isUserUploadedProfilePicture);
        editor.apply();

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
