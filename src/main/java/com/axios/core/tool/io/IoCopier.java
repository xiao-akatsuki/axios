package com.axios.core.tool.io;

/**
 * [IO拷贝抽象](IO copy abstraction)
 * @description zh - IO拷贝抽象
 * @description en - IO copy abstraction
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-08 08:38:29
 */
public abstract class IoCopier<S, T> {

	protected final int bufferSize;
	/**
	 * 拷贝总数
	 */
	protected final long count;


	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小，&lt; 0 表示默认{@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * @param count      拷贝总数，-1表示无限制
	 * @param progress   进度条
	 */
	public IoCopier(int bufferSize, long count) {
		this.bufferSize = bufferSize > 0 ? bufferSize : 2 << 12;
		this.count = count <= 0 ? Long.MAX_VALUE : count;
	}

	/**
	 * 执行拷贝
	 *
	 * @param source 拷贝源，如InputStream、Reader等
	 * @param target 拷贝目标，如OutputStream、Writer等
	 * @return 拷贝的实际长度
	 */
	public abstract long copy(S source, T target);

	/**
	 * 缓存大小，取默认缓存和目标长度最小值
	 *
	 * @param count 目标长度
	 * @return 缓存大小
	 */
	protected int bufferSize(long count) {
		return (int) Math.min(this.bufferSize, count);
	}
}
