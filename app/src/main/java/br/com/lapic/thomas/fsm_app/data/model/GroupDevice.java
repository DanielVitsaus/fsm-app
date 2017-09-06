package br.com.lapic.thomas.fsm_app.data.model;

import java.util.ArrayList;

import br.com.lapic.thomas.fsm_app.helper.StringHelper;

/**
 * Created by thomas on 06/09/17.
 */

public class GroupDevice {

    private String ip;
    private ArrayList<Device> devices;

    public GroupDevice(String ip) {
        this.ip = ip;
        this.devices = new ArrayList<>();
    }

    public String getId() {
        return ip;
    }

    public Device getDevice(int index) {
        return this.devices.get(index);
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }
}
