package snake;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;

public class Snakegame_1 extends JFrame {
	private static final long serialVersionUID = 400028185L; //Enables objects of this class type to be turned into Byte streams so that copies of that class can be reverted back from said byte stream to the regular object via Java.io
	private JFrame Main_window_1; //Our JFrame (main window frame that will enable us to render all elements inside of it)
	JOptionPane pane = new JOptionPane();//creating a new pane
	public Random randomnum_gen = new Random();
	
	public Snakegame_1() {
		Main_window_1 = new JFrame();
		
		//New border styles
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border whiteline = BorderFactory.createLineBorder(Color.white);
		Border grayline = BorderFactory.createLineBorder(Color.gray);

		//Fonts 
		Font f1 = new Font("Helvetica",Font.PLAIN, 10);
		Font f2 = new Font("Helvetica",Font.PLAIN, 15);
		
	    // Initializing the snake game and then adding the game component to the frame
		Drawing_Board Viewport = new Drawing_Board();
		Viewport.setSize(1520, 1080);
		
		//Page Header 1 View Button panel
		JPanel Side_Panel_Parent_Container = new JPanel();
		Side_Panel_Parent_Container.setBounds(1520,0,400,1080); 
		Side_Panel_Parent_Container.setSize(400, 1080);
		Side_Panel_Parent_Container.setBackground(Color.white);
		Side_Panel_Parent_Container.setBorder(whiteline);
		Side_Panel_Parent_Container.setVisible(true);
		Viewport.add(Side_Panel_Parent_Container);
	
		//our frame specifications 
		Main_window_1.setTitle("Snake Game");//Adds title header that appears at the top of the page
		Viewport.setFocusable(true);
		Viewport.addKeyListener(Viewport);
		Main_window_1.add(Viewport);
		Main_window_1.pack();
		Main_window_1.setSize(1920, 1080);
		Main_window_1.setResizable(false);//Makes it possible to resize the window
		Main_window_1.setVisible(true);//making the window visible to the user
		

	}
	
	class Drawing_Board extends JPanel implements KeyListener {

		private int position_X = 500;
		private int position_Y = 500;
		private int object_width = 20;
		private int object_height = 20;
		private int Snake_body_length;
		private int max_snake_length = 20;
		private ArrayList<Snake_element> Snake_Body;
		private int [] Snake_Body_PastCoordinate_X;
		private int [] Snake_Body_PastCoordinate_Y;
		private Snake_food_element Apple;
		private int horizontal_position = random_position_horizontalpos();
		private int vertical_position = random_position_verticalpos();
		private int Body_Segment_Count = 0;
		private int old_snake_position_X;
		private int old_snake_position_Y;
		private Snakegame_1 Main_session;
		
		//Generates a new random coordinate set for the snake food to spawn in at
		public int random_position_horizontalpos() {
			int random_num = randomnum_gen.nextInt(1720);
			
			while(random_num % 20 != 0) {
			random_num = randomnum_gen.nextInt(1720);
			}

			return random_num;
		}
		public int random_position_verticalpos() {
			int random_num = randomnum_gen.nextInt(820);

			while(random_num % 20 != 0) {
				random_num = randomnum_gen.nextInt(820);
				}
			
			return random_num;
		}
		//end 

