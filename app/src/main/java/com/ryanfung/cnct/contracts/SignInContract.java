package com.ryanfung.cnct.contracts;

public interface SignInContract {
    interface View extends Contract.View {
        void onAuthenticationSuccess();
        void onAuthenticationFailure();
    }

    interface Presenter extends Contract.Presenter {
        void authenticate(String email, String password);
    }
}
