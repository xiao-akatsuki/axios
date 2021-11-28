package com.axios.core.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import com.axios.core.assertion.Assert;
import com.axios.exception.NoResourceException;

/**
 * [文件资源访问对象](File resource access object)
 * @description zh - 文件资源访问对象
 * @description en - File resource access object
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 14:56:50
 */
public class FileResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final File file;

	public FileResource(Path path) {
		this(path.toFile());
	}

	public FileResource(File file) {
		this(file, file.getName());
	}

	public FileResource(File file, String fileName) {
		this.file = file;
	}

	public FileResource(String path) {
		this(file(path));
	}

	private static File file(String path) {
		if (null == path) {
			return null;
		}
		return new File(path);
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public URL getUrl(){
		return getURL(this.file);
	}

	private URL getURL(File file2) {
		Assert.notNull(file, "File is null !");
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new NoResourceException(e);
		}
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		try {
			return getInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BufferedInputStream getInputStream(InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	@Override
	public String toString() {
		return (null == this.file) ? "null" : this.file.toString();
	}

	public File getFile() {
		return this.file;
	}
}
