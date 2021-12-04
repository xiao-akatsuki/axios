package com.axios.core.body;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.axios.core.resource.MultiResource;
import com.axios.core.resource.Resource;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.core.tool.io.IoTool;
import com.axios.core.tool.random.RandomTool;
import com.axios.core.type.ContentType;
import com.axios.exception.IORuntimeException;

/**
 * [Multipart/form-data数据的请求体封装](Request body encapsulation of Multipart/form-data)
 * @description zh - Multipart/form-data数据的请求体封装
 * @description en - Request body encapsulation of Multipart/form-data
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-04 10:56:09
 */
public class MultipartBody implements RequestBody {

	private static final String BOUNDARY = "--------------------axios_" + RandomTool.randomString(16);
	private static final String BOUNDARY_END = "--" + BOUNDARY + "--\r\n";
	private static final String CONTENT_TYPE_MULTIPART_PREFIX = ContentType.MULTIPART.getValue() + "; boundary=";


	/**
	 * 存储表单数据
	 */
	private final Map<String, Object> form;

	/**
	 * 编码
	 */
	private final Charset charset;

	public MultipartBody(Map<String, Object> form, Charset charset) {
		this.form = form;
		this.charset = charset;
	}

	/** ---------------- create ---------------- */

	/**
	 * [根据已有表单内容，构建MultipartBody](Build a multipartbody based on the existing form content)
	 * @description zh - 根据已有表单内容，构建MultipartBody
	 * @description en - Build a multipartbody based on the existing form content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 09:52:10
	 * @param form 存储表单数据
	 * @param charset 编码
	 * @return com.axios.core.body.MultipartBody
	 */
	public static MultipartBody create(Map<String, Object> form, Charset charset){
		return new MultipartBody(form, charset);
	}

	/**
	 * [获取Multipart的Content-Type类型](Gets the content type of the multipart)
	 * @description zh - 获取Multipart的Content-Type类型
	 * @description en - Gets the content type of the multipart
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 09:53:16
	 * @return java.lang.String
	 */
	public static String getContentType(){
		return CONTENT_TYPE_MULTIPART_PREFIX + BOUNDARY;
	}

	public static String contentDisposition(String name){
		return "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n";
	}

	public static String contentDispositionFile(String name,String fileName){
		return "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"\r\n";
	}

	public static String contentTypeFile(String contentType){
		return "Content-Type: " + contentType + "\r\n\r\n";
	}

	/** ---------------- override ---------------- */

	@Override
	public void write(OutputStream out) {
		writeForm(out);
		formEnd(out);
	}

	/** ---------------- private ---------------- */

	private void writeForm(OutputStream out) {
		if (null != this.form && false == this.form.isEmpty()) {
			for (Map.Entry<String, Object> entry : this.form.entrySet()) {
				appendPart(entry.getKey(), entry.getValue(), out);
			}
		}
	}

	private void appendPart(String formFieldName, Object value, OutputStream out) throws IORuntimeException {
		// 多资源
		if (value instanceof MultiResource) {
			for (Resource subResource : (MultiResource) value) {
				appendPart(formFieldName, subResource, out);
			}
			return;
		}

		write(out, "--", BOUNDARY, "\r\n");

		if(value instanceof Resource){
			// 文件资源（二进制资源）
			final Resource resource = (Resource)value;
			final String fileName = resource.getName();
			write(out, contentDispositionFile(formFieldName, UrlTool.defaultIfNull(fileName, formFieldName)));
			// 根据name的扩展名指定互联网媒体类型，默认二进制流数据
			write(out, contentTypeFile(HttpTool.getMimeType(fileName, "application/octet-stream")));
			resource.writeTo(out);
		} else{
			// 普通数据
			write(out, contentDisposition(formFieldName));
			write(out, value);
		}

		write(out, "\r\n");
	}

	private void formEnd(OutputStream out) throws IORuntimeException {
		write(out, BOUNDARY_END);
	}

	private void write(OutputStream out, Object... objs) {
		IoTool.write(out, this.charset, false, objs);
	}
}
