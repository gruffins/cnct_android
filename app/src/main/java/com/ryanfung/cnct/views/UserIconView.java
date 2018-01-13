package com.ryanfung.cnct.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ryanfung.cnct.drawables.UserIconDrawable;

public class UserIconView extends View {

    private UserIconDrawable drawable;

    public UserIconView(Context context) {
        super(context);
        init();
    }

    public UserIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawable = new UserIconDrawable(getContext());

        setBackground(drawable);
    }

    // =============================================================================================
    // API
    // =============================================================================================

    public void setInitials(@Nullable String initials) {
        drawable.setInitials(initials);
        drawable.invalidateSelf();
    }

    @Nullable
    public String getInitials() {
        return drawable.getInitials();
    }
}
