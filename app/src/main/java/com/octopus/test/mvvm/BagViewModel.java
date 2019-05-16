package com.octopus.test.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

public class BagViewModel extends AndroidViewModel {
    MutableLiveData<String> data = new MutableLiveData<>();
    public void queryBaidu() {
       model.queryBaidu();
    }

    public class Bag{

    }

    MutableLiveData<List<Bag>> listLiveData = new MutableLiveData();
    private BagModel model;

    public BagViewModel(Application application){
        super(application);
        model =  new BagModel();
        model.setCallback(new BagModel.AA() {
            @Override
            public void getBody(String body) {
                data.setValue(body);
            }
        });
    }

    public void queryBag() {
        List<Bag>  bag = model.getBag();
        listLiveData.getValue().add(1,new Bag());
        listLiveData.setValue(bag);
    }

    public LiveData<List<Bag>> getListLiveData() {
        return listLiveData;
    }

}
