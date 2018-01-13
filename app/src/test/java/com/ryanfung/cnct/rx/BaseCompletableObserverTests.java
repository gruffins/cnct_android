package com.ryanfung.cnct.rx;

import org.junit.Before;
import org.junit.Test;

public class BaseCompletableObserverTests {

    private BaseCompletableObserver observer;

    @Before
    public void setup() {
        observer = new BaseCompletableObserver();
    }

    @Test
    public void onCompleteDoesntThrowExceptions() {
        observer.onComplete();
    }

    @Test
    public void onErrorDoesntThrowExceptions() {
        observer.onError(null);
    }
}
