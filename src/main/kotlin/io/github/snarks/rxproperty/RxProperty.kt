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

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A mutable delegated property that is also an [Observable]
 *
 * **Example:**
 * ```kotlin
 * val fooProperty = RxProperty("initial")
 * var foo by fooProperty
 *
 * fooProperty.subscribe(::println)
 * foo = "hello"
 * foo = "hello"
 * foo = "world"
 * // this will print 'hello', 'hello' and 'world'
 * ```
 *
 * This class is an unbounded observable, so it will never emit `onError` or `onComplete`. It will emit `onNext`
 * immediately after assigning a value to it.
 *
 * It's **not** safe to _assign_ values to `RxProperty` from multiple threads, but the usual RxJava operations are
 * thread-safe.
 */
class RxProperty<T>(initialValue: T) : Observable<T>(), ReadWriteProperty<Any?, T> {

	private val relay = PublishSubject.create<T>()
	private var value = initialValue

	override fun getValue(thisRef: Any?, property: KProperty<*>) = value

	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		this.value = value
		relay.onNext(value)
	}

	override fun subscribeActual(observer: Observer<in T>?) = relay.subscribe(observer)
}
