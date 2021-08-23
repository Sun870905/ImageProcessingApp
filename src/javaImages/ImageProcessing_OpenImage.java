package javaImages;

//Declaramos las librerias a usar
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.net.URL;

/**
 * Esta clase contiene los métodos para abrir una imágen.
 * 
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021
 */
public class ImageProcessing_OpenImage extends ImageProcessing {
	// Declaramos las propiedades
	private BufferedImage openImage; // Almacena la iamgen a abrir
	private JFileChooser selectorImage; // Almacena el selector de archivos
	private imageFormat selectedExtensionImage; // Almacena la extension de la
												// imagen

	/**
	 * Método que abre el selector de archivos.
	 * 
	 * @return Opción elegida del selector
	 */
	protected boolean openJFileChooser() {
		// Declaramos los atributos
		this.selectorImage = new JFileChooser(); // Almacena el selector de
													// archivos

		// Damos titulo al selector y comprobamos la opción elegida
		this.selectorImage.setDialogTitle("Escoge una imagen");
		selectImageFilter();
		int flag = this.selectorImage.showOpenDialog(null);

		return flag == JFileChooser.APPROVE_OPTION;
	}

	/**
	 * Método que selecciona el tipo de extensión.
	 */
	protected void selectImageFilter() {
		// Declaramos los atributos
		FileNameExtensionFilter imageFilter; // Almacena la extension del
												// archivo

		// Comprobamos el tipo de extensión
		imageFilter = new FileNameExtensionFilter("All files (*.*)", "*.*");
		switch (this.selectedExtensionImage) {
		case all:
			imageFilter = new FileNameExtensionFilter("All files (*.*)", "*.*");
			break;
		case all_images:
			imageFilter = new FileNameExtensionFilter("All image files", "bmp", "dib", "gif", "jpg", "jpeg", "jpe",
					"jfif", "png");
			break;
		case bmp:
			imageFilter = new FileNameExtensionFilter("Bitmap (*.bmp, *.dib)", "bmp", "dib");
			break;
		case gif:
			imageFilter = new FileNameExtensionFilter("GIF (*.gif)", "gif");
			break;
		case jpg:
			imageFilter = new FileNameExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jfif)", "jpg", "jpeg", "jpe", "jfif");
			break;
		case png:
			imageFilter = new FileNameExtensionFilter("PNG (*.png)", "png");
			break;
		}
		this.selectorImage.setFileFilter(imageFilter);
	}

	/**
	 * Método que extrae el nombre de una imagen local
	 * 
	 * @param imagePath
	 *            Ruta de la imagen
	 * @return Nombre de la imagen
	 */
	protected String extractLocalImageName(String imagePath) {
		int finalSlash = imagePath.lastIndexOf("\\");
		String nameReturn = imagePath.substring(finalSlash + 1);
		return nameReturn;
	}

	/**
	 * Método que extrae el nombre de una imagen remota
	 * 
	 * @param imagePath
	 * @return
	 */
	protected String extractRemoteImageName(String imagePath) {
		int finalSlash = imagePath.lastIndexOf("/");
		String nameReturn = imagePath.substring(finalSlash + 1);
		return nameReturn;
	}

	/**
	 * Método que muestra el selector de archivos.
	 * 
	 * @param defautlExtension
	 *            Extensión por defecto
	 * @return Imagen seleccionada
	 */
	public BufferedImage openFile(ImageProcessing.imageFormat defautlExtension) {
		// Declaramos los atributos
		this.selectedExtensionImage = defautlExtension; // Almacena la
														// extensión por
														// defecto
		this.openImage = null; // Almacena la imagen

		// Obtenemos la imagen seleccionada
		if (this.openJFileChooser() == true) {
			File imageFile = this.selectorImage.getSelectedFile();
			try {
				this.openImage = ImageIO.read(this.selectorImage.getSelectedFile());
				if (this.openImage != null) {
					super.updateImage("Imagen local cargada correctamente - "
							+ extractLocalImageName(imageFile.toString()) + " (" + imageFile.toString() + ")",
							this.openImage);
					super.newOpenImage(
							"Imagen local cargada correctamente - " + extractLocalImageName(imageFile.toString()),
							this.openImage);
				} else {
					super.updateActivityLog("Error: fallo al intentar cargar la imagen");
				}
			} catch (IOException e) {
				super.updateActivityLog(
						"Error: fallo al intentar cargar la imagen. Excepción generada:\n" + e.toString());
			}
		}
		return this.openImage;
	}

	/**
	 * Método que carga una imagen de internet
	 * 
	 * @param urlImage
	 *            Dirección de la imágen
	 * @return Imagen de internet
	 */
	public BufferedImage openUrl(String urlImage) {
		// Declaramos los atributos
		this.openImage = null; // Almacena la imagen

		// Obtenemos la imagen de internet
		try {
			URL url = new URL(urlImage);
			this.openImage = ImageIO.read(url);
			super.updateImage(
					"Imagen remota cargada correctamente - " + extractRemoteImageName(urlImage) + " (" + urlImage + ")",
					this.openImage);
			super.newOpenImage("Imagen remota cargada correctamente - " + extractRemoteImageName(urlImage),
					this.openImage);
		} catch (IOException e) {
			super.updateActivityLog("Error: fallo al intentar cargar la imagen. Excepción generada:\n" + e.toString());
		}
		return this.openImage;
	}
}
