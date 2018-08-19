package tanvir.lostandfound.HelperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import tanvir.lostandfound.Adapter.SwipeAdapterForPostView;
import tanvir.lostandfound.R;

public class ImageOverlayView extends RelativeLayout {

    Context context;
    public ImageOverlayView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void sendShareIntent() {
        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
        SwipeAdapterForPostView swipeAdapterForPostView=new SwipeAdapterForPostView();
        ImageViewer.Builder builder=swipeAdapterForPostView.getImageViewerBuilder();
        if (Fresco.hasBeenInitialized())
            Fresco.shutDown();
        else
            Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show();

        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
        sendIntent.setType("text/plain");
        getContext().startActivity(sendIntent);*/
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        view.findViewById(R.id.btnShare).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShareIntent();
            }
        });
    }


}
