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
import static org.junit.Assert.fail;
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
public class ThrowExceptionErrorHandlerTest extends ThrowExceptionErrorHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * MethodNotAllowedRuntimeExceptionが発生する.
	 *
	 * [条件]
	 * ・allowed: POST
	 * ・method: <SCRIPT></SCRIPT>
	 * [期待値]
	 * ・MethodNotAllowedRuntimeExceptionが発生する
	 * ・ExceptionのhttpMethodには&lt;SCRIPT&gt;&lt;/SCRIPT&gt;が入る
	 * ・ExceptionのallowedMethodsにはPOSTが入る
	 */
	@Test
	public void testError1() {
		// Arrange
		ThrowExceptionErrorHandler handler = new ThrowExceptionErrorHandler();

		HttpMethod[] allowed = new HttpMethod[]{HttpMethod.POST};
		String method = "<SCRIPT></SCRIPT>";

		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod(method);
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		try {
			// Act
			handler.error(request, response, allowed, method);
			fail("No exception.");
		} catch (MethodNotAllowedRuntimeException actual) {
			// Assert
			assertEquals("&lt;SCRIPT&gt;&lt;/SCRIPT&gt;", actual.getHttpMethod());
			assertEquals("POST", actual.getAllowedMethods());
		} catch (Throwable t) {
			fail("Unknown error:" + t.getMessage());
		}
	}

}
