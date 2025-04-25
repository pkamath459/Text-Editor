package text_editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TextEditor extends JFrame implements ActionListener
{
	//Initializing editor components from JFrame.
	JTextArea textArea;
	JScrollPane scrolling;
	JLabel fontLabel;
	JSpinner fontSize;
	JButton fontColor;
	JComboBox fontBox;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	
	//Constructor to initialize the editor and load all its components.
	public TextEditor()
	{
		//Kills the program when closing the editor.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Editor title.
		this.setTitle("Bro text Editor");
		this.setSize(500, 500);
		//Setting the default layout for the editor components, like menu, text area, etc.
		this.setLayout(new FlowLayout());
		//Setting the position of the window to the center of the screen.
		this.setLocationRelativeTo(null);

		//Setting the text area and its behavior.
		textArea = new JTextArea();
		//Wrapping the text.
		textArea.setLineWrap(true);
		//Word wrap.
		textArea.setWrapStyleWord(true);
		//Setting the default font for the text area.
		textArea.setFont(new Font("Arial",Font.PLAIN,20));

		//Adding the scroll window and setting the text area into it.
		scrolling = new JScrollPane(textArea);
		scrolling.setPreferredSize(new Dimension(450,450));
		//Adding a scroll bar, displaying it only if it is necessary.
		scrolling.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		//Label for the font selection.
		fontLabel = new JLabel("Font");

		//Using a spinner for increasing/decreasing the font size. 
		fontSize = new JSpinner();
		fontSize.setPreferredSize(new Dimension(50,25));
		//Default font size to be considered when the editor is opened.
		fontSize.setValue(20);
		//Calling a listener to read the user's action, and setting all the text in the text area to change to the selected font.
		fontSize.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSize.getValue())); 
			}
		});

		//Button to select the font color, and the calling the action listener to apply the selection.
		fontColor = new JButton("Color");
		fontColor.addActionListener(this);

		//Initializing all the available fonts in java to an array, for the list of fonts to load for the user to select.
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Arial");

		//Creating the menu bar.
		menuBar = new JMenuBar();
		//Menu bar components to be displayed on the menu.
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");

		//
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		//Setting the 
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		  
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSize);
		this.add(fontColor);
		this.add(fontBox);
		this.add(scrolling);
		this.setVisible(true);
	}
	 
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==fontColor)
		{
			JColorChooser colorChooser = new JColorChooser();
			Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
			textArea.setForeground(color);
		}
	  
		if(e.getSource()==fontBox)
		{
			textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
		}

	}
}