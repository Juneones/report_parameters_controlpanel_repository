package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

// Controller
public class MainFrame extends JFrame {

    private TextPanel textPanel;
//    private JButton jButton;
    private ToolBar toolBar;
    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private Controller controller;
    private TablePanel tablePanel;
    private PrefsDialog prefsDialog;
    private Preferences prefs;
    private JSplitPane splitPane;
    private JTabbedPane tabbedPane;
    private MessagePanel messagePanel;


    public MainFrame() {
        super("Control Panel");
//      adding components
//      first you need to
        setLayout(new BorderLayout());
        toolBar = new ToolBar();

        textPanel = new TextPanel();
        formPanel = new FormPanel();
        tablePanel = new TablePanel();
        prefsDialog = new PrefsDialog(this);
        prefs = Preferences.userRoot().node("db");
        tabbedPane = new JTabbedPane();
        messagePanel = new MessagePanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,formPanel,tabbedPane);

        splitPane.setOneTouchExpandable(true);

        tabbedPane.addTab("Person Data",tablePanel);
        tabbedPane.addTab("Messages",messagePanel);



        controller = new Controller();


        tablePanel.setData(controller.getPeople());

        tablePanel.setPersontableListener(new PersonTableListener() {
            public void rowDeleted(int row) {
                controller.removePerson(row);
            }
        });

        prefsDialog.setPrefsListener(new PrefsListener() {
            @Override
            public void preferenceSet(String user, String password, int port) {
                System.out.println(user + ": " + password);
                prefs.put("user", user);
                prefs.put("password", password);
                prefs.putInt("port", port);

            }
        });
        String user = prefs.get("user", "");
        String password = prefs.get("password", "");
        Integer port = prefs.getInt("pert", 3306);
        prefsDialog.setDefaults(user, password, port);

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new PersonFileFilter());

        setJMenuBar(createManuBar());

        toolBar.setToolbarListener(new ToolbarListener() {
            @Override
            public void saveEventOccurred() {
                connect();
                try {
                    controller.save();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Unable to save to database.", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);

                }
            }

            @Override
            public void refreshEventOccurred() {
                connect();
                try {
                    controller.load();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Unable to load from database.", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
                }

                tablePanel.refresh();
            }
        });

        formPanel.setFormListener(new FormListener() {
            public void formEventOccurred(FormEvent event) {

                controller.addPerson(event);

                tablePanel.refresh();


            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               controller.disconnect();
                dispose();
                System.gc();
            }
        });

//        add(formPanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);


        setMinimumSize(new Dimension(1400, 500));
        setSize(1400, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);


    }

    private void connect() {
        try {
            controller.connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Cannot connect to database.", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
        }

    }


    private JMenuBar createManuBar() {
        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("File");
        JMenuItem exportDataItem = new JMenuItem("Export Data ...");
        JMenuItem importDataItem = new JMenuItem("Import Data ...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);


        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");
        JMenuItem prefsItem = new JMenuItem("Preferences ...");


        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Form");
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        prefsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prefsDialog.setVisible(true);

            }
        });


        showFormItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();

                if(menuItem.isSelected()){

                }

                formPanel.setVisible(menuItem.isSelected());
                splitPane.setDividerLocation((int)formPanel.getMinimumSize().getWidth());
            }
        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                ActionEvent.CTRL_MASK));


        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));

        importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                ActionEvent.CTRL_MASK));

        importDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.loadfromFile(fileChooser.getSelectedFile());
                        tablePanel.refresh();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        exportDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.saveToFile(fileChooser.getSelectedFile());
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not save data to file",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }


                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = JOptionPane.showInputDialog(MainFrame.this,
                        "Enter your user name",
                        "Enter user name", JOptionPane.OK_OPTION | JOptionPane.QUESTION_MESSAGE);

                System.out.println(text);


                int action = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Do you really want to exit the application?",
                        "Confirm exit", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    WindowListener[] listeners = getWindowListeners();
                    for (WindowListener listener:listeners){
                        listener.windowClosing(new WindowEvent(MainFrame.this,0));
                    }

                }
            }
        });

        return menuBar;
    }

}
