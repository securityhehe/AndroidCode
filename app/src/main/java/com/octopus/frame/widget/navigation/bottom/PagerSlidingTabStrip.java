/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.octopus.frame.widget.navigation.bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

/**
 *  @author  zshh
 *  create by 2019/04/12
 */

public class PagerSlidingTabStrip extends HorizontalScrollView {

    private boolean allowMoreLine = false;
    //tab个数。
    private int tabCount;
    private int currentPosition = 0;
    private int lastPosition = 0;
    private int tabBackgroundResId = android.R.drawable.screen_background_dark;

    //当前偏移量。
    private float currentPositionOffset = 0f;
    private boolean shouldExpand = false;
    private boolean textAllCaps = false;

   //指示器滑动位置偏移量。
    private int scrollOffset = 0;

    //指示器的高度,宽度,指示器颜色。
    private int indicatorWidth = 0;
    private int indicatorHeight = 8;
    private int indicatorColor = 0xFF666666;

    //TAB之间的Padding
    //未选中(文字大小，文字颜色), 选中文字大小和颜色。文字选中是否加粗。
    private boolean checkedBold = false;
    private int tabTextSize = 15;
    private int tabTextSelectedSize = 0; //选中文字的大小
    private int tabTextColor = 0xFF666666;
    private int tabTextSelectedColor = 0xFF3775A3;
    private int tabPadding = 24;

    //TAB之间的竖向分割线[颜色，宽度，上线Padding]
    private int dividerColor = 0x1A000000;
    private int dividerWidth = 1;
    private int dividerPadding = 12;

    //底部分割线的高度和颜色。
    private int underlineColor = 0xBCBCBC;
    private int underlineHeight = 0;

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;
    private LinearLayout tabsContainer;
    private ViewPager pager;

    private Paint rectPaint;
    private Paint dividerPaint;
    private Paint mTabTextPaint;

    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;
    private int lastScrollX = 0;
    private Locale locale;
    private int displayWidth;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        // 屏幕宽度（像素)
        displayWidth  = getResources().getDisplayMetrics().widthPixels;

