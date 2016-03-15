package com.loovee.common.xmpp.utils;

import android.text.TextUtils;

import com.loovee.common.xmpp.security.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();
	private static final char[] LT_ENCODE = "&lt;".toCharArray();
	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	private static MessageDigest digest = null;

	private static Random randGen = new Random();

	private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	public static String parseName(String XMPPAddress) {
		if (XMPPAddress == null) {
			return null;
		}
		int atIndex = XMPPAddress.lastIndexOf("@");
		if (atIndex <= 0) {
			if(isInteger(XMPPAddress)) {
				return XMPPAddress;
			}else {
				return "";
			}
		}
		return XMPPAddress.substring(0, atIndex);
	}


	/**
	 * 判断字符串是否是数字
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
		String regex = "^\\d+\\d$";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(regex);
		Matcher matcher = sinaPatten.matcher(s);
		return matcher.matches();
	}


	public static String parseServer(String XMPPAddress) {
		if (XMPPAddress == null) {
			return null;
		}
		int atIndex = XMPPAddress.lastIndexOf("@");

		if (atIndex + 1 > XMPPAddress.length()) {
			return "";
		}
		int slashIndex = XMPPAddress.indexOf("/");
		if ((slashIndex > 0) && (slashIndex > atIndex)) {
			return XMPPAddress.substring(atIndex + 1, slashIndex);
		}

		return XMPPAddress.substring(atIndex + 1);
	}

	public static String parseResource(String XMPPAddress) {
		if (XMPPAddress == null) {
			return null;
		}
		int slashIndex = XMPPAddress.indexOf("/");
		if ((slashIndex + 1 > XMPPAddress.length()) || (slashIndex < 0)) {
			return "";
		}

		return XMPPAddress.substring(slashIndex + 1);
	}

	public static String parseBareAddress(String XMPPAddress) {
		if (XMPPAddress == null) {
			return null;
		}
		int slashIndex = XMPPAddress.indexOf("/");
		if (slashIndex < 0) {
			return XMPPAddress;
		}
		if (slashIndex == 0) {
			return "";
		}

		return XMPPAddress.substring(0, slashIndex);
	}

	public static String escapeNode(String node) {
		if (node == null) {
			return null;
		}
		StringBuilder buf = new StringBuilder(node.length() + 8);
		int i = 0;
		for (int n = node.length(); i < n; i++) {
			char c = node.charAt(i);
			switch (c) {
			case '"':
				buf.append("\\22");
				break;
			case '&':
				buf.append("\\26");
				break;
			case '\'':
				buf.append("\\27");
				break;
			case '/':
				buf.append("\\2f");
				break;
			case ':':
				buf.append("\\3a");
				break;
			case '<':
				buf.append("\\3c");
				break;
			case '>':
				buf.append("\\3e");
				break;
			case '@':
				buf.append("\\40");
				break;
			case '\\':
				buf.append("\\5c");
				break;
			default:
				if (Character.isWhitespace(c)) {
					buf.append("\\20");
				} else {
					buf.append(c);
				}
				break;
			}
		}
		return buf.toString();
	}

	public static String unescapeNode(String node) {
		if (node == null) {
			return null;
		}
		char[] nodeChars = node.toCharArray();
		StringBuilder buf = new StringBuilder(nodeChars.length);
		int i = 0;
		for (int n = nodeChars.length; i < n; i++) {
			char c = node.charAt(i);
			if ((c == '\\') && (i + 2 < n)) {
				char c2 = nodeChars[(i + 1)];
				char c3 = nodeChars[(i + 2)];
				if (c2 == '2')
					switch (c3) {
					case '0':
						buf.append(' ');
						i += 2;
						break;
					case '2':
						buf.append('"');
						i += 2;
						break;
					case '6':
						buf.append('&');
						i += 2;
						break;
					case '7':
						buf.append('\'');
						i += 2;
						break;
					case 'f':
						buf.append('/');
						i += 2;
						break;
					default:
						break;
					}
				if (c2 == '3')
					switch (c3) {
					case 'a':
						buf.append(':');
						i += 2;
						break;
					case 'c':
						buf.append('<');
						i += 2;
						break;
					case 'e':
						buf.append('>');
						i += 2;
						break;
					case 'b':
					case 'd':
					default:
						break;
					}
				if (c2 == '4') {
					if (c3 == '0') {
						buf.append("@");
						i += 2;
						continue;
					}
				} else if ((c2 == '5') && (c3 == 'c')) {
					buf.append("\\");
					i += 2;
					continue;
				}
			}

			buf.append(c);
		}

		return buf.toString();
	}

	public static String escapeForXML(String string) {
		if (string == null) {
			return null;
		}

		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuilder out = new StringBuilder((int) (len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '>') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(GT_ENCODE);
				} else if (ch == '&') {
					if (i > last) {
						out.append(input, last, i - last);
					}

					if ((len <= i + 5) || (input[(i + 1)] != '#')
							|| (!Character.isDigit(input[(i + 2)]))
							|| (!Character.isDigit(input[(i + 3)]))
							|| (!Character.isDigit(input[(i + 4)]))
							|| (input[(i + 5)] != ';')) {
						last = i + 1;
						out.append(AMP_ENCODE);
					}
				} else if (ch == '"') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(QUOTE_ENCODE);
				}
			}
		}
		if (last == 0) {
			return string;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	public static synchronized String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException nsae) {
				System.err
						.println("Failed to load the SHA-1 MessageDigest. Jive will be unable to function normally.");
			}
		}

		try {
			digest.update(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.err.println(e);
		}
		return encodeHex(digest.digest());
	}

	public static String encodeHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder(bytes.length * 2);

		for (byte aByte : bytes) {
			if ((aByte & 0xFF) < 16) {
				hex.append("0");
			}
			hex.append(Integer.toString(aByte & 0xFF, 16));
		}

		return hex.toString();
	}

	public static String encodeBase64(String data) {
		byte[] bytes = null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		return encodeBase64(bytes);
	}

	public static String encodeBase64(byte[] data) {
		return encodeBase64(data, false);
	}

	public static String encodeBase64(byte[] data, boolean lineBreaks) {
		return Base64.encode(data, 0, data.length, null).toString();
	}

	public static byte[] decodeBase64(String data) {
		return Base64.decode(data);
	}

	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}
	
	
	/**
	 * 将字符串变为首字母小写的格式
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if(TextUtils.isEmpty(s)) {
			return s;
		}
		if(Character.isLowerCase(s.charAt(0))) {
			return s;
		}else {
        	 return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
           
	}
	
	/**
	 * 将字符串里的首字母变成大写
	 * @param s
	 * @return
	 */
	public static String toUpercaseFirstOne(String s) {
		if(TextUtils.isEmpty(s)) {
			return s;
		}
		if(Character.isUpperCase(s.charAt(0))) {
			return s;
		}else {
			 return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}
	
	
	
}