package tanvir.lostandfound.HelperClass;

import android.app.Activity;
import android.content.Intent;

import tanvir.lostandfound.Activity.HomePage;
import tanvir.lostandfound.Activity.PinVerificationActivity;
import tanvir.lostandfound.Activity.UserPostCreationActivity;
import tanvir.lostandfound.R;
import tanvir.lostandfound.Activity.UserRegistrationActivity;


public class EnterOrBackFromActivity {

    public void startPinVerificationActivity(Activity activity) {
        Intent myIntent = new Intent(activity, PinVerificationActivity.class);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        activity.finish();
    }

    public void startHomePageActivity(Activity activity) {
        Intent myIntent = new Intent(activity, HomePage.class);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        activity.finish();
    }

    public void startuserRegistrationActivity(Activity activity) {
        Intent myIntent = new Intent(activity, UserRegistrationActivity.class);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        activity.finish();
    }

    public void startUserPostCreationActivity(Activity activity) {
        Intent myIntent = new Intent(activity, UserPostCreationActivity.class);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        activity.finish();
    }

    public void BackToHomePageActivity(Activity activity)
    {
        Intent myIntent = new Intent(activity, HomePage.class);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        activity.finish();
    }
}
