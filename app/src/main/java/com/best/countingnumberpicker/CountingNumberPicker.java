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
import android.widget.Toast;

import androidx.core.widget.ImageViewCompat;

public class CountingNumberPicker extends LinearLayout implements View.OnClickListener {
    private final static float DEFAULT_START_VALUE = 0F;
    private final static float DEFAULT_MIN_VALUE = -10000F;
    private final static float DEFAULT_MAX_VALUE = 10000F;

    private final static float DEFAULT_TICK_VALUE = 1F;
    private final static int EMPTY = 0;
    private float counterValue = DEFAULT_START_VALUE;
    private float tickValue = DEFAULT_TICK_VALUE;
    private float minValue, maxValue;
    private CustomClickListener customClickListener;
    private ImageButton minusBtn;
    private ImageButton plusBtn;
    private EditText numberInputET;
    private TextView unitCodeTV;

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
        numberInputET = findViewById(R.id.number_et);
        setupNumberEditTextChangeListener();
        minusBtn = findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(this);
        plusBtn = findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(this);
        unitCodeTV = findViewById(R.id.unit_code);
    }

    private void loadValuesFromAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountingNumberPicker, EMPTY, EMPTY);
        Drawable minusIcon, plusIcon;
        int tintColor;
        Drawable minusBtnBg, plusBtnBg;
        Drawable inputBackground;
        boolean shouldShowUnitCode;
        String unitCode;

        try {
            counterValue = typedArray.getFloat(R.styleable.CountingNumberPicker_startValue, DEFAULT_START_VALUE);
            tickValue = typedArray.getFloat(R.styleable.CountingNumberPicker_tickValue, DEFAULT_TICK_VALUE);
            minValue = typedArray.getFloat(R.styleable.CountingNumberPicker_minValue, DEFAULT_START_VALUE);
            maxValue = typedArray.getFloat(R.styleable.CountingNumberPicker_maxValue, DEFAULT_MAX_VALUE);
            shouldShowUnitCode = typedArray.getBoolean(R.styleable.CountingNumberPicker_showUnitCode, false);
            unitCode = typedArray.getString(R.styleable.CountingNumberPicker_unitCode);
            minusIcon = typedArray.getDrawable(R.styleable.CountingNumberPicker_minusBtnIconDrawable);
            plusIcon = typedArray.getDrawable(R.styleable.CountingNumberPicker_plusBtnIconDrawable);
            tintColor = typedArray.getColor(R.styleable.CountingNumberPicker_btnsIconTintColor, EMPTY);
            minusBtnBg = typedArray.getDrawable(R.styleable.CountingNumberPicker_btnsBackgroundDrawable);
            plusBtnBg = typedArray.getDrawable(R.styleable.CountingNumberPicker_btnsBackgroundDrawable);
            inputBackground = typedArray.getDrawable(R.styleable.CountingNumberPicker_inputBackgroundDrawable);
        } finally {
            typedArray.recycle();
        }
        if (shouldShowUnitCode)
            showUnitCode(unitCode);
        setCounterValue(counterValue);
        setMinusBtnIcon(minusIcon);
        setPlusBtnIcon(plusIcon);
        setIconsTint(tintColor, minusIcon, plusIcon);
        setBtnsBackground(minusBtnBg, plusBtnBg);
        setInputBackground(inputBackground);
    }

    private void showUnitCode(String unitCode) {
        if (unitCodeTV != null && unitCode != null && !TextUtils.isEmpty(unitCode)) {
            unitCodeTV.setVisibility(VISIBLE);
            unitCodeTV.setText(unitCode);
        }
    }

    private void setCounterValue(float counterValue) {

        if (numberInputET != null)
            numberInputET.setText(String.valueOf(counterValue));
    }

    private void setButtonsEnability() {
        enableButtons(true);
        if (counterValue >= maxValue) {
            plusBtn.setEnabled(false);
            return;
        } else if (counterValue <= minValue) {
            minusBtn.setEnabled(false);
            return;
        }
    }

    private void enableButtons(boolean enable) {
        plusBtn.setEnabled(enable);
        minusBtn.setEnabled(enable);
    }

    private void setupNumberEditTextChangeListener() {
        numberInputET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString()) && areNumbersValid(counterValue)) {
                    parseValue(s.toString());
                    setButtonsEnability();
                } else {
                    setCounterValue(DEFAULT_START_VALUE);
                    setCursorOnInputEnd();
                }
            }
        });
    }

    private boolean areNumbersValid(float inputValue) {
        if (minValue == 0f && maxValue == 0f)
        {
            if (inputValue >= minValue && inputValue <= maxValue)
                return true;
        }
        else
        if (inputValue >= DEFAULT_MIN_VALUE && inputValue <= DEFAULT_MAX_VALUE)
            return true;

        Toast.makeText(getContext(), "Niepoprawna wartość, ustawiono wartość domyślną.", Toast.LENGTH_LONG).show();
        return false;
    }

    private void setCursorOnInputEnd() {
        if (numberInputET != null) {
            numberInputET.setSelection(numberInputET.length());
        }
    }

    private void parseValue(String value) {
        try {
            counterValue = Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            numberInputET.setError("Dane wejściowe nie są liczbą");
        }
    }

    public void addCustomClickListener(CustomClickListener customClickListener) {
        this.customClickListener = customClickListener;
    }

    @Override
    public void onClick(View view) {
        setButtonsEnability();

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
        setCursorOnInputEnd();
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

    public void setBtnsBackground(Drawable minusBtnBg, Drawable plusBtnBg) {
        setButtonBackground(minusBtn, minusBtnBg);
        setButtonBackground(plusBtn, plusBtnBg);
    }

    private void setButtonBackground(ImageButton button, Drawable bgDrawable) {
        if (button != null) {
            button.setBackground(bgDrawable);
        }
    }

    public void setInputBackground(Drawable bgDrawable) {
        if (numberInputET != null)
            numberInputET.setBackground(bgDrawable);
    }
}
