package com.vpn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VPNManager {
    private List<VPNServer> availableServers;
    private VPNServer currentServer;
    private boolean isConnected;
    private VPNTunnel tunnel;

    public VPNManager() {
        initializeServers();
        this.tunnel = new VPNTunnel();
        this.isConnected = false;
    }

    private void initializeServers() {
        availableServers = new ArrayList<>();
        availableServers.add(new VPNServer("India", "Mumbai", "203.0.113.99", 20));
        availableServers.add(new VPNServer("US East", "New York", "198.51.100.1",45)); 
		  availableServers.add(new VPNServer("US West", "Los Angeles", "203.0.113.1", 32)); 
		  availableServers.add(new VPNServer("Europe", "London", "192.0.2.1", 67));
		  availableServers.add(new VPNServer("Asia", "Tokyo","198.51.100.2", 89));
		  availableServers.add(new VPNServer("Canada", "Toronto","203.0.113.2", 23));
		  availableServers.add(new VPNServer("Australia","Sydney", "192.0.2.2", 12));
		 
    }

    public List<VPNServer> getAvailableServers() {
        return new ArrayList<>(availableServers);
    }

    public boolean connect(VPNServer server) {
        try {
            // Simulate connection process
            Thread.sleep(2000); // Simulate connection delay
            
            if (tunnel.establishConnection(server)) {
                this.currentServer = server;
                this.isConnected = true;
                System.out.println("Connected to VPN server: " + server.getName());
                return true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public void disconnect() {
        if (isConnected) {
            tunnel.closeConnection();
            this.currentServer = null;
            this.isConnected = false;
            System.out.println("Disconnected from VPN");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public VPNServer getCurrentServer() {
        return currentServer;
    }

    public String getConnectionStatus() {
        if (isConnected && currentServer != null) {
            return "Connected to " + currentServer.getName();
        }
        return "Disconnected";
    }

    public CompletableFuture<Boolean> connectAsync(VPNServer server) {
        return CompletableFuture.supplyAsync(() -> connect(server));
    }
}


