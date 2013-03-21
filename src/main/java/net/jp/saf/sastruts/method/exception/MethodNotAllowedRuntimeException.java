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
package net.jp.saf.sastruts.method.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 許可されていないHTTPメソッド呼び出しが行われた時の例外です.
 * 
 * @author k-minemoto
 */
public class MethodNotAllowedRuntimeException extends SRuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String httpMethod;
	
	private String allowedMethods;

	/**
	 * エラー情報無しのコンストラクタ.
	 * 
	 * @param httpMethod 呼び出されたHTTPメソッド
	 * @param allowedMethods 許可されていたHTTPメソッド
	 */
	public MethodNotAllowedRuntimeException(String httpMethod, String allowedMethods) {
		this(httpMethod, allowedMethods, null);
	}

	/**
	 * エラー情報ありのコンストラクタ.
	 * 
	 * @param httpMethod 呼び出されたHTTPメソッド
	 * @param allowedMethods 許可されていたHTTPメソッド
	 * @param cause 関連するエラー
	 */
	public MethodNotAllowedRuntimeException(String httpMethod, String allowedMethods, Throwable cause) {
		super("ESASML0001", new Object[] { httpMethod, allowedMethods }, cause);
		this.httpMethod = httpMethod;
		this.allowedMethods = allowedMethods;
	}
	/**
	 * 呼び出されたHTTPメソッドを返します.
	 * 
	 * @return 呼び出されたHTTPメソッド
	 */
	public String getHttpMethod() {
		return this.httpMethod;
	}

	/**
	 * 許可されていたHTTPメソッドを返します.
	 * 
	 * @return 許可されていたHTTPメソッド
	 */
	public String getAllowedMethods() {
		return this.allowedMethods;
	}

}
