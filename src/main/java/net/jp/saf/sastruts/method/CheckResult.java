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
package net.jp.saf.sastruts.method;

import java.io.Serializable;

import net.jp.saf.sastruts.method.enums.HttpMethod;

/**
 * メソッドチェック結果を返します.
 *
 * @author k-minemoto
 */
public final class CheckResult implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/**
	 * チェック結果
	 */
	public final boolean ok;
	/**
	 * リクエストされたHTTPメソッド
	 */
	public final String requestMethod;
	/**
	 * 許可されていたHTTPメソッド
	 */
	public final HttpMethod[] allows;

	/**
	 * プライベートコンストラクタ.
	 *
	 * @param result チェック結果
	 * @param method チェックしたHTTPメソッド
	 * @param allows 許可されていたHTTPメソッド
	 */
	private CheckResult(boolean result, String method, HttpMethod[] allows) {
		this.ok = result;
		this.requestMethod = method;
		this.allows = allows;
	}

	/**
	 * チェックOKを表すインスタンスを生成します.
	 *
	 * @param method チェックしたHTTPメソッド
	 * @param allows 許可されていたHTTPメソッド
	 * @return チェックOKを表すインスタンス
	 */
	public static CheckResult ok(String method, HttpMethod[] allows) {
		return new CheckResult(true, method, allows);
	}

	/**
	 * チェックNGを表すインスタンスを生成します.
	 *
	 * @param method チェックしたHTTPメソッド
	 * @param allows 許可されていたHTTPメソッド
	 * @return チェックNGを表すインスタンス
	 */
	public static CheckResult ng(String method, HttpMethod[] allows) {
		return new CheckResult(false, method, allows);
	}

	/**
	 * デバッグ用
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("CheckResult:{ok=").append(ok).append( ",httpMethod=").append(requestMethod).append( ",allows=[");
		if (allows != null && allows.length > 0) {
			StringBuilder allowsEntry = new StringBuilder();
			for(HttpMethod allow : allows) {
				allowsEntry.append(',').append(allow.name());
			}
			sb.append(allowsEntry.substring(1));
			allowsEntry = null;
		}
		sb.append("]}");
		return sb.toString();
	}
}
