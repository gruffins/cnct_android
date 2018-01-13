package com.ryanfung.cnct.builders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ryanfung.cnct.models.Device;

import java.util.UUID;

public class DeviceBuilder extends Builder<Device> {

    private String id;
    private String uuid = UUID.randomUUID().toString();

    public DeviceBuilder id(@Nullable String id) {
        this.id = id;
        return this;
    }

    public DeviceBuilder uuid(@NonNull String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public Device build() {
        Device device = new Device();
        device.id = id;
        device.uuid = uuid;
        return device;
    }
}