        //各种Paint初始化。
        mTabTextPaint = new Paint();
        mTabTextPaint.setTextSize(tabTextSize);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        //布局参数初始化。
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabsContainer.setPadding(tabPadding,0,tabPadding,0);
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        if (!allowMoreLine) {
            tab.setSingleLine();
        }
        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        LinearLayout.LayoutParams layoutParams =  shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams;
        layoutParams.bottomMargin = underlineHeight + indicatorHeight;
        tabsContainer.addView(tab, position, layoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                if (i == 0) {
                    tab.setTextColor(tabTextSelectedColor);
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSelectedSize == 0 ? tabTextSize : tabTextSelectedSize);
                } else {
                    tab.setTextColor(tabTextColor);
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                }
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }
    }

    public void updateTextTabStyles(List<String> list) {
        for (int i = 0; i < tabCount && i < list.size(); i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                if (i == 0) {
                    tab.setText(list.get(i));
                    tab.setTextColor(tabTextSelectedColor);
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSelectedSize == 0 ? tabTextSize : tabTextSelectedSize);
                } else {
                    tab.setText(list.get(i));
                    tab.setTextColor(tabTextColor);
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                }
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }

    public void updateSelectedTextColor(int position) {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextColor(tabTextColor);
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                if(checkedBold) {
                    tab.getPaint().setFakeBoldText(false);
                }
            }
        }

        View v = tabsContainer.getChildAt(position);
        if (v instanceof TextView) {
            ((TextView) v).setTextColor(tabTextSelectedColor);
            if (checkedBold) {
                ((TextView) v).getPaint().setFakeBoldText(true);
            }
            ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSelectedSize == 0 ? tabTextSize : tabTextSelectedSize);
        }
    }

    //头部会固定不动，但是滚动过于突然，暂时舍弃
    private void scrollToChild(int position) {
        View child = tabsContainer.getChildAt(position);
        int left = child.getLeft();
        int childWidth = child.getWidth();
        int parentWidth = tabsContainer.getWidth();
        if (position > lastPosition) {    //右边滑动
            if (left + tabPadding * 2 + childWidth / 3 >= displayWidth) {     //容错,这点很重要，有可能某个tab的右边缘屏幕的右边缘有一定的距离
                scrollToVisible(childWidth, 0);
            }
        } else {                                         //向左滑
            if (parentWidth - left > displayWidth) {
                scrollToVisible(-childWidth, 0);
            }
        }
        lastPosition = position;
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private void scrollToVisible(final int x, final int y) {
        int lastWidth = getScrollX();
        scrollTo(lastWidth + x, y);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();
        float lineLeft = 0;
        float lineRight = 0;

        final View currentTab = tabsContainer.getChildAt(currentPosition);

        lineLeft = currentTab.getLeft();
        lineRight = currentTab.getRight();

        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        int tabsContainerWidth =  tabsContainer.getWidth();
        int paddingLeftRight = getPaddingLeft();

        rectPaint.setColor(indicatorColor);
        drawIndicatorLine(canvas, height, lineLeft, lineRight, currentTab, paddingLeftRight);

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight , tabsContainerWidth + paddingLeftRight* 2, height, rectPaint);

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++ ) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }

    }

    /**
     * draw indicator line
     * @param canvas canvas
     * @param height height
     * @param lineLeft lineLeft
     * @param lineRight lineRight
     * @param currentTab currentTab
     * @param paddingLeftRight paddingLeftRight
     */
    private static final int warp = -1;
    private static final int match = -2;
    private int textWidth  = 0 ;
    private void drawIndicatorLine(Canvas canvas, int height, float lineLeft, float lineRight, View currentTab, int paddingLeftRight) {
        RectF r2 = new RectF();

        if(indicatorWidth == warp){
            if (currentTab instanceof TextView) {
                textWidth = (int) mTabTextPaint.measureText(((TextView) currentTab).getText().toString());
            }
            int width = currentTab.getWidth();
            r2.left = lineLeft + (width - textWidth) / 2 + paddingLeftRight;
            r2.top = height - indicatorHeight - underlineHeight;
            r2.right = lineLeft + (width - textWidth) / 2 + textWidth + paddingLeftRight;                                //右边
            r2.bottom = height - underlineHeight;
        }else if(indicatorWidth == match){
            r2.left = lineLeft;
            r2.top = height - indicatorHeight - underlineHeight;
            r2.right = lineRight;
            r2.bottom = height - underlineHeight;
        }else{
            int width = currentTab.getWidth();
            r2.left = lineLeft + (width - indicatorWidth) / 2 + paddingLeftRight;
            r2.top = height - indicatorHeight - underlineHeight;
            r2.right = lineLeft + (width - indicatorWidth) / 2 + indicatorWidth + paddingLeftRight;                                //右边
            r2.bottom = height - underlineHeight;
        }
        canvas.drawRoundRect(r2, 30, 30, rectPaint);
    }

    /**
     * PageListener
      */
    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            updateSelectedTextColor(position);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public void setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
        updateTabStyles();
    }


    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }


    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextSelectedColor() {
        return tabTextSelectedColor;
    }


    public void setTextSelectedColor(int tabTextSelectedColor) {
        this.tabTextSelectedColor = tabTextSelectedColor;
        updateTabStyles();
    }


    public void setTextSelectedColorResource(int resId) {
        this.tabTextSelectedColor = getResources().getColor(resId);
        updateTabStyles();
    }

    /**
     * 设置选中当前选中的字体大小，
     * 如果没有设置的话（没有调用过此方法）
     * 默认会跟其他tab的字体大小一致
     *
     * @param fontSize
     */
    public void setTextSelectedSize(int fontSize) {
        this.tabTextSelectedSize = fontSize;
        updateTabStyles();
    }


    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setCheckedBold(boolean checkedBold) {
        this.checkedBold = checkedBold;
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
        invalidate();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        invalidate();
    }

    public void allowMoreLine() {
        this.allowMoreLine = true;
    }
}
