package com.abbtu.btutils.util.input;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * <description>
 * 内容限制
 * <p>
 * author: Rabber y
 * 2020/1/16 14:33
 */
public class InputAccountFilter implements InputFilter {

    private String accountRegex;

    public InputAccountFilter(String regex) {
        this.accountRegex = regex;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {

        boolean isMarch = Pattern.matches(accountRegex, charSequence.toString());
        if (!isMarch) {
            return "";
        }
        return null;
    }


}
