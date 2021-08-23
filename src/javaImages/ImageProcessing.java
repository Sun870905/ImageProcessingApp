package javaImages;

//Declaramos las librerias a usar
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTextArea;

/**
 * Esta clase contiene los métodos para el procesado de imágenes.
 * @author José Eduardo Fernández Garcia
 * @version 1.0.0/2021
 */
 
public class ImageProcessing {
        //Declaramos las constantes
        private static final ArrayList<String> ALL_STATUS = new ArrayList<>(); //Almacena todas las modificaciones que se han realizado.
        private static final ArrayList<BufferedImage> ALL_IMAGES = new ArrayList<>(); //Almacena todas las imagenes con las que se ha trabajado
        private static final ArrayList<String> ACTIVITY_LOG=new ArrayList<>(); //Almacena la actividad de la consola
        private static final ArrayList<String> ALL_INFO_OPENED_IMAGES=new ArrayList<>(); //Almacena la información de todas las imágenes que se han abierto
        private static final ArrayList<BufferedImage> ALL_OPENED_IMAGES=new ArrayList<>(); //Almacena todas las imagenes que se han abierto
        
        //Declaramos las propiedades
        static private String status; //Alamacena la modificación que se realiza sobre la imágen
        static private BufferedImage currentImage; //Almacena la imagen con la que estamos trabajando 
        static private String infolastOpenedImage; //Almacena la información de la última imagen abierta
        static private BufferedImage lastOpenedImage; //Almacena la última imágen abierta   
        static private int counterOpenedImages=-1; //Almacena un contador de imágenes abierta
        static private int counterImages=-1; //Almacena un contador de imágenes
        static private JTextArea txtArea; //Almacena un textArea
        static public enum imageFormat{all,all_images,bmp,gif,jpg,png} //Almacena los principales formatos de imágen

               
    /**
     * Método que comprueba si el tamaño del array que se le pasa es 0 o no.
     * @param arrayChecking Array a comprobar el tamaño
     * @return True si es mayor que 0 y false en caso contrario
     */
    private boolean checkingSize(ArrayList arrayChecking){
        boolean flag; //Almacena un booleano
        
        //Comprobamos el tamaño del Array
        flag = arrayChecking.size() > 0;
        
        return flag;
    }
    
    /**
     * Método que obtiene la hora del PC.
     * @return Devuelve la hora actual
     */
    private String getCurrentTime() {
        //Definimos atributos
        String fecha; //Almacena la fecha en String
        Date now; //Almacena un objeto Date para especificar la hora
        SimpleDateFormat formated; //Almacena el formato de la fecha
        
        //Obtenemos la fecha del PC y le damos formato.
        now = new Date();
        formated = new SimpleDateFormat("hh:mm:ss"); 
        fecha = formated.format(now);
        
        return fecha;
    }
    
    /**
     * Método que devuelve el último estado de la última modificación hecha.
     * @return Último estado aplicado
     */
    public String getStatus() {
        //Declaramos los atributos
        String estado = null; //Almacena el status de la última imágen
        
        //Comprobamos si hay status
        if (checkingSize(ImageProcessing.ALL_STATUS)){
            ImageProcessing.status = ALL_STATUS.get(ImageProcessing.ALL_STATUS.size() - 1);
            estado = ImageProcessing.status;
        }
        
        return estado;
    }

    /**
     * Método que devuelve el arraylist con todas las modificaciones.
     * @return Arraylist que contiene todas las modificaciones
     */
    public ArrayList<String> getAllStatus() {
        return ALL_STATUS;
    }

    /**
     * Método que devuelve la última imágen recibida.
     * @return BufferedImage que devuelve la última imágen.
     */
    public BufferedImage getCurrentImage() {
        //Declaramos los atributos
        ImageProcessing.currentImage = null;
        
        //Comprobamos si hay imágenes
        if (checkingSize(ImageProcessing.ALL_IMAGES)){
            ImageProcessing.currentImage = ALL_IMAGES.get(ImageProcessing.ALL_IMAGES.size() - 1);
        }
        
        return ImageProcessing.currentImage;
    }

    /**
     * Método que devuelve todas las imágenes recibidas.
     * @return ArrayList con todas las imágenes recibidas.
     */
    public ArrayList<BufferedImage> getAllImages() {
        return ALL_IMAGES;
    }
     
    /**
     * Método que devuelve todos los registros de la consola.
     * @return ArrayList con todos los registros y errores de la consola
     */
    public ArrayList<String> getActivityLog() {
        return ACTIVITY_LOG;
    }
    
    /**
     * Método que devuelve toda la información de las imágenes abiertas.
     * @return ArrayList con todas las informaciones
     */
    public static ArrayList<String> getinfoOpenedImages() {
        return ALL_INFO_OPENED_IMAGES;
    }

    /**
     * Método que devuelve todas las imágenes abierta.
     * @return ArrayList con todas las imágenes abiertas.
     */
    public static ArrayList<BufferedImage> getAllOpenedImages() {
        return ALL_OPENED_IMAGES;
    }
    
    /**
     * Método que devuelve la última imágen abierta.
     * @return Última imagen abierta
     */
    public BufferedImage lastOpenedImage(){
        //Comprobamos si hay imágenes y recuperamos la última.
        if(ImageProcessing.counterOpenedImages >= 0){
           ImageProcessing.lastOpenedImage = ImageProcessing.ALL_OPENED_IMAGES.get(ImageProcessing.counterOpenedImages);
           this.updateImage("Imagen original recuperada: ",ImageProcessing.lastOpenedImage);
        }
        
       return ImageProcessing.lastOpenedImage;
    }
    
