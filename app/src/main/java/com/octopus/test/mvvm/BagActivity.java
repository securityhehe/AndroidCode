package com.octopus.test.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.octopus.test.R;

public class BagActivity extends AppCompatActivity {

    private BagViewModel viewModel;
    private TextView mBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bag_test);
        mBody = findViewById(R.id.body);
        initViewModel();
        viewModel.queryBaidu();

    }

    private void initViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(BagViewModel.class);
        viewModel.data.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               mBody.setText(s);
            }
        });

    }
}
