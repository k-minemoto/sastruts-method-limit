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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.junit.Test;

public class AbstractErrorHandlerTest extends AbstractErrorHandler {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * 単項目が正しく戻ってくるか
	 */
	@Test
	public void testAllowHeaderValue_single() {
		assertEquals("", allowHeaderValue(null));
		assertEquals("", allowHeaderValue(new HttpMethod[]{}));
		assertEquals("GET", allowHeaderValue(new HttpMethod[]{HttpMethod.GET}));
		assertEquals("POST", allowHeaderValue(new HttpMethod[]{HttpMethod.POST}));
		assertEquals("PUT", allowHeaderValue(new HttpMethod[]{HttpMethod.PUT}));
		assertEquals("DELETE", allowHeaderValue(new HttpMethod[]{HttpMethod.DELETE}));
		assertEquals("HEAD", allowHeaderValue(new HttpMethod[]{HttpMethod.HEAD}));
		assertEquals("OPTIONS", allowHeaderValue(new HttpMethod[]{HttpMethod.OPTIONS}));
		assertEquals("TRACE", allowHeaderValue(new HttpMethod[]{HttpMethod.TRACE}));
		assertEquals("GET,POST", allowHeaderValue(new HttpMethod[]{HttpMethod.GET_POST}));
		assertEquals("DELETE,GET,POST,PUT", allowHeaderValue(new HttpMethod[]{HttpMethod.REST_CRUD}));
		assertEquals("DELETE,GET,HEAD,OPTIONS,POST,PUT,TRACE", allowHeaderValue(new HttpMethod[]{HttpMethod.ALL}));
	}

	/**
	 * 複合項目が重複無しで正しく戻ってくるか
	 */
	@Test
	public void testAllowHeaderValue_multi() {
		assertEquals("GET", allowHeaderValue(new HttpMethod[]{HttpMethod.GET, HttpMethod.GET, HttpMethod.GET}));
		assertEquals("GET,POST", allowHeaderValue(new HttpMethod[]{HttpMethod.GET_POST, HttpMethod.GET, HttpMethod.POST}));
		assertEquals("DELETE,GET,POST,PUT,TRACE", allowHeaderValue(new HttpMethod[]{HttpMethod.REST_CRUD, HttpMethod.PUT, HttpMethod.TRACE}));
	}

	/**
	 * NON-OP
	 */
	public String error(HttpServletRequest request, HttpServletResponse response, HttpMethod[] allowed, String method) {
		return null;
	}

}
