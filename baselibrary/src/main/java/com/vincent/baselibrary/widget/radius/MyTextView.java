package com.vincent.baselibrary.widget.radius;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.vincent.baselibrary.R;
import com.vincent.baselibrary.widget.radius.delegate.RadiusTextDelegate;
import com.vincent.baselibrary.widget.radius.delegate.RadiusTextDelegateImp;
import com.vincent.baselibrary.widget.radius.delegate.RadiusViewDelegate;
import com.vincent.baselibrary.widget.radius.delegate.RadiusViewDelegateImp;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RadiusTextDelegateImp radiusTextDelegate = new RadiusTextDelegateImp(this,context,attrs);
        radiusTextDelegate.setBackgroundColor(Color.BLUE).setTextCheckedColor(Color.RED);

    }
}
