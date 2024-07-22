package com.gj.gjchungsa.introduction;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan {
    private Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeface(ds);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeface(paint);
    }

    private void applyCustomTypeface(TextPaint paint) {
        Typeface oldTypeface = paint.getTypeface();
        int oldStyle = (oldTypeface != null) ? oldTypeface.getStyle() : Typeface.NORMAL;
        Typeface customTypeface = Typeface.create(typeface, oldStyle);
        paint.setTypeface(customTypeface);
    }
}
