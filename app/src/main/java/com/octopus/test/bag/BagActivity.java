package com.octopus.test.bag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.octopus.test.R;
import com.octopus.frame.widget.navigation.bottom.NavigationPagerFragmentContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BagActivity extends AppCompatActivity {

    private List<Fragment> fragments;
    private List<String> titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);

        NavigationPagerFragmentContainer pagerContainer = findViewById(R.id.rcNavigationPagerFragment);
        fragments = new ArrayList<>();
        fragments.add(new BagFragment());
        fragments.add(new GiftFragment());
        titles = Arrays.asList("test1","test2");
        pagerContainer.setData(titles,fragments);


    }
}
