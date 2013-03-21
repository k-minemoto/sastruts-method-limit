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
package net.jp.saf.sastruts.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.junit.Test;

public class CheckResultTest {

	@Test
	public void test_ok() {
		// Arrange
		String method = "POST";
		HttpMethod[] allows = {HttpMethod.GET_POST};
		String expected = "CheckResult:{ok=true,httpMethod=POST,allows=[GET_POST]}";
		// Act
		CheckResult actual = CheckResult.ok(method, allows);
		// Assert
		assertTrue(actual.ok);
		assertEquals(expected, actual.toString());
	}

	@Test
	public void test_ng() {
		// Arrange
		String method = "PROPFIND";
		HttpMethod[] allows = {HttpMethod.GET, HttpMethod.POST};
		String expected = "CheckResult:{ok=false,httpMethod=PROPFIND,allows=[GET,POST]}";
		// Act
		CheckResult actual = CheckResult.ng(method, allows);
		// Assert
		assertFalse(actual.ok);
		assertEquals(expected, actual.toString());
	}

}
