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

		/*
		*The below code is to implement the above items into the menu bar
		menuBar.add(fontLabel);
		menuBar.add(fontSize);
		menuBar.add(fontColor);
		menuBar.add(bgColor);
		menuBar.add(fontBox);
		
		*Comment all the frames mentioned below - same as the ones uncommented above. 
		*/

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
		
		//If action performed is clicking the background color change button.
		if(e.getSource() == bgColor)
		{
			JColorChooser selectColor = new JColorChooser();
			//Showing the color selector window.
			Color color = selectColor.showDialog(null, "Pick a color", Color.black);
			//Applying the selected color to the background.
			textArea.setBackground(color);
		}
		
		//Open item is clicked.
		if(e.getSource() == openItem)
		{
			JFileChooser chooseFile = new JFileChooser();
			//Setting the default location of the file to be opened.
			chooseFile.setCurrentDirectory(new File("."));
			//Add filters for the file name extensions - giving lists for certain file types.
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			//Setting the chosen filter/list item of the file type.
			chooseFile.setFileFilter(filter);
			
			int response = chooseFile.showOpenDialog(null);
			if(response == JFileChooser.APPROVE_OPTION)
			{
				//Get the file selected by the user from the file path and set it to an object.
				File file = new File(chooseFile.getSelectedFile().getAbsolutePath());
				//New scanner to read the file - will be initialized as a new file type scanner.
				Scanner fileIn = null;
				
				//To deal with file issues like corruption, locking, permission problems, try-catch is used.
				try
				{
					fileIn = new Scanner(file);
					//If the file is a type of file, then
					if(file.isFile())
					{
						//Check if there is another line in the file.
						while(fileIn.hasNextLine())
						{
							//While it is true, return that line and append the next line to that.
							String line = fileIn.nextLine() + "\n";
							//Then append that new line to the existing line present in the text area.
							textArea.append(line);
						}
					}
				}
				//Catch errors popping up from file related issues.
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				//After all the process is complete, close the scanner class to prevent resource leak/file corruption and to free up system memory..
				finally
				{
					fileIn.close();
				}
			}
		}
		
		//Save file option is clicked.
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
				//Initializing the object to write the data into the file.
				PrintWriter fileOut = null;
				
				file = new File(chooseFile.getSelectedFile().getAbsolutePath());
				try
				{
					//Opening the file and writing into it.
					fileOut = new PrintWriter(file);
					//Gets the current text in the text area and writes it into the file.
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
		//If the exit button is clicked. Exit the application immediately.
		if(e.getSource() == exitItem)
		{
			System.exit(0);
		}
	}
}
