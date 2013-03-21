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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.StringUtil;

/**
 * ステータス405「Method Not Allowed」を返すハンドラです.
 *
 * @author k-minemoto
 */
public class MethodNotAllowedErrorHandler extends AbstractErrorHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 5313099168871661730L;

	protected boolean disableAllowHeader;

	protected String forward;

	private boolean doForward;

	/**
	 * Allowヘッダーを無効にするかどうかを設定します.
	 *
	 * @param disableAllowHeader Allowヘッダーを付与しない場合true. デフォルトはfalse(付与する)
	 */
	@Binding(bindingType=BindingType.MAY)
	public void setDisableAllowHeader(boolean disableAllowHeader) {
		this.disableAllowHeader = disableAllowHeader;
	}

	/**
	 * エラー時のフォワード先を設定します.
	 *
	 * @param forward フォワード先. デフォルトはnull
	 */
	@Binding(bindingType=BindingType.MAY)
	public void setForward(String forward) {
		this.forward = forward;
		this.doForward = StringUtil.isNotEmpty(this.forward);
	}
	/**
	 * {@inheritDoc}
	 */
	public String error(HttpServletRequest request, HttpServletResponse response, HttpMethod[] allowed, String method) {
		try {
			if(!disableAllowHeader) {
				response.addHeader("Allow", allowHeaderValue(allowed));
			}
			if(this.doForward) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			} else {
				response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} catch (IOException e) {
			throwMethodNotAllowedRuntimeException(allowed, method, e);
		}
		return this.forward;
	}
}
