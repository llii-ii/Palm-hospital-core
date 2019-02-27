package com.kasite.core.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coreframework.util.StringUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.kasite.core.common.config.KasiteConfig;

/**
 * 二维码图片相关工具类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:34:01
 */
public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private MatrixToImageWriter() {
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
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
	
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}

	/**
	 * 生成二维码图片
	 * 
	 * @param qrUrl
	 *            二维码地址
	 * @param uuId
	 *            二维码图片名
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static String CreateQRCodePicture(String qrUrl, String uuId) throws WriterException, IOException, ParseException {
		/** 二维码图片参数 */
		StringBuffer qrFilePath = KasiteConfig.getQrFileUrl();
		String qrPicUrl = null;
		/** 存在路径，则生成二维码 */
		if (!StringUtil.isBlank(qrFilePath)) {
			int width = KasiteConfig.getQrFileWidth().intValue();
			if (width < 0) {
				width = 400;
			}
			int height = KasiteConfig.getQrFileHeigth().intValue();
			if (height < 0) {
				height = 400;
			}
			String qrFileSuffix = KasiteConfig.getQrFileSuffix();
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			Map hints = new HashMap(16);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE,
					KasiteConfig.getQrFileWidth(), KasiteConfig.getQrFileHeigth(), hints);
			File qrFile = new File(qrFilePath.toString(), uuId + "." + KasiteConfig.getQrFileSuffix());
			qrPicUrl = qrFile.getAbsolutePath();
			writeToFile(bitMatrix, qrFileSuffix, qrFile);
		}
		return qrPicUrl;
	}
	
	/**
	 * 创建企业二维码
	 * @param uid
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static String CreateQyQRCode(String uid, String URL) throws Exception {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		String content = URL;
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(content,BarcodeFormat.QR_CODE, 400, 400, hints);
		// 创建二维码本地存放根路径
		StringBuffer imagePath = KasiteConfig.getLocalConfigPathByName("uploadFile", true, true, true);
		String imageLocalPath = imagePath.append("/qyQRCode").toString();
		File file = new File(imageLocalPath);
		if(!file.exists()) {
			file.mkdirs();
		} 
		File file1 = new File(file.getAbsolutePath(), uid + ".jpg");
		MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		System.out.println(file1.getAbsolutePath());
		String imagePathUrl = file1.getAbsolutePath().substring(file1.getAbsolutePath().indexOf("uploadFile"));
		return imagePathUrl;
	}
}
