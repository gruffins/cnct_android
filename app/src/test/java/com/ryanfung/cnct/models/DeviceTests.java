package com.ryanfung.cnct.models;

import com.ryanfung.cnct.builders.DeviceBuilder;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeviceTests {

    private Device device;

    @Before
    public void setup() {
        device = new DeviceBuilder().build();
    }

    @Test
    public void equalsId() {
        device.id = "test";
        Device other = new DeviceBuilder().id("test").build();

        assertThat(device).isEqualTo(other);
    }

    @Test
    public void equalsUuid() {
        Device other = new DeviceBuilder().uuid(device.uuid).build();

        assertThat(device).isEqualTo(other);
    }

    @Test
    public void newDeviceHasUUID() {
        assertThat(Device.newDevice().uuid).isNotEmpty();
    }
}
