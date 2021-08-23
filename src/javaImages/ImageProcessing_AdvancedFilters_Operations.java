package javaImages;

//Declaramos las librerias a usar
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Esta clase contiene los métodos para aplicar los efectos y operaciones a las
 * imágenes.
 * 
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021
 */
public class ImageProcessing_AdvancedFilters_Operations extends ImageProcessing {
	// Declaramos las propiedades
	private BufferedImage imageTemp; // Almacena la imágen a modificar
	private int colorSRGB; // Almacena el color SRGB
	private double mean = 0;
	private double sigma = 15;

	/**
	 * Método que calcula la media de un color.
	 * 
	 * @param color
	 *            Color al que calcular la media
	 * @return Color de media
	 */
	private int calculateAverage(Color color) {
		// Declaramos los atributos
		int averageColor;

		// Calculamos el color medio
		averageColor = (int) ((color.getRed() + color.getGreen() + color.getBlue()) / 3);

		return averageColor;
	}

	/**
	 * Método que comprueba un color.
	 * 
	 * @param color
	 *            Color a comprobar
	 * @return Color comprobado
	 */
	private int colorCheck(int color) {
		color = (color > 255) ? 255 : color;
		color = (color < 0) ? 0 : color;

		return color;
	}

	/**
	 * Calcular color suavizado.
	 * 
	 * @param color
	 *            Color a suavizar
	 * @param umbral
	 *            Nivel de umbralización
	 * @param porporcion
	 *            Nivel de proporcion
	 * @return Color suavizado
	 */
	private int smoothingCalculate(Color color, int umbral, int proporcion) {
		// Declaramos los atributos
		int averageColor = this.calculateAverage(color); // Color medio
		int range; // Almacena el rango de suavizado
		int proportional; // Almacena la proporcion de suavizado
		int value; // Almacena el valor del color suavizado

		// Calculamos el color suavizado
		if (averageColor >= umbral) {
			range = 255 - umbral;
			proportional = averageColor - umbral;
			value = (int) (255 - ((proportional * proporcion) / range));
			this.colorSRGB = super.colorRGBtoSRGB(new Color(value, value, value, color.getAlpha()));
		} else {
			range = umbral;
			proportional = averageColor;
			value = (int) ((proportional * proporcion) / range);
			this.colorSRGB = super.colorRGBtoSRGB(new Color(value, value, value, color.getAlpha()));
		}

		return this.colorSRGB;
	}

	/**
	 * Método que calcula el ajuste de contraste.
	 * 
	 * @param value
	 *            Valor de contraste
	 * @return Contraste ajustado
	 */
	private double valueContrastAdjustment(double value) {
		// Declaramos los atributos
		final double calculeValue; // Almacena el valor del ajuste

		// Calculamos el ajuste
		calculeValue = (1 / (Math.PI / 4));
		value = (value + 1) * (Math.PI / 4);
		value *= calculeValue;

		return value;
	}

	/**
	 * Método que calcula la proporción de contraste.
	 * 
	 * @param color
	 *            Color al que calcular el contraste
	 * @param contrast
	 *            Cantidad de contraste
	 * @return Color con el contraste aplicado
	 */
	private int contrastCalculateProportion(int color, double contrast) {
		// Declaramos los atributos
		int colorValue; // Almacena el color

		// Calculamos el color con contraste
		colorValue = (int) (((color - 128) * contrast) + 128);
		colorValue = colorCheck(colorValue);

		return colorValue;
	}

	/**
	 * Método que calcula el contraste.
	 * 
	 * @param color
	 *            Color al que aplicar el contraste
	 * @param contrast
	 *            Cantidad de contraste
	 * @return Color con el contraste aplicado
	 */
	private int contrastCalculate(Color color, double contrast) {
		// Declaramos los atributos
		int red; // Almacena el componente rojo
		int green; // Almacena el componente verde
		int blue; // Almacena el componente azul

		// Calculamos los componentes RGB y lo asignamos al color SRGB
		red = contrastCalculateProportion(color.getRed(), contrast);
		green = contrastCalculateProportion(color.getGreen(), contrast);
		blue = contrastCalculateProportion(color.getBlue(), contrast);
		this.colorSRGB = super.colorRGBtoSRGB(new Color(red, green, blue, color.getAlpha()));

		return this.colorSRGB;
	}

	/**
	 * Convierte la imagen a blanco y negro suavizado.
	 * 
	 * @param image
	 *            BufferedImage a convertir
	 * @param umbral
	 *            Umbral para asignar blanco o negro
	 * @param proporcion
	 *            Valor de suavizado
	 * @return Imagen suavizada
	 */
	public BufferedImage blackAndWhiteSmoothing(BufferedImage image, int umbral, int proporcion) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen

