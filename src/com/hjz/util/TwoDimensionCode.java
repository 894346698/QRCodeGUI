package com.hjz.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.hjz.util.AES;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

public class TwoDimensionCode {
	// 二维码 SIZE
	private static final int CODE_IMG_SIZE = 500;//
	// LOGO SIZE (在二维码中间插入图片，选择在最中间插入，而且长宽建议为整个二维码的1/7至1/4)
	private static final int INSERT_IMG_SIZE = CODE_IMG_SIZE / 5;

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content       存储内容
	 * @param imgPath       二维码图片存储路径
	 * @param imgType       图片类型
	 * @param insertImgPath logo图片路径
	 */
	public void encoderQRCode(String content, String imgPath, String imgType, String insertImgPath) {
		try {
			//秘钥
			String password = "1245678";
			byte[] encode = AES.encrypt(content, password);
			// 传输过程,转成16进制的字符串
			String code = AES.parseByte2HexStr(encode);
			//生成二维码
			BufferedImage bufImg = this.qRCodeCommon(code, imgType, insertImgPath);
			File imgFile = new File(imgPath);
			if (!imgFile.exists()) {
				imgFile.mkdirs();
			}
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, imgType, imgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * @param content 存储内容
	 * @param imgType  输出流
	 * @param imgPath 图片路径
	 * @return
	 */
	private BufferedImage qRCodeCommon(String content, String imgType, String imgPath) {
		BufferedImage bufImg = null;
		try {
 
			
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(13);
			// 获得内容的字节数组，设置编码格式
			byte[] contentBytes = content.getBytes("utf-8");
			// 图片尺寸
			int imgSize = CODE_IMG_SIZE;
			bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// 设置背景颜色
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);

			// 设定图像颜色> BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量，不设置可能导致解析出错
			final int pixoff = 2;
			final int sz = 7;
			// 输出内容> 二维码

			if (contentBytes.length > 0) {
				System.out.println(contentBytes.length);
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * sz + pixoff, i * sz + pixoff, sz, sz);
						}
					}
				}
			} else {
				throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
			}
			// Ƕ��logo
			if (imgPath != null)
				this.insertImage(bufImg, imgPath, true);
			gs.dispose();
			bufImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImg;
	}

	private void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
		File file = new File(imgPath);
		if (!file.exists()) {
			System.err.println("" + imgPath + "  该文件不存在");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > INSERT_IMG_SIZE) {
				width = INSERT_IMG_SIZE;
			}
			if (height > INSERT_IMG_SIZE) {
				height = INSERT_IMG_SIZE;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (CODE_IMG_SIZE - width) / 2;
		int y = (CODE_IMG_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 解析二维码（QRCode）
	 * 
	 * @param imgPath 根据图片路径解密图片路径
	 * @return
	 */
	public String decoderQRCode(String imgPath) {
		// QRCode 文件
		long startTime = System.currentTimeMillis();
		File imageFile = new File(imgPath);
		BufferedImage bufImg = null;
		String content = null;
		String content1 = null;
		try {
			bufImg = ImageIO.read(imageFile);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
			// 解密
			String password = "1245678";
			byte[] decode = AES.parseHexStr2Byte(content);
			byte[] decryptResult = AES.decrypt(decode, password);
			content1 = new String(decryptResult, "UTF-8");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
			dfe.printStackTrace();
		}
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
		return content1;
	}
	
	/**
	 * 解析二维码（QRCode）
	 * 
	 * @param frame 根据输入流解密摄像头部分用到
	 * @return
	 */
	public String decoderQRCode(BufferedImage frame) {
		String result1 = null;
	    LuminanceSource source = new BufferedImageLuminanceSource(frame);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    Reader reader = new MultiFormatReader();
	    GenericMultipleBarcodeReader readerM = new GenericMultipleBarcodeReader(reader);
	    //Result result;
		try {
			Result[] result = readerM.decodeMultiple(bitmap);
			// System.out.println("Barcode text is " + result.getText());
			for (Result r : result) {	
				//System.out.println("Barcode text is " + r.getText());
				result1 = r.getText();
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			
		}

		return result1;
	}
	/**
	 * 解析密文（AES）
	 * 
	 * @param content 密文
	 * @return
	 */
	public String decoderString(String content) {
		//秘钥
		String password = "1245678";
		//转进制
		byte[] decode = AES.parseHexStr2Byte(content);
		//输出结果
		byte[] decryptResult = AES.decrypt(decode, password);
		try {
			content = new String(decryptResult, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 解析二维码（QRCode）
	 * 
	 * @param input 输入流
	 * @return
	 */
	public String decoderQRCode(InputStream input) {
		BufferedImage bufImg = null;
		String content = null;
		try {
			bufImg = ImageIO.read(input);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
			dfe.printStackTrace();
		}
		return content;
	}
	
	
	
}