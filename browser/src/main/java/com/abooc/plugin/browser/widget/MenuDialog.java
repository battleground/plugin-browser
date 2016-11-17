package com.abooc.plugin.browser.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;

import com.abooc.plugin.browser.R;


/**
 * @author liruiyu Email:allnet@live.cn
 * @Test
 */
public class MenuDialog extends Dialog {

    public static final int ANIMATION_UP_DOWN = 1;
    public static final int ANIMATION_DOWN_UP = 2;

    public MenuDialog(Context context) {
        this(context, 0);
    }

    public MenuDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setAnimationMode(ANIMATION_UP_DOWN);
    }

    public void setAnimationMode(int mode) {
        Window window = getWindow();
        if (mode == ANIMATION_DOWN_UP) {
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.menu_dialog_Animation);
        } else {
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(R.style.dialog_Animation_up_down);
        }
    }

}