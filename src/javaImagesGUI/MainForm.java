package javaImagesGUI;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javaImages.ImageProcessing;
import javaImages.ImageProcessing_AdvancedFilters_Operations;
import javaImages.ImageProcessing_BasicFilters;
import javaImages.ImageProcessing_Histogram;
import javaImages.ImageProcessing_OpenImage;
import javaImages.ImageProcessing_Resize;
import javaImages.ImageProcessing_SaveImage;
import javaImages.ImageProcessing_TransformFormatImages;
import javaImages.ImageProcessing_Resize.ScaleType;

/**
 *
 * @author Luis
 */
public class MainForm extends javax.swing.JFrame {

	private ImageProcessing ObjProcessing;
	private ImageProcessing_OpenImage ObjOpenImage;
	private ImageProcessing_SaveImage ObjSaveImage;
	private ImageProcessing_BasicFilters ObjBasicFilters;
	private ImageProcessing_AdvancedFilters_Operations ObjAdvancedFilters;
	private ImageProcessing_TransformFormatImages ObjTransformFormats;
	private ImageProcessing_Histogram ObjHistogram;
	private ImageProcessing_Resize ObjResize;
	private CreateHistogram ObjBarHistogram;
	private Boolean flagImage;
	private BufferedImage bufferImageTemp;

	private void DeleteText(JTextField jtextfield) {
		jtextfield.setText("");
	}

	private void interfaceModifications() {
		this.jSplitPanel1.setDividerLocation(0.85);
		this.jSplitPanel2_1.setDividerLocation(0.8);
		this.jSplitPanel2_1.setResizeWeight(1);
		this.jSplitPanel1.setResizeWeight(1);
	}

	private void enabledPanels(Boolean enabled) {
		for (int i = 0; i < panelHerramientas.getComponentCount(); i++) {
			if (panelHerramientas.getComponent(i) instanceof JPanel) {
				JPanel jpanelTemp = (JPanel) panelHerramientas.getComponent(i);
				for (int j = 0; j < jpanelTemp.getComponentCount(); j++) {
					jpanelTemp.getComponent(j).setEnabled(enabled);
				}
			}
		}
		if (enabled == false) {
			for (int j = 0; j < this.panelAbrirGuardarImagen.getComponentCount(); j++) {
				this.panelAbrirGuardarImagen.getComponent(j).setEnabled(!enabled);
			}
		}
	}

	private void initializeObjects() {
		ObjProcessing = new ImageProcessing();
		ObjOpenImage = new ImageProcessing_OpenImage();
		ObjSaveImage = new ImageProcessing_SaveImage();
		ObjBasicFilters = new ImageProcessing_BasicFilters();
		ObjAdvancedFilters = new ImageProcessing_AdvancedFilters_Operations();
		ObjTransformFormats = new ImageProcessing_TransformFormatImages();
		ObjHistogram = new ImageProcessing_Histogram();
		ObjResize = new ImageProcessing_Resize();
		ObjBarHistogram = new CreateHistogram();
		ObjProcessing.attachTextAreaStatus(jTextArea_Log);
	}

	private void formInitialize() {
		interfaceModifications();
		initializeObjects();
		enabledPanels(false);
		this.flagImage = false;
	}

	private void showActivity(Boolean visible) {
		if (visible == true) {
			this.jSplitPanel1.setDividerLocation(0.85);
		} else {
			this.jSplitPanel1.setDividerLocation(0.9999);
		}
	}

	private void openImage() {
		this.bufferImageTemp = ObjOpenImage.openFile(ImageProcessing.imageFormat.all_images);
		if (this.bufferImageTemp != null) {
			this.jLabelImage.setIcon(new ImageIcon(this.bufferImageTemp));
			if (this.flagImage == false) {
				this.flagImage = true;
				enabledPanels(true);
			}
		}
	}

	private void openURL() {
		if (!"".equals(this.JTextfield_URL.getText())) {
			this.bufferImageTemp = ObjOpenImage.openUrl(this.JTextfield_URL.getText());
			if (bufferImageTemp != null) {
				this.jLabelImage.setIcon(new ImageIcon(this.bufferImageTemp));
				if (flagImage == false) {
					this.flagImage = true;
					enabledPanels(true);
				}
			}
		}
	}

	private void saveImage() {
		if (this.jLabelImage.getIcon() != null) {
			ImageProcessing.imageFormat extension;
			switch (this.jComboBox1.getSelectedItem().toString()) {
			case "BMP":
				extension = ImageProcessing.imageFormat.bmp;
				break;
			case "GIF":
				extension = ImageProcessing.imageFormat.gif;
				break;
			case "JPG":
				extension = ImageProcessing.imageFormat.jpg;
				break;
			case "PNG":
				extension = ImageProcessing.imageFormat.png;
				break;
			default:
				extension = ImageProcessing.imageFormat.jpg;
				break;
			}
			ObjSaveImage.saveFile((BufferedImage) ObjTransformFormats.iconToImage(jLabelImage.getIcon()), extension);
		}
	}

	private void undoImage() {
		this.bufferImageTemp = ObjProcessing.undoImage();
		if (this.bufferImageTemp != null) {
			this.jLabelImage.setIcon(new ImageIcon(this.bufferImageTemp));
		}
	}

	private void redoImage() {
		this.bufferImageTemp = ObjProcessing.redoImage();
		if (this.bufferImageTemp != null) {
			this.jLabelImage.setIcon(new ImageIcon(this.bufferImageTemp));
		}
	}

	private void lastOpenedImage() {
		this.bufferImageTemp = ObjProcessing.lastOpenedImage();
		if (this.bufferImageTemp != null) {
			this.jLabelImage.setIcon(new ImageIcon(this.bufferImageTemp));
			this.JTexfield_lastOpened.setText(ObjProcessing.lastInfoOpenedImage());
		}
	}

	private void deleteAllImages() {
		int selected = JOptionPane.showConfirmDialog(null,
				"¿Quieres eliminar todas las imágenes?Esta acción no se podrá deshacer...", "Warning",
				JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == selected) {
			ObjProcessing.deleteAllStoredImages();
		}
	}

