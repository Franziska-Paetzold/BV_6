
public class ImageDecoder {
	private int[] pixels;
	private int[] pixelsNew;
	private int width;
	private int height;

	public ImageDecoder(int[] pixels, String praediktor, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		switch (praediktor) {
		case "A (horizontal)":
			calcA();
			break;
		// case "B (vertikal)":
		// calcB();
		// break;
		// case "C (diagonal)":
		// calcC();
		// break;
		// case "A+B-C":
		// calcABC();
		// break;
		// case "(A+B)/2":
		// calcAB();
		// break;
		// case "adaptiv":
		// calcAdaptiv();
		// break;
		default:
			System.out.println("Error");
			break;
		}
	}

	private void calcA() {
		pixelsNew = new int[pixels.length];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int posX = y * width + x;
				int posA = y * width + (x - 1);

				int argbX = pixels[posX];
				int gX = (argbX) & 0xff;

				int gNew;

				if (posA <0 ) {
					gNew = 128;
				} else {
					int argbA = pixelsNew[posA];
					int gA = (argbA) & 0xff;
					gNew = (gX -128) + (gA-128);

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
		
	}

	public int[] getPixelsNew() {
		return pixelsNew;
	}
}