/**
 * Copyright (C) 2013- k-minemoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jp.saf.sastruts.method.helpers.impl;

import java.util.TreeSet;

import net.jp.saf.sastruts.method.enums.HttpMethod;
import net.jp.saf.sastruts.method.exception.MethodNotAllowedRuntimeException;
import net.jp.saf.sastruts.method.helpers.ErrorHandler;

import org.seasar.struts.taglib.S2Functions;

/**
 * 共通で使用する処理を実装した基底クラス.
 *
 * @author k-minemoto
 */
public abstract class AbstractErrorHandler implements ErrorHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 5454469329362034342L;

	/**
	 * 重複無しで、かつREST_CRUDのようなenumをHTTPメソッドに展開してカンマ区切りで返します。
	 *
	 * @param methods 文字列にする{@link HttpMethod}の配列
	 * @return メソッドのカンマ区切り文字列
	 */
	protected String allowHeaderValue(HttpMethod[] methods) {
		if (methods == null || methods.length == 0) {
			return "";
		}
		TreeSet<String> allows = new TreeSet<String>();
		for(HttpMethod method : methods) {
			if (method == HttpMethod.GET_POST) {
				allows.add(HttpMethod.GET.name());
				allows.add(HttpMethod.POST.name());
			} else if (method == HttpMethod.REST_CRUD) {
				allows.add(HttpMethod.DELETE.name());
				allows.add(HttpMethod.GET.name());
				allows.add(HttpMethod.POST.name());
				allows.add(HttpMethod.PUT.name());
			} else if (method == HttpMethod.ALL) {
				allows.add(HttpMethod.DELETE.name());
				allows.add(HttpMethod.GET.name());
				allows.add(HttpMethod.POST.name());
				allows.add(HttpMethod.PUT.name());
				allows.add(HttpMethod.HEAD.name());
				allows.add(HttpMethod.OPTIONS.name());
				allows.add(HttpMethod.TRACE.name());
				break;
			} else {
				allows.add(method.name());
			}
		}
		if (allows.size() == 1) {
			return allows.first();
		}
		StringBuilder buff = new StringBuilder();
		for(String allow : allows) {
			buff.append(',').append(allow);
		}
		return buff.substring(1);
	}

	/**
	 * {@link MethodNotAllowedRuntimeException}の例外を発生させます.
	 *
	 * @param allowed 許可されている{@link HttpMethod}の配列
	 * @param method 実際に呼び出されたメソッド
	 * @param cause 元となった例外
	 */
	final protected void throwMethodNotAllowedRuntimeException(HttpMethod[] allowed, String method, Throwable cause) {
		String httpMethod = (method == null) ? "" : S2Functions.escape(method);
		String allowedMethods = allowHeaderValue(allowed);
		if (cause == null) {
			throw new MethodNotAllowedRuntimeException(httpMethod, allowedMethods);
		}
		throw new MethodNotAllowedRuntimeException(httpMethod, allowedMethods, cause);
	}
}
