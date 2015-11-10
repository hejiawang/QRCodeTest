package com.wang.qr.code.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成普通二维码和生成带logo的二维码
 * 
 * @author wang
 * 
 */
public class QRCodeTestUtil3 {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 生成普通没有logo的二维码
	 * 
	 * @param filePath
	 *            文本文件路径,该文件中的内容是生成二维码图片中的内容(文本需以UTF-8)
	 * @param imgFormate
	 *            指定生成的二维码图片的后缀名
	 * @param width
	 *            指定生成的二维码图片的宽度
	 * @param height
	 *            指定生成的二维码图片的高度
	 */
	public static void createTwoDimensionalCode(String filePath, String imgFormate, int width, int height) {
		File file = new File(filePath);
		StringBuffer contents = new StringBuffer("");
		// 读取文本文件内容到 contents
		try {
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			try {
				String temp = br.readLine();
				while (temp != null) {
					contents.append(temp + "\r\n");
					temp = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 得到图片该存放路径
		String imgPath = file.getParent();
		// 得到文件该设置的名字
		String imgName = file.getName();
		int endIndex = imgName.lastIndexOf(".");
		imgName = imgName.substring(0, endIndex);

		File imageFile = new File(imgPath, imgName + "." + imgFormate);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents.toString(), BarcodeFormat.QR_CODE, width,
					height, hints);
			MatrixToImageWriter.writeToPath(bitMatrix, imgFormate, imageFile.toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成带logo的二维码图片
	 * 
	 * @param url
	 *            要生成二维码的url
	 * @param imgPath
	 *            二维码生成的绝对路径
	 * @param logoPath
	 *            二维码中间logo绝对地址
	 * @throws Exception
	 */
	public static void get2CodeImage(String url, String imgPath, String logoPath) throws Exception {
		String format = "png";
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);
		File qrcodeFile = new File(imgPath);
		writeToFile(bitMatrix, format, qrcodeFile, logoPath);
	}

	/**
	 * 
	 * @param matrix
	 *            二维码矩阵相关
	 * @param format
	 *            二维码图片格式
	 * @param file
	 *            二维码图片文件
	 * @param logoPath
	 *            logo路径
	 * @throws IOException
	 */
	private static void writeToFile(BitMatrix matrix, String format, File file, String logoPath) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		Graphics2D gs = image.createGraphics();

		// 载入logo
		Image img = ImageIO.read(new File(logoPath));
		gs.drawImage(img, 125, 125, null);
		gs.dispose();
		img.flush();
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
