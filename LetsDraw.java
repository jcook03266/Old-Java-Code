package NotAPhotoShopCopy;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;  
import javax.swing.JOptionPane;
import java.awt.*;  
import javax.swing.*;  
import javax.swing.JButton; 
import javax.swing.JTextArea;
import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

/*Written by Justin Cook ID: 1268790
 *CSCI 185 M06
 *Java class 201
 *4/23/20
 *Spring 2020
 *Lab 11: Create Your Own Paint Program
 *The following program is a program oriented around a user painting on a blank canvas, much like MSPaint.
 *The basic version of the paint program allows the user to select from several colors, 
 *such as red, blue, and green,and also erase the current paint, or clear up the whole canvas.
 *an additional slider that allows the user to customize the width of the paint brush as well as eraser has been added 
 *an additional button that can save what you drew to an image file (such as jpg or gif, etc.) has also been added along with many other amenities. 
 */

public class LetsDraw extends JFrame
{ /**
 * 
 */
	private static final long serialVersionUID = 1L;

	JFrame f; //setting our JFrame variable
	JOptionPane pane = new JOptionPane();//creating a new pane
	private int canvas_size_percentage = 100;//canvas zoom percentage, will be utilized later on 
	private String save_name = null;//save name, to be determined by the user 
	private int press_amount = 0;//Press amount of the top drop down menu buttons, prevents multiple menus from being open at once
	private int index = 0;//index for the line drawing process
	private Point[] arr = new Point[1000000];//stored points for drawing, the limit is very high but will never be reached realistically speaking 
	private BufferedImage im;//buffered image variable, will be used to save our masterpiece later on
	private Graphics2D h;//2D graphics variables assigned to the created graphic of the buffered image object 
	private boolean Pen_Tool = true;//the default tool the user will be using, small compact, reliable
	private boolean Marker_Tool = false;//secondary pen tool the user has access to, much larger, and less uniform as a result
	private boolean Eraser_Tool = false;//Erases the mistakes of the pass, used in place of an undo button for now
	private Color pen_color = Color.black;//the default color of the pen 
	private int brush_size_unit = 5;//the default size of the pen
	private int eraser_size_unit = 50;//the default size of the eraser pen 
	//end of instance variable declarations 

