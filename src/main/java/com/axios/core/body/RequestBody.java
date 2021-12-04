package com.axios.core.body;

import java.io.OutputStream;

/**
 * [定义请求体接口](Define request body interface)
 * @description zh - 定义请求体接口
 * @description en - Define request body interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-04 09:38:11
 */
public interface RequestBody {

	/**
	 * [写出数据，不关闭流](Write out data without closing the stream)
	 * @description zh - 写出数据，不关闭流
	 * @description en - Write out data without closing the stream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 09:37:38
	 * @param out 输出流
	 */
	void write(OutputStream out);

}
