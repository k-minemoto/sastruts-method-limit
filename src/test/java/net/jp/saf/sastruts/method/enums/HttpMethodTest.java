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

import static org.junit.Assert.*;

import net.jp.saf.sastruts.method.enums.HttpMethod;

import org.junit.Test;

public class HttpMethodTest {

	@Test
	public void testCheck_GET() {
		// Arrange
		HttpMethod target = HttpMethod.GET;
		// Act
		// Assert
		assertEquals(true,  target.check("get"));
		assertEquals(true,  target.check("GET"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_POST() {
		// Arrange
		HttpMethod target = HttpMethod.POST;
		// Act
		// Assert
		assertEquals(true,  target.check("post"));
		assertEquals(true,  target.check("POST"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_PUT() {
		// Arrange
		HttpMethod target = HttpMethod.PUT;
		// Act
		// Assert
		assertEquals(true,  target.check("put"));
		assertEquals(true,  target.check("PUT"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_DELETE() {
		// Arrange
		HttpMethod target = HttpMethod.DELETE;
		// Act
		// Assert
		assertEquals(true,  target.check("delete"));
		assertEquals(true,  target.check("DELETE"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_HEAD() {
		// Arrange
		HttpMethod target = HttpMethod.HEAD;
		// Act
		// Assert
		assertEquals(true,  target.check("head"));
		assertEquals(true,  target.check("HEAD"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_OPTIONS() {
		// Arrange
		HttpMethod target = HttpMethod.OPTIONS;
		// Act
		// Assert
		assertEquals(true,  target.check("options"));
		assertEquals(true,  target.check("OPTIONS"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_TRACE() {
		// Arrange
		HttpMethod target = HttpMethod.TRACE;
		// Act
		// Assert
		assertEquals(true,  target.check("trace"));
		assertEquals(true,  target.check("TRACE"));
		assertEquals(false, target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("GET"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_GET_POST() {
		// Arrange
		HttpMethod target = HttpMethod.GET_POST;
		// Act
		// Assert
		assertEquals(true,  target.check("get"));
		assertEquals(true,  target.check("GET"));
		assertEquals(true,  target.check("post"));
		assertEquals(true,  target.check("POST"));
		assertEquals(false, target.check("PUT"));
		assertEquals(false, target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_REST_CRUD() {
		// Arrange
		HttpMethod target = HttpMethod.REST_CRUD;
		// Act
		// Assert
		assertEquals(true,  target.check("get"));
		assertEquals(true,  target.check("GET"));
		assertEquals(true,  target.check("post"));
		assertEquals(true,  target.check("POST"));
		assertEquals(true,  target.check("put"));
		assertEquals(true,  target.check("PUT"));
		assertEquals(true,  target.check("delete"));
		assertEquals(true,  target.check("DELETE"));
		assertEquals(false, target.check("HEAD"));
		assertEquals(false, target.check("OPTIONS"));
		assertEquals(false, target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}

	@Test
	public void testCheck_REST_ALL() {
		// Arrange
		HttpMethod target = HttpMethod.ALL;
		// Act
		// Assert
		assertEquals(true,  target.check("get"));
		assertEquals(true,  target.check("GET"));
		assertEquals(true,  target.check("post"));
		assertEquals(true,  target.check("POST"));
		assertEquals(true,  target.check("put"));
		assertEquals(true,  target.check("PUT"));
		assertEquals(true,  target.check("delete"));
		assertEquals(true,  target.check("DELETE"));
		assertEquals(true,  target.check("head"));
		assertEquals(true,  target.check("HEAD"));
		assertEquals(true,  target.check("options"));
		assertEquals(true,  target.check("OPTIONS"));
		assertEquals(true,  target.check("trace"));
		assertEquals(true,  target.check("TRACE"));
		assertEquals(false, target.check("PROPFIND"));
		assertEquals(false, target.check(""));
		assertEquals(false, target.check(null));
	}
}
