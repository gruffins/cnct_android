package com.ryanfung.cnct.rx;

import com.ryanfung.cnct.exceptions.RSAKeyPairGeneratorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RSAKeyPairGeneratorTests {

    @Mock
    private KeyPairGenerator kpg;
    private RSAKeyPairGenerator generator;
    private TestObserver<KeyPair> observer;
    private KeyPair keyPair;

    @Before
    public void setup() {
        initMocks(this);

        generator = new RSAKeyPairGenerator(kpg);
        observer = new TestObserver<>();
        keyPair = new KeyPair(mock(PublicKey.class), mock(PrivateKey.class));
    }

    @Test
    public void createEmitsOnNext() {
        when(kpg.genKeyPair()).thenReturn(keyPair);
        generator.create().subscribe(observer);

        observer.assertValueCount(1);
    }

    @Test
    public void createEmitsOnComplete() {
        when(kpg.genKeyPair()).thenReturn(keyPair);
        generator.create().subscribe(observer);

        observer.assertComplete();
    }

    @Test
    public void createEmitsOnError() {
        when(kpg.genKeyPair()).thenThrow(new RuntimeException("test"));

        generator.create().subscribe(observer);
        observer.assertError(RSAKeyPairGeneratorException.class);
    }
}