	private void grayScale() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage
				.setIcon(ObjTransformFormats.bufferedImageToImageIcon(ObjBasicFilters.grayScale(bufferImageTemp)));
	}

	private void blackWhite() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage.setIcon(ObjTransformFormats.bufferedImageToImageIcon(
				ObjBasicFilters.blackAndWhite(bufferImageTemp, jSlider_Threshold.getValue())));
	}

	private void invertColors() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		switch (jComboBox_Invert.getSelectedItem().toString()) {
		case "RGB":
			this.bufferImageTemp = ObjBasicFilters.invertColorsRGB(this.bufferImageTemp,
					ImageProcessing_BasicFilters.invertColorsAvailable.RGB);
			break;
		case "ROJO":
			this.bufferImageTemp = ObjBasicFilters.invertColorsRGB(this.bufferImageTemp,
					ImageProcessing_BasicFilters.invertColorsAvailable.red);
			break;
		case "VERDE":
			this.bufferImageTemp = ObjBasicFilters.invertColorsRGB(this.bufferImageTemp,
					ImageProcessing_BasicFilters.invertColorsAvailable.green);
			break;
		case "AZUL":
			this.bufferImageTemp = ObjBasicFilters.invertColorsRGB(this.bufferImageTemp,
					ImageProcessing_BasicFilters.invertColorsAvailable.blue);
			break;
		}
		this.jLabelImage.setIcon(ObjTransformFormats.bufferedImageToImageIcon(this.bufferImageTemp));
	}

	private void basicFilters() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		switch (jComboBox_BasicFilters.getSelectedItem().toString()) {
		case "ROJO":
			this.bufferImageTemp = ObjBasicFilters.basicFilters(this.bufferImageTemp,
					ImageProcessing_BasicFilters.filtersAvailable.red);
			break;
		case "VERDE":
			this.bufferImageTemp = ObjBasicFilters.basicFilters(this.bufferImageTemp,
					ImageProcessing_BasicFilters.filtersAvailable.green);
			break;
		case "AZUL":
			this.bufferImageTemp = ObjBasicFilters.basicFilters(this.bufferImageTemp,
					ImageProcessing_BasicFilters.filtersAvailable.blue);
			break;
		}
		this.jLabelImage.setIcon(ObjTransformFormats.bufferedImageToImageIcon(this.bufferImageTemp));
	}

	private void RGBto() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		switch (jComboBox_RGB.getSelectedItem().toString()) {
		case "GBR":
			this.bufferImageTemp = ObjBasicFilters.RGBto(this.bufferImageTemp,
					ImageProcessing_BasicFilters.RGBTransformAvailable.GBR);
			break;
		case "GRB":
			this.bufferImageTemp = ObjBasicFilters.RGBto(this.bufferImageTemp,
					ImageProcessing_BasicFilters.RGBTransformAvailable.GRB);
			break;
		case "BRG":
			this.bufferImageTemp = ObjBasicFilters.RGBto(this.bufferImageTemp,
					ImageProcessing_BasicFilters.RGBTransformAvailable.BRG);
			break;
		case "BGR":
			this.bufferImageTemp = ObjBasicFilters.RGBto(this.bufferImageTemp,
					ImageProcessing_BasicFilters.RGBTransformAvailable.BGR);
			break;
		case "RBG":
			this.bufferImageTemp = ObjBasicFilters.RGBto(this.bufferImageTemp,
					ImageProcessing_BasicFilters.RGBTransformAvailable.RBG);
			break;
		}
		this.jLabelImage.setIcon(ObjTransformFormats.bufferedImageToImageIcon(this.bufferImageTemp));
	}

	// private void blackWhiteSmoothing() {
	// this.bufferImageTemp =
	// ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
	// this.jLabelImage.setIcon(ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.blackAndWhiteSmoothing(
	// this.bufferImageTemp, this.jSlider_ThresholdSmoothing.getValue(),
	// this.jSlider_Range.getValue())));
	// }

	// private void contrastModificate() {
	// this.bufferImageTemp =
	// ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
	// double valueContrast = (double) ((double)
	// this.jSlider_Contrast.getValue() / 100);
	// this.jLabelImage.setIcon(ObjTransformFormats
	// .bufferedImageToImageIcon(ObjAdvancedFilters.contrast(bufferImageTemp,
	// valueContrast)));
	// }

	private void gaussianNoise() {
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		// double valueContrast = (double) ((double)
		// this.jSlider_Contrast.getValue() / 100);
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.addGaussianNoise(bufferImageTemp)));
	}

	private ScaleType scaleTypeSelect(String scaleType) {
		ScaleType scaleTypeSelected = ScaleType.SCALE_DEFAULT;
		switch (scaleType) {
		case "SCALE_AREA_AVERAGING":
			scaleTypeSelected = ScaleType.SCALE_AREA_AVERAGING;
			break;
		case "SCALE_DEFAULT":
			scaleTypeSelected = ScaleType.SCALE_DEFAULT;
			break;
		case "SCALE_FAST":
			scaleTypeSelected = ScaleType.SCALE_FAST;
			break;
		case "SCALE_REPLICATE":
			scaleTypeSelected = ScaleType.SCALE_REPLICATE;
			break;
		case "SCALE_SMOOTH":
			scaleTypeSelected = ScaleType.SCALE_SMOOTH;
			break;
		}
		return scaleTypeSelected;
	}

	private void resizeImage() {
		ScaleType scaleType = scaleTypeSelect(jComboBox_ResizeType.toString());
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		int widht, height;
		widht = Integer.parseInt(jFormatted_ResizeWidth.getText());
		height = Integer.parseInt(jFormatted_ResizeHeight.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjResize.resizeImage(bufferImageTemp, widht, height, scaleType)));
	}

	private void currentSize() {
		jTextField_CurrentSize
				.setText(this.jLabelImage.getIcon().getIconWidth() + "x" + this.jLabelImage.getIcon().getIconHeight());
	}

	private void resizePercentImage() {
		ScaleType scaleType = scaleTypeSelect(jComboBox_ResizePercentType.toString());
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		int widht, height;
		widht = Integer.parseInt(jFormatted_ResizeWidthPercent.getText());
		height = Integer.parseInt(jFormatted_ResizeHeightPErcent.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjResize.resizeImagePercent(bufferImageTemp, widht, height, scaleType)));
	}

	private void showHistogram() {
		int[] histogram;
		CreateHistogram.availableChannel histogramColor;
		switch (this.jComboBoxHistogram.getSelectedItem().toString()) {
		case "ROJO":
			histogram = ObjHistogram.histogramRed(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.red;
			break;
		case "VERDE":
			histogram = ObjHistogram
					.histogramGreen(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.green;
			break;
		case "AZUL":
			histogram = ObjHistogram.histogramBlue(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.blue;
			break;
		case "ALPHA":
			histogram = ObjHistogram
					.histogramAlpha(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.alpha;
			break;
		case "ESCALA DE GRISES":
			histogram = ObjHistogram
					.histogramGrayscale(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.grayscale;
			break;
		default:
			histogram = ObjHistogram
					.histogramGrayscale(ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon()));
			histogramColor = CreateHistogram.availableChannel.grayscale;
			break;
		}
		ObjBarHistogram.createHistogramBarChart(histogram, jPanelHistogram, histogramColor);
		this.jLabel_MaxHistogram.setText(
				"Max value " + ObjHistogram.getMaxValue()[0] + ", with " + ObjHistogram.getMaxValue()[1] + " pixels");
		this.jLabel_MinHistogram.setText(
				"Min value " + ObjHistogram.getMinValue()[0] + ", with " + ObjHistogram.getMinValue()[1] + " pixels");
	}

	public MainForm() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jSplitPanel1 = new javax.swing.JSplitPane();
		jSplitPanel2_1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		jLabelImage = new javax.swing.JLabel();
		jButton3 = new javax.swing.JButton();
		panelHerramientas = new javax.swing.JTabbedPane();
		panelAbrirGuardarImagen = new javax.swing.JPanel();
		jButton12 = new javax.swing.JButton();
		jComboBox1 = new javax.swing.JComboBox();
		jSeparator2 = new javax.swing.JSeparator();
		JTextfield_URL = new javax.swing.JTextField();
		jButton11 = new javax.swing.JButton();
		jButton10 = new javax.swing.JButton();
		panelPrincipal = new javax.swing.JPanel();
		jButton7 = new javax.swing.JButton();
		jButton8 = new javax.swing.JButton();
		jButton9 = new javax.swing.JButton();
		JTexfield_lastOpened = new javax.swing.JTextField();
		jSeparator1 = new javax.swing.JSeparator();
		jButton13 = new javax.swing.JButton();
		panelFiltrosAvanzados = new javax.swing.JPanel();
		jButton14 = new javax.swing.JButton();
		jButton15 = new javax.swing.JButton();
		jLabel_Threshold = new javax.swing.JLabel();
		jSlider_Threshold = new javax.swing.JSlider();
		jButton16 = new javax.swing.JButton();
		jComboBox_Invert = new javax.swing.JComboBox();
		jComboBox_BasicFilters = new javax.swing.JComboBox();
		jButton17 = new javax.swing.JButton();
		jButton18 = new javax.swing.JButton();
		jComboBox_RGB = new javax.swing.JComboBox();
		// jButton19 = new javax.swing.JButton();
		// jLabel_ThresholdSmoothing = new javax.swing.JLabel();
		// jSlider_ThresholdSmoothing = new javax.swing.JSlider();
		jLabel_Range = new javax.swing.JLabel();
		jSlider_Range = new javax.swing.JSlider();
		// jButton22 = new javax.swing.JButton();
		// jLabel_Contrast = new javax.swing.JLabel();
		// jSlider_Contrast = new javax.swing.JSlider();
		// btnLaplace = new javax.swing.JButton();
		btnGaussianNoise = new javax.swing.JButton();
		btnLow = new javax.swing.JButton();
		btnHigh = new javax.swing.JButton();
		btnBand = new javax.swing.JButton();
		panelConsola = new javax.swing.JPanel();
		jButton20 = new javax.swing.JButton();
		panelHistorigrama = new javax.swing.JPanel();
		jButton21 = new javax.swing.JButton();
		jComboBoxHistogram = new javax.swing.JComboBox();
		jPanelHistogram = new javax.swing.JPanel();
		jLabel_MaxHistogram = new javax.swing.JLabel();
		jLabel_MinHistogram = new javax.swing.JLabel();
		// panelRedimensionar = new javax.swing.JPanel();
		jButton26 = new javax.swing.JButton();
		jTextField_CurrentSize = new javax.swing.JTextField();
		jButton27 = new javax.swing.JButton();
		jComboBox_ResizeType = new javax.swing.JComboBox();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jButton23 = new javax.swing.JButton();
		jComboBox_ResizePercentType = new javax.swing.JComboBox();
		jSeparator4 = new javax.swing.JSeparator();
		jSeparator5 = new javax.swing.JSeparator();
		jSlider_ResizeWidth = new javax.swing.JSlider();
		jSlider_ResizeHeight = new javax.swing.JSlider();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jFormatted_ResizeWidth = new javax.swing.JFormattedTextField();
		jFormatted_ResizeHeight = new javax.swing.JFormattedTextField();
		jFormatted_ResizeWidthPercent = new javax.swing.JFormattedTextField();
		jFormatted_ResizeHeightPErcent = new javax.swing.JFormattedTextField();
		panelOperaciones = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		txtSumar = new javax.swing.JTextField();
		btnSumar = new javax.swing.JButton();
		jLabel7 = new javax.swing.JLabel();
		txtRestar = new javax.swing.JTextField();
		btnRestar = new javax.swing.JButton();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		txtMultiplicar = new javax.swing.JTextField();
		txtDividir = new javax.swing.JTextField();
		btnMultiplicar = new javax.swing.JButton();
		btnDividir = new javax.swing.JButton();
		jLabel11 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		txtExponencial = new javax.swing.JTextField();
		btnExponenecial = new javax.swing.JButton();
		lblLogaritmo = new javax.swing.JLabel();
		btnLogaritmo = new javax.swing.JButton();
		txtLogaritmo = new javax.swing.JTextField();
		panelRegistroConsola = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea_Log = new javax.swing.JTextArea();
		jButton2 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		menuHerramientas = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenu2 = new javax.swing.JMenu();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}
		});
		addWindowStateListener(new java.awt.event.WindowStateListener() {
			public void windowStateChanged(java.awt.event.WindowEvent evt) {
				formWindowStateChanged(evt);
			}
		});

		jSplitPanel1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPanel1.setMinimumSize(new java.awt.Dimension(500, 500));

		jLabelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jScrollPane1.setViewportView(jLabelImage);

		jSplitPanel2_1.setLeftComponent(jScrollPane1);

		jButton3.setText("jButton3");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});
		jSplitPanel2_1.setRightComponent(jButton3);

		jButton12.setText("Guardar Imagen");
		jButton12.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton12ActionPerformed(evt);
			}
		});

		jComboBox1
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BMP", "GIF", "JPG", "PNG", "NOTHING" }));

		JTextfield_URL.setText("http://");
		JTextfield_URL.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				JTextfield_URLMouseClicked(evt);
			}
		});

		jButton11.setText("Abrir URL");
		jButton11.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton11ActionPerformed(evt);
			}
		});

		jButton10.setText("Abrir Local");
		jButton10.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton10ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout panelAbrirGuardarImagenLayout = new javax.swing.GroupLayout(panelAbrirGuardarImagen);
		panelAbrirGuardarImagen.setLayout(panelAbrirGuardarImagenLayout);
		panelAbrirGuardarImagenLayout.setHorizontalGroup(panelAbrirGuardarImagenLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addGroup(panelAbrirGuardarImagenLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addGap(10, 10, 10)
								.addComponent(JTextfield_URL))
						.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addContainerGap()
								.addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
						.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addContainerGap().addComponent(
								jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
						.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addContainerGap()
								.addComponent(jSeparator2))
						.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addContainerGap()
								.addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jComboBox1, 0, 203, Short.MAX_VALUE)))
						.addContainerGap()));
		panelAbrirGuardarImagenLayout.setVerticalGroup(panelAbrirGuardarImagenLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelAbrirGuardarImagenLayout.createSequentialGroup().addContainerGap()
						.addComponent(jButton10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jButton11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(JTextfield_URL, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(panelAbrirGuardarImagenLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton12)
								.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(293, Short.MAX_VALUE)));

		panelHerramientas.addTab("Abrir/Guardar Imagen", panelAbrirGuardarImagen);

		jButton7.setText("Deshacer");
		jButton7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton7ActionPerformed(evt);
			}
		});

		jButton8.setText("Rehacer");
		jButton8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton8ActionPerformed(evt);
			}
		});

		jButton9.setText("Última Imagen Cargada");
		jButton9.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton9ActionPerformed(evt);
			}
		});

		jButton13.setText("Borrar historial de  imagenes almacenadas");
		jButton13.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton13ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
		panelPrincipal.setLayout(panelPrincipalLayout);
		panelPrincipalLayout
				.setHorizontalGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(panelPrincipalLayout.createSequentialGroup().addContainerGap()
								.addGroup(panelPrincipalLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 498,
												Short.MAX_VALUE)
										.addComponent(jSeparator1).addComponent(JTexfield_lastOpened)
										.addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap()));
		panelPrincipalLayout
				.setVerticalGroup(
						panelPrincipalLayout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelPrincipalLayout.createSequentialGroup().addContainerGap()
										.addComponent(jButton7)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton8)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton9)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(JTexfield_lastOpened, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton13).addContainerGap(264, Short.MAX_VALUE)));

		panelHerramientas.addTab("Principal", panelPrincipal);

		jButton14.setText("Escala de grises");
		jButton14.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton14ActionPerformed(evt);
			}
		});

		jButton15.setText("Blanco y negro");
		jButton15.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton15ActionPerformed(evt);
			}
		});

		jLabel_Threshold.setText("Umbral 128");

		jSlider_Threshold.setMaximum(255);
		jSlider_Threshold.setValue(128);
		jSlider_Threshold.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSlider_ThresholdStateChanged(evt);
			}
		});

		jButton16.setText("Invertir colores");
		jButton16.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton16ActionPerformed(evt);
			}
		});

		jComboBox_Invert
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RGB", "ROJO", "VERDE", "AZUL" }));

		jComboBox_BasicFilters.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ROJO", "VERDE", "AZUL" }));

		jButton17.setText("Filtro");
		jButton17.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton17ActionPerformed(evt);
			}
		});

		jButton18.setText("Convertir RGB a");
		jButton18.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton18ActionPerformed(evt);
			}
		});

		jComboBox_RGB
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GBR", "GRB", "BRG", "BGR", "RBG" }));

		// jButton19.setText("Suavizado de blanco y negro");
		// jButton19.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// jButton19ActionPerformed(evt);
		// }
		// });

		// jLabel_ThresholdSmoothing.setText("Umbral 128");
		//
		// jSlider_ThresholdSmoothing.setMaximum(255);
		// jSlider_ThresholdSmoothing.setValue(128);
		// jSlider_ThresholdSmoothing.addChangeListener(new
		// javax.swing.event.ChangeListener() {
		// public void stateChanged(javax.swing.event.ChangeEvent evt) {
		// jSlider_ThresholdSmoothingStateChanged(evt);
		// }
		// });

		jLabel_Range.setText("Rango 50");

		jSlider_Range.setMaximum(127);
		jSlider_Range.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSlider_RangeStateChanged(evt);
			}
		});

		// jButton22.setText("Contraste");
		// jButton22.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// jButton22ActionPerformed(evt);
		// }
		// });

		// jLabel_Contrast.setText("Contraste 0.2");
		// jLabel_Contrast.setToolTipText("");

		// jSlider_Contrast.setMinimum(-100);
		// jSlider_Contrast.setValue(20);
		// jSlider_Contrast.addChangeListener(new
		// javax.swing.event.ChangeListener() {
		// public void stateChanged(javax.swing.event.ChangeEvent evt) {
		// jSlider_ContrastStateChanged(evt);
		// }
		// });

		btnGaussianNoise.setText("Ruido gaussiano");
		btnGaussianNoise.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnGaussianNoiseActionPerformed(evt);
			}
		});
		// btnLaplace.setText("Filtro de Laplace");
		// btnLaplace.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnLaplaceActionPerformed(evt);
		// }
		// });
		btnLow.setText("Filtro de paso bajo");
		btnLow.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLowPassActionPerformed(evt);
			}
		});
		btnHigh.setText("Filtro de paso alto");
		btnHigh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnHighPassActionPerformed(evt);
			}
		});
		btnBand.setText("Filtro de paso banda");
		btnBand.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnBandPassActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout panelFiltrosAvanzadosLayout = new javax.swing.GroupLayout(panelFiltrosAvanzados);
		panelFiltrosAvanzados.setLayout(panelFiltrosAvanzadosLayout);
		panelFiltrosAvanzadosLayout.setHorizontalGroup(panelFiltrosAvanzadosLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelFiltrosAvanzadosLayout.createSequentialGroup().addContainerGap()
						.addGroup(panelFiltrosAvanzadosLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(btnLow, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnHigh, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnBand, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								// .addComponent(btnLaplace,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								.addComponent(jSlider_Threshold, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
								.addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(panelFiltrosAvanzadosLayout.createSequentialGroup()
										.addGroup(panelFiltrosAvanzadosLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jButton16, javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
												.addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(panelFiltrosAvanzadosLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jComboBox_BasicFilters, 0, 228, Short.MAX_VALUE)
												.addComponent(jComboBox_RGB, 0, 1, Short.MAX_VALUE)
												.addComponent(jComboBox_Invert, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)))
								.addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								// .addComponent(jButton19,
								// javax.swing.GroupLayout.Alignment.TRAILING,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								// .addGroup(panelFiltrosAvanzadosLayout.createSequentialGroup()
								// .addComponent(jLabel_ThresholdSmoothing)
								// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								// .addComponent(jLabel_Range))
								// .addGroup(panelFiltrosAvanzadosLayout.createSequentialGroup()
								// .addComponent(jSlider_ThresholdSmoothing,
								// javax.swing.GroupLayout.PREFERRED_SIZE, 400,
								// javax.swing.GroupLayout.PREFERRED_SIZE)
								// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								// .addComponent(jSlider_Range,
								// javax.swing.GroupLayout.PREFERRED_SIZE, 400,
								// javax.swing.GroupLayout.PREFERRED_SIZE))
								// .addComponent(jButton22,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// javax.swing.GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								.addComponent(btnGaussianNoise, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(panelFiltrosAvanzadosLayout.createSequentialGroup()
										.addGroup(panelFiltrosAvanzadosLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel_Threshold))
										.addGap(0, 0, Short.MAX_VALUE))
						// .addComponent(jSlider_Contrast,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// Short.MAX_VALUE)
						).addContainerGap()));
		panelFiltrosAvanzadosLayout
				.setVerticalGroup(
						panelFiltrosAvanzadosLayout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										panelFiltrosAvanzadosLayout.createSequentialGroup().addComponent(btnLow)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(btnHigh)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(btnBand)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												// .addComponent(btnLaplace)
												// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton14).addGap(4, 4, 4).addComponent(jButton15)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jLabel_Threshold).addGap(3, 3, 3)
												.addComponent(jSlider_Threshold, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addGroup(panelFiltrosAvanzadosLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jComboBox_Invert,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jButton16))
												.addGap(7, 7, 7)
												.addGroup(panelFiltrosAvanzadosLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jComboBox_BasicFilters,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jButton17))
												.addGap(7, 7, 7)
												.addGroup(
														panelFiltrosAvanzadosLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.BASELINE)
																.addComponent(jButton18).addComponent(jComboBox_RGB,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												// .addGap(18, 18,
												// 18).addComponent(jButton19).addGap(18,
												// 18, 18)
												// .addGroup(panelFiltrosAvanzadosLayout
												// .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												// .addComponent(jLabel_ThresholdSmoothing).addComponent(jLabel_Range))
												// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												// .addGroup(panelFiltrosAvanzadosLayout
												// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												// .addComponent(jSlider_ThresholdSmoothing,
												// javax.swing.GroupLayout.PREFERRED_SIZE,
												// 23,
												// javax.swing.GroupLayout.PREFERRED_SIZE)
												// .addComponent(jSlider_Range,
												// javax.swing.GroupLayout.PREFERRED_SIZE,
												// 23,
												// javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(18, 18, 18).addComponent(btnGaussianNoise)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												// .addGap(18, 18,
												// 18).addComponent(jButton22)
												// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												// .addComponent(jLabel_Contrast)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												// .addComponent(jSlider_Contrast,
												// javax.swing.GroupLayout.PREFERRED_SIZE,
												// javax.swing.GroupLayout.DEFAULT_SIZE,
												// javax.swing.GroupLayout.PREFERRED_SIZE)
												.addContainerGap(16, Short.MAX_VALUE)));

		panelHerramientas.addTab("Filtros Avanzados", panelFiltrosAvanzados);

		jButton20.setText("Mostrar actividad");
		jButton20.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton20ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout panelConsolaLayout = new javax.swing.GroupLayout(panelConsola);
		panelConsola.setLayout(panelConsolaLayout);
		panelConsolaLayout
				.setHorizontalGroup(panelConsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								panelConsolaLayout
										.createSequentialGroup().addContainerGap().addComponent(jButton20,
												javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
										.addContainerGap()));
		panelConsolaLayout
				.setVerticalGroup(panelConsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(panelConsolaLayout.createSequentialGroup().addContainerGap().addComponent(jButton20)
								.addContainerGap(405, Short.MAX_VALUE)));

		panelHerramientas.addTab("Consola", panelConsola);

		jButton21.setText("Mostrar historigrama");
		jButton21.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton21ActionPerformed(evt);
			}
		});

		jComboBoxHistogram.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "ROJO", "VERDE", "AZUL", "ALPHA", "ESCALA DE GRISES" }));

		javax.swing.GroupLayout jPanelHistogramLayout = new javax.swing.GroupLayout(jPanelHistogram);
		jPanelHistogram.setLayout(jPanelHistogramLayout);
		jPanelHistogramLayout.setHorizontalGroup(jPanelHistogramLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
		jPanelHistogramLayout.setVerticalGroup(jPanelHistogramLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 376, Short.MAX_VALUE));

		jLabel_MaxHistogram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		jLabel_MinHistogram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		javax.swing.GroupLayout panelHistorigramaLayout = new javax.swing.GroupLayout(panelHistorigrama);
		panelHistorigrama.setLayout(panelHistorigramaLayout);
		panelHistorigramaLayout.setHorizontalGroup(panelHistorigramaLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHistorigramaLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(panelHistorigramaLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel_MinHistogram, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanelHistogram, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING,
										panelHistorigramaLayout.createSequentialGroup()
												.addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, 250,
														Short.MAX_VALUE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jComboBoxHistogram, 0, 242, Short.MAX_VALUE))
								.addComponent(jLabel_MaxHistogram, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		panelHistorigramaLayout.setVerticalGroup(panelHistorigramaLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelHistorigramaLayout.createSequentialGroup().addContainerGap()
						.addGroup(panelHistorigramaLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton21)
								.addComponent(jComboBoxHistogram, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanelHistogram, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel_MinHistogram)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel_MaxHistogram).addContainerGap()));

		panelHerramientas.addTab("Historigrama", panelHistorigrama);

		jButton26.setText("Tamaño actual");
		jButton26.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton26ActionPerformed(evt);
			}
		});

		jTextField_CurrentSize.setHorizontalAlignment(javax.swing.JTextField.CENTER);

		jButton27.setText("Redimensionar");
		jButton27.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton27ActionPerformed(evt);
			}
		});

		jComboBox_ResizeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SCALE_AREA_AVERAGING",
				"SCALE_DEFAULT", "SCALE_FAST", "SCALE_REPLICATE", "SCALE_SMOOTH" }));

		jLabel3.setText("Ancho");

		jLabel4.setText("Alto");

		jButton23.setText("Porcentaje de redimensión");
		jButton23.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton23ActionPerformed(evt);
			}
		});

		jComboBox_ResizePercentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SCALE_AREA_AVERAGING",
				"SCALE_DEFAULT", "SCALE_FAST", "SCALE_REPLICATE", "SCALE_SMOOTH" }));

		jSlider_ResizeWidth.setMaximum(500);
		jSlider_ResizeWidth.setMinimum(1);
		jSlider_ResizeWidth.setValue(150);
		jSlider_ResizeWidth.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSlider_ResizeWidthStateChanged(evt);
			}
		});

		jSlider_ResizeHeight.setMaximum(500);
		jSlider_ResizeHeight.setMinimum(1);
		jSlider_ResizeHeight.setValue(150);
		jSlider_ResizeHeight.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSlider_ResizeHeightStateChanged(evt);
			}
		});

		jLabel5.setText("Porcentaje ancho");

		jLabel6.setText("Porcentaje alto");

		jFormatted_ResizeWidth.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
				new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
		jFormatted_ResizeWidth.setText("500");

		jFormatted_ResizeHeight.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
				new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
		jFormatted_ResizeHeight.setText("500");

		jFormatted_ResizeWidthPercent.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
				new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
		jFormatted_ResizeWidthPercent.setText("120");

		jFormatted_ResizeHeightPErcent.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
				new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
		jFormatted_ResizeHeightPErcent.setText("120");

		// javax.swing.GroupLayout panelRedimensionarLayout = new
		// javax.swing.GroupLayout(panelRedimensionar);
		// panelRedimensionar.setLayout(panelRedimensionarLayout);
		// panelRedimensionarLayout.setHorizontalGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addGroup(panelRedimensionarLayout.createSequentialGroup().addContainerGap()
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jSeparator5)
		// .addComponent(jSeparator4,
		// javax.swing.GroupLayout.Alignment.TRAILING)
		// .addGroup(panelRedimensionarLayout.createSequentialGroup()
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		// .addComponent(jSlider_ResizeWidth,
		// javax.swing.GroupLayout.Alignment.LEADING,
		// javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
		// .addComponent(jButton23, javax.swing.GroupLayout.Alignment.LEADING,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jButton27, javax.swing.GroupLayout.Alignment.LEADING,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jButton26, javax.swing.GroupLayout.Alignment.LEADING,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
		// panelRedimensionarLayout.createSequentialGroup()
		// .addComponent(jLabel5)
		// .addPreferredGap(
		// javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jFormatted_ResizeWidthPercent))
		// .addComponent(jFormatted_ResizeWidth,
		// javax.swing.GroupLayout.Alignment.LEADING))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jComboBox_ResizeType, 0,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jTextField_CurrentSize)
		// .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jComboBox_ResizePercentType, 0,
		// javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		// .addComponent(jSlider_ResizeHeight,
		// javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
		// .addGroup(panelRedimensionarLayout.createSequentialGroup()
		// .addComponent(jLabel6)
		// .addPreferredGap(
		// javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jFormatted_ResizeHeightPErcent))
		// .addComponent(jFormatted_ResizeHeight))))
		// .addContainerGap()));
		// panelRedimensionarLayout.setVerticalGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addGroup(panelRedimensionarLayout.createSequentialGroup().addContainerGap()
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		// .addComponent(jTextField_CurrentSize,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jButton26))
		// .addGap(13, 13, 13)
		// .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE,
		// 10,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton27)
		// .addComponent(jComboBox_ResizeType,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(
		// panelRedimensionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jLabel3).addComponent(jLabel4))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jFormatted_ResizeWidth,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jFormatted_ResizeHeight,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		// .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE,
		// 10,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jComboBox_ResizePercentType,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jButton23))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5)
		// .addComponent(jLabel6)
		// .addComponent(jFormatted_ResizeWidthPercent,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jFormatted_ResizeHeightPErcent,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(panelRedimensionarLayout
		// .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jSlider_ResizeWidth,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jSlider_ResizeHeight,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addContainerGap(189, Short.MAX_VALUE)));

		// panelHerramientas.addTab("Redimensionar", panelRedimensionar);

		jLabel2.setText("Sumar:");

		btnSumar.setText("Sumar");
		btnSumar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSumarActionPerformed(evt);
			}
		});

		jLabel7.setText("Restar:");

		btnRestar.setText("Restar");
		btnRestar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnRestarActionPerformed(evt);
			}
		});

		jLabel8.setText("Multiplicar:");

		jLabel9.setText("Dividir:");

		btnMultiplicar.setText("Multiplicar");
		btnMultiplicar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnMultiplicarActionPerformed(evt);
			}
		});

		btnDividir.setText("Dividir");
		btnDividir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDividirActionPerformed(evt);
			}
		});

		jLabel11.setForeground(new java.awt.Color(255, 0, 51));
		jLabel11.setText("IMPORTANTE: las operaciones solo admiten nº positivos.");

		jLabel10.setText("Exponencial:");

		btnExponenecial.setText("Exponencial");
		btnExponenecial.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnExponenecialActionPerformed(evt);
			}
		});

		lblLogaritmo.setText("Logaritmo:");

		btnLogaritmo.setText("Logaritmo");
		btnLogaritmo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLogaritmoActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout panelOperacionesLayout = new javax.swing.GroupLayout(panelOperaciones);
		panelOperaciones.setLayout(panelOperacionesLayout);
		panelOperacionesLayout
				.setHorizontalGroup(
						panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelOperacionesLayout.createSequentialGroup().addContainerGap().addGroup(
										panelOperacionesLayout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel11)
												.addGroup(panelOperacionesLayout
														.createSequentialGroup().addGap(0, 4, Short.MAX_VALUE).addGroup(
																panelOperacionesLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																		.addGroup(
																				panelOperacionesLayout
																						.createSequentialGroup()
																						.addComponent(jLabel10)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																						.addComponent(txtExponencial,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								40,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGroup(panelOperacionesLayout
																				.createSequentialGroup()
																				.addGroup(panelOperacionesLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(jLabel8)
																						.addComponent(jLabel9)
																						.addComponent(jLabel7)
																						.addComponent(jLabel2))
																				.addGap(20, 20, 20)
																				.addGroup(panelOperacionesLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(txtSumar,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								40,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(txtRestar,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								40,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(txtDividir,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								40,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(txtMultiplicar,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								40,
																								javax.swing.GroupLayout.PREFERRED_SIZE)))
																		.addGroup(panelOperacionesLayout
																				.createSequentialGroup()
																				.addComponent(lblLogaritmo)
																				.addGap(18, 18, 18)
																				.addComponent(txtLogaritmo)))
														.addGap(69, 69, 69)
														.addGroup(
																panelOperacionesLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(btnExponenecial,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(btnMultiplicar,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(btnRestar,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(btnSumar,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(btnDividir,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(btnLogaritmo,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))))
										.addContainerGap(594, Short.MAX_VALUE)));
		panelOperacionesLayout.setVerticalGroup(panelOperacionesLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelOperacionesLayout.createSequentialGroup().addContainerGap()
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2)
								.addComponent(txtSumar, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSumar))
						.addGap(18, 18, 18)
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel7).addGroup(panelOperacionesLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(txtRestar, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(btnRestar)))
						.addGap(18, 18, 18)
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelOperacionesLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel8).addComponent(txtMultiplicar,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(btnMultiplicar))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelOperacionesLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel9).addComponent(txtDividir,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(btnDividir))
						.addGap(18, 18, 18)
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelOperacionesLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel10)
										.addComponent(txtExponencial, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(btnExponenecial))
						.addGap(18, 18, 18)
						.addGroup(panelOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelOperacionesLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(lblLogaritmo).addComponent(txtLogaritmo,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(btnLogaritmo))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
						.addComponent(jLabel11).addGap(19, 19, 19)));

		panelHerramientas.addTab("Operaciones", panelOperaciones);

		jSplitPanel2_1.setRightComponent(panelHerramientas);

		jSplitPanel1.setTopComponent(jSplitPanel2_1);

		jTextArea_Log.setBackground(new java.awt.Color(0, 0, 0));
		jTextArea_Log.setColumns(20);
		jTextArea_Log.setForeground(new java.awt.Color(0, 255, 0));
		jTextArea_Log.setRows(5);
		jScrollPane2.setViewportView(jTextArea_Log);

		jButton2.setText("Ocultar");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton4.setText("Mostrar todo");

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel1.setText("Mostrar actividad");

		javax.swing.GroupLayout panelRegistroConsolaLayout = new javax.swing.GroupLayout(panelRegistroConsola);
		panelRegistroConsola.setLayout(panelRegistroConsolaLayout);
		panelRegistroConsolaLayout.setHorizontalGroup(panelRegistroConsolaLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(panelRegistroConsolaLayout.createSequentialGroup().addContainerGap()
						.addGroup(panelRegistroConsolaLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(panelRegistroConsolaLayout.createSequentialGroup().addComponent(jLabel1)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 634,
												Short.MAX_VALUE)
										.addComponent(jButton4)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton2))
								.addComponent(jScrollPane2))));
		panelRegistroConsolaLayout.setVerticalGroup(
				panelRegistroConsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING, panelRegistroConsolaLayout.createSequentialGroup()
								.addGroup(panelRegistroConsolaLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel1))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
								.addContainerGap()));

		jSplitPanel1.setRightComponent(panelRegistroConsola);

		jMenu1.setText("Archivo");
		menuHerramientas.add(jMenu1);

		jMenu2.setText("Editar");
		menuHerramientas.add(jMenu2);

		setJMenuBar(menuHerramientas);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPanel1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addComponent(jSplitPanel1,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void formWindowOpened(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowOpened
		formInitialize();
	}// GEN-LAST:event_formWindowOpened

	private void formWindowStateChanged(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowStateChanged

	}// GEN-LAST:event_formWindowStateChanged

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		showActivity(true);
	}// GEN-LAST:event_jButton3ActionPerformed

	private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton12ActionPerformed
		saveImage();
	}// GEN-LAST:event_jButton12ActionPerformed

	private void JTextfield_URLMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_JTextfield_URLMouseClicked
		DeleteText(JTextfield_URL);
	}// GEN-LAST:event_JTextfield_URLMouseClicked

	private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton11ActionPerformed
		openURL();
	}// GEN-LAST:event_jButton11ActionPerformed

	private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton10ActionPerformed
		openImage();
	}// GEN-LAST:event_jButton10ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		showActivity(false);
	}// GEN-LAST:event_jButton2ActionPerformed

	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
		undoImage();
	}// GEN-LAST:event_jButton7ActionPerformed

	private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton8ActionPerformed
		redoImage();
	}// GEN-LAST:event_jButton8ActionPerformed

	private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton9ActionPerformed
		lastOpenedImage();
	}// GEN-LAST:event_jButton9ActionPerformed

	private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton13ActionPerformed
		deleteAllImages();
	}// GEN-LAST:event_jButton13ActionPerformed

	private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton14ActionPerformed
		grayScale();
	}// GEN-LAST:event_jButton14ActionPerformed

	private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton15ActionPerformed
		blackWhite();
	}// GEN-LAST:event_jButton15ActionPerformed

	private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton16ActionPerformed
		invertColors();
	}// GEN-LAST:event_jButton16ActionPerformed

	private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton17ActionPerformed
		basicFilters();
	}// GEN-LAST:event_jButton17ActionPerformed

	private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton18ActionPerformed
		RGBto();
	}// GEN-LAST:event_jButton18ActionPerformed

	// private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//
	// GEN-FIRST:event_jButton19ActionPerformed
	// blackWhiteSmoothing();
	// }// GEN-LAST:event_jButton19ActionPerformed

	// private void
	// jSlider_ThresholdSmoothingStateChanged(javax.swing.event.ChangeEvent evt)
	// {// GEN-FIRST:event_jSlider_ThresholdSmoothingStateChanged
	// Events.changeLabel(jLabel_ThresholdSmoothing,
	// "Threshold " + String.valueOf(jSlider_ThresholdSmoothing.getValue()));
	// }// GEN-LAST:event_jSlider_ThresholdSmoothingStateChanged

	private void jSlider_RangeStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSlider_RangeStateChanged
		Events.changeLabel(jLabel_Range, "Range " + String.valueOf(jSlider_Range.getValue()));
	}// GEN-LAST:event_jSlider_RangeStateChanged

	private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton20ActionPerformed
		showActivity(true);
	}// GEN-LAST:event_jButton20ActionPerformed

	private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton21ActionPerformed
		showHistogram();
	}// GEN-LAST:event_jButton21ActionPerformed

	private void jSlider_ThresholdStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSlider_ThresholdStateChanged
		Events.changeLabel(jLabel_Threshold, "Threshold " + String.valueOf(jSlider_Threshold.getValue()));
	}// GEN-LAST:event_jSlider_ThresholdStateChanged

	// private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//
	// GEN-FIRST:event_jButton22ActionPerformed
	// contrastModificate();
	// }// GEN-LAST:event_jButton22ActionPerformed

	// private void jSlider_ContrastStateChanged(javax.swing.event.ChangeEvent
	// evt) {// GEN-FIRST:event_jSlider_ContrastStateChanged
	// double valueContrast = (double) ((double)
	// this.jSlider_Contrast.getValue() / 100);
	// Events.changeLabel(jLabel_Contrast, "Contrast " +
	// String.valueOf(valueContrast));
	// }// GEN-LAST:event_jSlider_ContrastStateChanged

	private void btnGaussianNoiseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton22ActionPerformed
		gaussianNoise();
	}// GEN-LAST:event_jButton22ActionPerformed

	private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton26ActionPerformed
		currentSize();
	}// GEN-LAST:event_jButton26ActionPerformed

	private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton27ActionPerformed
		resizeImage();
	}// GEN-LAST:event_jButton27ActionPerformed

	private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton23ActionPerformed
		resizePercentImage();
	}// GEN-LAST:event_jButton23ActionPerformed

	private void jSlider_ResizeWidthStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSlider_ResizeWidthStateChanged
		Events.changeTextField(jFormatted_ResizeWidthPercent, String.valueOf(jSlider_ResizeWidth.getValue()));
	}// GEN-LAST:event_jSlider_ResizeWidthStateChanged

	private void jSlider_ResizeHeightStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSlider_ResizeHeightStateChanged
		Events.changeTextField(jFormatted_ResizeHeightPErcent, String.valueOf(jSlider_ResizeHeight.getValue()));
	}// GEN-LAST:event_jSlider_ResizeHeightStateChanged

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón sumar.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnSumarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSumarActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		int valorSumar = Integer.parseInt(txtSumar.getText());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.sumar(bufferImageTemp, valorSumar)));
	}// GEN-LAST:event_btnSumarActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * Multiplicar.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnMultiplicarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnMultiplicarActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		double valorMultiplicar = (double) Double.parseDouble(txtMultiplicar.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjAdvancedFilters.multiplicar(bufferImageTemp, valorMultiplicar)));
	}// GEN-LAST:event_btnMultiplicarActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * restar.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnRestarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRestarActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		int valorRestar = Integer.parseInt(txtRestar.getText());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.restar(bufferImageTemp, valorRestar)));
	}// GEN-LAST:event_btnRestarActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * dividir.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnDividirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDividirActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		double valorDividir = (double) Double.parseDouble(txtDividir.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjAdvancedFilters.dividir(bufferImageTemp, valorDividir)));
	}// GEN-LAST:event_btnDividirActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * Exponencial.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnExponenecialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnExponenecialActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		double valorExponencial = (double) Double.parseDouble(txtExponencial.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjAdvancedFilters.exponencial(bufferImageTemp, valorExponencial)));
	}// GEN-LAST:event_btnExponenecialActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * Logaritmo.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnLogaritmoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLogaritmoActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		int valorLogaritmo = Integer.parseInt(txtLogaritmo.getText());
		this.jLabelImage.setIcon(ObjTransformFormats
				.bufferedImageToImageIcon(ObjAdvancedFilters.logaritmo(bufferImageTemp, valorLogaritmo)));
	}// GEN-LAST:event_btnLogaritmoActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón
	 * Laplace.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnLaplaceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLaplaceActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.filtroLaplace(bufferImageTemp)));
	}// GEN-LAST:event_btnLaplaceActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón Low
	 * Paso.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnLowPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLowPassActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.filtroLowPass(bufferImageTemp)));
	}// GEN-LAST:event_btnLowPassActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón High
	 * Paso.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnHighPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHighPassActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.filtroHighPass(bufferImageTemp)));
	}// GEN-LAST:event_btnHighPassActionPerformed

	/**
	 * Método para gestionar el evento que se genera al pulsar el botón Band
	 * Paso.
	 * 
	 * @param evt
	 *            Evento generado.
	 */
	private void btnBandPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBandPassActionPerformed
		this.bufferImageTemp = ObjTransformFormats.iconToBufferedImage(this.jLabelImage.getIcon());
		this.jLabelImage.setIcon(
				ObjTransformFormats.bufferedImageToImageIcon(ObjAdvancedFilters.filtroBandPass(bufferImageTemp)));
	}// GEN-LAST:event_btnBandPassActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			new MainForm().setVisible(true);
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTextField JTexfield_lastOpened;
	private javax.swing.JTextField JTextfield_URL;
	private javax.swing.JButton btnDividir;
	private javax.swing.JButton btnExponenecial;
	// private javax.swing.JButton btnLaplace;
	private javax.swing.JButton btnLow;
	private javax.swing.JButton btnHigh;
	private javax.swing.JButton btnBand;
	private javax.swing.JButton btnLogaritmo;
	private javax.swing.JButton btnMultiplicar;
	private javax.swing.JButton btnRestar;
	private javax.swing.JButton btnSumar;
	private javax.swing.JButton jButton10;
	private javax.swing.JButton jButton11;
	private javax.swing.JButton jButton12;
	private javax.swing.JButton jButton13;
	private javax.swing.JButton jButton14;
	private javax.swing.JButton jButton15;
	private javax.swing.JButton jButton16;
	private javax.swing.JButton jButton17;
	private javax.swing.JButton jButton18;
	// private javax.swing.JButton jButton19;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton20;
	private javax.swing.JButton jButton21;
	// private javax.swing.JButton jButton22;
	private javax.swing.JButton btnGaussianNoise;
	private javax.swing.JButton jButton23;
	private javax.swing.JButton jButton26;
	private javax.swing.JButton jButton27;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton7;
	private javax.swing.JButton jButton8;
	private javax.swing.JButton jButton9;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBoxHistogram;
	private javax.swing.JComboBox jComboBox_BasicFilters;
	private javax.swing.JComboBox jComboBox_Invert;
	private javax.swing.JComboBox jComboBox_RGB;
	private javax.swing.JComboBox jComboBox_ResizePercentType;
	private javax.swing.JComboBox jComboBox_ResizeType;
	private javax.swing.JFormattedTextField jFormatted_ResizeHeight;
	private javax.swing.JFormattedTextField jFormatted_ResizeHeightPErcent;
	private javax.swing.JFormattedTextField jFormatted_ResizeWidth;
	private javax.swing.JFormattedTextField jFormatted_ResizeWidthPercent;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabelImage;
	// private javax.swing.JLabel jLabel_Contrast;
	private javax.swing.JLabel jLabel_MaxHistogram;
	private javax.swing.JLabel jLabel_MinHistogram;
	private javax.swing.JLabel jLabel_Range;
	private javax.swing.JLabel jLabel_Threshold;
	// private javax.swing.JLabel jLabel_ThresholdSmoothing;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JPanel jPanelHistogram;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JSeparator jSeparator5;
	// private javax.swing.JSlider jSlider_Contrast;
	private javax.swing.JSlider jSlider_Range;
	private javax.swing.JSlider jSlider_ResizeHeight;
	private javax.swing.JSlider jSlider_ResizeWidth;
	private javax.swing.JSlider jSlider_Threshold;
	// private javax.swing.JSlider jSlider_ThresholdSmoothing;
	private javax.swing.JSplitPane jSplitPanel1;
	private javax.swing.JSplitPane jSplitPanel2_1;
	private javax.swing.JTextArea jTextArea_Log;
	private javax.swing.JTextField jTextField_CurrentSize;
	private javax.swing.JLabel lblLogaritmo;
	private javax.swing.JMenuBar menuHerramientas;
	private javax.swing.JPanel panelAbrirGuardarImagen;
	private javax.swing.JPanel panelConsola;
	private javax.swing.JPanel panelFiltrosAvanzados;
	private javax.swing.JTabbedPane panelHerramientas;
	private javax.swing.JPanel panelHistorigrama;
	private javax.swing.JPanel panelOperaciones;
	private javax.swing.JPanel panelPrincipal;
	// private javax.swing.JPanel panelRedimensionar;
	private javax.swing.JPanel panelRegistroConsola;
	private javax.swing.JTextField txtDividir;
	private javax.swing.JTextField txtExponencial;
	private javax.swing.JTextField txtLogaritmo;
	private javax.swing.JTextField txtMultiplicar;
	private javax.swing.JTextField txtRestar;
	private javax.swing.JTextField txtSumar;
	// End of variables declaration//GEN-END:variables
}