		public Drawing_Board() {

			setBorder(BorderFactory.createLineBorder(Color.black));
			setBackground(Color.black);
			
			Snake_Body_PastCoordinate_X = new int [20];
			Snake_Body_PastCoordinate_Y = new int [20];
			
			Apple = new Snake_food_element(horizontal_position,vertical_position,object_width,object_height,"Apple");

			Snake_Body = new ArrayList<Snake_element>();
			SnakeMaker(new Snake_element(position_X,position_Y,object_width,object_height,"Head"));	
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();  // Keyboard code for the pressed key.
			 
			if(key == KeyEvent.VK_RIGHT)
				moveSquare(20,0);
			if(key == KeyEvent.VK_LEFT)
				moveSquare(-20,0);
			if(key == KeyEvent.VK_UP)
				moveSquare(0,-20);
			if(key == KeyEvent.VK_DOWN)
				moveSquare(0,20);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		private void moveSquare(int x, int y) {
	        position_X = position_X + x;
            position_Y = position_Y + y;

            if(position_X >= (getWidth() - 1) + 40) //Adding 40 more pixels as buffer to make the transition look more fluid
            	position_X = 0;
            if(position_X <= -40)
            	position_X = (getWidth() - 1) + 40;
            if(position_Y >= (getHeight() - 1) + 40)
            	position_Y = 0;
            if(position_Y <= -40)
            	position_Y = (getHeight() - 1) + 40;

            if(position_X == Apple.get_X() && position_Y == Apple.get_Y()) {
            	horizontal_position = random_position_horizontalpos();
            	vertical_position = random_position_verticalpos();
            	
            	Apple.set_X(horizontal_position);
    			Apple.set_Y(vertical_position);
    			
            	Body_Segment_Count ++;
            	
            	if(Body_Segment_Count < 19)
            	SnakeMaker(new Snake_element(position_X,position_Y,object_width,object_height,"Body Segment " + Body_Segment_Count));
            	if(Body_Segment_Count == 19)
            		SnakeMaker(new Snake_element(position_X,position_Y,object_width,object_height,"Tail"));

            	repaint(horizontal_position,vertical_position,object_width,object_height);
            }

            if(Body_Segment_Count > 0){
            for(int i = 1; i < Snake_Body.size(); i++){
            	Snake_Body_PastCoordinate_X[i] = Snake_Body.get(i).get_X();
            	Snake_Body_PastCoordinate_Y[i] = Snake_Body.get(i).get_Y();

            	if(Snake_Body_PastCoordinate_X[i] == Snake_Body.get(0).get_X() && Snake_Body_PastCoordinate_Y[i] == Snake_Body.get(0).get_Y()) {
            		Main_window_1.setVisible(false);
            		Main_session = new Snakegame_1 ();
            	}

            }    
            }
            
            
            repaint(position_X,position_Y,object_width,object_height);
            repaint();

		} 
		
		public ArrayList<Snake_element> SnakeMaker(Snake_element New_snake_object) {
			Snake_Body.add(New_snake_object);
			return Snake_Body;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.RED);
			g.fillRect(Apple.get_X(), Apple.get_Y(), Apple.get_width(), Apple.get_height());
			g.setColor(Color.BLACK);
			g.drawRect(Apple.get_X(), Apple.get_Y(), Apple.get_width(), Apple.get_height());	
			
			for(int i = 0; i < Snake_Body.size(); i++){
				Snake_Body_PastCoordinate_X[i] = Snake_Body.get(i).get_X();
				Snake_Body_PastCoordinate_Y[i] = Snake_Body.get(i).get_Y();
			}
			
			for(int i = 0; i < Snake_Body.size(); i++) {
				System.out.println("Before reassignment Snake_Body_PastCoordinates: " + Snake_Body_PastCoordinate_X[i]);
				System.out.println("Before reassignment Snake_Body: " + Snake_Body.get(i).get_X());
			}
			
			Snake_Body.get(0).set_X(position_X);
			Snake_Body.get(0).set_Y(position_Y);

			for(int i = 0; i < Snake_Body.size(); i++) {
				System.out.println("After reassignment Snake_Body_PastCoordinates: " + Snake_Body_PastCoordinate_X[i]);
				System.out.println("After reassignment Snake_Body: " + Snake_Body.get(i).get_X());
			}
			
			g.setColor(Color.BLUE);
			g.fillRect(Snake_Body.get(0).get_X(), Snake_Body.get(0).get_Y(), Snake_Body.get(0).get_width(), Snake_Body.get(0).get_height());
			g.setColor(Color.BLACK);
			g.drawRect(Snake_Body.get(0).get_X(), Snake_Body.get(0).get_Y(), Snake_Body.get(0).get_width(), Snake_Body.get(0).get_height());
			
			for(int i = 1; i < Snake_Body.size(); i++) {
				Snake_Body.get(i).set_X(Snake_Body_PastCoordinate_X[i-1]);
				Snake_Body.get(i).set_Y(Snake_Body_PastCoordinate_Y[i-1]);
				
				g.setColor(Color.BLUE);
				g.fillRect(Snake_Body.get(i).get_X(), Snake_Body.get(i).get_Y(), Snake_Body.get(i).get_width(), Snake_Body.get(i).get_height());
				g.setColor(Color.BLACK);
				g.drawRect(Snake_Body.get(i).get_X(), Snake_Body.get(i).get_Y(),Snake_Body.get(i).get_width(), Snake_Body.get(i).get_height());
			}


		}
	}

	public static void main(String[] args) {
		Snakegame_1 Main_session = new Snakegame_1 ();
	}
}
