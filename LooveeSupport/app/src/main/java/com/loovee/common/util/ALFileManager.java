package com.loovee.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

public class ALFileManager {

	private static final int DEFAULT_BUFFER_SIZE = 8192;
	
//	public static boolean createFile(String path,String content){
//		
//		
//		if(!isFileExsit(path)){
//			createFile(path);
//		}
//		
//		File f = new File
//		
//	}
	
	// 获取完整路径中的文件名
	public static String getFileName(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}

		File f = new File(path);
		if (!f.isFile())
			return null;

		return f.getName();
	}
	
	public static boolean createFile(String path){
		File f = new File(path);
		if (!f.exists()) {
			return f.mkdirs();
		}
		return false;
	}

	public static boolean isFileExsit(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File f = new File(path);
		return f.exists();
	}

	public static boolean isFileExsit(File file) {
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	public static long getFileSize(String path) {
		if (!isFileExsit(path)) {
			return 0;
		}

		File f = new File(path);
		return f.length();
	}

	public static long getFileSize(File file) {
		if (!isFileExsit(file)) {
			return 0;
		}
		return file.length();
	}

	public static void deletFile(String path) {
		if (!isFileExsit(path)) {
			return;
		}

		File f = new File(path);
		f.delete();
	}

	public static boolean renameFile(String path, String newPath) {
		if (!isFileExsit(path)) {
			return false;
		}
		File f = new File(path);
		return f.renameTo(new File(newPath));
	}

	public static boolean renameFile(File src, File dst) {
		if (!isFileExsit(src)) {
			return false;
		}
		return src.renameTo(dst);
	}

	public static boolean fileEquals(File file1, File file2) {
		FileInputStream f1 = null;
		FileInputStream f2 = null;
		try {
			f1 = new FileInputStream(file1);
			f2 = new FileInputStream(file2);
			byte[] b1 = new byte[(int) file1.length()];
			byte[] b2 = new byte[(int) file2.length()];
			f1.read(b1);
			f2.read(b2);
			for (int j = 0; j < b1.length; j++) {
				if (b1[j] != b2[j])
					return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				f1.close();
				f2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getFilePathByUri(Context context ,Uri uri) {
		ContentResolver provider = context.getContentResolver();
		Cursor c = provider.query(uri, new String[] { "_data" }, null, null,
				null);
		String filePath = null;
		if (c != null && c.moveToFirst()) {
			filePath = c.getString(c.getColumnIndex("_data"));
			c.close();
		}
		return filePath;
	}
	
	public static long getFileAndSubFileSize(File f) throws Exception {
		long size = 0;
		if (f != null) {
			if (f.isFile()) {
				size = f.length();
			} else {
				File[] flist = f.listFiles();
				if (flist != null) {
					for (int i = 0; i < flist.length; i++) {
						if (flist[i].isDirectory()) {
							size = size + getFileSize(flist[i]);
						} else {
							size = size + flist[i].length();
						}
						flist[i] = null;
					}
				}
			}
		}
		return size;
	}

	/**
	 * 拷贝一个文件的内容到另一个文件当中。
	 * 
	 * @param src
	 *            目标源文件
	 * @param dst
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File src, File dst) throws IOException {
		FileInputStream in = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(dst);
		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();

		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}

		in.close();
		out.close();
	}

	/**
	 * 从某个inputstream拷贝数据到另外一个outputstream。缓存为8K.
	 * 
	 * @param input
	 *            输入inputstream
	 * @param output
	 *            输出outputstream
	 * @return 拷贝字节数
	 * @throws IOException
	 */
	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Copy the contents of the given Reader to the given Writer. Closes both
	 * when done.
	 * 
	 * @param in
	 *            the Reader to copy from
	 * @param out
	 *            the Writer to copy to
	 * @return the number of characters copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		try {
			int byteCount = 0;
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}

			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given Reader into a String. Closes the reader
	 * when done.
	 * 
	 * @param in
	 *            the reader to copy from
	 * @return the String that has been copied to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}

	public static String copyToString(InputStream in) throws IOException {
		StringBuffer s = new StringBuffer();

		byte buffer[] = new byte[1024 * 4];
		int temp = 0;
		while ((temp = in.read(buffer)) != -1) {
			s.append(new String(buffer, 0, temp));
		}
		return s.toString();

	}

	/**
	 * Copy the contents of the given input File to the given output File.
	 * 
	 * @param in
	 *            the file to copy from
	 * @param out
	 *            the file to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(File in, File out) throws IOException {
		return copy(new BufferedInputStream(new FileInputStream(in)),
				new BufferedOutputStream(new FileOutputStream(out)));
	}

	/**
	 * Copy the contents of the given byte array to the given output File.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the file to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(byte[] in, File out) throws IOException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(
				out));
		copy(inStream, outStream);
	}

	/**
	 * Copy the contents of the given input File into a new byte array.
	 * 
	 * @param in
	 *            the file to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}

	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Closes the stream when done.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given InputStream into a new byte array. Closes
	 * the stream when done.
	 * 
	 * @param in
	 *            the stream to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(
				DEFAULT_BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param size
	 *            文件大小
	 * @return 格式化后的文件大小。保留小数点后两位。
	 * @author Alonso Lee
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.##");
		float tmpSize = 0;

		if (size / (1024 * 1024) > 0) {
			tmpSize = (float) (size) / (float) (1024 * 1024);
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			tmpSize = (float) (size) / (float) (1024);
			return "" + df.format(tmpSize) + "KB";
		} else if(size == 0){
			return "" + size;
		}else {
			return "" + size + "B";
		}
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param file
	 *            待格式化的文件
	 * @return 格式化后的文件大小
	 * @author Alonso Lee
	 */
	public static String formatFileSize(File file) {
		long size = 0;
		size = getFileSize(file);
		return formatFileSize(size);
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param file
	 *            待格式化的文件
	 * @return 格式化后的文件大小
	 * @author Alonso Lee
	 */
	public static String formatFileSize(String path) {
		long size = 0;
		size = getFileSize(path);
		return formatFileSize(size);
	}

}
