package com.ryanfung.cnct.contracts;

public interface Contract {

    interface View {

    }

    interface Presenter {
        void viewDetached();
    }

}
