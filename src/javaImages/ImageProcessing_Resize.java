package javaImages;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Esta clase contiene los métodos para redimensionar una imagen.
 * 
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021 * @author Luis
 */
public class ImageProcessing_Resize extends ImageProcessing {
	// Declaramos las propiedades
	private BufferedImage imageTemp; // Almacena la imagen

	public enum ScaleType {
		SCALE_AREA_AVERAGING, SCALE_DEFAULT, SCALE_FAST, SCALE_REPLICATE, SCALE_SMOOTH
	} // Enumeración de los algoritmos para redimensionar la imagen

	private ImageProcessing_TransformFormatImages ObjTransform; // Almacena un
																// objeto de la
																// clase
																// ImageProcessing

	/**
	 * Método que calcula un porcentaje.
	 * 
	 * @param currentSize
	 *            Tamaño de la imagen
	 * @param percentSize
	 *            Porcentaje de tamaño
	 * @return Tamaño actualizado
	 */
	private int percentCalculate(int currentSize, int percentSize) {
		double sizeReturn = (currentSize * ((double) percentSize / 100));
		return (int) sizeReturn;
	}

	/**
	 * Método que devuelve la escala de redimensión.
	 * 
	 * @param hints
	 *            Escala
	 * @return Devuelve la escala
	 */
	private int selectScale(ScaleType hints) {
		int valueHints = 1;
		switch (hints) {
		case SCALE_AREA_AVERAGING:
			valueHints = 16;
			break;
		case SCALE_DEFAULT:
			valueHints = 1;
			break;
		case SCALE_FAST:
			valueHints = 2;
			break;
		case SCALE_REPLICATE:
			valueHints = 8;
			break;
		case SCALE_SMOOTH:
			valueHints = 4;
			break;
		}
		return valueHints;
	}

	/**
	 * Resize Bufferedimage to selected values.
	 * 
	 * @param image
	 *            BufferedImage is going to be resize
	 * @param width
	 *            the width to which to scale the image. Must be greater than 0.
	 * @param height
	 *            the height to which to scale the image. Must be greater than
	 *            0.
	 * @param hints
	 *            enumeration to indicate the type of algorithm to use for image
	 *            resampling.
	 * @return a scaled version of the Bufferedimage.
	 * @see javaImages.ImageProcessing_Resize.ScaleType
	 */
	public BufferedImage resizeImage(BufferedImage image, int width, int height, ScaleType hints) {
		this.imageTemp = super.cloneBufferedImage(image);
		try {
			if (width > 0 && height > 0) {
				ObjTransform = new ImageProcessing_TransformFormatImages();
				Image imgTemp = ObjTransform.bufferedImageToImage(image);
				imgTemp = imgTemp.getScaledInstance(width, height, selectScale(hints));
				this.imageTemp = ObjTransform.imageToBufferedImage(imgTemp);
				super.updateImage("Image Transformation: resize image (" + width + "," + height
						+ "). Image-scaling algorithm: " + hints.toString(), this.imageTemp);
			} else {
				super.updateActivityLog(
						"Resize error: values ​​can not be <= 0. Width: " + width + ", height: " + height);
			}
		} catch (Exception e) {
			super.updateActivityLog("Ups .. something has happened:( .The size may be too large. Exception generated:\n"
					+ e.toString());
		}

		return this.imageTemp;
	}

	/**
	 * BufferedImage resize the selected percents.
	 * 
	 * @param image
	 *            BufferedImage is going to be resize
	 * @param widthPercent
	 *            is the percentage of the width of the new image.Must be
	 *            greater than 0. the value 100 don´t alter the size
	 * @param heightPercent
	 *            is the percentage of the height of the new image. Must be
	 *            greater than 0. the value 100 don´t alter the size
	 * @param hints
	 *            enumeration to indicate the type of algorithm to use for image
	 *            resampling.
	 * @return a scaled version of the Bufferedimage.
	 * @see javaImages.ImageProcessing_Resize.ScaleType
	 */
	public BufferedImage resizeImagePercent(BufferedImage image, int widthPercent, int heightPercent, ScaleType hints) {
		this.imageTemp = super.cloneBufferedImage(image);
		try {
			if (widthPercent > 0 && heightPercent > 0) {
				ObjTransform = new ImageProcessing_TransformFormatImages();
				Image imgTemp = ObjTransform.bufferedImageToImage(image);
				imgTemp = imgTemp.getScaledInstance(percentCalculate(imgTemp.getWidth(null), widthPercent),
						percentCalculate(imgTemp.getHeight(null), heightPercent), selectScale(hints));
				this.imageTemp = ObjTransform.imageToBufferedImage(imgTemp);
				super.updateImage("Image Transformation: resize image (" + this.imageTemp.getWidth() + ","
						+ this.imageTemp.getHeight() + ")", this.imageTemp);
			} else {
				super.updateActivityLog("Resize error: values ​​can not be <= 0. Width: " + widthPercent
						+ ", height: " + heightPercent);
			}
		} catch (Exception e) {
			super.updateActivityLog("Ups .. something has happened:( .The size may be too large. Exception generated:\n"
					+ e.toString());
		}
		return this.imageTemp;
	}

}
