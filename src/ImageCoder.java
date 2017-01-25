
public class ImageCoder {
	private int[] pixels;
	private int[] pixelsNew;
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

	private int[] calcA() {
		pixelsNew = new int[pixels.length];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = y * width + (x - 1);

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA < 0) {
					gNew = 128;
				} else {
					int argbA = pixels[posA];
					int gA = (argbA) & 0xff;
					gNew = 128 + (gX - gA);

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				}

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
				
			}
		}
		return pixelsNew;

	}
	
	private int[] calcB() {
		pixelsNew = new int[pixels.length];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = (y-1) * width + x ;

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA < 0) {
					gNew = 128;
				} else {
					int argbA = pixels[posA];
					int gA = (argbA) & 0xff;
					gNew = 128 + (gX - gA);

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				}

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
				
			}
		}
		return pixelsNew;
	}

	private int[] calcC() {
		pixelsNew = new int[pixels.length];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = (y-1) * width + (x-1) ;

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA < 0) {
					gNew = 128;
				} else {
					int argbA = pixels[posA];
					int gA = (argbA) & 0xff;
					gNew = 128 + (gX - gA);

					if (gNew > 255) {
						gNew = 255;
					}
					if (gNew < 0) {
						gNew = 0;
					}
				}

				pixelsNew[posX] = (0xFF << 24) | (gNew << 16) | (gNew << 8) | gNew;
			}
		}
		return pixelsNew;
	}
	
	private void calcABC() {
		int[] pixelsA = calcA();
		int[] pixelsB = calcB();
		int[] pixelsC = calcC();
		
		for (int i=0; i<pixelsA.length; i++)
		{
			pixelsNew[i]= pixelsA[i] + pixelsB[i] - pixelsC[i];
		}

	}
	
	private void calcAdaptiv() {
		int[] pixelsA = calcA();
		int[] pixelsB = calcB();
		int[] pixelsC = calcC();
		
		for (int i=0; i<pixelsA.length; i++)
		{
			if (Math.abs(pixelsA[i]-pixelsC[i])<(Math.abs(pixelsB[i]-pixelsC[i])))
			{
				pixelsNew[i]= pixelsB[i];
			}
			else
			{
				pixelsNew[i]= pixelsA[i];
			}
		}

	
	}
	
	private void calcAB() {
		int[] pixelsA = calcA();
		int[] pixelsB = calcB();
		
		for (int i=0; i<pixelsA.length; i++)
		{
			pixelsNew[i]= ((pixelsA[i] + pixelsB[i]) /2);
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
	
	
	
}
