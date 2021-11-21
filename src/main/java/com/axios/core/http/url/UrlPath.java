package com.axios.core.http.url;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import com.axios.core.assertion.Assert;
import com.axios.core.rfc.RFC3986;
import com.axios.core.tool.UrlTool;

/**
 * [URL中Path部分的封装](Encapsulation of the path part of the URL)
 * @description zh - URL中Path部分的封装
 * @description en - Encapsulation of the path part of the URL
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-21 19:59:09
 */
public class UrlPath {

	private List<String> segments;
	private boolean withEngTag;

	/**
	 * [构建UrlPath](Build urlpath)
	 * @description zh - 构建UrlPath
	 * @description en - Build urlpath
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:57:59
	 * @param pathStr 初始化的路径字符串
	 * @param charset decode用的编码，null表示不做decode
	 * @return com.axios.core.http.url.UrlPath
	 */
	public static UrlPath of(CharSequence pathStr, Charset charset) {
		final UrlPath urlPath = new UrlPath();
		urlPath.parse(pathStr, charset);
		return urlPath;
	}

	/**
	 * [是否path的末尾加/](Add at the end of path/)
	 * @description zh - 是否path的末尾加/
	 * @description en - Add at the end of path/
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:59:36
	 * @param withEngTag 是否path的末尾加 /
	 * @return com.axios.core.http.url.UrlPath
	 */
	public UrlPath setWithEndTag(boolean withEngTag) {
		this.withEngTag = withEngTag;
		return this;
	}

	/**
	 * [获取path的节点列表](Get the node list of path)
	 * @description zh - 获取path的节点列表
	 * @description en - Get the node list of path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:00:28
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> getSegments() {
		return this.segments;
	}

	/**
	 * [获得指定节点](Gets the specified node)
	 * @description zh - 获得指定节点
	 * @description en - Gets the specified node
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:01:07
	 * @param index 节点
	 * @return java.lang.String
	 */
	public String getSegment(int index) {
		return null == this.segments || index >= this.segments.size()
			 ? null
			 : this.segments.get(index);
	}

	/**
	 * [添加到path最后面](Add to the end of path)
	 * @description zh - 添加到path最后面
	 * @description en - Add to the end of path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:02:31
	 * @param segment Path节点
	 * @return com.axios.core.http.url.UrlPath
	 */
	public UrlPath add(CharSequence segment) {
		addInternal(fixPath(segment), false);
		return this;
	}

	/**
	 * [添加到path最前面](Add to the front of path)
	 * @description zh - 添加到path最前面
	 * @description en - Add to the front of path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:05:28
	 * @param segment Path节点
	 * @return com.axios.core.http.url.UrlPath
	 */
	public UrlPath addBefore(CharSequence segment) {
		addInternal(fixPath(segment), true);
		return this;
	}

	/**
	 * [解析path](Parse path)
	 * @description zh - 解析path
	 * @description en - Parse path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:07:24
	 * @param path 路径
	 * @param charset decode编码
	 * @return com.axios.core.http.url.UrlPath
	 */
	public UrlPath parse(CharSequence path, Charset charset) {
		if (UrlTool.isNotEmpty(path)) {
			if(UrlTool.endWith(path, '/')){
				this.withEngTag = true;
			}
			path = fixPath(path);
			final String[] split = path.toString().split("/");
			for (String seg : split) {
				addInternal(URLDecoder.decodeForPath(seg, charset), false);
			}
		}

		return this;
	}

	/**
	 * [构建path，前面带'/'](Build path with '/' in front)
	 * @description zh - 构建path，前面带'/'
	 * @description en - Build path with '/' in front
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:09:48
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	public String build(Charset charset) {
		if (UrlTool.isEmpty(this.segments)) {
			return "";
		}
		final StringBuilder builder = new StringBuilder();
		for (String segment : segments) {
			builder.append('/').append(RFC3986.SEGMENT_NZ_NC.encode(segment, charset));
		}
		if (withEngTag || UrlTool.isEmpty(builder)) {
			builder.append('/');
		}
		return builder.toString();
	}

	/**
	 * [增加节点](Add node)
	 * @description zh - 增加节点
	 * @description en - Add node
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:11:46
	 * @param segment 节点
	 * @param before 是否在前面添加
	 */
	private void addInternal(CharSequence segment, boolean before) {
		if (this.segments == null) {
			this.segments = new LinkedList<>();
		}

		final String seg = UrlTool.toString(segment);
		if (before) {
			this.segments.add(0, seg);
		} else {
			this.segments.add(seg);
		}
	}

	/**
	 * [修正路径](Correction path)
	 * @description zh - 修正路径
	 * @description en - Correction path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:13:21
	 * @param path 路径
	 * @return java.lang.String
	 */
	private static String fixPath(CharSequence path) {
		Assert.notNull(path, "Path segment must be not null!");
		if ("/".contentEquals(path)) {
			return "";
		}
		String segmentStr = UrlTool.trim(path);
		segmentStr = UrlTool.removePrefix(segmentStr, "/");
		segmentStr = UrlTool.removeSuffix(segmentStr, "/");
		segmentStr = UrlTool.trim(segmentStr);
		return segmentStr;
	}


}
