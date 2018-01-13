package com.ryanfung.cnct.rx;

import org.junit.Before;
import org.junit.Test;

public class BaseObserverTests {

    private BaseObserver<Object> observer;

    @Before
    public void setup() {
        observer = new BaseObserver<>();
    }

    @Test
    public void onNextDoesntThrowExceptions() {
        observer.onNext(null);
    }

    @Test
    public void onErrorDoesntThrowExceptions() {
        observer.onError(null);
    }

    @Test
    public void onCompleteDoesntThrowExceptions() {
        observer.onComplete();
    }
}
