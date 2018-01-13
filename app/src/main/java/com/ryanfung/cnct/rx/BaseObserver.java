package com.ryanfung.cnct.rx;

import io.reactivex.observers.DisposableObserver;

public class BaseObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {}

    @Override
    public void onError(Throwable e) {}

    @Override
    public void onComplete() {}
}
