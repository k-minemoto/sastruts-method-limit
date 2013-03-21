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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jp.saf.sastruts.actionfilter.Actionfilter;
import net.jp.saf.sastruts.actionfilter.FilterContext;
import net.jp.saf.sastruts.actionfilter.FilterExecutor;
import net.jp.saf.sastruts.method.HttpMethodChecker;
import net.jp.saf.sastruts.method.annotation.HttpMethodLimit;
import net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior;
import net.jp.saf.sastruts.method.enums.HttpMethod;
import net.jp.saf.sastruts.method.helpers.impl.NotImplementedErrorHandler;
import net.jp.saf.sastruts.method.impl.HttpMethodCheckerImpl;
import net.jp.saf.sastruts.method.smartdeploy.actionfilter.HttpMethodLimitActionfilter;

import org.apache.struts.action.ActionForward;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.MethodUtil;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

@RunWith(Seasar2.class)
public class HttpMethodLimitActionfilterTest {

	private TestContext ctx;

	private FilterExecutor executor = new FilterExecutor(Arrays.asList((Actionfilter)new ExecActionfilter()));

	public void before() {
		ctx.register(FooAction.class, "fooAction");
	}
	/**
	 * アノテーション無しの場合、チェッカーのアノテーション無し時のデフォルト設定でチェックする
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * チェッカー
	 * ・defaultAllows: POST
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * 実行メソッド
	 * ・HttpMethodLimit: 無し
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスのAllowヘッダーにPOSTが設定される
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行されない
	 */
	@Test
	public void testExecute_noAnnotation1() {
		// Arrange
		String methodName = "noAnnotation1";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.POST));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);

		FooAction action = new FooAction();
		FilterContext context = createContext(action, methodName);

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, context, executor);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("POST", response.getHeader("Allow"));
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertFalse(action.executed);
	}

	/**
	 * アノテーションありの場合、アノテーションの設定でチェックする.
	 * アノテーションのvalueが未設定の場合、チェッカーのアノテーション用デフォルト値を使用する
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: PUT
	 * ・拡張ヘッダー: 未設定
	 * チェッカー
	 * ・defaultAllows: PUT
	 * ・defaultAnnotationAllows: GET_POST
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * 実行メソッド
	 * ・HttpMethodLimit: 設定
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスのAllowヘッダーにGET,POSTが設定される
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行されない
	 */
	@Test
	public void testExecute_annotationTest1() {
		// Arrange
		String methodName = "annotationTest1";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("PUT");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.PUT));
		httpMethodChecker.setDefaultAnnotationAllows(Arrays.asList(HttpMethod.GET_POST));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);

		FooAction action = new FooAction();
		FilterContext context = createContext(action, methodName);

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, context, executor);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("GET,POST", response.getHeader("Allow"));
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertFalse(action.executed);
	}

	/**
	 * アノテーションありの場合、アノテーションの設定でチェックする.
	 * アノテーションのvalueが設定されていれば、そちらを使用する
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * チェッカー
	 * ・defaultAllows: GET
	 * ・defaultAnnotationAllows: GET
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * 実行メソッド
	 * ・HttpMethodLimit: 設定
	 * 　・value: POST
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスのAllowヘッダーにPOSTが設定される
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行されない
	 */
	@Test
	public void testExecute_annotationTest2() {
		// Arrange
		String methodName = "annotationTest2";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setDefaultAnnotationAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);

		FooAction action = new FooAction();
		FilterContext context = createContext(action, methodName);

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, context, executor);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("POST", response.getHeader("Allow"));
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertFalse(action.executed);
	}

	/**
	 * アノテーションありの場合、アノテーションの設定でチェックする.
	 * アノテーションで拡張ヘッダーの指定がされていれば、そちらを使用する
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: POST
	 * チェッカー
	 * ・defaultAllows: GET
	 * ・defaultAnnotationAllows: GET
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * 実行メソッド
	 * ・HttpMethodLimit: 設定
	 * 　・extensionHeader: HEADER_ONLY
	 * [期待値]
	 * ・レスポンスステータスは405に変更される
	 * ・レスポンスのAllowヘッダーにGETが設定される
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行されない
	 */
	@Test
	public void testExecute_annotationTest3() {
		// Arrange
		String methodName = "annotationTest3";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		request.addHeader(HttpMethodChecker.DEFAULT_EXTENSION_HEADER_NAME, "POST");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setDefaultAnnotationAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);

		FooAction action = new FooAction();
		FilterContext context = createContext(action, methodName);

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, context, executor);
		// Assert
		assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
		assertEquals("GET", response.getHeader("Allow"));
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertFalse(action.executed);
	}

	/**
	 * 2度目のチェックは、デフォルトは実行しない.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: GET
	 * ・拡張ヘッダー: 未設定
	 * チェッカー
	 * ・defaultAllows: GET
	 * ・defaultAnnotationAllows: GET
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * 実行メソッド1回目
	 * ・HttpMethodLimit: 設定
	 * 　・value: GET
	 * 実行メソッド1回目
	 * ・HttpMethodLimit: 設定
	 * 　・value: POST
	 * [期待値]
	 * ・レスポンスステータスは200
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行される
	 */
	@Test
	public void testExecute_annotationTest4() {
		// Arrange
		String firstMethodName = "annotationTest4";
		String secondMethodName = "annotationTest2";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("GET");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setDefaultAnnotationAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);

		FooAction action = new FooAction();
		FilterContext firstContext = createContext(action, firstMethodName);
		FilterContext secondContext = createContext(action, secondMethodName);
		FilterExecutor executor2 = new FilterExecutor(Arrays.asList((Actionfilter)new ExecActionfilter()));

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, firstContext, executor);
		filter.execute(request, response, secondContext, executor2);
		// Assert
		assertEquals(MockHttpServletResponse.SC_OK, response.getStatus());
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertTrue(action.executed);
	}

	/**
	 * 指定したエラーハンドラがエラー処理を行う.
	 *
	 * [条件]
	 * HTTPリクエスト
	 * ・Method: POST
	 * ・拡張ヘッダー: 未設定
	 * チェッカー
	 * ・defaultAllows: GET
	 * ・defaultAnnotationAllows: GET
	 * ・defaultExtHeaderBehavior: NOT_USE
	 * ・errorHandler: NotImplementedErrorHandler
	 * 実行メソッド
	 * ・HttpMethodLimit: 設定
	 * [期待値]
	 * ・レスポンスステータスは500に変更される
	 * ・レスポンスのAllowヘッダーは存在しない
	 * ・リクエスト属性にチェック済みマークが付く
	 * ・Actionのメソッドは実行されない
	 */
	@Test
	public void testExecute_annotationTest5() {
		// Arrange
		String methodName = "annotationTest5";
		final MockHttpServletRequest request = (MockHttpServletRequest)RequestUtil.getRequest();
		request.setMethod("POST");
		final MockHttpServletResponse response = (MockHttpServletResponse)ResponseUtil.getResponse();
		response.setStatus(MockHttpServletResponse.SC_OK);

		HttpMethodCheckerImpl httpMethodChecker = new HttpMethodCheckerImpl();
		httpMethodChecker.setDefaultAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setDefaultAnnotationAllows(Arrays.asList(HttpMethod.GET));
		httpMethodChecker.setExtensionHeader(ExtensionHeaderBehavior.NOT_USE);
		httpMethodChecker.setErrorHandler(new NotImplementedErrorHandler());

		FooAction action = new FooAction();
		FilterContext context = createContext(action, methodName);

		HttpMethodLimitActionfilter filter = new HttpMethodLimitActionfilter();
		filter.httpMethodChecker = httpMethodChecker;
		// Act
		filter.execute(request, response, context, executor);
		// Assert
		assertEquals(MockHttpServletResponse.SC_NOT_IMPLEMENTED, response.getStatus());
		assertNull(response.getHeader("Allow"));
		assertEquals(Boolean.TRUE, request.getAttribute(HttpMethodLimitActionfilter.MARK_CHECKED));
		assertFalse(action.executed);
	}

	private FilterContext createContext(FooAction action, String methodName) {
		S2ActionMapping mapping = new S2ActionMapping();
		mapping.setPath("/foo");
		mapping.setComponentDef(ctx.getComponentDef("fooAction"));
		S2ExecuteConfig executeConfig = new S2ExecuteConfig();
		try {
			executeConfig.setMethod(FooAction.class.getMethod(methodName));
		} catch (Exception e) {
			fail(e.getMessage());
		}
		mapping.addExecuteConfig(executeConfig);
		return new FilterContext(mapping, executeConfig, action, action);
	}

	private static class ExecActionfilter implements Actionfilter {
		public ActionForward execute(HttpServletRequest request,
				HttpServletResponse response, FilterContext context,
				FilterExecutor executor) {
			MethodUtil.invoke(context.getActionMethod(), context.getAction(), null);
			return null;
		}
	}

	/**
	 * テスト用アクションクラス
	 */
	public static class FooAction {

		public boolean executed;

		@Execute(validator=false)
		public String noAnnotation1() {
			executed = true;
			return "noAnnotation1";
		}

		@HttpMethodLimit
		@Execute(validator=false)
		public String annotationTest1() {
			executed = true;
			return "annotationTest1";
		}

		@HttpMethodLimit({HttpMethod.POST})
		@Execute(validator=false)
		public String annotationTest2() {
			executed = true;
			return "annotationTest2";
		}

		@HttpMethodLimit(extensionHeader=ExtensionHeaderBehavior.HEADER_ONLY)
		@Execute(validator=false)
		public String annotationTest3() {
			executed = true;
			return "annotationTest3";
		}

		@HttpMethodLimit({HttpMethod.GET})
		@Execute(validator=false)
		public String annotationTest4() {
			// executeは変更しない
			return "annotationTest4";
		}

		@HttpMethodLimit
		@Execute(validator=false)
		public String annotationTest5() {
			executed = true;
			return "annotationTest5";
		}
	}
}
