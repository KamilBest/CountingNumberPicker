package com.best.countingnumberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

public class CountingNumberPicker extends LinearLayout implements View.OnClickListener {
    private final static float DEFAULT_START_VALUE = 0F;
    private final static float DEFAULT_TICK_VALUE = 1F;
    private final static int EMPTY = 0;
    private float counterValue = DEFAULT_START_VALUE;
    private float tickValue = DEFAULT_TICK_VALUE;
    private CustomClickListener customClickListener;
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
        initViews();
        loadValuesFromAttrs(attrs);
    }

    private void initViews() {
        hintTV = findViewById(R.id.hint_tv);
        numberET = findViewById(R.id.number_et);
        setupNumberEditTextChangeListener();
        minusBtn = findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(this);
        plusBtn = findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(this);
    }

    private void loadValuesFromAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountingNumberPicker, EMPTY, EMPTY);
        Drawable minusIcon;
        Drawable plusIcon;
        int tintColor;
        Drawable buttonBackground;
        String hintText;

        try {
            counterValue = typedArray.getFloat(R.styleable.CountingNumberPicker_startValue, DEFAULT_START_VALUE);
            tickValue = typedArray.getFloat(R.styleable.CountingNumberPicker_tickValue, DEFAULT_TICK_VALUE);

            minusIcon = typedArray.getDrawable(R.styleable.CountingNumberPicker_minusBtnIconDrawable);
            plusIcon = typedArray.getDrawable(R.styleable.CountingNumberPicker_plusBtnIconDrawable);
            tintColor = typedArray.getColor(R.styleable.CountingNumberPicker_btnsIconTintColor, EMPTY);
            buttonBackground = typedArray.getDrawable(R.styleable.CountingNumberPicker_btnsBackgroundDrawable);
            hintText = typedArray.getString(R.styleable.CountingNumberPicker_hintText);
        } finally {
            typedArray.recycle();
        }
        setCounterValue(counterValue);
        setMinusBtnIcon(minusIcon);
        setPlusBtnIcon(plusIcon);
        setIconsTint(tintColor, minusIcon, plusIcon);
        setBtnsBackground(buttonBackground);
        setHintText(hintText);
    }

    private void setCounterValue(float counterValue) {
        numberET.setText(String.valueOf(counterValue));
    }


    private void setupNumberEditTextChangeListener() {
        numberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString()))
                    parseValue(s.toString());
                else
                    setCounterValue(DEFAULT_START_VALUE);
            }
        });
    }

    private void parseValue(String value) {
        try {
            counterValue = Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            numberET.setError("Dane wejściowe nie są liczbą");
        }
    }

    public void addCustomClickListener(CustomClickListener customClickListener) {
        this.customClickListener = customClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view == minusBtn) {
            if (customClickListener != null)
                customClickListener.onMinusClick();
            counterValue -= tickValue;
        } else if (view == plusBtn) {
            if (customClickListener != null)
                customClickListener.onPlusClick();
            counterValue += tickValue;
        }
        setCounterValue(counterValue);
    }

    public Drawable getMinusBtnIcon() {
        return minusBtn.getDrawable();
    }

    public void setMinusBtnIcon(Drawable minusIconDrawable) {
        if (minusIconDrawable != null)
            minusBtn.setImageDrawable(minusIconDrawable);
    }

    public Drawable getPlusBtnIcon() {
        return plusBtn.getDrawable();
    }

    public void setPlusBtnIcon(Drawable plusIconDrawable) {
        if (plusIconDrawable != null)
            plusBtn.setImageDrawable(plusIconDrawable);
    }

    private void setIconsTint(int tintColor, Drawable minusIcon, Drawable plusIcon) {
        if (tintColor != EMPTY) {
            minusIcon.setTint(tintColor);
            plusIcon.setTint(tintColor);
        } else {
            resetTint();
        }
    }

    private void resetTint() {
        ImageViewCompat.setImageTintList(minusBtn, null);
        ImageViewCompat.setImageTintList(plusBtn, null);
    }

    public Drawable getBtnsBackround() {
        return plusBtn.getBackground();
    }

    public void setBtnsBackground(Drawable btnsBackgroundDrawable) {
        minusBtn.setBackground(btnsBackgroundDrawable);
        plusBtn.setBackground(btnsBackgroundDrawable);
    }

    public String getHintText() {
        return hintTV.getText().toString();
    }

    public void setHintText(String text) {
        if (text != null && !TextUtils.isEmpty(text))
            hintTV.setText(text);
    }
}
