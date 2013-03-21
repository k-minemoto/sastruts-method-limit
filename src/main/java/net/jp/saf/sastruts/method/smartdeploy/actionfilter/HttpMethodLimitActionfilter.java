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
package net.jp.saf.sastruts.method.smartdeploy.actionfilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.actionfilter.Actionfilter;
import net.jp.saf.sastruts.actionfilter.FilterContext;
import net.jp.saf.sastruts.actionfilter.FilterExecutor;
import net.jp.saf.sastruts.method.CheckResult;
import net.jp.saf.sastruts.method.HttpMethodChecker;
import net.jp.saf.sastruts.method.annotation.HttpMethodLimit;
import net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior;
import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.apache.struts.action.ActionForward;

/**
 * Actionの実行メソッド実行前に、HTTPメソッドチェックを行います.
 *
 * @author k-minemoto
 *
 */
public class HttpMethodLimitActionfilter implements Actionfilter {
	/**
	 * チェック実行済みをHTTP属性に設定する時のキー
	 */
	public static final String MARK_CHECKED = "HttpMethodLimitActionfilter#checked";
	@Resource
	protected HttpMethodChecker httpMethodChecker;

	/**
	 * Actionの実行メソッド呼び出し前に、HTTPメソッドのチェックを行います.
	 */
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response, FilterContext context,
			FilterExecutor executor) {
		final HttpMethodLimit methodLimit = context.getActionMethod().getAnnotation(HttpMethodLimit.class);
		if (request.getAttribute(MARK_CHECKED) != null && (methodLimit == null || !methodLimit.checkForward())) {
			return executor.execute(request, response, context);
		}
		HttpMethod[] allows = null;
		ExtensionHeaderBehavior extensionHeaderBehavior = null;
		if (methodLimit != null) {
			allows = methodLimit.value();
			extensionHeaderBehavior = methodLimit.extensionHeader();
		}
		CheckResult check = httpMethodChecker.check(request, allows, extensionHeaderBehavior);
		request.setAttribute(MARK_CHECKED, Boolean.TRUE);
		if (check.ok) {
			return executor.execute(request, response, context);
		}
		String returnPath = httpMethodChecker.handleError(request, response, check.allows, check.requestMethod);
		return context.getActionMapping().createForward(returnPath);
	}

}
