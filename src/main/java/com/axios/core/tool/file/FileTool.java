package com.axios.core.tool.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

import com.axios.core.assertion.Assert;
import com.axios.core.tool.http.HttpTool;
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

	public static boolean del(File file) throws IORuntimeException {
		if (file == null || false == file.exists()) {
			// 如果文件不存在或已被删除，此处返回true表示删除成功
			return true;
		}

		if (file.isDirectory()) {
			// 清空目录下所有文件和目录
			boolean isOk = clean(file);
			if (false == isOk) {
				return false;
			}
		}

		// 删除文件或清空后的目录
		final Path path = file.toPath();
		try {
			delFile(path);
		} catch (DirectoryNotEmptyException e) {
			// 遍历清空目录没有成功，此时补充删除一次（可能存在部分软链）
			del(path);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return true;
	}

	public static boolean clean(File directory) throws IORuntimeException {
		if (directory == null || directory.exists() == false || false == directory.isDirectory()) {
			return true;
		}

		final File[] files = directory.listFiles();
		if (null != files) {
			for (File childFile : files) {
				if (false == del(childFile)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean del(Path path) throws IORuntimeException {
		if (Files.notExists(path)) {
			return true;
		}

		try {
			if (isDirectory(path,false)) {
				Files.walkFileTree(path, DelVisitor.INSTANCE);
			} else {
				delFile(path);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return true;
	}

	public static Path rename(File file, String newName, boolean isOverride) {
		return move(file.toPath(), file.toPath().resolveSibling(newName), isOverride);
	}

	public static Path move(Path src, Path target, boolean isOverride) {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");

		if (isDirectory(target, false)) {
			target = target.resolve(src.getFileName());
		}
		return moveContent(src, target, isOverride);
	}

	public static boolean isDirectory(Path path, boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		return Files.isDirectory(path, options);
	}

	public static Path moveContent(Path src, Path target, boolean isOverride) {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");
		final CopyOption[] options = isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
		// 自动创建目标的父目录
		mkdir(target.getParent());
		try {
			return Files.move(src, target, options);
		} catch (IOException e) {
			// 移动失败，可能是跨分区移动导致的，采用递归移动方式
			try {
				Files.walkFileTree(src, new MoveVisitor(src, target, options));
				// 移动后空目录没有删除，
				del(src);
			} catch (IOException e2) {
				throw new IORuntimeException(e2);
			}
			return target;
		}
	}

	public static Path mkdir(Path dir) {
		if (null != dir && false == exists(dir, false)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return dir;
	}

	public static boolean exists(Path path, boolean isFollowLinks) {
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		return Files.exists(path, options);
	}

	public static File file(String path) {
		if (null == path) {
			return null;
		}
		return new File(path);
	}

	public static File file(File directory, String... names) {
		Assert.notNull(directory, "directory must not be null");
		if (HttpTool.isEmpty(names)) {
			return directory;
		}

		File file = directory;
		for (String name : names) {
			if (null != name) {
				file = file(file, name);
			}
		}
		return file;
	}

	protected static void delFile(Path path) throws IOException {
		try {
			Files.delete(path);
		} catch (AccessDeniedException e) {
			if (false == path.toFile().delete()) {
				throw e;
			}
		}
	}
}
