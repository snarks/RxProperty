/*
 * Copyright 2017 James Cruz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.snarks.rxproperty

import org.junit.Assert.assertEquals
import org.junit.Test

class RxPropertyTest {
	@Test fun subscribe() {
		val delegate = RxProperty("initial")
		var field by delegate

		val rxTester = delegate
				.doOnNext { println("Emitted: $it") }
				.test()

		field = "One"
		assertEquals("One", field)

		rxTester.assertValue("One")

		field = "Two"
		assertEquals("Two", field)

		rxTester.assertValues("One", "Two")

		field = "Two"
		assertEquals("Two", field)

		rxTester.assertValues("One", "Two", "Two")

		field = "Three"
		assertEquals("Three", field)

		rxTester.assertValues("One", "Two", "Two", "Three")
				.assertNotTerminated()
	}
}
