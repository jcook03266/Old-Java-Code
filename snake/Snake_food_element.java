package snake;

public class Snake_food_element extends Snake_element {
	private int horiz_position;
	private int vert_position;
	private int element_width;
	private int element_height;
	private String name;

	public Snake_food_element(int horiz_position, int vert_position, int element_width, int element_height, String name) {
		super(horiz_position, vert_position, element_width, element_height, name);
		this.horiz_position = horiz_position;
		this.vert_position = vert_position;
		this.element_width = element_width;
		this.element_height = element_height;
		this.name = name;
	}

	public String get_name() {
		return this.name;
	}
	public int get_X() {
		return this.horiz_position;
	}
	public int get_Y() {
		return this.vert_position;
	}
	public int get_height() {
		return this.element_height;
	}
	public int get_width() {
		return this.element_width;
	}
	public void set_name(String name) {
		this.name = name;
	}
	public void set_X(int horiz_position) {
		this.horiz_position = horiz_position;
	}
	public void set_Y(int vert_position) {
		this.vert_position = vert_position;
	}
	public void set_height(int element_height) {
		this.element_height = element_height;
	}
	public void set_width(int element_width) {
		this.element_width = element_width;
	}

	public static void main(String[] args) {
	}

}
