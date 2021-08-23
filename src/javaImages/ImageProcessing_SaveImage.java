package javaImages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Luis Marcos
 */
public class ImageProcessing_SaveImage extends ImageProcessing {

	private JFileChooser selectorImage;
	private ImageProcessing.imageFormat selectedExtensionImage;
	private String stringSelectedExtension;

	protected boolean openJFileChooser() {
		this.selectorImage = new JFileChooser();
		this.selectorImage.setDialogTitle("Save an image");
		selectImageFilter();
		int flag = selectorImage.showSaveDialog(null);
		return flag == JFileChooser.APPROVE_OPTION;
	}

	protected void selectImageFilter() {
		FileNameExtensionFilter imageFilter;
		imageFilter = new FileNameExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jfif)", "jpg", "jpeg", "jpe", "jfif");
		this.stringSelectedExtension = "jpg";
		switch (this.selectedExtensionImage) {
		case bmp:
			imageFilter = new FileNameExtensionFilter("Bitmap (*.bmp, *.dib)", "bmp", "dib");
			this.stringSelectedExtension = "bmp";
			break;
		case gif:
			imageFilter = new FileNameExtensionFilter("GIF (*.gif)", "gif");
			this.stringSelectedExtension = "gif";
			break;
		case jpg:
			imageFilter = new FileNameExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jfif)", "jpg", "jpeg", "jpe", "jfif");
			this.stringSelectedExtension = "jpg";
			break;
		case png:
			imageFilter = new FileNameExtensionFilter("PNG (*.png)", "png");
			this.stringSelectedExtension = "png";
			break;
		}
		this.selectorImage.setFileFilter(imageFilter);
	}

	protected File checkExtension(File imagePath) {
		File fileTemp = imagePath;
		if (!imagePath.getAbsoluteFile().toString().endsWith("." + this.stringSelectedExtension)) {
			fileTemp = new File(imagePath.getAbsoluteFile().toString() + "." + this.stringSelectedExtension);
		}
		return fileTemp;
	}

	/**
	 * shows the JFileChooser to save an image
	 * 
	 * @param saveImage
	 *            BufferedImage is going to be saved
	 * @param saveExtension
	 *            extension to which the image is saved
	 * @return returns true if successfully saved. Returns false if the image
	 *         wasn´t saved
	 * @see javaImages.ImageProcessing.imageFormat
	 */
	public Boolean saveFile(BufferedImage saveImage, ImageProcessing.imageFormat saveExtension) {
		Boolean flag = false;
		this.selectedExtensionImage = saveExtension;
		if (this.openJFileChooser() == true) {
			File imageFile = checkExtension(this.selectorImage.getSelectedFile());
			try {
				ImageIO.write(saveImage, this.stringSelectedExtension, imageFile);
				super.updateActivityLog("Image saved in directory: " + imageFile.toString());
				flag = true;
			} catch (IOException e) {
				super.updateActivityLog("Error: Failed when saving image. Exception generated:\n" + e.toString());
			}
		}
		return flag;
	}
}
