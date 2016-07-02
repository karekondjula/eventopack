package com.evento.team2.eventspack.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.evento.team2.eventspack.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 23-Jan-16.
 */
// TODO animations in the new fancy way of android with connecting components
public class ActivityFullScreenImage extends Activity {

    public static final int EVENT_IMAGE = 0;
    public static final int PLACE_IMAGE = 1;

    @IntDef({EVENT_IMAGE, PLACE_IMAGE,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    public static final String CATEGORY = "category";
    public static final String IMAGE_URI = "image_uri";
    public static final String TITLE = "title";

    @Bind(R.id.image)
    SubsamplingScaleImageView image;

    @Bind(R.id.event_title)
    TextView eventTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String imageUri = intent.getStringExtra(IMAGE_URI);
        final String title = intent.getStringExtra(TITLE);

        new Thread() {
            @Override
            public void run() {
                try {
                    Bitmap b;

                    if (TextUtils.isEmpty(imageUri)) {
                        int category = intent.getIntExtra(CATEGORY, PLACE_IMAGE);

                        if (category == EVENT_IMAGE) {

                            b = Glide.with(ActivityFullScreenImage.this).load(R.drawable.party_image).asBitmap().into(-1, -1).get();

//                Glide.with(this).load(R.drawable.party_image).into(image);
                        } else {
//                Glide.with(this).load(R.drawable.place_image).into(image);
                            b = Glide.with(ActivityFullScreenImage.this).load(R.drawable.party_image).asBitmap().into(-1, -1).get();
                        }
                    } else {
                        b = Glide.with(ActivityFullScreenImage.this).load(imageUri).asBitmap().into(-1, -1).get();
//            Glide.with(this).load(imageUri).into(image);
                    }
                    runOnUiThread(() -> image.setImage(ImageSource.bitmap(b)));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        eventTitle.setText(title);
    }

    public static Intent createIntent(Context context, @Category int category, String imageUri, String eventTitle) {
        Intent intent = new Intent(context, ActivityFullScreenImage.class);
        intent.putExtra(ActivityFullScreenImage.CATEGORY, category);
        intent.putExtra(ActivityFullScreenImage.IMAGE_URI, imageUri);
        intent.putExtra(ActivityFullScreenImage.TITLE, eventTitle);

        return intent;
    }
}
