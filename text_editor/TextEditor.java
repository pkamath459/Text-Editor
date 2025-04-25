package text_editor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener
{
	//Initializing editor components from JFrame.
	JTextArea textArea;
	JScrollPane scrolling;
	JLabel fontLabel;
	JSpinner fontSize;
	JButton fontColor, bgColor;
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
		this.setTitle("Text editor project");
		this.setSize(600, 500);
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
		scrolling.setPreferredSize(new Dimension(550,450));
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
		
		bgColor = new JButton("Background");
		bgColor.addActionListener(this);

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

		//Calling the method that will read the user's action when selecting the list items present in the file menu.
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		//Setting the Menu items inside the file menu option.
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		//Setting the file menu item into the menu bar.
		menuBar.add(fileMenu);

//		menuBar.add(fontLabel);
//		menuBar.add(fontSize);
//		menuBar.add(fontColor);
//		menuBar.add(bgColor);
//		menuBar.add(fontBox);
		
		//Calling all the initialized Frames.
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSize);
		this.add(fontColor);
		this.add(bgColor);
		this.add(fontBox);
		this.add(scrolling);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//Checking if the action was performed to change the font color and implementing the same to the contents of the editor. 
		if(e.getSource() == fontColor)
		{
			//Gets the color that the user picked.
			JColorChooser selectColor = new JColorChooser();
			//Giving a heading to the color-picker window. 
			Color color = selectColor.showDialog(null, "Pick a color", Color.black);
			//The text is considered as the foreground.
			textArea.setForeground(color);
		}

		//If the action was performed to change the color of the font, then implement the same.
		if(e.getSource() == fontBox)
		{
			//Setting the text area with the new font that was selected, considering the existing font style (plain) and its size.
			textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
		}
		
		if(e.getSource() == bgColor)
		{
			JColorChooser selectColor = new JColorChooser();
			Color color = selectColor.showDialog(null, "Pick a color", Color.black);
			textArea.setBackground(color);
		}
		
		if(e.getSource() == openItem)
		{
			JFileChooser chooseFile = new JFileChooser();
			//Setting the default location of the file to be opened.
			chooseFile.setCurrentDirectory(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			chooseFile.setFileFilter(filter);
			
			int response = chooseFile.showOpenDialog(null);
			if(response == JFileChooser.APPROVE_OPTION)
			{
				File file = new File(chooseFile.getSelectedFile().getAbsolutePath());
				Scanner fileIn = null;
				
				try
				{
					fileIn = new Scanner(file);
					if(file.isFile())
					{
						while(fileIn.hasNextLine())
						{
							String line = fileIn.nextLine() + "\n";
							textArea.append(line);
						}
					}
				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				finally
				{
					fileIn.close();
				}
			}
		}
		
		if(e.getSource() == saveItem)
		{
			JFileChooser chooseFile = new JFileChooser();
			//Setting the default location of the file to be saved.
			chooseFile.setCurrentDirectory(new File("."));
			
			//Showing the saving window.
			int response = chooseFile.showSaveDialog(null);
			//If "Yes" is selected...
			if(response == JFileChooser.APPROVE_OPTION)
			{
				File file;
				PrintWriter fileOut = null;
				
				file = new File(chooseFile.getSelectedFile().getAbsolutePath());
				try
				{
					fileOut = new PrintWriter(file);
					fileOut.println(textArea.getText());
				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				finally
				{
					fileOut.close();
				}
			}
		}
		
		if(e.getSource() == exitItem)
		{
			System.exit(0);
		}
	}
}
