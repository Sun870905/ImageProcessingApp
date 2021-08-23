package javaImages;

//Declaramos las librerias a usar
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Esta clase contiene los métodos para gestionar el historigrama.
 * 
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021
 */
public class ImageProcessing_Histogram extends ImageProcessing {
	// Declaramos las propiedades
	protected enum chanelsAvailable {
		red, green, blue, alpha, grayscale
	} // Alamcena una enumeración con los canales admitidos

	private final int[] maxValue = new int[2]; // Alamcena los valores máximos
	private final int[] minValue = new int[2]; // Almacena los valores mínimos

	/**
	 * Método que devuelve el píxel con mayor valor y la cantidad de píxels
	 * con dicho valor
	 * 
	 * @return Mayor valor de pixel y cantidad de pixeles con dicho valor
	 */
	public int[] getMaxValue() {
		return maxValue;
	}

	/**
	 * Método que devuelve el píxel con menor valor y la cantidad de píxels
	 * con dicho valor
	 * 
	 * @return Menor valor de pixel y cantidad de pixeles con dicho valor
	 */
	public int[] getMinValue() {
		return minValue;
	}

	/**
	 * Método que copia el historigrama.
	 * 
	 * @return Historigrama copiado
	 */
	private int[] copyHistogram(int[][] histogram, int row) {
		// Declaramos los atributos
		int[] histReturn = new int[256]; // Almacena el historigrama

		// Copiamos el historigrama
		System.arraycopy(histogram[row], 0, histReturn, 0, histogram[row].length);

		return histReturn;
	}

	/**
	 * Método que calcula la media de un color.
	 * 
	 * @param color
	 *            Color a calcular la media
	 * @return Media calculada
	 */
	private int calculateAverage(Color color) {
		// Declaramos los atributos
		int averageColor; // Almacena la media del color.

		// Calculamos la media
		averageColor = (int) ((color.getRed() + color.getGreen() + color.getBlue()) / 3);

		return averageColor;
	}

	/**
	 * Método que calcula el máximo y mínimo valor de un historigrama
	 * 
	 * @param histogramArray
	 *            Historigrama a analizar
	 */
	protected void calculateMaxMinValue(int[] histogramArray) {
		// Declaramos los atributos
		int maxiValue = 0; // Máximo valor
		int miniValue = 0; // Mínimo valor
		int maxiPixel = 0; // Máximo pixel
		int miniPixel = 0; // Mínimo píxel

		// Calculamos los máximos y mínimos
		for (int i = 0; i < histogramArray.length; i++) {
			if (histogramArray[i] > maxiValue) {
				maxiValue = histogramArray[i];
				maxiPixel = i;
			}
			if (histogramArray[i] < maxiValue) {
				miniValue = histogramArray[i];
				miniPixel = i;
			}
		}
		this.maxValue[0] = maxiPixel;
		this.maxValue[1] = maxiValue;
		this.minValue[0] = miniPixel;
		this.minValue[1] = miniValue;
	}

	/**
	 * Método que muestra el historigrama
	 * 
	 * @param image
	 *            Imagen para generar el historigrama
	 * @param chanel
	 *            Canales que mostrar en el historigrama
	 * @return Historigrama
	 */
	protected int[][] histogram(BufferedImage image, chanelsAvailable chanel) {
		// Declaramos los atributos
		Color auxColor; // Alamcena el color
		int histogramReturn[][] = new int[5][256]; // Almacena el historigrama

		// Creamos el historigrama a partir de la imágen
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				auxColor = new Color(image.getRGB(i, j));
				histogramReturn[0][auxColor.getRed()] += 1;
				histogramReturn[1][auxColor.getGreen()] += 1;
				histogramReturn[2][auxColor.getBlue()] += 1;
				histogramReturn[3][auxColor.getAlpha()] += 1;
				histogramReturn[4][calculateAverage(auxColor)] += 1;
			}
		}
		super.updateActivityLog("Información: Historigrama " + chanel.toString());

		return histogramReturn;
	}

	/**
	 * Método que calcula la frequencia roja de la imágen.
	 * 
	 * @param image
	 *            Imagen de la que sacar el historigrama
	 * @return Historigrama de frecuencia roja
	 */
	public int[] histogramRed(BufferedImage image) {
		// Declaramos los atributos
		int[] histRed; // Almacena los datos del historigrama

		// Calculamos el hisotrigrama rojo
		histRed = copyHistogram(this.histogram(image, chanelsAvailable.red), 0);
		calculateMaxMinValue(histRed);

		return histRed;
	}

	/**
	 * Método que calcula la frequencia verde de la imágen.
	 * 
	 * @param image
	 *            Imagen de la que sacar el historigrama
	 * @return Historigrama de frecuencia verde
	 */
	public int[] histogramGreen(BufferedImage image) {
		// Declaramos los atributos
		int[] histGreen;// Alamcena los datos del historigrama

		// Calculamos el historigrama verde
		histGreen = copyHistogram(this.histogram(image, chanelsAvailable.green), 1);
		calculateMaxMinValue(histGreen);

		return histGreen;
	}

	/**
	 * Método que calcula la frequencia azul de la imagen
	 * 
	 * @param image
	 *            Imagen de la que sacar el historigrama
	 * @return Historigrama de frecuencia azul
	 */
	public int[] histogramBlue(BufferedImage image) {
		// Declaramos los atributos
		int[] histBlue; // Almacena los datos del historigrama

		// Calculamos el historigrama
		histBlue = copyHistogram(this.histogram(image, chanelsAvailable.blue), 2);
		calculateMaxMinValue(histBlue);

		return histBlue;
	}

	/**
	 * Método que calcula la frequencia alpha de la imagen
	 * 
	 * @param image
	 *            Imagen de la que sacar el historigrama
	 * @return Historigrama de frecuencia alpha
	 */
	public int[] histogramAlpha(BufferedImage image) {
		// Declaramos los atributos
		int[] histAlpha; // Almacena los datos del historigrama

		// Calculamos el historigrama
		histAlpha = copyHistogram(this.histogram(image, chanelsAvailable.alpha), 3);
		calculateMaxMinValue(histAlpha);

		return histAlpha;
	}

	/**
	 * Método que calcula la frequencia de grises de la imagen
	 * 
	 * @param image
	 *            Imagen de la que sacar el historigrama
	 * @return Historigrama de frecuencia de grises
	 */
	public int[] histogramGrayscale(BufferedImage image) {
		// Declaramos los atributos
		int[] histGrayscale; // Almacena los datos del historigrama

		// Calculamos el historigrama
		histGrayscale = copyHistogram(this.histogram(image, chanelsAvailable.grayscale), 4);
		calculateMaxMinValue(histGrayscale);

		return histGrayscale;
	}

}
