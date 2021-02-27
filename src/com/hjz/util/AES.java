package com.hjz.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
* AES加密
* @author Chen
* @version 1
*/
public class AES {
	/**
	 * 加密
	 * 
	 * @param content  需要加密的内容
	 * @param password 加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content  待解密内容
	 * @param password 解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 加密
	 *
	 * @param content  需要加密的内容
	 * @param password 加密密码
	 * @return
	 */
	public static byte[] encrypt2(String content, String password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
//测试主函数
//	public static void main(String[] args) throws UnsupportedEncodingException {
//		String content = "针对现有分布式深度学习外包计算模式下存在的数据隐私泄露问题，分析生成式对抗网络攻击和成员推理攻击等隐私攻击特征，定义分布式深度学习外包计算敌手模型，重点研究基于恶意多方计算协议和矩阵盲化的安全训练交互方法，设计基于多因子盲化与加法同态加密相结合的隐私数据和模型参数安全聚合方案，实现用户数据和训练模型的保护。此外，考虑到针对特定受害者隐私数据的多方共谋攻击场景，研究利用Shamir秘密共享协议等隐私保护技术强化更新聚合和验证过程，从抵御攻击注入以及防止模型滥用角度实现数据隐私的保护。\r\n" + 
//				"仿：针对现有分布式机器学习模型下存在的模型安全问题，分析后门攻击等隐私攻击特征，定义分布式机器学习敌手模型。研究基于分布式机器学习模型的后门攻击检测方案，实质是研究对于植入模型中静态或动态触发器检测。对传统的防御方法频谱签名，激活聚类，STRIP和神经清洁进行优化，设计基于多因子盲化与加法同态加密相结合的隐私数据和模型参数安全聚合方案，实现用户数据和训练模型的保护。此外，考虑到用户共谋攻击场景，研究设计模型培训和测试以及数据收集和验证应由系统提供商直接执行，然后进行安全维护的分布式机器学习模型，从而避免后门攻击的绝大部分威胁。（3）分布式深度学习外包中的可验证性方法研究针对分布式深度学习外包计算过程中数据可用性和结果正确性的验证问题，重点研究多源高维数据的特征提取、聚类分析、离群点检测技术，设计可实时更新的恶意检测模型，实";
//		String password = "12345678";
//		// 加密
//		System.out.println("加密前：" + content);
//		byte[] encode = encrypt(content, password);
//
//		// 传输过程,不转成16进制的字符串，就等着程序崩溃掉吧
//		String code = parseByte2HexStr(encode);
//		System.out.println("密文字符串：" + code);
//		byte[] decode = parseHexStr2Byte(code);
//		// 解密
//		byte[] decryptResult = decrypt(decode, password);
//		System.out.println("解密后：" + new String(decryptResult, "UTF-8")); // 不转码会乱码
//
//	}
}