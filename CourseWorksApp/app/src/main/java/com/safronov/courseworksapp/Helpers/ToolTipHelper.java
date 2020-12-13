package com.safronov.courseworksapp.Helpers;

import android.content.Context;
import android.view.View;

import com.safronov.courseworksapp.R;

import it.sephiroth.android.library.tooltip.Tooltip;

public class ToolTipHelper {
    public static void show(Context context, View anchor, String text) {
        Tooltip.make(context, new Tooltip
                .Builder()
                .anchor(anchor, Tooltip.Gravity.BOTTOM) // position
                .withStyleId(R.style.ToolTipStyle)      //style
                .closePolicy(new Tooltip.ClosePolicy()
                        .insidePolicy(true, false)
                        .outsidePolicy(true, false), 15000)
                .activateDelay(1500)
                .showDelay(300)
                .text(text)
                .maxWidth(500)
                .withArrow(true)
                .withOverlay(true)
                .floatingAnimation(Tooltip.AnimationBuilder.SLOW)
                .build()
        ).show();
    }
}
