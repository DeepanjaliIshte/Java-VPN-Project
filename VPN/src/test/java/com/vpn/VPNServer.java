package com.vpn;

public class VPNServer {
    private String name;
    private String location;
    private String ipAddress;
    private int ping;
    private boolean isOnline;

    public VPNServer(String name, String location, String ipAddress, int ping) {
        this.name = name;
        this.location = location;
        this.ipAddress = ipAddress;
        this.ping = ping;
        this.isOnline = true;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPing() {
        return ping;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return name + " (" + location + ") - " + ping + "ms";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VPNServer vpnServer = (VPNServer) obj;
        return ipAddress.equals(vpnServer.ipAddress);
    }

    @Override
    public int hashCode() {
        return ipAddress.hashCode();
    }
}
