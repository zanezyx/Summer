package com.deter.TaxManager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deter.TaxManager.R;

/**
 * Created by xiaolu on 17-8-15.
 */

public class AroundButton extends LinearLayout {

    private ImageView icon;

    private TextView title;

    public AroundButton(Context context) {
        super(context);
    }

    public AroundButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AroundButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AroundButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.AroundButton);
        int iconRes = a.getResourceId(R.styleable.AroundButton_aroundicon, R.drawable
                .drawable_around_ap);
        int titleRes = a.getResourceId(R.styleable.AroundButton_arounditle, R.string.around_ap);
        a.recycle();

        setGravity(Gravity.CENTER_VERTICAL);

        icon = new ImageView(context);
        title = new TextView(context);
        addView(icon);
        addView(title);
        LayoutParams layoutParams = (LayoutParams) title.getLayoutParams();
        layoutParams.setMarginStart(18);
        title.setLayoutParams(layoutParams);

        setIcon(iconRes);
        setTitle(titleRes);
        setClickable(true);
        setBackgroundResource(R.drawable.layout_around_item_background);
    }

    public void setIcon(int iconRes) {
        icon.setImageResource(iconRes);
    }

    public void setTitle(int titleRes) {
        title.setText(titleRes);
    }


}
