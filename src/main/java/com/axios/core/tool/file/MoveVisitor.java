package com.axios.core.tool.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * [文件移动操作的FileVisitor实现](Implementation of filevisitor for file movement operation)
 * @description zh - 文件移动操作的FileVisitor实现
 * @description en - Implementation of filevisitor for file movement operation
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-07 20:15:44
 */
public class MoveVisitor extends SimpleFileVisitor<Path> {

	private final Path source;
	private final Path target;
	private boolean isTargetCreated;
	private final CopyOption[] copyOptions;

	public MoveVisitor(Path source, Path target, CopyOption... copyOptions) {
		if(FileTool.exists(target, false) && false == FileTool.isDirectory(target,false)){
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		initTarget();
		final Path targetDir = target.resolve(source.relativize(dir));
		if(false == Files.exists(targetDir)){
			Files.createDirectories(targetDir);
		} else if(false == Files.isDirectory(targetDir)){
			throw new FileAlreadyExistsException(targetDir.toString());
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		initTarget();
		Files.move(file, target.resolve(source.relativize(file)), copyOptions);
		return FileVisitResult.CONTINUE;
	}

	private void initTarget(){
		if(false == this.isTargetCreated){
			FileTool.mkdir(this.target);
			this.isTargetCreated = true;
		}
	}
}
