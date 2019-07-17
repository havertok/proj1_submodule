package com.revature.proj1.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {

	public static void main(String[] args) {
		File testImage = new File("images/reciept.png");
		try {
			BufferedImage image = ImageIO.read(testImage);
			ImageIO.write(image, "png", new File("imageTarget/recieptCopy.png"));
			ImageIO.write(image, "jpg", new File("imageTarget/recieptCopy.jpg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
