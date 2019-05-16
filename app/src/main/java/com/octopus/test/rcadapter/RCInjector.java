package com.octopus.test.rcadapter;


import com.octopus.test.rcadapter.viewinjector.IViewInjector;

/**
 * Created by octopus on 2019/01/13
 */

public interface RCInjector<T> {
    void onInject(T data, IViewInjector injector);
}
