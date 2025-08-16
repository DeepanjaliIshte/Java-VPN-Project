package com.vpn;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class VPNTunnel {
    private VPNServer connectedServer;
    private AtomicBoolean isConnected;
    private AtomicLong bytesTransferred;
    private Socket tunnelSocket;
    private Thread dataTransferThread;

    public VPNTunnel() {
        this.isConnected = new AtomicBoolean(false);
        this.bytesTransferred = new AtomicLong(0);
    }

    public boolean establishConnection(VPNServer server) {
        try {
            // Simulate VPN tunnel establishment
            System.out.println("Establishing secure tunnel to " + server.getName());
            
            // Simulate handshake process
            performHandshake(server);
            
            // Create mock tunnel socket
            tunnelSocket = createMockTunnelSocket(server);
            
            this.connectedServer = server;
            this.isConnected.set(true);
            
            // Start data transfer simulation
            startDataTransferSimulation();
            
            System.out.println("VPN tunnel established successfully");
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to establish VPN tunnel: " + e.getMessage());
            return false;
        }
    }

    private void performHandshake(VPNServer server) throws InterruptedException {
        System.out.println("Performing handshake with " + server.getIpAddress());
        Thread.sleep(1000); // Simulate handshake delay
        
        // Simulate key exchange
        generateEncryptionKeys();
        
        System.out.println("Handshake completed");
    }

    private void generateEncryptionKeys() {
        // Simulate encryption key generation
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256-bit key
        random.nextBytes(key);
        System.out.println("Encryption keys generated");
    }

    private Socket createMockTunnelSocket(VPNServer server) throws IOException {
        // In a real implementation, this would create an actual socket connection
        // For demonstration, we'll create a mock socket
        return new Socket() {
            @Override
            public boolean isConnected() {
                return VPNTunnel.this.isConnected.get();
            }
            
            @Override
            public void close() throws IOException {
                VPNTunnel.this.isConnected.set(false);
            }
        };
    }

    private void startDataTransferSimulation() {
        dataTransferThread = new Thread(() -> {
            while (isConnected.get()) {
                try {
                    // Simulate data transfer
                    Thread.sleep(1000);
                    long transferred = (long)(Math.random() * 1024 * 1024); // Random bytes
                    bytesTransferred.addAndGet(transferred);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        dataTransferThread.setDaemon(true);
        dataTransferThread.start();
    }

    public void closeConnection() {
        if (isConnected.get()) {
            try {
                isConnected.set(false);
                
                if (tunnelSocket != null && !tunnelSocket.isClosed()) {
                    tunnelSocket.close();
                }
                
                if (dataTransferThread != null) {
                    dataTransferThread.interrupt();
                }
                
                System.out.println("VPN tunnel closed");
                
            } catch (IOException e) {
                System.err.println("Error closing VPN tunnel: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public VPNServer getConnectedServer() {
        return connectedServer;
    }

    public long getBytesTransferred() {
        return bytesTransferred.get();
    }

    public void routeTraffic(byte[] data, String destination) {
        if (!isConnected.get()) {
            throw new IllegalStateException("VPN tunnel is not connected");
        }
        
        // Simulate traffic routing through VPN tunnel
        System.out.println("Routing " + data.length + " bytes to " + destination + 
                          " via " + connectedServer.getName());
        
        // Simulate encryption
        byte[] encryptedData = encryptData(data);
        
        // Simulate transmission
        transmitData(encryptedData, destination);
        
        bytesTransferred.addAndGet(data.length);
    }

    private byte[] encryptData(byte[] data) {
        // Simulate data encryption
        byte[] encrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            encrypted[i] = (byte)(data[i] ^ 0xAA); // Simple XOR encryption for demo
        }
        return encrypted;
    }

    private void transmitData(byte[] encryptedData, String destination) {
        // Simulate data transmission through VPN tunnel
        try {
            Thread.sleep(10); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
