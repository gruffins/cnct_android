package com.ryanfung.cnct.contracts;

public interface LaunchContract {

    interface View extends Contract.View {
        void onSetup();
        void onAuthed();
        void onWelcome();
    }

    interface Presenter extends Contract.Presenter {
        void checkState(boolean hasPermission);
    }
}
