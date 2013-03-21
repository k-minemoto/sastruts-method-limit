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
package net.jp.saf.sastruts.method.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.jp.saf.sastruts.method.CheckResult;
import net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior;
import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.unit.Seasar2;
import org.seasar.struts.util.RequestUtil;

/**
 * Unitテスト.
 *
 * 特に断りの無い部分は、次の値が使用されている。
 *
 * チェック設定
 * ・defaultExtHeaderBehavior: デフォルト(NOT_USE)
 * ・extensionHeaderName: デフォルト(X-Http-Method-Override)
 *
 * @author k-minemoto
 */
@RunWith(Seasar2.class)
public class HttpMethodCheckerImplTest extends HttpMethodCheckerImpl {

	private static final long serialVersionUID = 1L;

	/**
	 * 全てデフォルト.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * [期待値]
	 * ・GET
	 */
	@Test
	public void testDetectCurrentHttpMethod01() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("GET", actual);
	}

	/**
	 * 全てデフォルト.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * [期待値]
	 * ・GET
	 */
	@Test
	public void testDetectCurrentHttpMethod02() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(DEFAULT_EXTENSION_HEADER_NAME, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("GET", actual);
	}

	/**
	 * 拡張ヘッダーを優先的に使用する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_FIRST
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod03() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(DEFAULT_EXTENSION_HEADER_NAME, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_FIRST);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 拡張ヘッダーを優先的に使用するが、拡張ヘッダーは設定しない.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_FIRST
	 * [期待値]
	 * ・GET
	 */
	@Test
	public void testDetectCurrentHttpMethod04() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_FIRST);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("GET", actual);
	}

	/**
	 * 拡張ヘッダーを優先的に使用する. 拡張ヘッダー名は変更する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_FIRST
	 * ・extensionHeaderName: X-Http-Method
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod05() {
		// Arrange
		final String headerName = "X-Http-Method";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(headerName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_FIRST);
		checker.setExtensionHeaderName(headerName);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 拡張ヘッダーのみ使用する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_ONLY
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod06() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(DEFAULT_EXTENSION_HEADER_NAME, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_ONLY);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 拡張ヘッダーのみ使用するが、拡張ヘッダーは設定しない.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_ONLY
	 * [期待値]
	 * ・null
	 */
	@Test
	public void testDetectCurrentHttpMethod07() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_ONLY);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertNull(actual);
	}

	/**
	 * 拡張ヘッダーのみ使用する. 拡張ヘッダー名は変更する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_ONLY
	 * ・extensionHeaderName: X-Http-Method
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod08() {
		// Arrange
		final String headerName = "X-Http-Method";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(headerName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_ONLY);
		checker.setExtensionHeaderName(headerName);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 個別設定で、拡張ヘッダーは使用しない.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_USE
	 * チェック設定
	 * ・defaultExtHeaderBehavior: HEADER_ONLY
	 * [期待値]
	 * ・GET
	 */
	@Test
	public void testDetectCurrentHttpMethod09() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(extensionHeaderName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.HEADER_ONLY);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_USE);
		// Assert
		assertEquals("GET", actual);
	}

	/**
	 * 個別設定で、拡張ヘッダーを優先的に使用する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: HEADER_FIRST
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod10() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(extensionHeaderName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.HEADER_FIRST);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 個別設定で、拡張ヘッダーのみ使用する.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: HEADER_ONLY
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * [期待値]
	 * ・POST
	 */
	@Test
	public void testDetectCurrentHttpMethod11() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(extensionHeaderName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.HEADER_ONLY);
		// Assert
		assertEquals("POST", actual);
	}

	/**
	 * 全てNOT_DEFINE.
	 * チェック設定のNOT_DEFINEはNOT_USEと同じ扱いになる
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE(指定しない)
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_DEFINE(指定しない)
	 * [期待値]
	 * ・GET
	 */
	@Test
	public void testDetectCurrentHttpMethod12() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(extensionHeaderName, "POST");

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_DEFINE);
		// Act
		String actual = checker.detectCurrentHttpMethod(request, ExtensionHeaderBehavior.NOT_DEFINE);
		// Assert
		assertEquals("GET", actual);
	}
	/**
	 * ヘッダーは使用しない設定でOKが返る.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE
	 * 許可するHTTPメソッド
	 * ・POST
	 * ・GET
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_DEFINE
	 * [期待値]
	 * ・チェックはOK
	 * ・チェックしたメソッドはGET
	 */
	@Test
	public void testCheck01() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		HttpMethod[] allows = new HttpMethod[]{HttpMethod.POST, HttpMethod.GET};
		ExtensionHeaderBehavior extensionHeaderBehavior = ExtensionHeaderBehavior.NOT_DEFINE;

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_DEFINE);
		// Act
		CheckResult actual = checker.check(request, allows, extensionHeaderBehavior);
		// Assert
		assertTrue(actual.ok);
		assertEquals("GET", actual.requestMethod);
	}

	/**
	 * ヘッダーは使用しない設定でOKが返る.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: POST
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE
	 * 許可するHTTPメソッド
	 * ・GET_POST
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * [期待値]
	 * ・チェックはOK
	 * ・チェックしたメソッドはPOST
	 */
	@Test
	public void testCheck02() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("POST");
		HttpMethod[] allows = new HttpMethod[]{HttpMethod.GET_POST};
		ExtensionHeaderBehavior extensionHeaderBehavior = ExtensionHeaderBehavior.NOT_DEFINE;

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);
		// Act
		CheckResult actual = checker.check(request, allows, extensionHeaderBehavior);
		// Assert
		assertTrue(actual.ok);
		assertEquals("POST", actual.requestMethod);
	}

	/**
	 * ヘッダーは使用しない設定でNGが返る.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: POST
	 * ・拡張ヘッダー: 未設定
	 * ・個別の拡張ヘッダー利用指定: NOT_DEFINE
	 * 許可するHTTPメソッド
	 * ・PUT
	 * チェック設定
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * [期待値]
	 * ・チェックはNG
	 * ・チェックしたメソッドはPOST
	 */
	@Test
	public void testCheck03() {
		// Arrange
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("POST");
		HttpMethod[] allows = new HttpMethod[]{HttpMethod.PUT};
		ExtensionHeaderBehavior extensionHeaderBehavior = ExtensionHeaderBehavior.NOT_DEFINE;

		HttpMethodCheckerImpl checker = new HttpMethodCheckerImpl();
		checker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);
		// Act
		CheckResult actual = checker.check(request, allows, extensionHeaderBehavior);
		// Assert
		assertFalse(actual.ok);
		assertEquals("POST", actual.requestMethod);
	}

}
