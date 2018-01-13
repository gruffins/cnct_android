package com.ryanfung.cnct.contracts;

import com.ryanfung.cnct.rx.RSAKeyPairGenerator;

public interface SetupContract {

    interface View extends Contract.View {
        void onRequestPermission();
        void onFailed(Throwable ex);
        void onSuccess();
    }

    interface Presenter extends Contract.Presenter {
        void setup(String authorization, RSAKeyPairGenerator generator, boolean hasPermission);
    }
}
