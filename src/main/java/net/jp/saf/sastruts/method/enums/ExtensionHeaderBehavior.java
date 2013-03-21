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
package net.jp.saf.sastruts.method.enums;

/**
 * 拡張ヘッダーに対する振る舞いを表します.
 *
 * @author k-minemoto
 */
public enum ExtensionHeaderBehavior {
	/**
	 * 個別定義しない.
	 * <p>
	 * アノテーションが使用します.
	 * </p>
	 */
	NOT_DEFINE,
	/**
	 * 拡張ヘッダーを使用しない.
	 * <p>
	 * 拡張ヘッダーの値が存在しても無視します.
	 * </p>
	 * 
	 */
	NOT_USE,
	/**
	 * 拡張ヘッダーを優先的に使用する.
	 * <p>
	 * 拡張ヘッダーの値が存在しない場合、{@link javax.servlet.http.HttpServletRequest#getMethod()}を使用します.
	 * </p>
	 */
	HEADER_FIRST,
	/**
	 * 拡張ヘッダーのみ使用する.
	 * <p>
	 * {@link javax.servlet.http.HttpServletRequest#getMethod()}の値は使用しません.
	 * </p>
	 */
	HEADER_ONLY;
}
