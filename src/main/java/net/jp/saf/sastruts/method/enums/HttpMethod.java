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

import org.seasar.framework.util.StringUtil;

/**
 * HTTPメソッドの種類を表します.
 * 
 * <p>
 * RFCで決められているメソッドに加えて、よく使われる組み合わせを表す定義も含まれます.
 * </p>
 * 
 * @author k-minemoto
 */
public enum HttpMethod {
	/** GETメソッドを表します. */
	GET,
	/** POSTメソッドを表します. */
	POST,
	/** PUTメソッドを表します. */
	PUT,
	/** DELETEメソッドを表します. */
	DELETE,
	/** HEADメソッドを表します. */
	HEAD,
	/** OPTIONSメソッドを表します. */
	OPTIONS,
	/** TRACEメソッドを表します. */
	TRACE,
	/** GET,POSTの組み合わせを表します. */
	GET_POST() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean check(String method) {
			if (GET.check(method)) {
				return true;
			}
			return POST.check(method);
		}
	},
	/** GET,POST,PUT,DELETEの組み合わせを表します. */
	REST_CRUD(){
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean check(String method) {
			if (GET_POST.check(method)) {
				return true;
			}
			if (PUT.check(method)) {
				return true;
			}
			return DELETE.check(method);
		}
	},
	/** GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACEの組み合わせを表します. */
	ALL(){
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean check(String method) {
			if (REST_CRUD.check(method)) {
				return true;
			}
			if (HEAD.check(method)) {
				return true;
			}
			if (OPTIONS.check(method)) {
				return true;
			}
			return TRACE.check(method);
		}
	};
	/**
	 * このクラスが表すHTTPメソッドに該当するか返します.
	 * <p>
	 * 文字の大小は無視します.
	 * </p>
	 *
	 * @param method HTTPメソッドの文字列
	 * @return 該当する場合true
	 */
	public boolean check(String method) {
		if (StringUtil.isEmpty(method)) {
			return false;
		}
		return name().equalsIgnoreCase(method);
	}
}
