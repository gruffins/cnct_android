package com.ryanfung.cnct.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class UserIconDrawableTests {

    private UserIconDrawable drawable;

    @Before
    public void setup() {
        drawable = new UserIconDrawable(RuntimeEnvironment.application);
    }

    @Test
    public void getSetInitials() {
        drawable.setInitials("abc");
        assertThat(drawable.getInitials()).isEqualTo("abc");
    }

    @Test
    public void drawsText() {
        Canvas canvas = mock(Canvas.class);

        drawable.setInitials("abc");
        drawable.draw(canvas);

        verify(canvas).drawText(eq("abc"), anyFloat(), anyFloat(), any(Paint.class));
    }

    @Test
    public void noInitialsDoesntDrawText() {
        Canvas canvas = mock(Canvas.class);

        drawable.setInitials(null);
        drawable.draw(canvas);

        verify(canvas, never()).drawText(anyString(), anyFloat(), anyFloat(), any(Paint.class));
    }
}
