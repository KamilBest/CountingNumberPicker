package com.best.countingnumberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountingNumberPicker extends LinearLayout {
    private ImageButton minusBtn;
    private ImageButton plusBtn;
    private TextView hintTV;
    private EditText numberET;

    public CountingNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.counting_number_picker_component, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountingNumberPicker, 0, 0);
        Drawable minusBtnDrawable;
        Drawable minusBackgroundDrawable;
        try {
            minusBtnDrawable = typedArray.getDrawable(R.styleable.CountingNumberPicker_minusBtnIconDrawable);
            minusBackgroundDrawable = typedArray.getDrawable(R.styleable.CountingNumberPicker_minusBtnBackground);
        } finally {
            typedArray.recycle();
        }
        initComponents();
        setMinusDrawable(minusBtnDrawable);
        setMinusBtnBackground(minusBackgroundDrawable);

    }

    private void initComponents() {
        minusBtn = findViewById(R.id.minus_btn);
    }

    public Drawable getMinusDrawable() {
        return minusBtn.getDrawable();
    }

    public void setMinusDrawable(Drawable minusDrawable) {
        if (minusDrawable != null)
            minusBtn.setImageDrawable(minusDrawable);
    }

    public Drawable getMinusBtnBackground() {
        return minusBtn.getBackground();
    }

    public void setMinusBtnBackground(Drawable minusBtnBackground) {
        if (minusBtnBackground != null)
            minusBtn.setBackground(minusBtnBackground);
    }
}
