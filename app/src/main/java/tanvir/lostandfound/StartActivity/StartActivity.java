package tanvir.lostandfound.StartActivity;

import android.app.Activity;
import android.content.Intent;

import tanvir.lostandfound.PinVerificationActivity;
import tanvir.lostandfound.R;


public class StartActivity {

    public void startPinVerificationActivity(Activity activity) {
        Intent myIntent = new Intent(activity, PinVerificationActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(myIntent);
        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        activity.finish();
    }
}
