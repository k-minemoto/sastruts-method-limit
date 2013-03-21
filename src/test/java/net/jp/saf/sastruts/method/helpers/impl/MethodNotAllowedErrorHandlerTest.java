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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.jp.saf.sastruts.method.enums.HttpMethod;
import net.jp.saf.sastruts.method.exception.MethodNotAllowedRuntimeException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.unit.Seasar2;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

@RunWith(Seasar2.class)
public class MethodNotAllowedErrorHandlerTest extends MethodNotAllowedErrorHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Allowヘッダーを出力し、forwardはnullを返す.
	 *
	 * [条件]
	 * ・disableAllowHeader: false
	 * ・forward: null
	 * ・allowed: POST
	 * ・method: GET
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスのAllowヘッダーにPOSTが設定される
	 * ・戻り値はnullが戻る
	 */
	@Test
	public void testError1() {
		// Arrange
		final boolean disableAllowHeader = false;
		final String forward = null;
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "GET";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);
		// Act
		String actual = handler.error(request, response, allowed, method);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("POST", response.getHeader("Allow"));
		assertNull(actual);
	}

	/**
	 * Allowヘッダーを出力せず、forwardはnullを返す.
	 *
	 * [条件]
	 * ・disableAllowHeader: true
	 * ・forward: null
	 * ・allowed: POST
	 * ・method: GET
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスにAllowヘッダーが存在しない
	 * ・戻り値はnullが戻る
	 */
	@Test
	public void testError2() {
		// Arrange
		final boolean disableAllowHeader = true;
		final String forward = null;
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "GET";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);
		// Act
		String actual = handler.error(request, response, allowed, method);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertNull(response.getHeader("Allow"));
		assertNull(actual);
	}

	/**
	 * Allowヘッダーを出力せず、forwardは"WEB-INF/view/common/err405.jsp"を返す.
	 *
	 * [条件]
	 * ・disableAllowHeader: true
	 * ・forward: "WEB-INF/view/common/err405.jsp"
	 * ・allowed: POST
	 * ・method: GET
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスにAllowヘッダーが存在しない
	 * ・戻り値は"WEB-INF/view/common/err405.jsp"が戻る
	 */
	@Test
	public void testError3() {
		// Arrange
		final boolean disableAllowHeader = true;
		final String forward = "WEB-INF/view/common/err405.jsp";
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "GET";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);
		// Act
		String actual = handler.error(request, response, allowed, method);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertNull(response.getHeader("Allow"));
		assertEquals(forward, actual);
	}

	/**
	 * Allowヘッダーを出力し、forwardは"WEB-INF/view/common/err405.jsp"を返す.
	 *
	 * [条件]
	 * ・disableAllowHeader: false
	 * ・forward: "WEB-INF/view/common/err405.jsp"
	 * ・allowed: POST
	 * ・method: GET
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスにAllowヘッダーが存在しない
	 * ・戻り値は"WEB-INF/view/common/err405.jsp"が戻る
	 */
	@Test
	public void testError4() {
		// Arrange
		final boolean disableAllowHeader = false;
		final String forward = "WEB-INF/view/common/err405.jsp";
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "GET";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);
		// Act
		String actual = handler.error(request, response, allowed, method);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("POST", response.getHeader("Allow"));
		assertEquals(forward, actual);
	}

	/**
	 * sendErrorでIOExceptionが発生すると、MethodNotAllowedRuntimeExceptionを発生させる.
	 *
	 * [条件]
	 * ・disableAllowHeader: true
	 * ・forward: ""
	 * ・allowed: POST
	 * ・method: GET
	 * ・sendErrorでIOExceptionが発生
	 * [期待値]
	 * ・MethodNotAllowedRuntimeExceptionが発生する
	 * ・ExceptionのhttpMethodにはGETが入る
	 * ・ExceptionのallowedMethodsにはPOSTが入る
	 */
	@Test
	public void testError5() {
		// Arrange
		final boolean disableAllowHeader = true;
		final String forward = "";
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "GET";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final HttpServletResponse response = new HttpServletResponseWrapper(ResponseUtil.getResponse()) {
			/**
			 * 常にIOExceptionを発生させる
			 */
			@Override
			public void sendError(int sc, String msg) throws IOException {
				throw new IOException("sc="+sc);
			}
			/**
			 * 常にIOExceptionを発生させる
			 */
			@Override
			public void sendError(int sc) throws IOException {
				throw new IOException("sc="+sc);
			}
		};
		response.setStatus(MockHttpServletResponse.SC_OK);
		try {
			// Act
			handler.error(request, response, allowed, method);
			fail("No exception.");
		} catch (MethodNotAllowedRuntimeException actual) {
			// Assert
			assertEquals("GET", actual.getHttpMethod());
			assertEquals("POST", actual.getAllowedMethods());
		} catch (Throwable t) {
			fail("Unknown error:" + t.getMessage());
		}
	}

	/**
	 * sendErrorでIOExceptionが発生すると、MethodNotAllowedRuntimeExceptionを発生させる.
	 * 不正なHTTPメソッドが渡された時、HTMLエスケープされている(拡張ヘッダーから取った場合を想定)
	 *
	 * [条件]
	 * ・disableAllowHeader: true
	 * ・forward: null
	 * ・allowed: POST
	 * ・method: <GET>
	 * ・sendErrorでIOExceptionが発生
	 * [期待値]
	 * ・MethodNotAllowedRuntimeExceptionが発生する
	 * ・ExceptionのhttpMethodには&lt;GET&gt;が入る
	 * ・ExceptionのallowedMethodsにはPOSTが入る
	 */
	@Test
	public void testError6() {
		// Arrange
		final boolean disableAllowHeader = true;
		final String forward = null;
		MethodNotAllowedErrorHandler handler = new MethodNotAllowedErrorHandler();
		handler.setDisableAllowHeader(disableAllowHeader);
		handler.setForward(forward);

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "<GET>";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final HttpServletResponse response = new HttpServletResponseWrapper(ResponseUtil.getResponse()) {
			/**
			 * 常にIOExceptionを発生させる
			 */
			@Override
			public void sendError(int sc, String msg) throws IOException {
				throw new IOException("sc="+sc);
			}
			/**
			 * 常にIOExceptionを発生させる
			 */
			@Override
			public void sendError(int sc) throws IOException {
				throw new IOException("sc="+sc);
			}
		};
		response.setStatus(MockHttpServletResponse.SC_OK);
		try {
			// Act
			handler.error(request, response, allowed, method);
			fail("No exception.");
		} catch (MethodNotAllowedRuntimeException actual) {
			// Assert
			assertEquals("&lt;GET&gt;", actual.getHttpMethod());
			assertEquals("POST", actual.getAllowedMethods());
		} catch (Throwable t) {
			fail("Unknown error:" + t.getMessage());
		}
	}

}
