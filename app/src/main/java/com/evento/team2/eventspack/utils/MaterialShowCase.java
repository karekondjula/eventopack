package com.evento.team2.eventspack.utils;

import android.app.Activity;

import com.evento.team2.eventspack.R;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by Daniel on 22-Nov-15.
 */
public class MaterialShowCase {

    public static MaterialShowcaseView.Builder createBaseShowCase(Activity activity) {
        return new MaterialShowcaseView.Builder(activity)
//                    .setTarget(mButtonShow)
//                    .setContentText("This is some amazing feature you should know about")
                .setDismissOnTouch(true)
                .setContentTextColor(activity.getResources().getColor(android.R.color.white))
                .setMaskColour(activity.getResources().getColor(R.color.colorPrimary))
                .setDismissText(android.R.string.ok);

    }
}
