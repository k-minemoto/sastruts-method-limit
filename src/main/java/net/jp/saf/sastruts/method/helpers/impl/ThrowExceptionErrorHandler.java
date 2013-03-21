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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.method.enums.HttpMethod;
import net.jp.saf.sastruts.method.exception.MethodNotAllowedRuntimeException;

/**
 * {@link MethodNotAllowedRuntimeException}を発生させるハンドラです.
 *
 * @author k-minemoto
 */
public class ThrowExceptionErrorHandler extends AbstractErrorHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 2971209822665762660L;

	/**
	 * {@inheritDoc}
	 */
	public String error(HttpServletRequest request, HttpServletResponse response, HttpMethod[] allowed, String method) {
		throwMethodNotAllowedRuntimeException(allowed, method, null);
		return null;
	}
}
