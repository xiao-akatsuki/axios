package com.axios.core.tool.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * [删除操作的FileVisitor实现，用于递归遍历删除文件夹](The filevisitor implementation of the delete operation is used to recursively traverse the deleted folder)
 * @description zh - 删除操作的FileVisitor实现，用于递归遍历删除文件夹
 * @description en - The filevisitor implementation of the delete operation is used to recursively traverse the deleted folder
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-07 20:13:41
 */
public class DelVisitor extends SimpleFileVisitor<Path> {

	public static DelVisitor INSTANCE = new DelVisitor();

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		if (e == null) {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		} else {
			throw e;
		}
	}

}
