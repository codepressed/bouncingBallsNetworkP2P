package bouncingballs;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ControlPanel extends JPanel implements Runnable {

    public static final String PLAYTEXT = "PLAY";
    public static final String PAUSETEXT = "PAUSE";
    public static final String STOPTEXT = "STOP";
    public static final String ADDBALL = "Add ball";
    public static final String CONNECT = "CONNECT";
    public static final String LEFTSIDE = "LEFT";
    public static final String RIGHTSIDE = "RIGHT";
    private static final String FONTNAME = "Courier New";
    private static final String[] strSide = {LEFTSIDE, RIGHTSIDE};

    private Thread controlPanelThread;
    private Statistics statistics;
    private BallTask ballTask;

    private JTable statisticsTable;
    public JToggleButton play, pause, stop;
    public JButton addBall, saveConnection;
    private JLabel controlPanelTitle, connectionPanelTitle, ipText, portText, sideText;
    public JTextField connectionIP, connectionPort;
    public JComboBox sideBox;

    private Font font = new Font(FONTNAME, Font.PLAIN, 12);
    private Border blackLine = BorderFactory.createLineBorder(Color.black);

    public JTextField getConnectionIP() {
        return connectionIP;
    }

    public void setConnectionIP(JTextField connectionIP) {
        this.connectionIP = connectionIP;
    }

    public JTextField getConnectionPort() {
        return connectionPort;
    }

    public void setConnectionPort(JTextField connectionPort) {
        this.connectionPort = connectionPort;
    }

    public ControlPanel(BallTask ballTask, Statistics statistics){
        this.setPreferredSize(new Dimension(150, 300));
        this.setBackground(new Color(191,250,238));
        this.ballTask = ballTask;
        this.statistics = statistics;
        this.setBorder(blackLine);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gCons = new GridBagConstraints();
        gCons.fill = GridBagConstraints.HORIZONTAL;
        this.createPanel(gCons);
        this.controlPanelThread = new Thread(this);
        this.controlPanelThread.start();
    }

    /**
     * bouncingballs.Main method to create the bouncingballs.ControlPanel
     * It creates the methods and then invokes the statisticsTable creator
     * @param gCons
     */
    private void createPanel(GridBagConstraints gCons) {
        this.controlPanelTitle = new JLabel("♥ CONTROL ♥ PANEL ♥");
        this.controlPanelTitle.setFont(this.font);
        this.controlPanelTitle.setHorizontalAlignment(JLabel.CENTER);
        gCons.insets = new Insets(1, 5, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 0;
        gCons.weightx = 2;
        this.add(this.controlPanelTitle, gCons);

        this.play = new JToggleButton(PLAYTEXT);
        this.play.setFont(this.font);
        this.play.addActionListener(this.ballTask);
        gCons.insets = new Insets(1, 5, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 1;
        gCons.weightx = 2;
        this.add(this.play, gCons);

        this.pause = new JToggleButton(PAUSETEXT);
        this.pause.setFont(this.font);
        this.pause.addActionListener(this.ballTask);
        gCons.insets = new Insets(1, 5, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 2;
        gCons.weightx = 2;
        this.add(this.pause, gCons);

        this.stop = new JToggleButton(STOPTEXT);
        this.stop.setFont(this.font);
        this.stop.addActionListener(this.ballTask);
        gCons.gridx = 0;
        gCons.gridy = 3;
        gCons.weightx = 2;
        this.add(this.stop, gCons);

        this.addBall = new JButton(ADDBALL);
        addBall.setFont(this.font);
        addBall.addActionListener(this.ballTask);
        gCons.insets = new Insets(1, 5, 30, 5);
        gCons.gridx = 0;
        gCons.gridy = 4;
        gCons.weightx = 2;
        this.add(addBall, gCons);

        addStatisticsTable(gCons);
        addConnectionPanel(gCons);
    }

    /**
     * Method to create the bouncingballs.Statistics Table in bouncingballs.ControlPanel
     * Data is saved in bouncingballs.Statistics.class
     * @param gCons
     */
    private void addStatisticsTable(GridBagConstraints gCons) {
        this.statisticsTable = new JTable(4, 2);
        this.statisticsTable.getColumnModel().getColumn(0).setHeaderValue("DATA");
        this.statisticsTable.getColumnModel().getColumn(1).setHeaderValue("NUM");
        this.statisticsTable.setValueAt("Total balls: ", 0, 0);
        this.statisticsTable.setValueAt("Balls in BH: ", 1, 0);
        this.statisticsTable.setValueAt("Balls waiting: ", 2, 0);
        this.statisticsTable.setValueAt("Active threads: ", 3, 0);
        this.statisticsTable.setVisible(true);
        gCons.gridx = 0;
        gCons.gridy = 4;
        gCons.weightx = 2;
        gCons.insets = new Insets(50, 5, 0 ,5);
        this.statisticsTable.getTableHeader().setVisible(true);
        this.statisticsTable.getColumnModel().getColumn(1).setPreferredWidth(5);
        this.add(this.statisticsTable.getTableHeader(), gCons);
        gCons.gridy = 4;
        gCons.gridheight = 4;
        gCons.insets = new Insets(70, 5, 0, 5);
        this.add(this.statisticsTable, gCons);
    }


    /**
     * Method to add the Connection Panel
     * @param gCons
     */
    private void addConnectionPanel(GridBagConstraints gCons){
        this.connectionPanelTitle = new JLabel("♥ CONNECTION P2P ♥");
        this.connectionPanelTitle.setFont(this.font);
        this.connectionPanelTitle.setHorizontalAlignment(JLabel.CENTER);
        gCons.insets = new Insets(85, 5, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 5;
        gCons.weightx = 2;
        this.add(this.connectionPanelTitle, gCons);

        this.ipText = new JLabel("IP");
        this.ipText.setFont(this.font);
        gCons.insets = new Insets(0, 5, 0, 7);
        gCons.gridx = 0;
        gCons.gridy = 6;
        gCons.weightx = 0;
        this.add(this.ipText, gCons);

        this.connectionIP = new JTextField();
        this.connectionIP.setBounds(5, 0, 5, 5);
        gCons.insets = new Insets(0, 50, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 6;
        gCons.weighty = 1;
        gCons.weightx = 2;
        this.add(this.connectionIP, gCons);

        this.portText = new JLabel("Port");
        this.portText.setFont(this.font);
        gCons.insets = new Insets(50, 5, 0, 0);
        gCons.gridx = 0;
        gCons.gridy = 7;
        gCons.weightx = 0;
        this.add(this.portText, gCons);

        this.connectionPort = new JTextField();
        this.connectionPort.setBounds(5, 0, 3, 2);
        gCons.insets = new Insets(50, 50, 1, 5);
        gCons.gridx = 0;
        gCons.gridy = 7;
        gCons.weighty = 1;
        gCons.weightx = 1;
        this.add(this.connectionPort, gCons);


        this.sideText = new JLabel("Side");
        this.sideText.setFont(this.font);
        gCons.insets = new Insets(50, 5, 0, 0);
        gCons.gridx = 0;
        gCons.gridy = 8;
        gCons.weightx = 0;
        this.add(this.sideText, gCons);

        this.sideBox = new JComboBox(strSide);
        gCons.insets = new Insets(50, 50, 0, 5);
        gCons.gridx = 0;
        gCons.gridy = 8;
        gCons.weightx = 0;
        this.add(sideBox, gCons);

        this.saveConnection = new JButton(CONNECT);
        saveConnection.setFont(this.font);
        saveConnection.addActionListener(this.ballTask);
        gCons.insets = new Insets(70, 5, 0, 5);
        gCons.gridx = 0;
        gCons.gridy = 9;
        gCons.weightx = 2;
        this.add(saveConnection, gCons);

    }


    /**
     * Thread used this method to update constantly statistics.
     * When a bouncingballs.Ball is generated, when a ball gets in BH and total active Threads.
     */
    private void updateStatistics(){
        this.statisticsTable.setValueAt(statistics.getTotalBalls(), 0, 1);
        this.statisticsTable.setValueAt(statistics.getInsideBH(), 1, 1);
        this.statisticsTable.setValueAt(statistics.getPausedBalls(), 2, 1);
        this.statisticsTable.setValueAt(Thread.activeCount(), 3, 1);
    }

    @Override
    public void run() {
        do {
            try{
            updateStatistics();}
            catch (Exception e){
                e.printStackTrace();
            }
        }while(true);
    }
}