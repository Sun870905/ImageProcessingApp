package javaImages;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Esta clase contiene los métodos para aplicar los filtros básicos.
 * 
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021
 */
public class ImageProcessing_BasicFilters extends ImageProcessing {
	// Declaramos las propiedades
	private BufferedImage imageTemp; // Almacena la imágen a modificar
	private int colorSRGB; // Almacena un color SRGB

	public enum filtersAvailable {
		red, green, blue
	} // Enumera todos los filtros posibles

	public enum RGBTransformAvailable {
		GBR, GRB, BRG, BGR, RBG
	} // Enumera todos los cambios posibles

	public enum invertColorsAvailable {
		RGB, red, green, blue
	} // Enumera todos los canales posibles

	/**
	 * Método que calcula la media de un color.
	 * 
	 * @param color
	 *            Color a calcular la media
	 * @return Color medio
	 */
	private int calculateAverage(Color color) {
		// Declaramos los atributos
		int averageColor; // Almacena la media del color

		// Calculamos la media
		averageColor = (int) ((color.getRed() + color.getGreen() + color.getBlue()) / 3);

		return averageColor;
	}

	/**
	 * Método que comprueba el umbral de un color.
	 * 
	 * @param color
	 *            Color que comprobar
	 * @param threshold
	 *            Umbral
	 * @return Color umbral.
	 */
	private int checkthreshold(Color color, int threshold) {
		if (this.calculateAverage(color) >= threshold) {
			this.colorSRGB = super.colorRGBtoSRGB(new Color(255, 255, 255, color.getAlpha()));
		} else {
			this.colorSRGB = super.colorRGBtoSRGB(new Color(0, 0, 0, color.getAlpha()));
		}
		return this.colorSRGB;
	}

	/**
	 * Método que aplica un filtro a la imágen.
	 * 
	 * @param image
	 *            Imagen a la que aplicar el filtro
	 * @param colorFilter
	 *            Filtro que aplicar
	 * @return Imagen con el filtro aplicado
	 */
	private BufferedImage selectFilter(BufferedImage image, filtersAvailable colorFilter) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena un color

