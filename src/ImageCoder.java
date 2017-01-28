
public class ImageCoder {
	private int[] pixels;
	private int[] pixelsNew;
	private int[] errors = null;
	

	private int width;
	private int height;


	public ImageCoder(int[] pixels, String praediktor, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		switch (praediktor) {
		case "A (horizontal)":
			calcA();
			break;
		case "B (vertikal)":
			calcB();
			break;
		case "C (diagonal)":
			calcC();
			break;
		case "A+B-C":
			calcABC();
			break;
		case "(A+B)/2":
			calcAB();
			break;
		case "adaptiv":
			calcAdaptiv();
			break;
		default:
			System.out.println("Error");
			break;
		}
	}

	private int calcA() {
		pixelsNew = new int[pixels.length];
		
		errors = new int[pixels.length];

		int gA = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = y * width + (x - 1);

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA < 0) {
					gA = 128;
				} else {
					int argbA = pixels[posA];
					 gA = (argbA) & 0xff;
				}
					int error = (gX - gA);
					
					gNew = error + 128;

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
			    errors[posX] = error;
				
			}
		}
		return gA;

	}
	
	private int calcB() {
		pixelsNew = new int[pixels.length];

		errors = new int[pixels.length];
		

		int gA = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = (y-1) * width + x ;

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;
				if (posA < 0) {
					gA = 128;
				} else {
					int argbA = pixels[posA];
					 gA = (argbA) & 0xff;
				}
					int error = (gX - gA);
					
					gNew = error + 128;

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
			    errors[posX] = error;
				
			}
		}
		return gA;
	}

	private int calcC() {
		pixelsNew = new int[pixels.length];

		errors = new int[pixels.length];


		int gA = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = (y-1) * width + (x-1) ;

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA < 0) {
					gA = 128;
				} else {
					int argbA = pixels[posA];
					 gA = (argbA) & 0xff;
				}
					int error = (gX - gA);
					
					gNew = error + 128;

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
			    errors[posX] = error;
			}
		}
		return gA;
	}
	
	private int[] abc(int i){
		int[] res = {128,128,128};
		
		int posA = i - 1;
		if (posA >=0) res[0] = pixels[posA];
		
		int posB = i - width;
		if (posB >= 0) res[1] = pixels[posB];
		
		int posC = i -width -1;
		if (posC >= 0) res[2] = pixels[posC];
		
		return res;
	}
	
	private void calcABC() {
		pixelsNew = new int[pixels.length];
		errors = new int[pixels.length];

		
		for (int i=0; i<pixels.length; i++)
		{
			int pixelsA = abc(i)[0];
			int pixelsB = abc(i)[1];
			int pixelsC = abc(i)[2];
			
			int error= (pixels[i] & 255) - ((pixelsA & 255) + (pixelsB & 255) - (pixelsC & 255));
			errors[i] = error;
			
			error = error  + 128;
			if (error < 0) error = 0;
			if (error > 255) error = 255;
			pixelsNew[i] = (0XFF << 24) | (error << 16) | (error << 8) | error;
		}

	}
	
	private void calcAdaptiv() {
		int pixelsA = calcA();
		int pixelsB = calcB();
		int pixelsC = calcC();
		
		for (int i=0; i<pixels.length; i++)
		{
			if (Math.abs(pixelsA-pixelsC)<(Math.abs(pixelsB-pixelsC)))
			{
				pixelsNew[i]= pixelsB;
			}
			else
			{
				pixelsNew[i]= pixelsA;
			}
		}

	
	}
	
	private void calcAB() {
		int pixelsA = calcA();
		int pixelsB = calcB();
		
		for (int i=0; i<pixels.length; i++)
		{
			//Differenz (Error) berechnen
			// pixelsA
			// pixelsB übernehmen aus input
			
			pixelsNew[i]= ((pixelsA + pixelsB) /2);
			int argb = pixelsNew[i];
			int g = (argb) & 0xff;

				if (g> 255) {
					g = 255;
				}
				if (g < 0) {
					g = 0;
				}

			pixelsNew[i] = (0xFF << 24) | (g << 16) | (g << 8) | g;
			
		}

	
	}
	
	public int[] getPixelsNew() {
		return pixelsNew;
	}
	
	public int[] getErrors() {
		return errors;
	}
	
	
}
