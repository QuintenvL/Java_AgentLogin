package nu.educom.wiki.quinten_mi6.views;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

enum FrameVariables {
	;
	static String MAIN_FONT_STYLE = "arial";
	static int NUMBER_OF_COLUMNS = 15;
	static int WIDTH = 550;
	static int HEIGHT = 350;
	static Font MAIN_FONT = new Font(MAIN_FONT_STYLE, Font.PLAIN, 28);
	static Font NORMAL_FONT = new Font(MAIN_FONT_STYLE, Font.PLAIN, 14);
	static Font ITALIC_NORMAL_FONT = new Font(MAIN_FONT_STYLE, Font.ITALIC, 14);
	static Font BOLD_MAIN_FONT = new Font(MAIN_FONT_STYLE, Font.BOLD, 28);
	static Font MAIN_FIELD_FONT = new Font(MAIN_FONT_STYLE, Font.PLAIN, 20);
	static Font MAIN_BUTTON_FONT = new Font(MAIN_FONT_STYLE, Font.PLAIN, 15);
	static Color ERROR_COLOR = Color.RED;
	static Color INFO_COLOR = Color.BLUE;
	static JButton EXIT_BUTTON = new JButton("Exit");

}
