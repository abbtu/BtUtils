package com.abbtu.btutils.util.input;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.nio.charset.Charset;

/**
 * ggband
 * 限制 小数和整数的位数
 */
public class InputByteLengthFilter implements InputFilter {

    private int max;

    public InputByteLengthFilter(int max) {
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int len = 0;
        boolean more = false;
        do {
            SpannableStringBuilder builder =
                    new SpannableStringBuilder(dest).replace(dstart, dend, source.subSequence(start, end));
            len = builder.toString().getBytes(Charset.defaultCharset()).length;
            more = len > max;
            if (more) {
                end--;
                source = source.subSequence(start, end);
            }
        } while (more);
        return source;
    }


}
