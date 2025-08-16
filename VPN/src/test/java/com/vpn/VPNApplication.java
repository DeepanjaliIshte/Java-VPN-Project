package com.vpn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class VPNApplication extends JFrame {
    private VPNManager vpnManager;
    private JButton connectButton;
    private JComboBox<VPNServer> serverComboBox;
    private JLabel statusLabel, ipLabel, connectionTimeLabel, dataUsageLabel;
    private JProgressBar dataUsageBar;
    private Timer connectionTimer;
    private boolean isConnected = false;
    private long connectionStartTime;

    public VPNApplication() {
        vpnManager = new VPNManager();
        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setTitle("DeepSecureVPN");
        setSize(750, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Base gradient - Deep purple to black
                GradientPaint baseGradient = new GradientPaint(
                    0, 0, new Color(25, 0, 51), 
                    0, getHeight(), new Color(0, 0, 0)
                );
                g2d.setPaint(baseGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Radial gradient overlay - Purple to transparent
                RadialGradientPaint radialGradient = new RadialGradientPaint(
                    getWidth() * 0.3f, getHeight() * 0.2f, getWidth() * 0.8f,
                    new float[]{0.0f, 0.6f, 1.0f},
                    new Color[]{
                        new Color(138, 43, 226, 120), // BlueViolet with transparency
                        new Color(75, 0, 130, 80),    // Indigo with transparency  
                        new Color(25, 0, 51, 0)       // Transparent
                    }
                );
                g2d.setPaint(radialGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Second radial gradient - Magenta accent
                RadialGradientPaint magentaGradient = new RadialGradientPaint(
                    getWidth() * 0.8f, getHeight() * 0.7f, getWidth() * 0.6f,
                    new float[]{0.0f, 0.4f, 1.0f},
                    new Color[]{
                        new Color(199, 21, 133, 100), // MediumVioletRed with transparency
                        new Color(139, 0, 139, 60),   // DarkMagenta with transparency
                        new Color(25, 0, 51, 0)       // Transparent
                    }
                );
                g2d.setPaint(magentaGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Diagonal accent gradient
                GradientPaint diagonalGradient = new GradientPaint(
                    0, 0, new Color(148, 0, 211, 40),           // DarkViolet with low transparency
                    getWidth(), getHeight(), new Color(75, 0, 130, 20) // Indigo with very low transparency
                );
                g2d.setPaint(diagonalGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add some sparkle effects
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < 50; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int size = (int) (Math.random() * 3) + 1;
                    g2d.fillOval(x, y, size, size);
                }
                
                // Add subtle glow lines
                g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(new Color(186, 85, 211, 25)); // MediumOrchid with transparency
                for (int i = 0; i < 8; i++) {
                    int y = (int) (Math.random() * getHeight());
                    g2d.drawLine(0, y, getWidth(), y + (int)(Math.random() * 100 - 50));
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel(" Deep Secure VPN", SwingConstants.CENTER);
        title.setFont(new Font("Algerian", Font.BOLD, 34));
        title.setForeground(new Color(0, 255, 255));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel serverLabel = new JLabel("Choose your Server");
        serverLabel.setFont(new Font("Lucida Calligraphy", Font.BOLD, 20));
        serverLabel.setForeground(Color.yellow);
        center.add(serverLabel, gbc);

        gbc.gridy = 1;
        serverComboBox = new JComboBox<>(vpnManager.getAvailableServers().toArray(new VPNServer[0]));
        serverComboBox.setPreferredSize(new Dimension(350, 35));
        serverComboBox.setFont(new Font("Courier New", Font.PLAIN, 16));
        center.add(serverComboBox, gbc);

        gbc.gridy = 2;
        connectButton = new JButton(" CONNECT");
        connectButton.setPreferredSize(new Dimension(250, 50));
        connectButton.setFont(new Font("Verdana", Font.BOLD, 18));
        connectButton.setBackground(new Color(0, 191, 255));
        connectButton.setForeground(Color.BLACK);
        connectButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        center.add(connectButton, gbc);

        gbc.gridy = 3;
        statusLabel = createLabel("Status: Disconnected", Color.YELLOW);
        center.add(statusLabel, gbc);

        gbc.gridy = 4;
        ipLabel = createLabel("IP: Not Connected", Color.LIGHT_GRAY);
        center.add(ipLabel, gbc);

        gbc.gridy = 5;
        connectionTimeLabel = createLabel("Connection Time: 00:00:00", Color.GRAY);
        center.add(connectionTimeLabel, gbc);

        gbc.gridy = 6;
        dataUsageLabel = createLabel(" Data Usage: 0 MB", Color.CYAN);
        center.add(dataUsageLabel, gbc);

        gbc.gridy = 7;
        dataUsageBar = new JProgressBar(0, 100);
        dataUsageBar.setPreferredSize(new Dimension(400, 30));
        dataUsageBar.setStringPainted(true);
        dataUsageBar.setForeground(new Color(0, 153, 255));
        dataUsageBar.setBackground(Color.DARK_GRAY);
        dataUsageBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dataUsageBar.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        center.add(dataUsageBar, gbc);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(color);
        return label;
    }

    private void setupEvents() {
        connectButton.addActionListener((ActionEvent e) -> {
            if (!isConnected) {
                connectToVPN();
            } else {
                disconnectVPN();
            }
        });
    }

    private void connectToVPN() {
        VPNServer selectedServer = (VPNServer) serverComboBox.getSelectedItem();
        connectButton.setText(" Connecting...");
        connectButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            protected Boolean doInBackground() {
                return vpnManager.connect(selectedServer);
            }

            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        isConnected = true;
                        updateConnectedUI(selectedServer.getIpAddress());
                        startTimer();
                        simulateDataUsage();
                    } else {
                        statusLabel.setText("Status:  Failed");
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Status:  Error");
                }
                connectButton.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void updateConnectedUI(String ip) {
        connectButton.setText(" DISCONNECT");
        connectButton.setBackground(Color.RED);
        statusLabel.setText("Status: Connected");
        ipLabel.setText("IP: " + ip);
        connectionStartTime = System.currentTimeMillis();
    }

    private void disconnectVPN() {
        vpnManager.disconnect();
        isConnected = false;
        connectButton.setText(" CONNECT");
        connectButton.setBackground(new Color(0, 191, 255));
        statusLabel.setText("Status: Disconnected");
        ipLabel.setText("IP: Not Connected");
        connectionTimeLabel.setText("Connection Time: 00:00:00");
        dataUsageLabel.setText("Data Usage: 0 MB");
        dataUsageBar.setValue(0);
        dataUsageBar.setString("0 MB of 100 MB");
        if (connectionTimer != null) connectionTimer.cancel();
    }

    private void startTimer() {
        connectionTimer = new Timer();
        connectionTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long elapsed = System.currentTimeMillis() - connectionStartTime;
                long s = (elapsed / 1000) % 60;
                long m = (elapsed / (1000 * 60)) % 60;
                long h = (elapsed / (1000 * 60 * 60)) % 24;
                SwingUtilities.invokeLater(() ->
                        connectionTimeLabel.setText(String.format("Connection Time: %02d:%02d:%02d", h, m, s))
                );
            }
        }, 1000, 1000);
    }

    private void simulateDataUsage() {
        Timer dataTimer = new Timer();
        dataTimer.scheduleAtFixedRate(new TimerTask() {
            int dataUsed = 0;
            public void run() {
                if (!isConnected) {
                    this.cancel();
                    return;
                }
                dataUsed += (int) (Math.random() * 5 + 1);
                int usage = Math.min(dataUsed, 100);
                SwingUtilities.invokeLater(() -> {
                    dataUsageLabel.setText("Data Usage: " + usage + " MB");
                    dataUsageBar.setValue(usage);
                    dataUsageBar.setString(usage + " MB of 100 MB");
                });
            }
        }, 2000, 2000);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and feel error");
        }
        SwingUtilities.invokeLater(() -> new VPNApplication().setVisible(true));
    }
}