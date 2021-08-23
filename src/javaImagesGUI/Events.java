package javaImagesGUI;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

/**
 *
 * @author Luis
 */
public class Events {
	public static void changeLabel(JLabel label, String message) {
		label.setText(message);
	}

	public static void changeTextField(JFormattedTextField textField, String message) {
		textField.setText(message);
	}
}