		// Calculamos la nueva imagen con el filtro aplicado
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				imageTemp.setRGB(i, j, this.selectFilterTransform(colorFilter, auxColor));
			}
		}
		super.updateImage("Transformación: Filtro " + colorFilter.toString(), imageTemp);

		return imageTemp;
	}

	/**
	 * Método que selecciona el filtro
	 * 
	 * @param filter
	 *            Filtro a seleccionar
	 * @param color
	 *            Color con el filtro
	 * @return Color filtrado
	 */
	private int selectFilterTransform(filtersAvailable filter, Color color) {
		// Declaramos los atributos
		int colorReturn = 0; // Almacena el color filtrado
		int averageColor = this.calculateAverage(color); // Almacena la media
															// del color

		// Calculamos el color filtrado
		switch (filter) {
		case red:
			colorReturn = super.colorRGBtoSRGB(new Color(averageColor, 0, 0, color.getAlpha()));
			break;
		case green:
			colorReturn = super.colorRGBtoSRGB(new Color(0, averageColor, 0, color.getAlpha()));
			break;
		case blue:
			colorReturn = super.colorRGBtoSRGB(new Color(0, 0, averageColor, color.getAlpha()));
			break;

		}
		return colorReturn;
	}

	/**
	 * Método que convierte el color RGB a otro tipo.
	 * 
	 * @param image
	 *            Imagen a convertir
	 * @param chanel
	 *            Canal a convertir
	 * @return Imagen convertida
	 */
	private BufferedImage RGB_to(BufferedImage image, RGBTransformAvailable chanel) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Calculamos el color convertido
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				this.colorSRGB = this.selectRGBTransform(chanel, auxColor);
				imageTemp.setRGB(i, j, colorSRGB);
			}
		}
		super.updateImage("Transformación: RGB a " + chanel.toString(), imageTemp);

		return imageTemp;
	}

	/**
	 * Método que selecciona el canal a transformar.
	 * 
	 * @param chanel
	 *            Canal a seleccionar
	 * @param color
	 *            Color a convertir
	 * @return Color convertido
	 */
	private int selectRGBTransform(RGBTransformAvailable chanel, Color color) {
		// Declaramos los atributos
		int colorReturn = 0; // Almacena el color transformado

		// Seleccionamos el tipo de conversión y convertimos el color
		switch (chanel) {
		case GBR:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getGreen(), color.getBlue(), color.getRed(), color.getAlpha()));
			break;
		case GRB:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getGreen(), color.getRed(), color.getBlue(), color.getAlpha()));
			break;
		case BRG:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getBlue(), color.getRed(), color.getGreen(), color.getAlpha()));
			break;
		case BGR:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha()));
			break;
		case RBG:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha()));
			break;
		}

		return colorReturn;
	}

	/**
	 * Método que invierte los colores.
	 * 
	 * @param image
	 *            Imagen a convetir
	 * @param chanelInvert
	 *            Canal para convertir
	 * @return Imagen convertida
	 */
	private BufferedImage invertColors(BufferedImage image, invertColorsAvailable chanelInvert) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Convertimos los colores
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				this.colorSRGB = this.selectInvertColors(chanelInvert, auxColor);
				imageTemp.setRGB(i, j, this.colorSRGB);
			}
		}
		super.updateImage("Transformación: invertir colores (" + chanelInvert + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método que selecciona el canal para convertir el color.
	 * 
	 * @param chanelInvert
	 *            Canal para convertir
	 * @param color
	 *            Color a convertir
	 * @return Color convertido
	 */
	private int selectInvertColors(invertColorsAvailable chanelInvert, Color color) {
		// Declaramos los atributos
		int colorReturn = 0; // Almacena el color a devolver

		// Seleccionamos el tipo de canal y convertimos el color.
		switch (chanelInvert) {
		case RGB:
			colorReturn = super.colorRGBtoSRGB(
					new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha()));
			break;
		case red:
			colorReturn = super.colorRGBtoSRGB(
					new Color(255 - color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
			break;
		case green:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getRed(), 255 - color.getGreen(), color.getBlue(), color.getAlpha()));
			break;
		case blue:
			colorReturn = super.colorRGBtoSRGB(
					new Color(color.getRed(), color.getGreen(), 255 - color.getBlue(), color.getAlpha()));
			break;
		}

		return colorReturn;
	}

	/**
	 * Método que convierte una imágen a escala de grises.
	 * 
	 * @param image
	 *            Imágen a convertir
	 * @return Imagen convertida
	 */
	public BufferedImage grayScale(BufferedImage image) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imágen
		int averageColor; // Almacen la media del color
		Color auxColor; // Almacena el color

		// Calculamos la escala de grises y generamos la nueva imágen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				averageColor = this.calculateAverage(auxColor);
				this.colorSRGB = super.colorRGBtoSRGB(
						new Color(averageColor, averageColor, averageColor, auxColor.getAlpha()));
				imageTemp.setRGB(i, j, colorSRGB);
			}
		}
		super.updateImage("Transformación: escala de grises", imageTemp);

		return imageTemp;
	}

	/**
	 * Mñetodo que convierte una imagen a blanco y negro.
	 * 
	 * @param image
	 *            Imagen a transformar
	 * @param umbral
	 *            Umbral de transformación
	 * @return Imagen convertida
	 */
	public BufferedImage blackAndWhite(BufferedImage image, int umbral) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Calculamos el blanco y negro
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				imageTemp.setRGB(i, j, this.checkthreshold(auxColor, umbral));
			}
		}
		super.updateImage("Transformación: blanco y negro. Umbral: " + umbral, imageTemp);

		return imageTemp;
	}

	/**
	 * Método que invierte los colores de la imagen.
	 * 
	 * @param image
	 *            Imagen a invertir
	 * @param chanelInvert
	 *            Canal que invertir
	 * @return Imagen invertida
	 */
	public BufferedImage invertColorsRGB(BufferedImage image, invertColorsAvailable chanelInvert) {
		return this.invertColors(image, chanelInvert);
	}

	/**
	 * Método que aplica los filtros rojo, verde y azul.
	 * 
	 * @param image
	 *            Imagen a cnvertir
	 * @param colorFilter
	 *            Filtro para convertir
	 * @return Imagen convertida
	 */
	public BufferedImage basicFilters(BufferedImage image, filtersAvailable colorFilter) {
		return this.selectFilter(image, colorFilter);
	}

	/**
	 * Método que convierte RGB a otra especificacion
	 * 
	 * @param image
	 *            Imagne a convertir
	 * @param chanel
	 *            Canal a convertir
	 * @return Imagen convertida
	 */
	public BufferedImage RGBto(BufferedImage image, RGBTransformAvailable chanel) {
		return this.RGB_to(image, chanel);
	}
}
