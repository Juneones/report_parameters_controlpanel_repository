package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormPanel extends JPanel implements ActionListener {

    private JLabel nameLabel;
    private JLabel occupationLabel;

    private JTextField nameField;
    private JTextField occupationField;
    private JButton okButton;
    private FormListener formListener;
    private JList ageList;
    private JComboBox empCombo;

    private JLabel resultLabel;
    private JLabel result;
    private JComboBox dateCombo;
    private String currentPattern;

    private JCheckBox citizenCheck;
    private JTextField taxField;
    private JLabel taxLabel;

    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup genderGroup;


    public FormPanel() {
        Dimension dim = getPreferredSize();
        dim.width = 350;
        setPreferredSize(dim);
        setMinimumSize(dim);

        nameLabel = new JLabel("Name: ");
        occupationLabel = new JLabel("Occupation: ");
        nameField = new JTextField(10);
        occupationField = new JTextField(10);
        ageList = new JList();
        empCombo = new JComboBox();

        dateCombo = new JComboBox();


        citizenCheck = new JCheckBox();
        taxField = new JTextField(10);
        taxLabel = new JLabel("Tax ID: ");
        okButton = new JButton("OK");

        //Set up the mnemonics
        okButton.setMnemonic(KeyEvent.VK_O);

        nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        nameLabel.setLabelFor(nameField);


        maleRadio = new JRadioButton("male");
        femaleRadio = new JRadioButton("female");

        maleRadio.setActionCommand("male");
        femaleRadio.setActionCommand("female");


        genderGroup = new ButtonGroup();


        maleRadio.setSelected(true);


        //      Set up gender radios
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        //      Set up Tax ID
        taxLabel.setEnabled(false);
        taxField.setEnabled(false);

        citizenCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isTicked = citizenCheck.isSelected();
                taxLabel.setEnabled(isTicked);
                taxField.setEnabled(isTicked);
            }
        });


//       Set up list box
        DefaultListModel ageModel = new DefaultListModel();
        ageModel.addElement(new AgeCategory(0, "under 18"));
        ageModel.addElement(new AgeCategory(1, "18 to 65"));
        ageModel.addElement(new AgeCategory(2, "65 or over"));
        ageList.setModel(ageModel);

        ageList.setPreferredSize(new Dimension(110, 60));
        ageList.setBorder(BorderFactory.createEtchedBorder());
        ageList.setSelectedIndex(1);

//        set up combo box
        DefaultComboBoxModel empModel = new DefaultComboBoxModel();
        empModel.addElement("employed");
        empModel.addElement("self-employed");
        empModel.addElement("unemployed");
        empCombo.setModel(empModel);
        empCombo.setSelectedIndex(0);
        empCombo.setEditable(true);
        String[] patternExamples = {
                "dd MMMMM yyyy",
                "dd.MM.yy",
                "MM/dd/yy",
                "yyyy.MM.dd G 'at' hh:mm:ss z",
                "EEE, MMM d, ''yy",
                "h:mm a",
                "H:mm:ss:SSS",
                "K:mm a,z",
                "yyyy.MMMMM.dd GGG hh:mm aaa"
        };
        DefaultComboBoxModel dateModel = new DefaultComboBoxModel();

        dateModel.addElement(patternExamples[0]);
        dateModel.addElement(patternExamples[1]);
        dateModel.addElement(patternExamples[2]);
        dateModel.addElement(patternExamples[3]);
        dateModel.addElement(patternExamples[4]);
        dateModel.addElement(patternExamples[5]);
        dateModel.addElement(patternExamples[6]);
        dateModel.addElement(patternExamples[7]);
        dateModel.addElement(patternExamples[8]);
//        dateCombo.setSelectedIndex(0);
        dateCombo.setModel(dateModel);
        dateCombo.setEditable(true);


//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


       currentPattern = patternExamples[0];

        //Set up the UI for selecting a pattern.

//        dateModel.addElement(patternExamples);
//        dateCombo.setModel(dateModel);

//
//        dateCombo.setEditable(true);
//        dateCombo.addActionListener(this);

        //Create the UI for displaying result.
        resultLabel = new JLabel("Current"+"\n"+" Date/Time"); //== LEFT

        result = new JLabel();


//        result.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.black),
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)
//        ));

        //Lay out everything.
//        JPanel patternPanel = new JPanel();
//        patternPanel.setLayout(new BoxLayout(patternPanel,
//                BoxLayout.LINE_AXIS));

//        dateCombo.setAlignmentX(Component.BOTTOM_ALIGNMENT);
//        patternPanel.add(dateCombo);

//        JPanel resultPanel = new JPanel(new GridLayout(0, 1));
//        resultPanel.add(resultLabel);
//        resultPanel.add(result);

//        patternPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        add(patternPanel);
//
//        add(resultPanel);

//        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        reformat();

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String occupation = occupationField.getText();
                        AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
                        String empCat = (String) empCombo.getSelectedItem();
                        String taxId = taxField.getText();
                        boolean usCitizen = citizenCheck.isSelected();
                        String gender = genderGroup.getSelection().getActionCommand();
                        FormEvent ev = new FormEvent(this, name, occupation, ageCat.getId(),
                                empCat, taxId, usCitizen, gender);
                        if (formListener != null) {
                            formListener.formEventOccurred(ev);
                        }
                    }
                });

        Border innerBorder = BorderFactory.createTitledBorder("Add Person");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        layoutComponents();
    }


    public void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

//     ////////////////////////////////////////// First row

        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(nameLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(nameField, gc);
//    ////////////////////////////////////////  Second Row

        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(occupationLabel, gc);


        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(occupationField, gc);


//       /////////////////////////////////////// Next (Third) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(new JLabel("Age: "), gc);


        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(ageList, gc);

        /////////////////////////////////////// Next (Copy of Third) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(new JLabel("Employment: "), gc);


        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(empCombo, gc);

        // Next component Date ComboBox
        gc.gridy++;
//
        gc.weightx = 1;
        gc.weighty = 0.2;
//
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(new JLabel("Date: "), gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(dateCombo,gc);

        // Next component Date Result from ComboBox
        gc.gridy++;
//
        gc.weightx = 1;
        gc.weighty = 0.2;
//
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(new JLabel("Current date: "), gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(result,gc);





        /////////////////////////////////////// Next for CheckBox1 (Copy of copy of Third) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(new JLabel("US Citizen: "), gc);


        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(citizenCheck, gc);

        /////////////////////////////////////// Next for CheckBox2 (Copy of copy of Third) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(taxLabel, gc);


        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(taxField, gc);
        /////////////////////////////////////// Next for Radio Button 1  Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.05;

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Gender: "), gc);


        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(maleRadio, gc);

        /////////////////////////////////////// Next for RadioButton 2 (Copy of copy of Third) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(femaleRadio, gc);

        /////////////////////////////////////// Next (Fourth) Row
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 2.0;

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(okButton, gc);
    }


    public void setFormListener(FormListener listener) {
        this.formListener = listener;
    }
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String newSelection = (String)cb.getSelectedItem();
        currentPattern = newSelection;
        reformat();
    }
    public void reformat() {
        Date today = new Date();
        SimpleDateFormat formatter =
                new SimpleDateFormat(currentPattern);
        try {
            String dateString = formatter.format(today);
            result.setForeground(Color.black);
            result.setText(dateString);
        } catch (IllegalArgumentException iae) {
            result.setForeground(Color.red);
            result.setText("Error: " + iae.getMessage());
        }
    }



}

class AgeCategory {
    private int id;
    private String text;

    public AgeCategory(int id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public int getId() {
        return id;
    }
}
