package com.ryanfung.cnct.rx;

import android.support.annotation.Nullable;

import com.ryanfung.cnct.exceptions.RSAKeyPairGeneratorException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RSAKeyPairGenerator {

    private static final String RSA = "RSA";
    private static final int KEY_SIZE = 2048;

    @Nullable
    private KeyPairGenerator generator;

    public RSAKeyPairGenerator() {
        this(null);
    }

    public RSAKeyPairGenerator(@Nullable KeyPairGenerator generator) {
        this.generator = generator;
    }

    public Observable<KeyPair> create() {
        return Observable.create(new ObservableOnSubscribe<KeyPair>() {
            @Override
            public void subscribe(ObservableEmitter<KeyPair> emitter) throws Exception {
                try {
                    if (generator == null) {
                        generator = KeyPairGenerator.getInstance(RSA);
                        generator.initialize(KEY_SIZE);
                    }

                    emitter.onNext(generator.genKeyPair());
                    emitter.onComplete();
                } catch (Exception ex) {
                    emitter.onError(new RSAKeyPairGeneratorException(ex));
                }
            }
        });
    }

}