	public LetsDraw(){
		//New frame is enumerated 
		f = new JFrame();

		//New border styles
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border whiteline = BorderFactory.createLineBorder(Color.white);
		Border grayline = BorderFactory.createLineBorder(Color.gray);

		//Fonts 
		Font f1 = new Font("Helvetica",Font.PLAIN, 10);
		Font f2 = new Font("Helvetica",Font.PLAIN, 15);


		//Color(s)
		Color white = new Color(255,255,255);

		//Panel Styling note:
		//(x, y, width, height) x & y refer to the coordinates of the upper left corner of the component relative to the frame corner
		//with 0,0 being the actual corner of the frame / window
		//Panels
		//Page Header 1 Quick Tools Button panel
		JPanel Quick_Tools_Panel_Expansion = new JPanel();
		Quick_Tools_Panel_Expansion.setLayout(new GridLayout(4, 1));
		Quick_Tools_Panel_Expansion.setBounds(250,30,120,120); 
		Quick_Tools_Panel_Expansion.setBackground(Color.BLACK);
		Quick_Tools_Panel_Expansion.setBorder(whiteline);
		Quick_Tools_Panel_Expansion.setVisible(false);
		f.add(Quick_Tools_Panel_Expansion);

		//Eraser Button
		JPanel Quick_Tools_Menu_Button_Eraser = new JPanel();
		Quick_Tools_Menu_Button_Eraser.setLayout(new BorderLayout());
		Quick_Tools_Menu_Button_Eraser.setBackground(Color.BLACK);
		JButton Quick_Tools_Eraser = new JButton("Eraser");
		Quick_Tools_Eraser.setFont(f2);
		Quick_Tools_Eraser.setHorizontalAlignment(SwingConstants.LEFT);
		Quick_Tools_Eraser.setBackground(Color.black);
		Quick_Tools_Eraser.setForeground(Color.WHITE);
		Quick_Tools_Menu_Button_Eraser.add(Quick_Tools_Eraser);
		Quick_Tools_Panel_Expansion.add(Quick_Tools_Menu_Button_Eraser);

		//Pen Button
		JPanel Quick_Tools_Menu_Button_Pen = new JPanel();
		Quick_Tools_Menu_Button_Pen.setLayout(new BorderLayout());
		Quick_Tools_Menu_Button_Pen.setBackground(Color.BLACK);
		JButton Quick_Tools_Pen = new JButton("Pen");
		Quick_Tools_Pen.setFont(f2);
		Quick_Tools_Pen.setHorizontalAlignment(SwingConstants.LEFT);
		Quick_Tools_Pen.setBackground(Color.black);
		Quick_Tools_Pen.setForeground(Color.WHITE);
		Quick_Tools_Menu_Button_Pen.add(Quick_Tools_Pen);
		Quick_Tools_Panel_Expansion.add(Quick_Tools_Menu_Button_Pen);

		//Marker Button
		JPanel Quick_Tools_Menu_Button_Marker = new JPanel();
		Quick_Tools_Menu_Button_Marker.setLayout(new BorderLayout());
		Quick_Tools_Menu_Button_Marker.setBackground(Color.BLACK);
		JButton Quick_Tools_Marker = new JButton("Marker");
		Quick_Tools_Marker.setFont(f2);
		Quick_Tools_Marker.setHorizontalAlignment(SwingConstants.LEFT);
		Quick_Tools_Marker.setBackground(Color.black);
		Quick_Tools_Marker.setForeground(Color.WHITE);
		Quick_Tools_Menu_Button_Marker.add(Quick_Tools_Marker);
		Quick_Tools_Panel_Expansion.add(Quick_Tools_Menu_Button_Marker);

		//Clear Button
		JPanel Quick_Tools_Menu_Button_Clear = new JPanel();
		Quick_Tools_Menu_Button_Clear.setLayout(new BorderLayout());
		Quick_Tools_Menu_Button_Clear.setBackground(Color.BLACK);
		JButton Quick_Tools_Clear = new JButton("Clear");
		Quick_Tools_Clear.setFont(f2);
		Quick_Tools_Clear.setHorizontalAlignment(SwingConstants.LEFT);
		Quick_Tools_Clear.setBackground(Color.black);
		Quick_Tools_Clear.setForeground(Color.WHITE);
		Quick_Tools_Menu_Button_Clear.add(Quick_Tools_Clear);
		Quick_Tools_Panel_Expansion.add(Quick_Tools_Menu_Button_Clear);
		//End of Quick Tools Drop Down Menu 

		//Page Header 1 View Button panel
		JPanel View_Panel_Expansion = new JPanel();
		View_Panel_Expansion.setLayout(new GridLayout(4, 1));
		View_Panel_Expansion.setBounds(150,30,150,120); 
		View_Panel_Expansion.setBackground(Color.BLACK);
		View_Panel_Expansion.setBorder(whiteline);
		View_Panel_Expansion.setVisible(false);
		f.add(View_Panel_Expansion);

		//Zoom in button
		JPanel View_Menu_Button_Zoom_in = new JPanel();
		View_Menu_Button_Zoom_in.setLayout(new BorderLayout());
		View_Menu_Button_Zoom_in.setBackground(Color.BLACK);
		JButton View_Zoom_in = new JButton("Zoom In [WIP]");
		View_Zoom_in.setFont(f2);
		View_Zoom_in.setHorizontalAlignment(SwingConstants.LEFT);
		View_Zoom_in.setBackground(Color.black);
		View_Zoom_in.setForeground(Color.WHITE);
		View_Menu_Button_Zoom_in.add(View_Zoom_in);
		View_Panel_Expansion.add(View_Menu_Button_Zoom_in);

		//Zoom out button
		JPanel View_Menu_Button_Zoom_out = new JPanel();
		View_Menu_Button_Zoom_out.setLayout(new BorderLayout());
		View_Menu_Button_Zoom_out.setBackground(Color.BLACK);
		JButton View_Zoom_out = new JButton("Zoom Out [WIP]");
		View_Zoom_out.setFont(f2);
		View_Zoom_out.setHorizontalAlignment(SwingConstants.LEFT);
		View_Zoom_out.setBackground(Color.black);
		View_Zoom_out.setForeground(Color.WHITE);
		View_Menu_Button_Zoom_out.add(View_Zoom_out);
		View_Panel_Expansion.add(View_Menu_Button_Zoom_out);

		//100% Zoom Button
		JPanel View_Menu_Button_Zoom_100 = new JPanel();
		View_Menu_Button_Zoom_100.setLayout(new BorderLayout());
		View_Menu_Button_Zoom_100.setBackground(Color.BLACK);
		JButton View_Zoom_100 = new JButton("100% [WIP]");
		View_Zoom_100.setFont(f2);
		View_Zoom_100.setHorizontalAlignment(SwingConstants.LEFT);
		View_Zoom_100.setBackground(Color.black);
		View_Zoom_100.setForeground(Color.WHITE);
		View_Menu_Button_Zoom_100.add(View_Zoom_100);
		View_Panel_Expansion.add(View_Menu_Button_Zoom_100);

		//200% Zoom Button
		JPanel View_Menu_Button_Zoom_200 = new JPanel();
		View_Menu_Button_Zoom_200.setLayout(new BorderLayout());
		View_Menu_Button_Zoom_200.setBackground(Color.BLACK);
		JButton View_Zoom_200 = new JButton("200% [WIP]");
		View_Zoom_200.setFont(f2);
		View_Zoom_200.setHorizontalAlignment(SwingConstants.LEFT);
		View_Zoom_200.setBackground(Color.black);
		View_Zoom_200.setForeground(Color.WHITE);
		View_Menu_Button_Zoom_200.add(View_Zoom_200);
		View_Panel_Expansion.add(View_Menu_Button_Zoom_200);

		//File Drop Down menu Panel
		JPanel File_Panel_Expansion = new JPanel();
		File_Panel_Expansion.setLayout(new GridLayout(4, 1));
		File_Panel_Expansion.setBounds(50,30,120,120); 
		File_Panel_Expansion.setBackground(Color.BLACK);
		File_Panel_Expansion.setBorder(whiteline);
		File_Panel_Expansion.setVisible(false);
		//File's Drop Down Menu buttons

		//New button
		JPanel File_Menu_Button_New = new JPanel();
		File_Menu_Button_New.setLayout(new BorderLayout());
		File_Menu_Button_New.setBackground(Color.BLACK);
		JButton File_New = new JButton("New");
		File_New.setFont(f2);
		File_New.setHorizontalAlignment(SwingConstants.LEFT);
		File_New.setBackground(Color.black);
		File_New.setForeground(Color.WHITE);
		File_Menu_Button_New.add(File_New);
		File_Panel_Expansion.add(File_Menu_Button_New);

		//Save button
		JPanel File_Menu_Button_Save = new JPanel();
		File_Menu_Button_Save.setLayout(new BorderLayout());
		File_Menu_Button_Save.setBackground(Color.BLACK);
		JButton File_Save = new JButton("Save");
		File_Save.setFont(f2);
		File_Save.setHorizontalAlignment(SwingConstants.LEFT);
		File_Save.setBackground(Color.black);
		File_Save.setForeground(Color.WHITE);
		File_Menu_Button_Save.add(File_Save);
		File_Panel_Expansion.add(File_Menu_Button_Save);

		//Save As button
		JPanel File_Menu_Button_Save_As = new JPanel();
		File_Menu_Button_Save_As.setLayout(new BorderLayout());
		File_Menu_Button_Save_As.setBackground(Color.BLACK);
		JButton File_Save_As = new JButton("Save As...");
		File_Save_As.setFont(f2);
		File_Save_As.setHorizontalAlignment(SwingConstants.LEFT);
		File_Save_As.setBackground(Color.black);
		File_Save_As.setForeground(Color.WHITE);
		File_Menu_Button_Save_As.add(File_Save_As);
		File_Panel_Expansion.add(File_Menu_Button_Save_As);

		//Exit button
		JPanel File_Menu_Button_Exit = new JPanel();
		File_Menu_Button_Exit.setLayout(new BorderLayout());
		File_Menu_Button_Exit.setBackground(Color.BLACK);
		JButton File_Exit = new JButton("Exit");
		File_Exit.setFont(f2);
		File_Exit.setHorizontalAlignment(SwingConstants.LEFT);
		File_Exit.setBackground(Color.black);
		File_Exit.setForeground(Color.WHITE);
		File_Menu_Button_Exit.add(File_Exit);
		File_Panel_Expansion.add(File_Menu_Button_Exit);
		f.add(File_Panel_Expansion);
		//End of File's Drop Down Menu

		//Mini Side Panel 3 Subset 3
		JPanel Mini_Side_Panel3_Subset3 = new JPanel();
		Mini_Side_Panel3_Subset3.setLayout(new BorderLayout());
		Mini_Side_Panel3_Subset3.setBounds(1240,660,20,15); 
		Mini_Side_Panel3_Subset3.setBorder(blackline);
		Mini_Side_Panel3_Subset3.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel3_Subset3);