    /**
     * Método que devuelve la información de la última imágen abierta.
     * @return Información de la última imágen abierta
     */
    public String lastInfoOpenedImage(){
        //Comprobamos si hay imágenes
        if(ImageProcessing.counterOpenedImages >= 0){
           ImageProcessing.infolastOpenedImage=ImageProcessing.ALL_INFO_OPENED_IMAGES.get(ImageProcessing.counterOpenedImages);
        }
        
       return ImageProcessing.infolastOpenedImage;
    }    
    
    /**
      * Method que muestra toda la información en la consola.
      * @param message Mensaje a mostrar por consola
      */
    protected void updateActivityLog(String message){
        //Almacenamos la información en el arraylist.
        ImageProcessing.ACTIVITY_LOG.add(message);

        //Si tenemos la consola abierta, mostramos el mensaje
        if (txtArea != null){
            ImageProcessing.txtArea.setText(ImageProcessing.txtArea.getText() + this.getCurrentTime() + " - " + message + "\n");
        }
    }
   
    /**
     * Método que almacena la información de la transformación de la imágen y la imágen.
     * @param statusImage String que almacena la información de la transformación de la imágen
     * @param currentImage Imagen que va a ser procesada
     */
    protected void updateImage(String statusImage,BufferedImage currentImage){
        //Almacenamos la información y la imágen
        ALL_STATUS.add(statusImage);
        ALL_IMAGES.add(currentImage);
        
        //Actualizamos la consola
        updateActivityLog(statusImage);
        
        //Aumentamos el contador de imágenes procesadas
        ImageProcessing.counterImages += 1;
    }
    
    /**
     * Método que almacena la información y la imágen a los Arrays. 
     * @param infoImage Información de la imágen
     * @param openedImage Imágen
     */
    protected void newOpenImage(String infoImage, BufferedImage openedImage){
        //Almacenamos la información e imágen e incrementamos el contador
        ImageProcessing.ALL_INFO_OPENED_IMAGES.add(infoImage);
        ImageProcessing.ALL_OPENED_IMAGES.add(openedImage);
        ImageProcessing.counterOpenedImages += 1;
    }
    
    /**
     * Método que borra todas las imágenes almacenadas y sus respectivas informaciones.
     */
    public void deleteAllStoredImages(){
        //Borramos toda la información y reseteamos las variables
        ImageProcessing.ALL_IMAGES.clear();
        ImageProcessing.ALL_STATUS.clear();
        ImageProcessing.ALL_OPENED_IMAGES.clear();
        ImageProcessing.ALL_INFO_OPENED_IMAGES.clear();
        ImageProcessing.counterImages =- 1;
        ImageProcessing.counterOpenedImages =- 1;
        ImageProcessing.currentImage = null;
        ImageProcessing.status = null;
        ImageProcessing.lastOpenedImage = null;
        ImageProcessing.infolastOpenedImage = null;
        this.updateActivityLog("Se han eliminado todos los registros de las imágenes...");
    }
    
    /**
     * Método que asocia al JTextArea con la información de las imágenes y transformaciones.
     * @param textAreaAttached JTextArea donde mostrar la información
     */
    public void attachTextAreaStatus(JTextArea textAreaAttached){
        ImageProcessing.txtArea = textAreaAttached;
        this.updateActivityLog("TextArea asignado...");
    }
    
    /**
     * Método que deshace la acción realizada.
     * @return Estado anterior a la modificación
     */
    public BufferedImage undoImage(){
        //Declaramos los atributos
        ImageProcessing.currentImage = null;
        
        //Comprobamos si hay imágenes
        if(ImageProcessing.counterImages > 0){
            ImageProcessing.counterImages -= 1;
            ImageProcessing.currentImage = ALL_IMAGES.get(ImageProcessing.counterImages);
            this.updateActivityLog("Deshacer imagen. Información de la imagen: " + ALL_STATUS.get(ImageProcessing.counterImages) + ")");
        }
        
        return currentImage;
    }
    
    /**
      * Método que rehace las modificaciones de la última imágen.
      * @return Imagen modificada 
      */
    public BufferedImage redoImage(){
        //Declaramos los atributos
         ImageProcessing.currentImage = null;

         //Comprobamos si hay imágenes
         if(ImageProcessing.counterImages < (ImageProcessing.ALL_IMAGES.size() - 1)){
             ImageProcessing.counterImages += 1;
             ImageProcessing.currentImage = ALL_IMAGES.get(ImageProcessing.counterImages);
             this.updateActivityLog("Rehacer imagen. Información de la imágen: " + ALL_STATUS.get(ImageProcessing.counterImages) + ")");
         }

         return currentImage;
    }
   
    /**
     * Método que convierte un valor RGB a SRGB.
     * @param colorValueRGB Valor RGB a convertir
     * @return Valor SRGB
     */

    protected int colorRGBtoSRGB(Color colorValueRGB){
        //Declaramos los atributos
         int colorSRGB;

         //Calculamos el color SRGB asociado
         colorSRGB=(colorValueRGB.getRed() << 16) | (colorValueRGB.getGreen() << 8) | colorValueRGB.getBlue();

         return colorSRGB;
    }
    
   /**
    * Método que copia la imágen.
    * @param bufferImage Imagen a copiar
    * @return Imagen copiada
    */
   protected BufferedImage cloneBufferedImage(BufferedImage bufferImage){
        //Declaramos los atributos 
        BufferedImage copy;
        
        //Copiamos la imágen
        copy = new BufferedImage (bufferImage.getWidth(),bufferImage.getHeight(),bufferImage.getType());
        copy.setData(bufferImage.getData());
        
        return copy;
    }
}
