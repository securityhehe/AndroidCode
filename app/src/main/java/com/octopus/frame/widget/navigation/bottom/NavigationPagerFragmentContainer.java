package com.octopus.frame.widget.navigation.bottom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.octopus.test.R;

import java.util.List;

/**
 *
 * @author zshh create by 2019/4/9
 *
 */

public  class NavigationPagerFragmentContainer extends LinearLayout {

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private NavigationFragmentPagerAdapter mPagerAdapter;
    private ViewPager mPager;

    public NavigationPagerFragmentContainer(Context context) {
        this(context,null);
    }

    public NavigationPagerFragmentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        initEvent();
    }

    //指示器宽高,颜色。
    private int indicatorWidth = 0 ;
    private int indicatorHeight = 0 ;
    private int indicatorColor = 0xffffff;

    //底部分割线的高度和颜色。
    private int underlineColor = 0xcccccc;
    private int underlineHeight = 2;

    //tab之间的分割线。
    private int dividerColor = 0xcccccc;
    private int dividerPadding = 10;
    private int dividerWidth = 2;

    //tab字体大小，颜色。选中是否加粗。文字是否全部大小。背景颜色。
    private int tabTextSize;
    private int tabTextColor;
    private int tabSelectTextSize;
    private int tabSelectTextColor;
    private boolean tabCheckedBold = false;
    private int tabBackground = android.R.color.black;

    //tab的高度。
    private int navigationHeight = getResources().getDimensionPixelSize(R.dimen._60);

    //padding
    private int tabPaddingLeftRight = 0 ;
    private boolean tabShouldExpand = true;

    private void initView(AttributeSet attrs) {

        mPagerSlidingTabStrip =  findViewById(R.id.navigation);
        mPager = findViewById(R.id.viewPager);

        mPagerAdapter = new NavigationFragmentPagerAdapter(((FragmentActivity)getContext()).getSupportFragmentManager());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationPagerFragmentContainer);

        indicatorWidth = a.getLayoutDimension(R.styleable.NavigationPagerFragmentContainer_rcIndicatorWidth,indicatorWidth);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcIndicatorHeight,indicatorHeight);
        indicatorColor = a.getColor(R.styleable.NavigationPagerFragmentContainer_rcIndicatorColor,indicatorColor);

        underlineColor = a.getColor(R.styleable.NavigationPagerFragmentContainer_rcUnderlineColor,underlineColor);
        underlineHeight = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcUnderlineHeight, underlineHeight);

        dividerColor = a.getColor(R.styleable.NavigationPagerFragmentContainer_rcDividerColor, dividerColor);
        dividerPadding = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcDividerPadding,dividerPadding);
        dividerWidth = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcDividerWidth ,dividerWidth);

        tabTextSize = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcTabTextSize,tabTextSize);
        tabSelectTextSize = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcTabTextSelectedSize, tabSelectTextSize);
        tabTextColor = a.getColor(R.styleable.NavigationPagerFragmentContainer_rcTabTextColor,tabTextColor);
        tabSelectTextColor  = a.getColor(R.styleable.NavigationPagerFragmentContainer_rcTabTextSelectedColor,tabSelectTextColor);
        tabCheckedBold = a.getBoolean(R.styleable.NavigationPagerFragmentContainer_rcTabCheckedBold,tabCheckedBold);
        tabBackground = a.getResourceId(R.styleable.NavigationPagerFragmentContainer_rcTabBackground, tabBackground);

        //navigation的高度。
        navigationHeight = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcNavigationHeight, navigationHeight);
        tabBackground = a.getResourceId(R.styleable.NavigationPagerFragmentContainer_rcTabBackground, tabBackground);

        tabPaddingLeftRight = a.getDimensionPixelSize(R.styleable.NavigationPagerFragmentContainer_rcTabPaddingLeftRight,tabPaddingLeftRight);
        tabShouldExpand = a.getBoolean(R.styleable.NavigationPagerFragmentContainer_rcTabShouldExpand, tabShouldExpand);

        a.recycle();

        mPagerSlidingTabStrip.setTextSize(tabTextSize);
        mPagerSlidingTabStrip.setTextSelectedSize(tabSelectTextSize);
        mPagerSlidingTabStrip.setTextColor(tabTextColor);
        mPagerSlidingTabStrip.setTextSelectedColor(tabSelectTextColor);
        mPagerSlidingTabStrip.setCheckedBold(tabCheckedBold);
        mPagerSlidingTabStrip.setTabBackground(tabBackground);

        mPagerSlidingTabStrip.setIndicatorWidth(indicatorWidth);
        mPagerSlidingTabStrip.setIndicatorHeight(indicatorHeight);
        mPagerSlidingTabStrip.setIndicatorColor(indicatorColor);

        mPagerSlidingTabStrip.setUnderlineColor(underlineColor);
        mPagerSlidingTabStrip.setUnderlineHeight(underlineHeight);

        mPagerSlidingTabStrip.setDividerColor(dividerColor);
        mPagerSlidingTabStrip.setDividerPadding(dividerPadding);
        mPagerSlidingTabStrip.setDividerWidth(dividerWidth);

        mPagerSlidingTabStrip.setTabPaddingLeftRight(tabPaddingLeftRight);
        mPagerSlidingTabStrip.setShouldExpand(tabShouldExpand);

        ViewGroup.LayoutParams layoutParams = mPagerSlidingTabStrip.getLayoutParams();
        layoutParams.height = MeasureSpec.EXACTLY | navigationHeight;

    }

    private void initEvent() {
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {}

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    private class NavigationFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;
        private List<String> titles;

        NavigationFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        public List<Fragment> getList() {
            return list;
        }

        void setPagers(List<Fragment> list) {
            this.list = list;
        }

        public List<String> getTitles() {
            return titles;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }
    }

    public void setData(List<String> titles, List<Fragment> fragments) {
        mPagerAdapter.setTitles(titles);
        mPagerAdapter.setPagers(fragments);
        mPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mPager);
    }

    public void setViewPagerPosition(int index) {
        mPager.setCurrentItem(index);
    }

    public int getCurrentItem() {
        return mPager.getCurrentItem();
    }

    public void setOffscreenPageLimit(int offscreenPageLimit) {
        mPager.setOffscreenPageLimit(offscreenPageLimit);
    }

}