		//Mini Side Panel 3 Subset 2
		JPanel Mini_Side_Panel3_Subset2 = new JPanel();
		Mini_Side_Panel3_Subset2.setLayout(new BorderLayout());
		Mini_Side_Panel3_Subset2.setBounds(1140,655,100,20); 
		Mini_Side_Panel3_Subset2.setBorder(blackline);
		Mini_Side_Panel3_Subset2.setBackground(Color.LIGHT_GRAY);
		f.add(Mini_Side_Panel3_Subset2);

		//Mini Side Panel 3 Subset 1
		JPanel Mini_Side_Panel3_Subset1 = new JPanel();
		Mini_Side_Panel3_Subset1.setLayout(new BorderLayout());
		Mini_Side_Panel3_Subset1.setBounds(1040,650,100,25);
		Mini_Side_Panel3_Subset1.setBorder(blackline);
		Mini_Side_Panel3_Subset1.setBackground(Color.DARK_GRAY);
		Mini_Side_Panel3_Subset1.add(new JLabel("<html><font size='3.5'color=white> Event Log </font></html>"));
		f.add(Mini_Side_Panel3_Subset1);

		//Mini Side Panel 3.5 //The event log 
		JPanel Mini_Side_Panel3_5 = new JPanel();
		Mini_Side_Panel3_5.setLayout(new BorderLayout());
		Mini_Side_Panel3_5.setBounds(1050,685,200,230); 
		Mini_Side_Panel3_5.setBorder(blackline);
		Mini_Side_Panel3_5.setBackground(Color.DARK_GRAY);
		JTextArea MSP3_5 = new JTextArea();
		Mini_Side_Panel3_5.add(MSP3_5);
		MSP3_5.setEditable(false);
		JScrollPane MSP3_5SP = new JScrollPane(MSP3_5);
		MSP3_5SP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		MSP3_5SP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		Mini_Side_Panel3_5.add(MSP3_5SP);
		f.add(Mini_Side_Panel3_5);

