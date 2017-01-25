import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DPCM extends JPanel{
	private static JFrame frame;
	private static final long serialVersionUID = 1L;
	private static final String author = "Lai, Pätzold";		// TODO: type in your name here
	private static final String initialFilename = "test1.jpg";
	private static final File openPath = new File(".");
	private static final int border = 10;
	private static final int maxWidth = 910; 
	private static final int maxHeight = 910; 
	private static final int graySteps = 256;
	private JLabel entropieInput;
	private JLabel entropiePraedikation;
	private JLabel entropieReconstruction;
	
	private ImageView imgViewInput;	
	private ImageView imgViewPraedikation;
	private ImageView imgViewReconstruction;	
	
	// TODO: add an array that holds the ARGB-Pixels of the originally loaded image
		public int[] argbOrigPixels;
	
	public DPCM() {
        super(new BorderLayout(border, border));
        
        // load the default image
        File input = new File(initialFilename);
        
        if(!input.canRead()) input = openFile(); // file not found, choose another image
        
        imgViewInput = new ImageView(input);
        imgViewInput.setMaxSize(new Dimension(maxWidth, maxHeight));
        imgViewInput.makeGray();
        imgViewPraedikation = new ImageView(imgViewInput.getImgWidth(), imgViewInput.getImgHeight());
        imgViewReconstruction = new ImageView(imgViewInput.getImgWidth(), imgViewInput.getImgHeight());
        
        
        
        // TODO: initialize the original ARGB-Pixel array from the loaded image
        argbOrigPixels = imgViewInput.getPixels();
        
        entropieInput = new JLabel("Entropie = " );
        entropieReconstruction = new JLabel("Entropie = ");
        entropiePraedikation = new JLabel("Entropie = " );
        
        JComboBox<String> box = new JComboBox<String>();
        box.addItem("A (horizontal)");
        box.addItem("B (vertikal)");
        box.addItem("C (diagonal)");
        box.addItem("A+B-C");
        box.addItem("(A+B)/2");
        box.addItem("adaptiv");
        box.setSelectedIndex(0);
        box.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ImageCoder coder = new ImageCoder(argbOrigPixels, box.getItemAt(box.getSelectedIndex()), imgViewInput.getImgWidth(), imgViewInput.getImgHeight());
    			imgViewPraedikation.setPixels(coder.getPixelsNew());
    			imgViewPraedikation.applyChanges();
    			ImageDecoder decoder = new ImageDecoder(imgViewPraedikation.getPixels(),box.getItemAt(box.getSelectedIndex()), imgViewPraedikation.getImgWidth(), imgViewPraedikation.getImgHeight());
    			imgViewReconstruction.setPixels(decoder.getPixelsNew());
    			imgViewReconstruction.applyChanges();
    			refreshEntropie();
        	}
        });
        
    	// load image button
        JButton load = new JButton("Open Image");
        load.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		File input = openFile();
        		if(input != null) {
        			imgViewInput.loadImage(input);
        			imgViewInput.setMaxSize(new Dimension(maxWidth, maxHeight));
        			imgViewInput.makeGray();
        			
        			
        	        // TODO: initialize the original ARGB-Pixel array from the newly loaded image
        	        argbOrigPixels = imgViewInput.getPixels();
        	        
        	        ImageCoder coder = new ImageCoder(argbOrigPixels, box.getItemAt(box.getSelectedIndex()), imgViewInput.getImgWidth(), imgViewInput.getImgHeight());
        			imgViewPraedikation.setPixels(coder.getPixelsNew());
        			imgViewPraedikation.applyChanges();
        			ImageDecoder decoder = new ImageDecoder(imgViewPraedikation.getPixels(),box.getItemAt(box.getSelectedIndex()), imgViewPraedikation.getImgWidth(), imgViewPraedikation.getImgHeight());
        			imgViewReconstruction.setPixels(decoder.getPixelsNew());
        			imgViewReconstruction.applyChanges();
        			refreshEntropie();
        			
        			frame.pack();
        		}
        	}        	
        });

        ImageCoder coder = new ImageCoder(argbOrigPixels, box.getItemAt(box.getSelectedIndex()), imgViewInput.getImgWidth(), imgViewInput.getImgHeight());
		imgViewPraedikation.setPixels(coder.getPixelsNew());
		imgViewPraedikation.applyChanges();
		ImageDecoder decoder = new ImageDecoder(imgViewPraedikation.getPixels(),box.getItemAt(box.getSelectedIndex()), imgViewPraedikation.getImgWidth(), imgViewPraedikation.getImgHeight());
		imgViewReconstruction.setPixels(decoder.getPixelsNew());
		imgViewReconstruction.applyChanges();
		refreshEntropie();
        
        JPanel borderInput = new JPanel();
        borderInput.setLayout(new BoxLayout(borderInput, BoxLayout.Y_AXIS));
        TitledBorder titBorderInput = BorderFactory.createTitledBorder("Input");
        borderInput.setBorder(titBorderInput);
        borderInput.add(imgViewInput);
        borderInput.add(entropieInput);
        
        
        JPanel borderPraedikation = new JPanel();
        borderPraedikation.setLayout(new BoxLayout(borderPraedikation, BoxLayout.Y_AXIS));
        TitledBorder titBorderPraedikation = BorderFactory.createTitledBorder("Praedikation");
        borderPraedikation.setBorder(titBorderPraedikation);
        borderPraedikation.add(imgViewPraedikation);
        borderPraedikation.add(entropiePraedikation);
        
        JPanel borderReconstruction = new JPanel();
        borderReconstruction.setLayout(new BoxLayout(borderReconstruction, BoxLayout.Y_AXIS));
        TitledBorder titBorderReconstruction = BorderFactory.createTitledBorder("Reconstruction");
        borderReconstruction.setBorder(titBorderReconstruction);
        borderReconstruction.add(imgViewReconstruction);
        borderReconstruction.add(entropieReconstruction);
         
        // add to main panel
       add(load, BorderLayout.SOUTH);
       add(box, BorderLayout.NORTH);
       add(borderInput, BorderLayout.WEST);
       add(borderPraedikation, BorderLayout.CENTER);
       add(borderReconstruction, BorderLayout.EAST);
       
     
               
        // add border to main panel
        setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
        
        
	}
	
	
	private File openFile() {
		// file open dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(openPath);
        int ret = chooser.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;		 
	}


	public static void main(String[] args) {
        // schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

	private static void createAndShowGUI() {
		// create and setup the window
		frame = new JFrame("DPCM - " + "Lai, Paetzold");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComponent contentPane = new DPCM(); 
        contentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(contentPane);

        // display the window
        frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);
	}
	
	private void refreshEntropie(){
        entropieInput.setText("Entropie = " + calcEntropie(imgViewInput.getPixels()));
        entropiePraedikation.setText("Entropie = " + calcEntropie(imgViewPraedikation.getPixels()));
        entropieReconstruction.setText("Entropie = " + calcEntropie(imgViewReconstruction.getPixels()));
	}

	
	private double calcEntropie(int[] pixels)
	{
		int[] histogram = new int[256];
		//set histogram
		for (int j=0; j<pixels.length; j++)
		{
			int argb = pixels[j];
			int g = argb & 0xFF;

			histogram[g]++;
		}
		
		int pixelsSum = 0;
		for(int i=0; i<histogram.length; i++) {
			pixelsSum += histogram[i];
		}
		
		//set entropy
		double entropie=0;
		for(int i=0; i < histogram.length; i++) {
					
					double p = (histogram[i] / (double) pixelsSum);
					if (p > 0){
					entropie += p * (Math.log(p) / Math.log(2.0));
					System.out.println(entropie);
					}
				}

				entropie= -entropie;
				return entropie;
	}

	

}

