package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar implements ActionListener {
    private JButton saveButton;
    private JButton refreshButton;

//    private TextPanel textPanel;

    private ToolbarListener textListener;

    public ToolBar() {
        setBorder(BorderFactory.createEtchedBorder());
        saveButton = new JButton();
        saveButton.setIcon(Utils.createIcon("/images/Save16.gif"));
        saveButton.setToolTipText("Save");

        refreshButton = new JButton();
        refreshButton.setIcon(Utils.createIcon("/images/Refresh16.gif"));
        refreshButton.setToolTipText("Save");

        saveButton.addActionListener(this);
        refreshButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(saveButton);
//        addSeparator();
        add(refreshButton);
    }



    public void setToolbarListener(ToolbarListener listener) {
        this.textListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (clicked == saveButton) {
            if (textListener != null) {
                textListener.saveEventOccurred();
            }
//            textPanel.appendText("Hello\n");

        } else if (clicked == refreshButton) {
            if (textListener != null) {
                textListener.refreshEventOccurred();
            }
//            textPanel.appendText("GoodBye\n");
        }

    }
}