		//Mini Side Panel 3
		JPanel Mini_Side_Panel3 = new JPanel();
		Mini_Side_Panel3.setLayout(new BorderLayout());
		Mini_Side_Panel3.setBounds(1040,675,220,250); 
		Mini_Side_Panel3.setBorder(blackline);
		Mini_Side_Panel3.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel3);

		//Mini Side Panel 2 Subset 3
		JPanel Mini_Side_Panel2_Subset3 = new JPanel();
		Mini_Side_Panel2_Subset3.setLayout(new BorderLayout());
		Mini_Side_Panel2_Subset3.setBounds(1240,280,20,15); 
		Mini_Side_Panel2_Subset3.setBorder(blackline);
		Mini_Side_Panel2_Subset3.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel2_Subset3);

		//Mini Side Panel 2 Subset 2
		JPanel Mini_Side_Panel2_Subset2 = new JPanel();
		Mini_Side_Panel2_Subset2.setLayout(new BorderLayout());
		Mini_Side_Panel2_Subset2.setBounds(1140,275,100,20); 
		Mini_Side_Panel2_Subset2.setBorder(blackline);
		Mini_Side_Panel2_Subset2.setBackground(Color.LIGHT_GRAY);
		f.add(Mini_Side_Panel2_Subset2);

		//Mini Side Panel 2 Subset 1
		JPanel Mini_Side_Panel2_Subset1 = new JPanel();
		Mini_Side_Panel2_Subset1.setLayout(new BorderLayout());
		Mini_Side_Panel2_Subset1.setBounds(1040,270,100,25);
		Mini_Side_Panel2_Subset1.setBorder(blackline);
		Mini_Side_Panel2_Subset1.setBackground(Color.DARK_GRAY);
		Mini_Side_Panel2_Subset1.add(new JLabel("<html><font size='3.5'color=white> Properties </font></html>"));
		f.add(Mini_Side_Panel2_Subset1);

		//Mini Side Panel 2.5 
		JPanel Mini_Side_Panel2_5 = new JPanel();
		Mini_Side_Panel2_5.setLayout(new GridLayout(2, 2));
		Mini_Side_Panel2_5.setBounds(1050,305,200,330); 
		Mini_Side_Panel2_5.setBorder(blackline);
		Mini_Side_Panel2_5.setBackground(Color.DARK_GRAY);
		f.add(Mini_Side_Panel2_5);

		//Brush Size Slider Label
		JLabel brush_Size_label = new JLabel("Brush Size");
		brush_Size_label.setBackground(Color.BLACK); 
		brush_Size_label.setForeground(Color.WHITE);
		Mini_Side_Panel2_5.add(brush_Size_label);

		//Brush Size Slider
		JPanel brush_Size = new JPanel();
		brush_Size.setLayout(new BorderLayout());
		brush_Size.setBackground(Color.BLACK);
		JSlider brush_size_slider = new JSlider(JSlider.VERTICAL,
				1, 30, 15);
		brush_size_slider.setMinorTickSpacing(10);
		brush_size_slider.setPaintTicks(true);
		brush_size_slider.setPaintLabels(true);
		Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
		position.put(1, new JLabel("Small"));
		position.put(15, new JLabel("Medium"));
		position.put(30, new JLabel("Large"));	
		brush_size_slider.setLabelTable(position);   
		brush_Size.add(brush_size_slider);      
		Mini_Side_Panel2_5.add(brush_Size);

		//Eraser Size Slider Label
		JLabel Eraser_Size_label = new JLabel("Eraser Size");
		Eraser_Size_label.setBackground(Color.BLACK); 
		Eraser_Size_label.setForeground(Color.WHITE);
		Mini_Side_Panel2_5.add(Eraser_Size_label);

		//Eraser Size Slider
		JPanel eraser_Size = new JPanel();
		eraser_Size.setLayout(new BorderLayout());
		eraser_Size.setBackground(Color.BLACK);
		JSlider eraser_size_slider = new JSlider(JSlider.VERTICAL,
				20, 80, 50);
		eraser_size_slider.setMinorTickSpacing(10);
		eraser_size_slider.setPaintTicks(true);
		eraser_size_slider.setPaintLabels(true);
		Hashtable<Integer, JLabel> position2 = new Hashtable<Integer, JLabel>();
		position2.put(20, new JLabel("Small"));
		position2.put(50, new JLabel("Medium"));
		position2.put(80, new JLabel("Large"));
		eraser_size_slider.setLabelTable(position2);
		eraser_Size.add(eraser_size_slider); 
		Mini_Side_Panel2_5.add(eraser_size_slider);

		//Mini Side Panel 2 
		JPanel Mini_Side_Panel2 = new JPanel();
		Mini_Side_Panel2.setLayout(new BorderLayout());
		Mini_Side_Panel2.setBounds(1040,295,220,350); 
		Mini_Side_Panel2.setBorder(blackline);
		Mini_Side_Panel2.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel2);

		//Mini Side Panel 1 Subset 3
		JPanel Mini_Side_Panel1_Subset3 = new JPanel();
		Mini_Side_Panel1_Subset3.setLayout(new BorderLayout());
		Mini_Side_Panel1_Subset3.setBounds(1240,30,20,15); 
		Mini_Side_Panel1_Subset3.setBorder(blackline);
		Mini_Side_Panel1_Subset3.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel1_Subset3);

		//Mini Side Panel 1 Subset 2
		JPanel Mini_Side_Panel1_Subset2 = new JPanel();
		Mini_Side_Panel1_Subset2.setLayout(new BorderLayout());
		Mini_Side_Panel1_Subset2.setBounds(1140,25,100,20); 
		Mini_Side_Panel1_Subset2.setBorder(blackline);
		Mini_Side_Panel1_Subset2.setBackground(Color.LIGHT_GRAY);
		f.add(Mini_Side_Panel1_Subset2);

		//Mini Side Panel 1 Subset 1
		JPanel Mini_Side_Panel1_Subset1 = new JPanel();
		Mini_Side_Panel1_Subset1.setLayout(new BorderLayout());
		Mini_Side_Panel1_Subset1.setBounds(1040,20,100,25); 
		Mini_Side_Panel1_Subset1.setBorder(blackline);
		Mini_Side_Panel1_Subset1.setBackground(Color.DARK_GRAY);
		Mini_Side_Panel1_Subset1.add(new JLabel("<html><font size='3.5'color=white> Color Options </font></html>"));
		f.add(Mini_Side_Panel1_Subset1);

		//Mini Side Panel 1.5 
		JPanel Mini_Side_Panel1_5 = new JPanel();
		Mini_Side_Panel1_5.setLayout(new GridLayout(10, 2));
		Mini_Side_Panel1_5.setBounds(1050,55,200,200); 
		Mini_Side_Panel1_5.setBorder(blackline);
		Mini_Side_Panel1_5.setBackground(Color.DARK_GRAY);
		f.add(Mini_Side_Panel1_5);

		//Red Color Button
		JPanel Red_color_Panel = new JPanel();
		Red_color_Panel.setLayout(new BorderLayout());
		Red_color_Panel.setBackground(Color.BLACK);
		JButton Red_color_Button = new JButton("Red");
		Red_color_Button.setFont(f2);
		Red_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		Red_color_Button.setBackground(Color.red);
		Red_color_Button.setForeground(Color.BLACK);
		Red_color_Panel.add(Red_color_Button);
		Mini_Side_Panel1_5.add(Red_color_Panel);

		//Green Color Button
		JPanel Green_color_Panel = new JPanel();
		Green_color_Panel.setLayout(new BorderLayout());
		Green_color_Panel.setBackground(Color.BLACK);
		JButton Green_color_Button = new JButton("Green");
		Green_color_Button.setFont(f2);
		Green_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		Green_color_Button.setBackground(Color.green);
		Green_color_Button.setForeground(Color.BLACK);
		Green_color_Panel.add(Green_color_Button);
		Mini_Side_Panel1_5.add(Green_color_Panel);

		//Blue Color Button
		JPanel Blue_color_Panel = new JPanel();
		Blue_color_Panel.setLayout(new BorderLayout());
		Blue_color_Panel.setBackground(Color.BLACK);
		JButton Blue_color_Button = new JButton("Blue");
		Blue_color_Button.setFont(f2);
		Blue_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		Blue_color_Button.setBackground(Color.blue);
		Blue_color_Button.setForeground(Color.WHITE);
		Blue_color_Panel.add(Blue_color_Button);
		Mini_Side_Panel1_5.add(Blue_color_Panel);

		//Yellow Color Button
		JPanel Yellow_color_Panel = new JPanel();
		Yellow_color_Panel.setLayout(new BorderLayout());
		Yellow_color_Panel.setBackground(Color.BLACK);
		JButton Yellow_color_Button = new JButton("Yellow");
		Yellow_color_Button.setFont(f2);
		Yellow_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		Yellow_color_Button.setBackground(Color.yellow);
		Yellow_color_Button.setForeground(Color.BLACK);
		Yellow_color_Panel.add(Yellow_color_Button);
		Mini_Side_Panel1_5.add(Yellow_color_Panel);

		//Orange Color Button
		JPanel Orange_color_Panel = new JPanel();
		Orange_color_Panel.setLayout(new BorderLayout());
		Orange_color_Panel.setBackground(Color.BLACK);
		JButton Orange_color_Button = new JButton("Orange");
		Orange_color_Button.setFont(f2);
		Orange_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		Orange_color_Button.setBackground(Color.orange);
		Orange_color_Button.setForeground(Color.BLACK);
		Orange_color_Panel.add(Orange_color_Button);
		Mini_Side_Panel1_5.add(Orange_color_Panel);

		//Cyan Color Button
		JPanel cyan_color_Panel = new JPanel();
		cyan_color_Panel.setLayout(new BorderLayout());
		cyan_color_Panel.setBackground(Color.BLACK);
		JButton cyan_color_Button = new JButton("Cyan");
		cyan_color_Button.setFont(f2);
		cyan_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		cyan_color_Button.setBackground(Color.cyan);
		cyan_color_Button.setForeground(Color.BLACK);
		cyan_color_Panel.add(cyan_color_Button);
		Mini_Side_Panel1_5.add(cyan_color_Panel);

		//Pink Color Button
		JPanel pink_color_Panel = new JPanel();
		pink_color_Panel.setLayout(new BorderLayout());
		pink_color_Panel.setBackground(Color.BLACK);
		JButton pink_color_Button = new JButton("Pink");
		pink_color_Button.setFont(f2);
		pink_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		pink_color_Button.setBackground(Color.pink);
		pink_color_Button.setForeground(Color.BLACK);
		pink_color_Panel.add(pink_color_Button);
		Mini_Side_Panel1_5.add(pink_color_Panel);

		//Gray Color Button
		JPanel gray_color_Panel = new JPanel();
		gray_color_Panel.setLayout(new BorderLayout());
		gray_color_Panel.setBackground(Color.BLACK);
		JButton gray_color_Button = new JButton("Gray");
		gray_color_Button.setFont(f2);
		gray_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		gray_color_Button.setBackground(Color.gray);
		gray_color_Button.setForeground(Color.BLACK);
		gray_color_Panel.add(gray_color_Button);
		Mini_Side_Panel1_5.add(gray_color_Panel);

		//Black Color Button
		JPanel black_color_Panel = new JPanel();
		black_color_Panel.setLayout(new BorderLayout());
		black_color_Panel.setBackground(Color.BLACK);
		JButton black_color_Button = new JButton("Black (Default)");
		black_color_Button.setFont(f2);
		black_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		black_color_Button.setBackground(Color.black);
		black_color_Button.setForeground(Color.WHITE);
		black_color_Panel.add(black_color_Button);
		Mini_Side_Panel1_5.add(black_color_Panel);

		//Black Color Button
		JPanel eraser_color_Panel = new JPanel();
		eraser_color_Panel.setLayout(new BorderLayout());
		eraser_color_Panel.setBackground(Color.BLACK);
		JButton eraser_color_Button = new JButton("Eraser");
		eraser_color_Button.setFont(f2);
		eraser_color_Button.setHorizontalAlignment(SwingConstants.LEFT);
		eraser_color_Button.setBackground(Color.white);
		eraser_color_Button.setForeground(Color.BLACK);
		eraser_color_Panel.add(eraser_color_Button);
		Mini_Side_Panel1_5.add(eraser_color_Panel);


		//Mini Side Panel 1 
		JPanel Mini_Side_Panel1 = new JPanel();
		Mini_Side_Panel1.setLayout(new BorderLayout());
		Mini_Side_Panel1.setBounds(1040,45,220,220); 
		Mini_Side_Panel1.setBorder(blackline);
		Mini_Side_Panel1.setBackground(Color.GRAY);
		f.add(Mini_Side_Panel1);

		//Side Panel 1
		JPanel Side_Panel1 = new JPanel();
		Side_Panel1.setLayout(new BorderLayout());
		Side_Panel1.setBounds(1030,15,250,940); 
		Side_Panel1.setBorder(blackline);
		Side_Panel1.setBackground(Color.DARK_GRAY);
		f.add(Side_Panel1);

		//Window Padding 1
		JPanel Window_Padding1 = new JPanel();
		Window_Padding1.setLayout(new BorderLayout());
		Window_Padding1.setBounds(1040,30,220,900); 
		Window_Padding1.setBorder(blackline);
		Window_Padding1.setBackground(Color.GRAY);
		f.add(Window_Padding1);

		//Left Side panel1
		JPanel Left_Side_Panel1 = new JPanel();
		Left_Side_Panel1.setLayout(new BorderLayout());
		Left_Side_Panel1.setBounds(0,30,50,950); 
		Left_Side_Panel1.setBorder(blackline);
		Left_Side_Panel1.setBackground(Color.DARK_GRAY);
		f.add(Left_Side_Panel1);

		//Page Header 2 Readout Console 
		JTextArea Page_Readout_Console = new JTextArea();
		Page_Readout_Console.setLayout(new BorderLayout());
		Page_Readout_Console.setBounds(140,33,860,18); 
		Page_Readout_Console.setBorder(grayline);
		Page_Readout_Console.setFont(f2);
		Page_Readout_Console.setEditable(false);
		Page_Readout_Console.setBackground(Color.WHITE);
		f.add(Page_Readout_Console);

		//Page Header 2 Readout Console subset 
		JTextArea Page_Readout_Console_Label = new JTextArea();
		Page_Readout_Console_Label.setLayout(new BorderLayout());
		Page_Readout_Console_Label.setBounds(70,33,70,18); 
		Page_Readout_Console_Label.setBorder(grayline);
		Page_Readout_Console_Label.setFont(f2);
		Page_Readout_Console_Label.setText("\\\\Console:");
		Page_Readout_Console_Label.setEditable(false);
		Page_Readout_Console_Label.setBackground(Color.WHITE);
		f.add(Page_Readout_Console_Label);

		//Page Header 2 Readout Console subset 2 
		JTextArea Page_Readout_Console_Label2 = new JTextArea();
		Page_Readout_Console_Label2.setLayout(new BorderLayout());
		Page_Readout_Console_Label2.setBounds(1000,33,30,18); 
		Page_Readout_Console_Label2.setBorder(grayline);
		Page_Readout_Console_Label2.setFont(f2);
		Page_Readout_Console_Label2.setText(":\\\\");
		Page_Readout_Console_Label2.setEditable(false);
		Page_Readout_Console_Label2.setBackground(Color.WHITE);
		f.add(Page_Readout_Console_Label2);

		//Page Header 2 Inside Panel
		JPanel Page_Header2_Inside = new JPanel();
		Page_Header2_Inside.setLayout(new BorderLayout());
		Page_Header2_Inside.setBounds(60,30,1015,35); 
		Page_Header2_Inside.setBorder(blackline);
		Page_Header2_Inside.setBackground(Color.DARK_GRAY);
		f.add(Page_Header2_Inside);

		//Page Header 2  
		JPanel Page_Header2 = new JPanel();
		Page_Header2.setLayout(new BorderLayout());
		Page_Header2.setBounds(50,30,1005,40); 
		Page_Header2.setBorder(blackline);
		Page_Header2.setBackground(Color.BLACK);
		f.add(Page_Header2);

		//Page footer 2 subset  
		JPanel Page_Footer2_subset = new JPanel();
		Page_Footer2_subset.setLayout(new BorderLayout());
		Page_Footer2_subset.setBounds(1035,960,230,15); 
		Page_Footer2_subset.setBorder(blackline);
		Page_Footer2_subset.setBackground(Color.DARK_GRAY);
		f.add(Page_Footer2_subset);

		//Page footer 2  
		JPanel Page_Footer2 = new JPanel();
		Page_Footer2.setLayout(new BorderLayout());
		Page_Footer2.setBounds(1030,950,245,50); 
		Page_Footer2.setBorder(blackline);
		Page_Footer2.setBackground(Color.DARK_GRAY);
		f.add(Page_Footer2);

		//Page footer 1 subset
		JPanel Page_Footer1_subset = new JPanel();
		Page_Footer1_subset.setLayout(new BorderLayout());
		Page_Footer1_subset.setBounds(52,960,50,15); 
		Page_Footer1_subset.setBorder(blackline);
		Page_Footer1_subset.setBackground(Color.DARK_GRAY);
		//Text area for explicitly stating the zoom percentage of the text area
		JTextArea canvas_size = new JTextArea("  " + canvas_size_percentage + "%"); 
		canvas_size.setFont(f1);
		canvas_size.setForeground(Color.black);
		canvas_size.setEditable(false);
		Page_Footer1_subset.add(canvas_size);
		f.add(Page_Footer1_subset);

		//Page footer 1  
		JPanel Page_Footer1 = new JPanel();
		Page_Footer1.setLayout(new BorderLayout());
		Page_Footer1.setBounds(55,960,975,15); 
		Page_Footer1.setBorder(blackline);
		Page_Footer1.setBackground(Color.GRAY);
		f.add(Page_Footer1);

		//Page Header 1 Help Button
		JPanel Page_Header1_Help_Button = new JPanel();
		Page_Header1_Help_Button.setLayout(new BorderLayout());
		Page_Header1_Help_Button.setBounds(400,10,100,19); 
		Page_Header1_Help_Button.setBackground(Color.GRAY);
		JButton Help = new JButton("Help");
		Help.setFont(f2);
		Help.setBackground(Color.black);
		Help.setForeground(Color.WHITE);
		Page_Header1_Help_Button.add(Help);
		f.add(Page_Header1_Help_Button);

		//Page Header 1 Quick Tools Button
		JPanel Page_Header1_Quick_Tools_Button = new JPanel();
		Page_Header1_Quick_Tools_Button.setLayout(new BorderLayout());
		Page_Header1_Quick_Tools_Button.setBounds(250,10,150,19); 
		Page_Header1_Quick_Tools_Button.setBackground(Color.GRAY);
		JButton Quick_Tools = new JButton("Quick Tools");
		Quick_Tools.setFont(f2);
		Quick_Tools.setBackground(Color.black);
		Quick_Tools.setForeground(Color.WHITE);
		Page_Header1_Quick_Tools_Button.add(Quick_Tools);
		f.add(Page_Header1_Quick_Tools_Button);

		//Page Header 1 View Button
		JPanel Page_Header1_View_Button = new JPanel();
		Page_Header1_View_Button.setLayout(new BorderLayout());
		Page_Header1_View_Button.setBounds(150,10,100,19); 
		Page_Header1_View_Button.setBackground(Color.GRAY);
		JButton View = new JButton("View");
		View.setFont(f2);
		View.setBackground(Color.black);
		View.setForeground(Color.WHITE);
		Page_Header1_View_Button.add(View);
		f.add(Page_Header1_View_Button);

		//Page Header 1 File Button
		JPanel Page_Header1_File_Button = new JPanel();
		Page_Header1_File_Button.setLayout(new BorderLayout());
		Page_Header1_File_Button.setBounds(50,10,100,19); 
		Page_Header1_File_Button.setBackground(Color.GRAY);
		JButton File = new JButton("File");
		File.setFont(f2);
		File.setBackground(Color.black);
		File.setForeground(Color.WHITE);
		Page_Header1_File_Button.add(File);
		f.add(Page_Header1_File_Button);        

		//Page Header 1 subset  
		JPanel Page_Header1_subset = new JPanel();
		Page_Header1_subset.setLayout(new BorderLayout());
		Page_Header1_subset.setBounds(500,10,530,20); 
		Page_Header1_subset.setBorder(blackline);
		Page_Header1_subset.setBackground(Color.BLACK);
		f.add(Page_Header1_subset);

		//Page Header 1  
		JPanel Page_Header1 = new JPanel();
		Page_Header1.setLayout(new BorderLayout());
		Page_Header1.setBounds(5,5,1025,25); 
		Page_Header1.setBorder(blackline);
		Page_Header1.setBackground(Color.DARK_GRAY);
		f.add(Page_Header1);

		//Outer border 
		JPanel Outer_Border_panel = new JPanel ();
		Outer_Border_panel.setLayout(new BorderLayout());
		Outer_Border_panel.setBounds(50,70,980,850); 
		Outer_Border_panel.setBorder(blackline);
		Outer_Border_panel.setBackground(Color.BLACK);

		//The Canvas //Our drawing surface of choice 
		Canvas Canvas1 = new Canvas(){ 
			public void paint(Graphics g) 
			{ 
			} 
		}; 
		Canvas1.setBounds(0,0,975,845); 
		Canvas1.setBackground(Color.WHITE);
		Canvas1.setVisible(true);
		Outer_Border_panel.add(Canvas1);
		f.add(Outer_Border_panel);

		//declaring our bufferedImage which we will be manipulating side by side with the canvas as a real time volatile memory source 
		im = new BufferedImage(Canvas1.getWidth(),Canvas1.getHeight(), BufferedImage.TYPE_INT_ARGB);
		h = im.createGraphics();//creates graphics from the parameters given to the BufferedImage object, this new graphic will be filled with the appropriate damages 
		h.setColor(white);//sets the color for the painting source
		h.fillRect(0, 0, im.getWidth(), im.getHeight());//Fills up the Buffered image with white space by painting a white rectangle the size of its bound over the object 

		//Window1 border 
		JPanel Window1 = new JPanel();
		Window1.setLayout(new BorderLayout());
		Window1.setBounds(0,0,1280,1020); 
		Window1.setBorder(blackline);
		Window1.setBackground(Color.DARK_GRAY);
		f.add(Window1);
		//end of Panels

		//our frame specifications 
		f.setTitle("LD");//Adds title header that appears at the top of the page
		f.setSize(1280,1020);//setting the default size of our window to 1280 (H) pixels by 1020 (V)
		f.setResizable(false);//Makes it impossible to resize the window, for styling purposes and uniformity of the program in its current state
		f.setVisible(true);//making the window visible to the user

		//Action listeners

		//Button Action Listeners 

		//File Drop Down Menu Action Listeners
		//Expands the drop down menu by deploying the grid panel containing all of the secondary buttons after the first button is consequently pressed 
		File.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(View_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					View_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(true); press_amount += 1; 
				}
				else if(press_amount == 0){
					File_Panel_Expansion.setVisible(true); press_amount += 1; 
					Quick_Tools_Panel_Expansion.setVisible(false);
					View_Panel_Expansion.setVisible(false);
				}else{
					File_Panel_Expansion.setVisible(false); press_amount = 0; 
				}
			}

		});
		//Creates a new Let's Draw session/window/object, and prompts the user to save their work as a precaution
		File_New.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(pane,"Save any changes?","Advisory",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					f.setVisible(false);
					LetsDraw LD = new LetsDraw();
				} else if (response == JOptionPane.YES_OPTION) {
					File_Save_As.doClick();
					f.setVisible(false);
					LetsDraw LD = new LetsDraw();
				} else if (response == JOptionPane.CLOSED_OPTION) {

				}                   
			}

		});
		//Writes the Buffered Image to the user's desktop using the appropriate name chosen by them as well
		File_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {                    
				if(View_Panel_Expansion.isVisible() == true || File_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					View_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
					press_amount = 0;
				}                  
				if(save_name != null){
					try{
						ImageIO.write(im, "PNG", new File(System.getProperty("user.home") + "\\Desktop\\" + save_name + "." + "png"));
						Page_Readout_Console.setText("Image Saved to:" + (System.getProperty("user.home") + "\\Desktop\\" + save_name + "." + "png"));
					} catch (Exception f) {
						f.printStackTrace();
						Page_Readout_Console.setText("Error Image not saved" + f.getMessage());
					}
				}
				if(save_name == null)
					File_Save_As.doClick();
			}

		});

		//Resave or save under a different name 
		File_Save_As.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save_name = null;
				//Handles cancel button exception, if the returned value is still null then nothing happens aka pressing cancel or exiting the window
				save_name = JOptionPane.showInputDialog("Save As:");
				//Handles first exception where the string length is 0
				if(save_name.isEmpty())
					Page_Readout_Console.setText("Error: Image not saved \n File name field cannot be empty");
				//isBlank used to detect white space characters aka space bar/ tab etc
				//isEmpty used to detect if the string length is 0
				if(View_Panel_Expansion.isVisible() == true || File_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					View_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
					press_amount = 0;
				}
				while(save_name.isEmpty() || save_name.trim().length() == 0){                    
					try{
						save_name = JOptionPane.showInputDialog("Save As:");
						if(save_name.isEmpty() || save_name.trim().length() == 0)
							throw new RuntimeException();
					} catch (RuntimeException  f) {
						f.printStackTrace();
						Page_Readout_Console.setText("Error: Image not saved \n File name field cannot be empty");
					}
				}
				try{
					ImageIO.write(im, "PNG", new File(System.getProperty("user.home") + "\\Desktop\\" + save_name + "." + "png"));
					Page_Readout_Console.setText("Image Saved to:" + (System.getProperty("user.home") + "\\Desktop\\" + save_name + "." + "png"));
				} catch (Exception f) {
					f.printStackTrace();
					Page_Readout_Console.setText("Error: Image not saved \n File name field can not be empty");
				}                  
			}

		});

		//Exits the program
		File_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		//End of File Drop Menu Action Listeners

		//View Drop Menu Action Listeners 
		View.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(File_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
					View_Panel_Expansion.setVisible(true); press_amount += 1; 
				}
				else if(press_amount == 0){
					View_Panel_Expansion.setVisible(true); press_amount += 1; 
					Quick_Tools_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
				}else{
					View_Panel_Expansion.setVisible(false); press_amount = 0; 
				}
			}

		});

		/**Work in Progress
        //Max zoom in is 1000%, zoom in moves the zoom by 100% for each button press
         View_Zoom_in.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){                    
                    if(canvas_size_percentage < 1000 && canvas_size_percentage >= 100){
                        canvas_size_percentage += 100;
                        String canvas_size_string = Integer.toString(canvas_size_percentage);
                        canvas_size.setText("  " + canvas_size_string + "%");
                        g2.scale(2.0, 2.0);
                    }
                    if(canvas_size_percentage <= 100 && canvas_size_percentage >= 25){
                        canvas_size_percentage = canvas_size_percentage*2;
                        String canvas_size_string = Integer.toString(canvas_size_percentage);
                        canvas_size.setText("  " + canvas_size_string + "%");
                        g2.scale(2.0, 2.0);           
                    }
                }
            });
        View_Zoom_out.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){                    
                    if(canvas_size_percentage == 200){
                        canvas_size_percentage = canvas_size_percentage/2;
                        String canvas_size_string = Integer.toString(canvas_size_percentage);
                        canvas_size.setText("  " + canvas_size_string + "%");
                        Canvas_width = Canvas_width/2;
                        Canvas_height = Canvas_height/2;
                        Canvas.setBounds(0,0,Canvas_width,Canvas_height);
                    }
                    else if(canvas_size_percentage > 100 ){
                        canvas_size_percentage -= 100;
                        String canvas_size_string = Integer.toString(canvas_size_percentage);
                        canvas_size.setText("  " + canvas_size_string + "%");
                        Canvas_width = Canvas_width/2;
                        Canvas_height = Canvas_height/2;
                        Canvas.setBounds(0,0,Canvas_width,Canvas_height);
                    }               
                    else if(canvas_size_percentage <= 100 && canvas_size_percentage > 25){
                        canvas_size_percentage = canvas_size_percentage/2;
                        String canvas_size_string = Integer.toString(canvas_size_percentage);
                        canvas_size.setText("  " + canvas_size_string + "%");
                        Canvas_width = Canvas_width/2;
                        Canvas_height = Canvas_height/2;
                        Canvas.setBounds(0,0,Canvas_width,Canvas_height);                     
                    }
                }

            });

        View_Zoom_100.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){                    
                    canvas_size_percentage = 100;
                    String canvas_size_string = Integer.toString(canvas_size_percentage);
                    canvas_size.setText("  " + canvas_size_string + "%");

                }
            });
        View_Zoom_200.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){                    
                    canvas_size_percentage = 200;
                    String canvas_size_string = Integer.toString(canvas_size_percentage);
                    canvas_size.setText("  " + canvas_size_string + "%");

                }
            });

        End of View Drop Menu Action Listeners 
		 **/


		//Quick Tools Drop Menu Action Listeners 
		Quick_Tools.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(View_Panel_Expansion.isVisible() == true || File_Panel_Expansion.isVisible() == true ){
					View_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(true); press_amount += 1; 
				}
				else if(press_amount == 0){
					Quick_Tools_Panel_Expansion.setVisible(true); press_amount += 1; 
					File_Panel_Expansion.setVisible(false);
					View_Panel_Expansion.setVisible(false);
				}else{
					Quick_Tools_Panel_Expansion.setVisible(false); press_amount = 0; 
				}
			}

		});
		Quick_Tools_Eraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pen_Tool = false;
				Marker_Tool = false;
				Eraser_Tool = true;
			}

		});
		Quick_Tools_Pen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pen_Tool = true;
				Marker_Tool = false;
				Eraser_Tool = false;
			}

		});

		Quick_Tools_Marker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pen_Tool = false;
				Marker_Tool = true;
				Eraser_Tool = false;
			}

		});

		Quick_Tools_Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graphics g = Canvas1.getGraphics(); 
				g.setColor(white);
				g.fillRect(0, 0, Canvas1.getWidth(), Canvas1.getHeight());

				h.setColor(white);
				h.fillRect(0, 0, im.getWidth(), im.getHeight());

				MSP3_5.setText("");
			}

		});
		//end of Quick Tools Drop Menu Action Listeners 

		//Color option Button Action Listeners, provides the switch over for all of the color options 
		Red_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.red;
			}
		});      

		Green_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {   
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.green;
			}
		});      

		Blue_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.blue;
			}
		});      

		Yellow_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {    
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.yellow;
			}
		});   

		Orange_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {     
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.orange;
			}
		});      

		cyan_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {     
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.cyan;
			}
		});      

		pink_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {   
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.pink;
			}
		});      

		gray_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.gray;
			}
		});    

		black_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				Eraser_Tool = false;
				Pen_Tool = true;
				pen_color = Color.black;
			}
		});      

		eraser_color_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {        	
				Quick_Tools_Eraser.doClick();
			}
		});  
		//End of Color option Button Action Listeners

		//Help Button Action Listener, provides a short and decent tutorial about this current piece of software 
		Help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(View_Panel_Expansion.isVisible() == true || File_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					View_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
				}
				press_amount = 0;

				JOptionPane.showMessageDialog(pane,"Welcome to Let's Draw's General Manual");
				JOptionPane.showMessageDialog(pane, "This program is still in its infancy \n but a lot of kinks have been polished out");
				JOptionPane.showMessageDialog(pane,"Right now there are 3 basic 'pen' tools you can utilize: \n \n"
						+ "Marker - a line drawing tool made up of successive ovals \n"
						+ "Pen - the default option, this is your base tool, and your smallest \n"
						+ "Eraser - another oval tool, this allows you to erase, pretty self explanatory \n \n"
						+ "The line tools work by drawing a vector aka line between two points determined by the motion \n"
						+ "of your mouse. \n Whenever you click your mouse a press event is triggered and this starts a new line. \n \n"
						+ "As you drag your mouse around the page while pressing down, hundreds of little 2D coordinates are fed back \n"
						+ "to the 'paintcomponent' constructor, which is the method responsible for \n drawing these amazing lines.");
				JOptionPane.showMessageDialog(pane,"There are a total of 9 possible colors to choose from \n and even more will be available in "
						+ "the future. \n \n You can adjust the width/ radius of your brush with the sliders located on the right panel.");
				JOptionPane.showMessageDialog(pane,"An Event log is provided at the lower right hand corner of the program to give you a bit of \n"
						+ " insight as to what exactly is going on with the back-end of this program in terms of data streaming. ");
				JOptionPane.showMessageDialog(pane,"At the top of the program you can see a console readout, this is where you check to see the \n"
						+ "file path of your saved image, oh, did I forget to mention? \n \n you can save your work at any time by pressing file and then "
						+ "clicking save or save as. \n \n Proper importation functionality will be added later on.");
				JOptionPane.showMessageDialog(pane,"At the top you can also find the appropriate drop down menus and the help button you just \n"
						+ "clicked! \n \n The view tab is currently a work in progress, but when it is finished you'll be able to zoom in and out \n"
						+ "and get a better look at the finer details of your masterpiece. \n \n"
						+ "Quick tools is a short-cut to the basic pen tools provided to you, \n and the clear button which clears both the canvas"
						+ "and the event log so that you can have a brand new start");
				JOptionPane.showMessageDialog(pane,"Thanks for checking out Let's Draw, I hope to improve upon this design in the near future \n"
						+ "much more than imaginable. \n Farewell! \n"
						+ "-JJC");
			}

		});
		//end of Help Button Action Listener 

		//Canvas Listeners for mouse activity, this is how you draw :)
		//The container within which you wish to draw has to be specified directly 
		Canvas1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Graphics g = Canvas1.getGraphics(); 
				if(View_Panel_Expansion.isVisible() == true || File_Panel_Expansion.isVisible() == true || Quick_Tools_Panel_Expansion.isVisible() == true){
					View_Panel_Expansion.setVisible(false);
					File_Panel_Expansion.setVisible(false);
					Quick_Tools_Panel_Expansion.setVisible(false);
					press_amount = 0;
				}
				arr[index] = new Point(e.getX(), e.getY());
				index++;
				System.out.println(index);
				MSP3_5.append("////////Mouse Press Event///////// \n");
				paintComponent(g); //We draw to the actual canvas first to avoid any latency issues
				paintComponent(h); //and then we draw to the buffered image's graphics second because we can't see it and therefore don't have to worry about its current performance/appearance 
			}
		});
		Canvas1.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseDragged(MouseEvent e) {
				Graphics g = Canvas1.getGraphics(); 
				arr[index] = new Point(e.getX(), e.getY());
				index++;
				MSP3_5.append("Mouse Drag Event Count: \n" + index + "\n");//Stores info in the Event log 
				paintComponent(g);
				paintComponent(h);
			}

		});
		Canvas1.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				arr = new Point[1000000]; 
				index = 0; //Resets the index, thus preventing two points from mistakenly connecting to one another whilst drawing 

				MSP3_5.append("----*Mouse Release Event*---- \n");
			}
		});
		//End of canvas mouse listeners 


		//Frame event listeners, general precautions for the user
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				int response = JOptionPane.showConfirmDialog(pane,"Save any changes?","Advisory",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {

				} else if (response == JOptionPane.YES_OPTION) {
					File_Save_As.doClick();
				} else if (response == JOptionPane.CLOSED_OPTION) {

				}   
			}
		});
		//end of frame event listeners

		//Brush slider state change listener, gets the values the user selects by sliding the slider and sets that to the brush size instance variable
		brush_size_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				brush_size_unit = ((JSlider)e.getSource()).getValue();
			}
		});
		//end

		//Eraser slider state change listener
		eraser_size_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				eraser_size_unit = ((JSlider)e.getSource()).getValue();
			}
		});
		//end

	}

	//paintComponent with multiple if statements for the specific cases at hand depending on which tool the user is currently utilizing 
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		if(Pen_Tool == true && Marker_Tool == false && Eraser_Tool == false) {
			g.setColor(pen_color);
			for (int i = 0; i < index - 1; i++) {
				((Graphics2D) g).setStroke(new BasicStroke(brush_size_unit));
				g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
			}
		}

		if(Marker_Tool == true && Pen_Tool == false && Eraser_Tool == false) {
			g.setColor(pen_color);
			for (int i = 0; i < index - 1; i++)
				g.fillOval((arr[i+1].x), (arr[i+1].y), brush_size_unit , brush_size_unit);
		}

		if(Marker_Tool == false && Pen_Tool == false && Eraser_Tool == true) {
			g.setColor(Color.white);
			for (int i = 0; i < index - 1; i++)
				g.fillOval((arr[i+1].x), (arr[i+1].y), eraser_size_unit , eraser_size_unit);
		}
	}

	//paint method
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void close_all_windows(){
		System.exit(0);
	}

	//Main method
	public static void main(String[] args){
		LetsDraw LD = new LetsDraw();
	}
}