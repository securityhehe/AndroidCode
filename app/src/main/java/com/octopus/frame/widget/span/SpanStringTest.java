package com.octopus.frame.widget.span;

import android.content.Context;
import android.text.SpannableString;
import com.octopus.test.R;

public class SpanStringTest {

    public static SpannableString test(Context context){
        SpannableString spannableString = new SpannableString("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        VerticalImageSpan span = new VerticalImageSpan(context,R.drawable.test);
        spannableString.setSpan(span,0,1,SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;

    }



}
