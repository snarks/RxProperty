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
 * val foo by fooProperty
 *
 * fooProperty.subscribe(::println)
 * foo = "hello"
 * foo = "hello"
 * foo = "world"
 * // this will print 'hello', 'hello' and 'world'
 * ```
 *
 * This class uses an **unsynchronized**, **non-volatile** field to store its value. So you may need to use external
 * synchronization if you need to make multiple assignments from more than one thread.
 *
 * This class uses a [PublishSubject] for its subscribers. So even if it's not safe to _assign_ and _read_ values from
 * multiple threads, you can still _subscribe_ and do the usual RxJava operators safely.
 *
 * Every time a value is assigned to this property, it will emit that value to its subscribers (even if that value is
 * identical to the last one). Note that terminal signals (i.e. `onComplete` and `onError`) will never get emitted by
 * this class.
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
