package com.abbtu.btutils.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.abbtu.btutils.util.input.InputAccountFilter;
import com.abbtu.btutils.util.input.InputByteLengthFilter;
import com.abbtu.btutils.util.input.InputNumLengthFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <b></b><br>
 *
 * @author michaellee <br> create at 2019/3/4 19:46
 */
public class ViewHelper {

    /**
     * 设置资产界面不足时循环闪烁的动画
     *
     * @param view
     * @param alpha    最低透明度
     * @param duration 单次渐变时间（一次变淡变暗循环是两次渐变时间）
     */
    public static void setResourceAnimation(View view, float alpha, long duration) {
        AlphaAnimation animation = new AlphaAnimation(1, alpha);
        animation.setStartOffset(500);
        animation.setDuration(duration);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(animation);
        view.startAnimation(set);
    }


    /**
     * 让一个输入框只能输入指定位数小数
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText, final int maxPoint) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > maxPoint) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + maxPoint + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }


    /**
     * 让一个输入框只能输入指定位数小数 和整数位
     *
     * @param editText   EditText
     * @param maxInteger 最大整数位数
     * @param maxPoint   最大小数位数
     *                   create by ggband
     */
    public static void setPricePointWithInteger(final EditText editText, final int maxPoint, final int maxInteger, InputFilter... inputFilters) {

        if (inputFilters == null || inputFilters.length == 0) {
            editText.setFilters(new InputFilter[]{new InputNumLengthFilter(maxPoint, maxInteger)});
        } else {
            InputFilter[] newInputFilters = new InputFilter[inputFilters.length + 1];
            System.arraycopy(inputFilters, 0, newInputFilters, 0, inputFilters.length);
            newInputFilters[inputFilters.length] = new InputNumLengthFilter(maxPoint, maxInteger);
            editText.setFilters(newInputFilters);
        }
    }

    public static void setPriceMaxPointWithInteger(final EditText editText, final int maxPoint, final int maxInteger, InputFilter... inputFilters) {
        if (inputFilters == null || inputFilters.length == 0) {
            editText.setFilters(new InputFilter[]{new InputNumLengthFilter(maxPoint, maxInteger)});
        } else {
            InputFilter[] newInputFilters = new InputFilter[inputFilters.length + 1];
            System.arraycopy(inputFilters, 0, newInputFilters, 0, inputFilters.length);
            newInputFilters[inputFilters.length] = new InputNumLengthFilter(maxPoint, maxInteger);
            editText.setFilters(newInputFilters);
        }
    }

    public static void setAccountCheck(final EditText editText, String regex) {
        editText.setFilters(new InputFilter[]{new InputAccountFilter(regex)});
    }


    public static void setEosAccountCheck(final EditText editText, String regex) {
        editText.setFilters(new InputFilter[]{new InputAccountFilter(regex), new InputFilter.LengthFilter(12)});
    }


    /**
     * 保留小数
     *
     * @param value
     * @param dotNum
     * @return
     */
    public static String formatMoney(double value, int dotNum) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(dotNum, RoundingMode.HALF_UP);
            return bd.toString();
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }


    public static void setOnTouchVisual(View view) {
        view.setOnTouchListener(onTouchListener);
    }

    private static Interpolator interpolator = new DecelerateInterpolator();

    private static View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).setInterpolator(interpolator);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setInterpolator(interpolator);
                    break;
            }
            return false;
        }
    };


    /**
     * 设置GridView根据item来显示高度
     *
     * @param gridView
     * @param cols     每一行的个数
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setGridViewHeightBasedOnChildren(GridView gridView,
                                                        int cols) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        try {
            View view = listAdapter
                    .getView(0, gridView.getChildAt(0), gridView);
            view.measure(0, 0);
            totalHeight = view.getMeasuredHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int lines = (listAdapter.getCount() + (cols - 1)) / cols;
        int padding = 0;
        try {
            padding = gridView.getVerticalSpacing();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPadding = padding * lines;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight * lines + totalPadding;

        gridView.setLayoutParams(params);
    }

    /**
     * EditText 字节数限制
     *
     * @param size     限制长度
     * @param editText EditText
     */
    public static void etTextViewByteSizeLimit(int size, EditText editText) {
        editText.setFilters(new InputFilter[]{new InputByteLengthFilter(size)});
    }


    // 单独设置EditText控件中hint文本的字体大小，可能与EditText文字大小不同
// @param editText 输入控件
// @param hintText hint的文本内容
// @param textSize hint的文本的文字大小（以dp为单位设置即可）
    public static void setHintTextSize(EditText editText, String hintText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


}
