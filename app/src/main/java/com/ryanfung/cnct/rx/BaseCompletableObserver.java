package com.ryanfung.cnct.rx;

import io.reactivex.observers.DisposableCompletableObserver;

public class BaseCompletableObserver extends DisposableCompletableObserver {
    @Override
    public void onComplete() {}

    @Override
    public void onError(Throwable e) {}
}
