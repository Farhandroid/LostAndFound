package tanvir.lostandfound.HelperClass;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import tanvir.lostandfound.R;

public class ProgressDialog {

    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String message) {
        Log.i("DialogProgress", "ProgressDialog");
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        final TextView loading_msg = dialogView.findViewById(R.id.loading_msg);
        loading_msg.setTypeface(null, Typeface.NORMAL);
        loading_msg.setText(message);
        TextView textView=dialogView.findViewById(R.id.image_upload_message);
        textView.setVisibility(View.GONE);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void showProgressDialog(String message,Boolean aBoolean) {
        Log.i("DialogProgress", "ProgressDialog");
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        final TextView loading_msg = dialogView.findViewById(R.id.loading_msg);
        loading_msg.setTypeface(null, Typeface.BOLD);
        TextView textView=dialogView.findViewById(R.id.image_upload_message);
        textView.setVisibility(View.VISIBLE);
        loading_msg.setText(message);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void hideProgressDialog() {
        alertDialog.dismiss();
    }

    public void setAlertdialogNull() {
        alertDialog = null;
    }
}