		// Calculamos el suavizado
		if (umbral >= proporcion && (255 - umbral >= proporcion)) {
			Color auxColor;
			for (int i = 0; i < imageTemp.getWidth(); i++) {
				for (int j = 0; j < imageTemp.getHeight(); j++) {
					auxColor = new Color(imageTemp.getRGB(i, j));
					imageTemp.setRGB(i, j, this.smoothingCalculate(auxColor, umbral, proporcion));
				}
			}
			super.updateImage(
					"Transformación: Suavizado de blanco y negro. Umbral: " + umbral + " Proporcion: " + proporcion,
					imageTemp);
		} else {
			super.updateActivityLog(
					"Fallo al aplicar la transformación de suavizado de blanco y negro con los parámetros introducidos...");
		}

		return imageTemp;
	}

	/**
	 * CMétodo que aplicar a una imágen el contrate indicado.
	 * 
	 * @param image
	 *            Imagen a transformar
	 * @param contrastValue
	 *            Valor del contraste
	 * @return Imagen con contraste
	 */
	public BufferedImage contrast(BufferedImage image, double contrastValue) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		double contrastValueTemp; // Almacena el valor del contraste

		// Calculamos el nivel de contraste y generamos el color con el
		// contraste aplicado
		contrastValueTemp = valueContrastAdjustment(contrastValue);
		Color auxColor;
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				imageTemp.setRGB(i, j, this.contrastCalculate(auxColor, contrastValueTemp));
			}
		}
		super.updateImage("Transformación: contraste modificado (" + contrastValue + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para sumar un valor a la imagen. Como resultado, aumenta el
	 * brillo de esta.
	 * 
	 * @param image
	 *            Imagen a la que sumar el valor.
	 * @param valorSumar
	 *            Valor que sumar.
	 * @return Imagen con el valor sumado.
	 */
	public BufferedImage sumar(BufferedImage image, int valorSumar) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color
		double valor = (double) valorSumar; // Almacen el valor a sumar

		// Calculamos el nuevo color despues de sumarle la constante y mostramos
		// la nueva imágen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.min(255, auxColor.getRed() + valor));
				int green = (int) Math.round(Math.min(255, auxColor.getGreen() + valor));
				int blue = (int) Math.round(Math.min(255, auxColor.getBlue() + valor));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: sumar valor (" + valor + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para restar un valor a la imagen. Como resultado, disminuye el
	 * brillo de esta.
	 * 
	 * @param image
	 *            Imagen a la que restar el valor.
	 * @param valorRestar
	 *            Valor que restar.
	 * @return Imagen con el valor restado.
	 */
	public BufferedImage restar(BufferedImage image, int valorRestar) {
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Alamcena el color
		double valor = (double) valorRestar; // Almacena el valore a restar

		// Calculamos el nuevo color generado al restar el valor indicado
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.max(0, auxColor.getRed() - valor));
				int green = (int) Math.round(Math.max(0, auxColor.getGreen() - valor));
				int blue = (int) Math.round(Math.max(0, auxColor.getBlue() - valor));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: restar valor (" + valor + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para multiplicar un valor a la imagen. Como resultado, aumenta la
	 * intensidad de esta.
	 * 
	 * @param image
	 *            Imagen a la que multiplicar el valor.
	 * @param valorMultiplicar
	 *            Valor que multiplicar.
	 * @return Imagen con el valor multiplicado.
	 */
	public BufferedImage multiplicar(BufferedImage image, double valorMultiplicar) {
		imageTemp = super.cloneBufferedImage(image); // Almacena la imágen
		Color auxColor; // Almacena el color

		// Calculamos el nuevo color al multiplicar el valor introducido y
		// generamos la nueva imagen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.min(255, auxColor.getRed() * valorMultiplicar));
				int green = (int) Math.round(Math.min(255, auxColor.getGreen() * valorMultiplicar));
				int blue = (int) Math.round(Math.min(255, auxColor.getBlue() * valorMultiplicar));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: multiplicar valor (" + valorMultiplicar + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para dividir un valor a la imagen.
	 * 
	 * @param image
	 *            Imagen a la que dividir el valor.
	 * @param valorDividir
	 *            Valor que dividir.
	 * @return Imagen con el valor dividido.
	 */
	public BufferedImage dividir(BufferedImage image, double valorDividir) {
		imageTemp = super.cloneBufferedImage(image); // Alamcena la imagen
		Color auxColor; // Almacena el color

		// Calculamos el nuevo color al dividir el valor indicado y generamos la
		// nueva imagen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.max(0, auxColor.getRed() / valorDividir));
				int green = (int) Math.round(Math.max(0, auxColor.getGreen() / valorDividir));
				int blue = (int) Math.round(Math.max(0, auxColor.getBlue() / valorDividir));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: dividir valor (" + valorDividir + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para elevar la imagen por un valor.
	 * 
	 * @param image
	 *            Imagen a la que elevar el valor.
	 * @param valorExponencial
	 *            Valor que elevar.
	 * @return Imagen con el valor elevado.
	 */
	public BufferedImage exponencial(BufferedImage image, double valorExponencial) {
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Generamos el nuevo color y la nueva iamgen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.min(255, Math.pow(auxColor.getRed(), valorExponencial)));
				int green = (int) Math.round(Math.min(255, Math.pow(auxColor.getGreen(), valorExponencial)));
				int blue = (int) Math.round(Math.min(255, Math.pow(auxColor.getBlue(), valorExponencial)));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: elevar valor (" + valorExponencial + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para logaritmizar la imagen por un valor.
	 * 
	 * @param image
	 *            Imagen a la que elevar el valor
	 * @param valorLogaritmo
	 *            Valor de la base del logaritmo
	 * @return Imagen con el logaritmo
	 */
	public BufferedImage logaritmo(BufferedImage image, int valorLogaritmo) {
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Generamos el nuevo color y la nueva iamgen
		for (int i = 0; i < imageTemp.getWidth(); i++) {
			for (int j = 0; j < imageTemp.getHeight(); j++) {
				auxColor = new Color(imageTemp.getRGB(i, j));
				int red = (int) Math.round(Math.max(0, Math.log(auxColor.getRed()) / Math.log(valorLogaritmo)));
				int green = (int) Math.round(Math.max(0, Math.log(auxColor.getGreen()) / Math.log(valorLogaritmo)));
				int blue = (int) Math.round(Math.max(0, Math.log(auxColor.getBlue()) / Math.log(valorLogaritmo)));
				int alpha = auxColor.getAlpha();

				auxColor = new Color(red, green, blue, alpha);
				imageTemp.setRGB(i, j, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: logaritmo base (" + valorLogaritmo + ")", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para aplicar el filtro de Laplace.
	 * 
	 * @param image
	 *            Imagen a la que aplicar el filtro
	 * @return Imagen con el filtro aplicado
	 */
	public BufferedImage filtroLaplace(BufferedImage image) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		Color auxColor; // Almacena el color

		// Calculamos los colores de la nueva imagen aplicando el filtro de
		// laplace.
		for (int y = 1; y < imageTemp.getHeight() - 1; y++) {
			for (int x = 1; x < imageTemp.getWidth() - 1; x++) {
				// Obtenemos los colores en un cuadro de 3x3 alrededor del
				// píxel
				Color c00 = new Color(imageTemp.getRGB(x - 1, y - 1));
				Color c01 = new Color(imageTemp.getRGB(x, y - 1));
				Color c02 = new Color(imageTemp.getRGB(x + 1, y - 1));
				Color c10 = new Color(imageTemp.getRGB(x - 1, y));
				Color c11 = new Color(imageTemp.getRGB(x, y));
				Color c12 = new Color(imageTemp.getRGB(x + 1, y));
				Color c20 = new Color(imageTemp.getRGB(x - 1, y + 1));
				Color c21 = new Color(imageTemp.getRGB(x, y + 1));
				Color c22 = new Color(imageTemp.getRGB(x + 1, y + 1));

				// Calculamos el nuevo color aplicando el filtro de Laplace
				// Completo [1, 1 , 1]
				// [1, -8 , 1]
				// [1, 1 , 1]
				int r = +c00.getRed() + c01.getRed() + c02.getRed() + c10.getRed() - (8 * c11.getRed()) + c12.getRed()
						+ c20.getRed() + c21.getRed() + c22.getRed();

				int g = +c00.getGreen() + c01.getGreen() + c02.getGreen() + c10.getGreen() - (8 * c11.getGreen())
						+ c12.getGreen() + c20.getGreen() + c21.getGreen() + c22.getGreen();

				int b = +c00.getBlue() + c01.getBlue() + c02.getBlue() + c10.getBlue() - (8 * c11.getBlue())
						+ c12.getBlue() + c20.getBlue() + c21.getBlue() + c22.getBlue();

				// Comprobamos que no se salga de los límites de rgb
				r = Math.min(255, Math.max(0, r));
				g = Math.min(255, Math.max(0, g));
				b = Math.min(255, Math.max(0, b));

				auxColor = new Color(r, g, b);

				imageTemp.setRGB(x, y, auxColor.getRGB());
			}
		}
		super.updateImage("Transformación: filtro de Laplace", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para aplicar el filtro de Paso Bajo.
	 * 
	 * @param image
	 *            Imagen a la que aplicar el filtro
	 * @return Imagen con el filtro aplicado
	 */
	public BufferedImage filtroLowPass(BufferedImage image) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		float val = 1f / 9f;
		float[] data = { val, val, val, val, val, val, val, val, val };
		Kernel kernel = new Kernel(3, 3, data); // Crea el kernel de convoluci�n
		// Utilice la clase BufferedImageOp para crear el objeto ConvolveOp que
		// contiene y almacena en b�fer el kernel de convoluci�n.
		BufferedImageOp ConOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		imageTemp = ConOp.filter(imageTemp, null); // Llamar al m�todo de filtro
		super.updateImage("Transformación: filtro de Paso Low", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para aplicar el filtro de Paso Alto.
	 * 
	 * @param image
	 *            Imagen a la que aplicar el filtro
	 * @return Imagen con el filtro aplicado
	 */
	public BufferedImage filtroHighPass(BufferedImage image) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		float val = -1f;
		float valOfCenter = 8f;
		float[] data = { val, val, val, val, valOfCenter, val, val, val, val };
		Kernel kernel = new Kernel(3, 3, data); // Crea el kernel de convoluci�n
		// Utilice la clase BufferedImageOp para crear el objeto ConvolveOp que
		// contiene y almacena en b�fer el kernel de convoluci�n.
		BufferedImageOp ConOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		imageTemp = ConOp.filter(imageTemp, null); // Llamar al m�todo de filtro
		super.updateImage("Transformación: filtro de Paso Alto", imageTemp);

		return imageTemp;
	}

	/**
	 * Método para aplicar el filtro de Paso Banda.
	 * 
	 * @param image
	 *            Imagen a la que aplicar el filtro
	 * @return Imagen con ruido gaussiano aplicado
	 */
	public BufferedImage filtroBandPass(BufferedImage image) {
		// Declaramos los atributos
		imageTemp = super.cloneBufferedImage(image); // Almacena la imagen
		// float[] banddata = { -2, 0, 0, 0, 1, 0, 0, 0, 2 };
		float[] banddata = { 1, 0, 0, 0, -1, 0, 0, 0, 1 };
		Kernel kernel = new Kernel(3, 3, banddata);
		// Utilice la clase BufferedImageOp para crear el objeto ConvolveOp que
		// contiene y almacena en b�fer el kernel de convoluci�n.
		BufferedImageOp ConOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		imageTemp = ConOp.filter(imageTemp, null);
		super.updateImage("Transformación: filtro de Paso Banda", imageTemp);

		return imageTemp;
	}

	/**
	 * M�todo para agregar ruido gaussiano a la imagen
	 * 
	 * @param image
	 *            Imagen para agregar el ruido gaussiano
	 * @return Imagen con ruido gaussiano aplicado
	 */
	public BufferedImage addGaussianNoise(BufferedImage image) {
		imageTemp = super.cloneBufferedImage(image);
		double variance = sigma * sigma;
		int width = imageTemp.getWidth();
		int height = imageTemp.getHeight();

		double a = 0.0;
		double b = 0.0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				while (a == 0.0)
					a = Math.random();
				b = Math.random();

				double x = Math.sqrt(-2 * Math.log(a)) * Math.cos(2 * Math.PI * b);
				double noise = mean + Math.sqrt(variance) * x;

				int sourceR = new Color(imageTemp.getRGB(i, j)).getRed();
				double colorR = sourceR + noise;
				colorR = colorCheck((int) colorR);

				int sourceG = new Color(imageTemp.getRGB(i, j)).getGreen();
				double colorG = sourceG + noise;
				colorG = colorCheck((int) colorG);

				int sourceB = new Color(imageTemp.getRGB(i, j)).getBlue();
				double colorB = sourceB + noise;
				colorB = colorCheck((int) colorB);

				Color newColor = new Color((int) colorR, (int) colorG, (int) colorB);
				imageTemp.setRGB(i, j, newColor.getRGB());

			} // j
		} // i
		super.updateImage("Transformaci�n: Agregue ruido gaussiano", imageTemp);

		return imageTemp;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}
}
