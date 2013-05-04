package gui;

import model.Person;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class TablePanel extends JPanel {

    private JTable table;
    private PersonTableModel tableModel;
    private JPopupMenu popup;
    private PersonTableListener personTableListener;

    public TablePanel() {
        tableModel = new PersonTableModel();
        table = new JTable(tableModel);
        popup = new JPopupMenu();

        // remove row from app table
        JMenuItem removeItem = new JMenuItem("Delete row");
        popup.add(removeItem);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
               // select row in table by the mouse
                int row= table.rowAtPoint(e.getPoint());
                System.out.println(row);
                  // display the selected row at app table
                table.getSelectionModel().setSelectionInterval(row,row);
                if (e.getButton() == MouseEvent.BUTTON3)
                    popup.show(table, e.getX(), e.getY());

            }
        });

        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (personTableListener!=null){
                    personTableListener.rowDeleted(row);
                    tableModel.fireTableRowsDeleted(row,row);
                }



            }
        });


        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(List<Person> database) {
        tableModel.setData(database);
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
    public void setPersontableListener (PersonTableListener listener){
        this.personTableListener = listener;

    }

}
