import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

/**
 * Phone Book
 * @version 1.0
 * @author Peter Skurski
 */
class PhoneBook extends JFrame {
    private DefaultTableModel dm;
    private JTable table;
    private JComboBox deleteBox;
    private JMenuBar menuBar;
    private JScrollPane scrollPane;
    private JPanel aboutPanel;
    private JPanel addPersonPanel;
    private JLabel footer;
    private Font myFont = new Font("Serif", Font.PLAIN, 16);

    public PhoneBook() {
        //create linked list with data
        //loaded from file
        Model.createLinkedList();

        //set up the menu
        setMenu();

        //create table within scroll panel  - north area of frame
        createTable();

        //add panel to append new contacts - center area of frame
        addPersonPanel = new JPanel(new GridLayout(5,2));
        JLabel firstNameLabel = new JLabel("Name: ", SwingConstants.RIGHT);
        final JTextField firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Surname: ", SwingConstants.RIGHT);
        final JTextField lastNameField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone number: ", SwingConstants.RIGHT);
        final JTextField phoneField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email: ", SwingConstants.RIGHT);
        final JTextField emailField = new JTextField(20);

        //panel for add and delete buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add contact");
        final JButton delButton = new JButton("Delete contact");
        buttonPanel.add(addButton);
        buttonPanel.add(delButton);

        //add action - adding person to linked list and update table
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Model.addPerson(firstNameField.getText(), lastNameField.getText(),
                                phoneField.getText(), emailField.getText());
                int index = Model.personListSize() -1;
                Person per = Model.getPersonList().get(index);
                dm.addRow(new Object[]{per.getFirstName(),
                        per.getLastName(), per.getPhone(), per.getEmail()});
                table.revalidate();
            }
        });

        //delete action - select row and click delete button to delete
        //person from linked list and update table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(final ListSelectionEvent listEvent) {
                delButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(!listEvent.getValueIsAdjusting()) {
                            int row = table.getSelectedRow();
                            Model.getPersonList().remove(Model.getPersonList().get(row));
                            dm.removeRow(row);
                            dm.fireTableDataChanged();
                            table.revalidate();
                        }
                    }
                });

            }
        });

        //add labels and fields to person panel
        addPersonPanel.add(firstNameLabel);
        addPersonPanel.add(firstNameField);
        addPersonPanel.add(lastNameLabel);
        addPersonPanel.add(lastNameField);
        addPersonPanel.add(phoneLabel);
        addPersonPanel.add(phoneField);
        addPersonPanel.add(emailLabel);
        addPersonPanel.add(emailField);
        addPersonPanel.add(new JLabel());
        addPersonPanel.add(buttonPanel);

        //footer - north area of frame
        footer = new JLabel(" Made by Peter S.", SwingConstants.LEFT);

        //about me scroll panel with text area
        String aboutMessage = "Phone Book version 1.0\nMade by Peter Skurski\n\n" +
                "Phone Book is a simply program to store phone contacts which " +
                "allow users to simply add, delete and show all contacts. ";
        JTextArea aboutArea = new JTextArea(aboutMessage,6,36);
        aboutArea.setEditable(false);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setLineWrap(true);
        aboutArea.setBackground(Color.WHITE);
        aboutArea.setFont(myFont);
        JScrollPane scrollAbout = new JScrollPane(aboutArea);
        Border matteBorderAbout = BorderFactory.createMatteBorder(10,10,10,10,Color.WHITE);
        scrollAbout.setBorder(matteBorderAbout);
        JButton returnButton = new JButton("Return");
        aboutPanel = new JPanel();
        aboutPanel.setBackground(Color.WHITE);
        aboutPanel.add(scrollAbout, BorderLayout.CENTER);
        aboutPanel.add(returnButton, BorderLayout.SOUTH);
        aboutPanel.setVisible(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aboutPanel.setVisible(false);
                scrollPane.setVisible(true);
                addPersonPanel.setVisible(true);
            }
        });

        //add panels to main frame
        add(scrollPane, BorderLayout.NORTH);
        add(addPersonPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        pack();
    }

    public void createTable() {

        dm = new DefaultTableModel();
        table = new JTable(dm);
        table.setPreferredScrollableViewportSize((new Dimension(600,240)));
        scrollPane = new JScrollPane(table);
        Border matteFrame = BorderFactory.createMatteBorder(5,5,5,5,Color.GREEN);
        scrollPane.setBorder(matteFrame);

        dm.addColumn("Name");
        dm.addColumn("Surname");
        dm.addColumn("Phone");
        dm.addColumn("Email");

        //center alignment of columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        //add persons from linked list to table
        for(Person person: Model.getPersonList()) {
            dm.addRow(new Object[]{person.getFirstName(), person.getLastName(),
                        person.getPhone(), person.getEmail()});
        }
    }

    /**
     * Setting up the menu and menu actions
     */
    public void setMenu() {
        menuBar = new JMenuBar();

        //menus
        JMenu edit = new JMenu("Edit");
        final JMenu about = new JMenu("About");

        //items of edit menu
        JMenuItem showContactItem = new JMenuItem("Show contacts");
        showContactItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutPanel.setVisible(false);
                scrollPane.setVisible(true);
                addPersonPanel.setVisible(true);
                //validate();
            }
        });
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        edit.add(showContactItem);
        edit.add(closeItem);

        //items of about menu
        JMenuItem aboutItem = new JMenuItem("About me");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                scrollPane.setVisible(false);
                addPersonPanel.setVisible(false);
                //add scrollAbout, default not visible
                add(aboutPanel, BorderLayout.CENTER);
                aboutPanel.setVisible(true);
                validate();
            }
        });
        about.add(aboutItem);

        //adds all menus to menu bar and add menu bar to frame
        menuBar.add(edit);
        menuBar.add(about);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        JFrame frame = new PhoneBook();
        frame.setTitle("Phone Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                Model.writeListToFile();
            }
        });
    }
}
