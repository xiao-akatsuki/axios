package com.axios.core.resource;

import java.io.File;
import java.util.Collection;

/**
 * [多文件组合资源](Multi file combination resource)
 * @description zh - 多文件组合资源
 * @description en - Multi file combination resource
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 14:53:01
 */
public class MultiFileResource extends MultiResource {
	private static final long serialVersionUID = 1L;

	public MultiFileResource(Collection<File> files) {
		add(files);
	}

	public MultiFileResource(File... files) {
		add(files);
	}

	public MultiFileResource add(File... files) {
		for (File file : files) {
			this.add(new FileResource(file));
		}
		return this;
	}

	public MultiFileResource add(Collection<File> files) {
		for (File file : files) {
			this.add(new FileResource(file));
		}
		return this;
	}

	@Override
	public MultiFileResource add(Resource resource) {
		return (MultiFileResource)super.add(resource);
	}
}
