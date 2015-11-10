package com.wang.qr.code;

import org.junit.Test;

import com.wang.qr.code.utils.QRCodeTestUtil3;

/**
 * @author wang
 * 
 */
public class QRCodeTest3 {

	@Test
	public void createNoLogoQRCode() {
		QRCodeTestUtil3.createTwoDimensionalCode("E://test.txt", "png", 200, 200);
	}

	@Test
	public void createHaveLogoQRCode() {
		try {
			QRCodeTestUtil3.get2CodeImage("http://hejiawangjava.iteye.com/", "E://havalogoQRCode.png", "E://logo.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
