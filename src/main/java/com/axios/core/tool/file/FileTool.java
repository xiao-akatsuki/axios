package com.axios.core.tool.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.axios.core.tool.io.IoTool;
import com.axios.exception.IORuntimeException;

/**
 * [文件工具类](File tool class)
 * @description zh - 文件工具类
 * @description en - File tool class
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-05 09:52:01
 */
public class FileTool {
	/**
	 * [获得一个输出流对象](Get an output stream object)
	 * @description zh - 获得一个输出流对象
	 * @description en - Get an output stream object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:53:06
	 * @param file 文件
	 * @throws com.axios.exception.IORuntimeException
	 * @return java.io.BufferedOutputStream
	 */
	public static BufferedOutputStream getOutputStream(File file) throws IORuntimeException {
		final OutputStream out;
		try {
			out = new FileOutputStream(touch(file));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return IoTool.toBuffered(out);
	}

	/**
	 * [创建文件及其父目录](Create a file and its parent directory)
	 * @description zh - 创建文件及其父目录
	 * @description en - Create a file and its parent directory
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:55:33
	 * @param file 文件
	 * @throws com.axios.exception.IORuntimeException
	 * @return java.io.File
	 */
	public static File touch(File file) throws IORuntimeException {
		if (null == file) {
			return null;
		}
		if (false == file.exists()) {
			mkParentDirs(file);
			try {
				file.createNewFile();
			} catch (Exception e) {
				throw new IORuntimeException(e);
			}
		}
		return file;
	}

	/**
	 * [创建所给文件或目录的父目录](Creates the parent directory of the given file or directory)
	 * @description zh - 创建所给文件或目录的父目录
	 * @description en - Creates the parent directory of the given file or directory
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:56:31
	 * @param file 文件
	 * @return java.io.File
	 */
	public static File mkParentDirs(File file) {
		if (null == file) {
			return null;
		}
		return mkdir(file.getParentFile());
	}

	/**
	 * [创建文件夹](create folder)
	 * @description zh - 创建文件夹
	 * @description en - create folder
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:57:20
	 * @param dir 文件夹
	 * @return java.io.File
	 */
	public static File mkdir(File dir) {
		if (dir == null) {
			return null;
		}
		if (false == dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
}
