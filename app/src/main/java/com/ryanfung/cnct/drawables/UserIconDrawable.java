package com.ryanfung.cnct.drawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.ryanfung.cnct.R;

import static com.ryanfung.cnct.utils.AndroidUtil.dpToPx;

public class UserIconDrawable extends GradientDrawable {

    private String initials;
    private Paint paint;

    public UserIconDrawable(@NonNull Context context) {
        super();

        int backgroundColor = ContextCompat.getColor(context, R.color.white);
        int strokeColor = ContextCompat.getColor(context, R.color.secret_space);
        int textColor = ContextCompat.getColor(context, R.color.blue_chips);

        setShape(GradientDrawable.OVAL);
        setColor(backgroundColor);
        setStroke(dpToPx(2), strokeColor);

        paint = new Paint();
        paint.setColor(textColor);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    // =============================================================================================
    // API
    // =============================================================================================

    public void setInitials(@Nullable String initials) {
        this.initials = initials;
    }

    @Nullable
    public String getInitials() {
        return initials;
    }

    // =============================================================================================
    // Drawable
    // =============================================================================================

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!TextUtils.isEmpty(getInitials())) {
            Rect bounds = new Rect();

            paint.setTextSize(getTextSize(canvas.getWidth()));
            paint.getTextBounds(getInitials(), 0, getInitials().length(), bounds);

            int xPos = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            canvas.drawText(getInitials(), xPos, yPos, paint);
        }
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private float getTextSize(int maxWidth) {
        return 0.3f * maxWidth;
    }

}
