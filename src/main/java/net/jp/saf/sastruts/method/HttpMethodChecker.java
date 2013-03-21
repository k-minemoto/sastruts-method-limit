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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior;
import net.jp.saf.sastruts.method.enums.HttpMethod;

/**
 * HTTPメソッドチェックを行うインタフェースです.
 *
 * @author k-minemoto
 */
public interface HttpMethodChecker extends Serializable {
	/**
	 * デフォルトのHTTP実行メソッドを指定するHTTPヘッダー名
	 */
	public static final String DEFAULT_EXTENSION_HEADER_NAME = "X-Http-Method-Override";

	/**
	 * HTTPメソッドのチェックを行います.
	 *
	 * @param request HTTPリクエスト
	 * @param allows 許可されているメソッド
	 * @param extensionHeaderBehavior 拡張ヘッダーの扱い
	 * @return チェック結果
	 */
	CheckResult check(HttpServletRequest request, HttpMethod[] allows, ExtensionHeaderBehavior extensionHeaderBehavior);

	/**
	 * チェックエラー時の処理を行います.
	 *
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param allowed 許可されている{@link HttpMethod}の配列
	 * @param method 実際に呼び出されたメソッド
	 * @return フォワード先のパス. フォワードしない場合nullを返すこと.
	 */
	String handleError(HttpServletRequest request, HttpServletResponse response, HttpMethod[] allowed, String method);
}
