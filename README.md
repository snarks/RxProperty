[![Release](https://jitpack.io/v/snarks/RxProperty.svg)](https://jitpack.io/#snarks/RxProperty)

# RxProperty
A [Delegated Property](https://kotlinlang.org/docs/reference/delegated-properties.html) that is also an [RxJava](https://github.com/ReactiveX/RxJava) Observable.

## Example
Code:
```kotlin
val fooProperty = RxProperty("initial")
var foo by fooProperty

fooProperty.subscribe(::println)
foo = "hello"
foo = "hello"
foo = "world"
```

Output:
```
hello
hello
world
```

## Adding RxProperty to your Project

You can add this project as a dependency via [JitPack](https://jitpack.io/).

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
dependencies {
     compile 'io.github.snarks:RxProperty:1.0'
}
```
(_`com.github.snarks` will also work_)

## Behaviour
- It will never emit `onError` or `onComplete`
- It will emit `onNext` immediately after a value is assigned to it
- It does not operate by default on a particular `Scheduler`

## Thread Safety
It's **not** safe to _assign_ values to `RxProperty` from multiple threads, but the usual RxJava operations are thread-safe.
