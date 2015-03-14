package phonebook.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import phonebook.database.*;

/*
 * Phone Book Application
 * 
 * @version 1.1 updated
 * @author Peter Skurski
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	//array of JTextField
	private JTextField[] _textField = new JTextField[4];
	//map used to store all JPanels, key is the name of JPanel
	private Map<String, JPanel> _panelMap = new HashMap<String, JPanel>();
	//map used to store all JButtons, key is the name of JButton
	private Map<String, JButton> _buttonMap = new HashMap<String, JButton>();
	//map used to store all JMenuItems, key is the name of JMenuItem
	private Map<String, JMenuItem> _itemMap = new HashMap<String, JMenuItem>();
	private JTable _table = null;
	private DefaultTableModel _dm = null;
	final static int extraWindowWidth = 50;
    private Font _font = new Font("Serif", Font.PLAIN, 12);
    private JScrollPane _scrollTable = null;
    private JTabbedPane _tab = null;
	
	public MainFrame() {
		//read the database file and create set of persons
		Model.read();
		
		/*************** SET MENU **********************/
		//methods assiociated with menu: setMenu, createMenu, actionMenu
		setMenu(); 
		actionMenu();
		/************ END SET MENU ********************/
		
		/*********** CREATE AND STORE THE PANELS ***********************/
		//methods assiociated with panels: createPanel
		_panelMap.put("contactPanel", createFormPanel(new String[] 
									{"Name: ", "Lastname: ", "Phone: ", "Email: "}));
		_panelMap.put("infoPanel", createAreaPanel(
				"Instruction how to use phonebook application:\n"+
				"ADD CONTACT add contact to table and phonebook database\n"+
				"DELETE CONTACT delete contact from phonebook permanently,"
				+ " you have to mark chosen row and click button\n"+
				"CLEAR FORM clear the form"));
		/************** END PANELS ************************************/
		
		/************** SET TABBED PANE ***************************/
		_tab = new JTabbedPane();
		_tab.setPreferredSize(new Dimension(660,120));
		_tab.addTab("Add contact",null,_panelMap.get("contactPanel"),"Add or Search contact");
		_tab.addTab("Instruction",null,_panelMap.get("infoPanel"),"How to use this application");
		/************** END TABBED PANE **************************/
		
		/************** CREATE TABLE ******************************/
		_scrollTable = createTable();
		/************** END TABLE ********************************/
		
		/************** CREATE BUTTONS AND ACTIONS ***********************/
		//methods assiociated with buttons: createButtons, actionButton
		String[] names = {"Add contact","Delete contact","Clear form","Return"};
		String[] objName = {"contactButt","deleteButt","clearButt","returnButt"};
		//create buttons and put it into map
		createButtons(names,objName);
		//create wider panel to store buttons in one row
        _panelMap.put("buttPanel",  new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        });
		_panelMap.get("buttPanel").add(_buttonMap.get("contactButt"));
		_panelMap.get("buttPanel").add(_buttonMap.get("deleteButt"));
		_panelMap.get("buttPanel").add(_buttonMap.get("clearButt"));
		actionButton();
		/***************** END BUTTONS **********************************/
		
		/***************** ABOUT PANEL *********************************/	
		_panelMap.put("messagePanel", createAreaPanel("Phone Book version 1.0\nMade by Peter Skurski\n\n" +
										"Phone Book is a simply program to store phone contacts which " +
										"allow users to simply add, delete and show all contacts."));
		_panelMap.put("aboutPanel", new JPanel(new BorderLayout()));
		_panelMap.put("returnPanel", new JPanel());
		_panelMap.get("returnPanel").add(_buttonMap.get("returnButt"));
		_panelMap.get("aboutPanel").add(_panelMap.get("messagePanel"), BorderLayout.CENTER);
		_panelMap.get("aboutPanel").add(_panelMap.get("returnPanel"), BorderLayout.PAGE_END);
		_panelMap.get("returnPanel").setBackground(Color.WHITE);
		_panelMap.get("aboutPanel").setVisible(false);
		/***************** END ABOUT *********************************/
        
		/***************** ADD TO MAIN FRAME ****************************/
		add(_tab,BorderLayout.PAGE_START);
		add(_scrollTable, BorderLayout.CENTER);
		add(_panelMap.get("buttPanel"), BorderLayout.PAGE_END);
		/***************** END MAIN FRAME ******************************/
		pack();
	}
	
	/*
	 * Create table and scrollable panel for table
	 * Read data from database and insert into table
	 * 
	 * @return JScrollPane	scrollable panel with table inside
	 */
    private JScrollPane createTable() {
    	//create array of objects with columns name
        Object[] fields = new Object[] {"Name", "Lastname", "Phone", "Email"};   
        _dm = new DefaultTableModel(fields, 0);

        _table = new JTable(_dm);
        _table.setPreferredScrollableViewportSize(new Dimension(640,270));
        //create scrollable panel for table
        JScrollPane scrollPane = new JScrollPane(_table);

        //center alignment of columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<_table.getColumnCount(); i++) {
        	_table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        //list the persons
        for(Person person: Model.getPersons()) {
        	_dm.addRow(new Object[]{person.getFirstName(), person.getLastName(),
                        person.getPhone(), person.getEmail()});
        }
        
        return scrollPane;
    }
	
	/*
	 * Set up the JMenuBar
     * Create JMenuBar, call createMenu method and set JMenuBar
	 */
    private void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        //each JMenu name
        String[] menuLabel = {"Edit", "About"};
        //each JMenuItem name, one row correspond to one JMenu object
        String[][] itemLabel = {{"Show contacts", "Close"},
        						{"About me"}};
        //object JItemMenu name for _itemMap map
        //itemObjName array have to have the exacly same size as itemLabel array
        String[][] itemObjName = {{"contactItem", "closeItem"},
        						  {"aboutItem"}};
        //create array of JMenu with JMenuItems and add JMenuItems to map
        menuBar = createMenu(menuBar,menuLabel,itemLabel,itemObjName);  
        //set JMenuBar
        this.setJMenuBar(menuBar);
    }
    
    /*
     * Create JMenus and add each JMenu to JMenuBar
     * Create JMenuItems and add each to corresponding JMenu
     * Put each JMenuItem in Map, the key is the object name 
     * taken from the array of strings
     * 
     * @param JMenuBar menuBar	JMenuBar which will be set
     * @param String[] menuLabel	strings array with names for JMenus
     * @param String[][] itemLabel	strings array with names for JMenuItems
     * @param String[][] itemObjName	strings array with names for JMenuItems
     * 									objects stored in Map
     * @return JMenuBar
     */
	private JMenuBar createMenu(JMenuBar menuBar, String[] menuLabel, 
								String[][] itemLabel, String[][] itemObjName) {
		//create empty array of JMenu in size of menuLabel array
		JMenu[] menu = new JMenu[menuLabel.length];
		//create each JMenu and add it to menuBar
		for(int i=0; i<menuLabel.length; i++) {
			menu[i] = new JMenu(menuLabel[i]);
			menuBar.add(menu[i]);
		}
		
		//add menu items to each menu and to _itemMap map	
		JMenuItem item = null;
		for(int i=0; i<itemLabel.length;i++) {
			for(int j=0; j<itemLabel[i].length; j++) {
				item = new JMenuItem(itemLabel[i][j]);
				menu[i].add(item);
				_itemMap.put(itemObjName[i][j], item);
			}
		}	
		return menuBar;
	}
	
	/*
	 * Assiociate action with every JMenuItem
	 */
	private void actionMenu() {
		//close item 
		_itemMap.get("closeItem").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Model.write();
				System.exit(0);
			}
		});
        //show contacts item
        _itemMap.get("contactItem").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	_tab.setVisible(true);
            	_scrollTable.setVisible(true);
            	_panelMap.get("aboutPanel").setVisible(false);
            }
        });
        //about item
        _itemMap.get("aboutItem").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _scrollTable.setVisible(false);
                _tab.setVisible(false);
                add(_panelMap.get("aboutPanel"), BorderLayout.CENTER);
                _panelMap.get("aboutPanel").setVisible(true);
                //add scrollAbout, default not visible
               // add(aboutPanel, BorderLayout.CENTER);
              //  aboutPanel.setVisible(true);
                validate();
            }
        });
	}
	
	/*
	 * Create and return the JPanel 
	 * JPanel consists of JLabel (with given name) and JTextField
	 * stored in member array of JTextFields
	 * 
	 * @param String[] names	strings array with names for JLabels
	 * @return JPanel	created JPanel
	 */
	private JPanel createFormPanel(String[] names) {
		int len = names.length;
		JPanel panel = new JPanel(new GridLayout(len,len));
		for(int i=0; i<len; i++) {
			panel.add(new JLabel(names[i], SwingConstants.RIGHT));
			panel.add(_textField[i] = new JTextField(10));
		}
		return panel;
	}
	
	/*
	 * Create and return JPanel with JTextArea inside
	 * 
	 * @return JPanel	JPanel with JTextArea inside
	 */
	private JPanel createAreaPanel(String message) {
		JPanel panel = new JPanel(new BorderLayout());
		JTextArea area = new JTextArea(message);
		area.setEditable(false);
		area.setFont(_font);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		Border matte = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE);
		panel.setBorder(matte);
		panel.add(area, BorderLayout.CENTER);
		panel.setEnabled(false);
		return panel;
	}
	
	/*
	 * Create buttons and put it in map 
	 * 
	 * @param String[] names	strings array with labels for buttons
	 * @param String[] objName	strings array with objects names
	 */
	private void createButtons(String[] names, String[] objName) {
		for(int i=0; i<names.length; i++)
			_buttonMap.put(objName[i], new JButton(names[i]));
	}
	
	/*
	 * Assiociate action with each button
	 */
	private void actionButton() {
		//add contact button
		_buttonMap.get("contactButt").addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				Model.addPerson(_textField[0].getText(), _textField[1].getText(),
								_textField[2].getText(), _textField[3].getText());
                Person per = Model.getPersons().get(Model.getSize()-1);
                _dm.addRow(new Object[]{per.getFirstName(),
                        per.getLastName(), per.getPhone(), per.getEmail()});
                _table.revalidate();
			}
		});
        //delete button - select row and click delete button to delete
        //person from linked list and update table
        _table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(final ListSelectionEvent listEvent) {
                _buttonMap.get("deleteButt").addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(!listEvent.getValueIsAdjusting()) {
                            int row = _table.getSelectedRow();
                            Model.getPersons().remove(Model.getPersons().get(row));
                            _dm.removeRow(row);
                            _dm.fireTableDataChanged();
                            _table.revalidate();
                        }
                    }
                });

            }
        });
        //clear button - clear the form (set empty string)
        _buttonMap.get("clearButt").addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		for(JTextField field: _textField)
        			field.setText("");
        	}
        });
        //return button - return to contact view
        _buttonMap.get("returnButt").addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                _panelMap.get("aboutPanel").setVisible(false);
                _tab.setVisible(true);
                _scrollTable.setVisible(true);
        	}
        });
	}

	/*
	 * ************ RUN APPLICATION *******************
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame main = new MainFrame();
				main.setTitle("Phone Book Application");
				main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				main.setVisible(true);	
				main.setResizable(false);
				main.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						Model.write();
					}
				});
			}
		});
	}

}
