package br.com.lapic.thomas.fsm_app.data.model;

/**
 * Created by thomas on 06/09/17.
 */

public class Device {

    private String id;
    private String macAddres;
    private String ipAddress;
    private String port;

    public Device(String id, String macAddres, String ipAddress, String port) {
        this.id = id;
        this.macAddres = macAddres;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public String getMacAddres() {
        return macAddres;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMacAddres(String macAddres) {
        this.macAddres = macAddres;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
