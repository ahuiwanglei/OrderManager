package com.jsong.android.library.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileItem {
	private String fileName;
	private String mimeType;
	private byte[] content;
	private File file;

	/**
	 * 基于本地文件的构造器�?
	 * 
	 * @param file
	 *            本地文件
	 */
	public FileItem(File file) {
		this.file = file;
	}

	/**
	 * 基于文件绝对路径的构造器�?
	 * 
	 * @param filePath
	 *            文件绝对路径
	 */
	public FileItem(String filePath) {
		this(new File(filePath));
	}

	/**
	 * 基于文件名和字节流的构�?器�?
	 * 
	 * @param fileName
	 *            文件�?
	 * @param content
	 *            文件字节�?
	 */
	public FileItem(String fileName, byte[] content) {
		this.fileName = fileName;
		this.content = content;
	}

	/**
	 * 基于文件名�?字节流和媒体类型的构造器�?
	 * 
	 * @param fileName
	 *            文件�?
	 * @param content
	 *            文件字节�?
	 * @param mimeType
	 *            媒体类型
	 */
	public FileItem(String fileName, byte[] content, String mimeType) {
		this(fileName, content);
		this.mimeType = mimeType;
	}

	public String getFileName() {
		if (this.fileName == null && this.file != null && this.file.exists()) {
			this.fileName = file.getName();
		}
		return this.fileName;
	}

	public String getMimeType() throws IOException {
		if (this.mimeType == null) {
			this.mimeType = HouseHoldUtile.getMimeType(getContent());
		}
		return this.mimeType;
	}

	public byte[] getContent() throws IOException {
		if (this.content == null && this.file != null && this.file.exists()) {
			InputStream in = null;
			BufferedInputStream bis = null;
			ByteArrayOutputStream out = null;

			try {
				in = new FileInputStream(this.file);
				bis = new BufferedInputStream(in);
				out = new ByteArrayOutputStream();
				int len = bis.available();
				byte[] isBuffer = new byte[len];
				bis.read(isBuffer);
				this.content = isBuffer;
				// int len = 0;
				// while ((len = bis.read(isBuffer)) != -1) {
				// out.write(isBuffer, 0, len);
				// }

				// int ch;
				// while ((ch = in.read()) != -1) {
				// out.write(ch);
				// }
				// this.content = out.toByteArray();
			} finally {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (bis != null) {
					bis.close();
				}
			}
		}
		return this.content;
	}

}
