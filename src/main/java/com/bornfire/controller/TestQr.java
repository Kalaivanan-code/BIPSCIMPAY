package com.bornfire.controller;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class TestQr {

	 public String getQrInfo() throws Exception {
	 String url = "https://news.bbc.co.uk";

     int imageSize = 200;
     BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
         imageSize, imageSize);
     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     MatrixToImageWriter.writeToStream(matrix, "png", bos);
     String image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode

     return image;
}
}
