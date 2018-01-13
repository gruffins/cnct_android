package com.ryanfung.cnct.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

public class Device {

    @Nullable
    public String id;

    @NonNull
    public String uuid = "";

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            Device other = (Device) obj;

            return (id != null && id.equals(other.id)) ||
                    uuid.equals(other.uuid);
        }

        return false;
    }

    // =============================================================================================
    // Static
    // =============================================================================================

    public static Device newDevice() {
        Device device = new Device();
        device.uuid = UUID.randomUUID().toString();
        return device;
    }

}
