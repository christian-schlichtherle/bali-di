[![Release Notes](https://img.shields.io/github/release/christian-schlichtherle/bali-di.svg)](https://github.com/christian-schlichtherle/bali-di/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/global.namespace.bali/bali-annotations)](https://search.maven.org/artifact/global.namespace.bali/bali-annotations)
[![Apache License 2.0](https://img.shields.io/github/license/christian-schlichtherle/bali-di.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Git Maven Workflow](https://img.shields.io/github/workflow/status/christian-schlichtherle/bali-di/Java%20CI%20with%20Maven)](https://github.com/christian-schlichtherle/bali-di/actions?query=workflow%3A%22Java+CI+with+Maven%22)

# Bali DI

Bali DI is a Java code generator for dependency injection. As a pure annotation processor, it works with any Java
compiler which is compliant to Java 8 or later.

> Bali is also an [island](https://en.wikipedia.org/wiki/Bali) between Java and Lombok in Indonesia.
> For disambiguation, the name of this project is "Bali DI", not just "Bali", where DI is an acronym for [_dependency
> injection_](https://en.wikipedia.org/wiki/Dependency_injection).
> In code however, the term "DI" is dropped because there is no ambiguity in this context.

# Getting Started

Bali DI provides an annotation processor for the Java compiler to do its magic. If you use Maven, you need to add the
following dependency to your project:

```xml
<dependency>
    <groupId>global.namespace.bali</groupId>
    <artifactId>bali-java</artifactId>
    <!-- see https://github.com/christian-schlichtherle/bali-di/releases/latest -->
    <version>0.6.0</version>
    <scope>provided</scope>
</dependency>
```

Note that this is a compile-time-only dependency - there is no runtime dependency of your code on Bali DI!

## Sample Code

This project uses a modular build. The module [`bali-samples`](samples) provides lots of sample code showing how to use
the annotations and generated source code.
`bali-samples` is not published on Maven Central however - it is only available as source code. You can browse the
source code [here](samples/src/main/java/bali/sample) or clone this project.

The module `bali-samples` is organized into different packages with each package representing an individual,
self-contained showcase. For example, the package [`bali.sample.greeting`](samples/src/main/java/bali/sample/greeting)
showcases a glorified way to produce a simple "Hello world!" message by using different components with dependency
injection.

Please forgive me for not providing real-world samples, but I believe learning a new tool is simpler if you don't have
to learn about a specific problem domain first.

Enjoy!

# Motivation

Does the world really need yet another tool for an idiom as simple as dependency injection? Well, I've been frustrated
with existing popular solutions like Spring, Guice, PicoContainer, Weld/CDI, MacWire etc in various projects for years,
so I think the answer is "yes, please"!

Most of these tools try to resolve dependencies at runtime, which is already the first mistake:
By deferring the dependency resolution to runtime, you need to compile, unit-test, integration-test, package and start
up your app first. But chances are that no matter what your test coverage is, you may still find out that your app is
not working properly or not even starting up because something is wrong with its dependency graph.

Furthermore, Spring is well-known for being slow to start-up because it typically scans the entire byte code of your
app for its annotations.
Of course, you can avoid that entirely by going down XML configuration hell - oh my!

Then again, if something is not working because you forgot to sprinkle your code with a qualifier annotation in order to
discriminate two dependencies of the same type but with different semantics (say, a `String` representing a username and
a password - yeah, don't do that), then you may spend a lot of time debugging and analyzing this problem.

By the way, good luck with debugging [IOC](https://en.wikipedia.org/wiki/Inversion_of_control) containers:
If your code is not called because you forgot to add an annotation somewhere then debugging it is pointless because...
as I said, it's not called.

For worse, dependency injection at runtime is not even type-safe when it comes to generic classes due to type erasure.
For example, your component may get a `List<String>` injected when it actually wants a `List<User>`.

Last but not least, all of these tools (even Macwire, which is for Scala) support dependency injection into
constructors, methods and fields. For Java, this means you either have to write a lot of boiler plate code, for example
the constructor plus the fields if you want to use constructor injection (which is the least bad of the three options),
or your code gets hardwired to your DI tool by sprinkling it with even more annotations, for example Spring's
`@Autowired` on fields (again - don't do that).

## A New Approach

To resolve these issues, Bali DI employs a completely different approach:

1. Dependency resolution happens at compile-time by an annotation processor which generates Java source code to wire up
   your components.
2. Dependencies are made accessible to your components by calling abstract, parameterless methods.
3. Dependencies are created lazily and - if desired - cached in modules, which are auto-generated, abstract classes.

There are many features and benefits resulting from this approach:

<dl>
<dt>Quick trouble-shooting
<dd>If a dependency is missing, or doesn't have a compatible type, the compiler will tell you.
<dt>Completely type-safe, including generics
<dd>A <code>Map&lt;User, List&lt;Order&gt;&gt;</code> never gets assigned to a <code>Map&lt;String, String&gt;</code>.
<dt>Dependencies are identified by name, not just type
<dd>A <code>String username()</code> is different from a <code>String password()</code> without the need for any
    qualifier annotations.
<dt>All dependency access is inherently lazy
<dd>Dependencies are created and, if desired, cached just-in-time when they are accessed without unsolicited overhead in
    memory size or runtime complexity.
    If you follow this through, it results in a more responsive behavior of your app than if everything is done in an
    eager fashion.
<dt>Quick application startup
<dd>No need to scan the byte code of your app to figure out how to wire your components.
<dt>No reflection
<dd>It's not just faster without reflection, but also avoids problems with byte code analysis or obfuscation tools.
<dt>No runtime dependency
<dd>No need to worry about the diamond dependency problem, large dependencies or dependency management at all.
<dt>Tailor-made code generation
<dd>You can inspect, test and debug the generated source code which exactly matches the requirements of your project
    without unsolicited overhead in memory size or runtime complexity.
</dl>

# Quick Demo

The following simple app uses a generic clock to print the current date and time.
Again, please excuse me for not picking a less contrived example:

```java
package bali.sample.genericclock;

import bali.Cache;
import bali.Module;

import java.util.Date;
import java.util.concurrent.Callable;

import static java.lang.System.out;

@Module
public interface GenericClockApp {

    @Cache
    Callable<Date> clock();

    Date call() throws Exception;

    default void run() throws Exception {
        out.printf("It is now %s.\n", clock().call());
    }

    static void main(String... args) throws Exception {
        GenericClockApp$.new$().run();
    }
}
```

This results in the annotation processor to generate the following code for you:

```java
package bali.sample.genericclock;

/*
@javax.annotation.Generated(
    comments = "round=1, version=0.6.0",
    date = "2021-01-17T20:21:06.023+01:00",
    value = "bali.java.AnnotationProcessor"
)
*/
public abstract class GenericClockApp$ implements bali.sample.genericclock.GenericClockApp {

    static bali.sample.genericclock.GenericClockApp new$() {
        return new GenericClockApp$() {
        };
    }

    private volatile java.util.concurrent.Callable<java.util.Date> clock;

    @Override
    public java.util.concurrent.Callable<java.util.Date> clock() {
        java.util.concurrent.Callable<java.util.Date> value;
        if (null == (value = this.clock)) {
            synchronized (this) {
                if (null == (value = this.clock)) {
                    this.clock = value = new Callable$1();
                }
            }
        }
        return value;
    }

    @Override
    public java.util.Date call() throws java.lang.Exception {
        return new java.util.Date();
    }

    private final class Callable$1 implements java.util.concurrent.Callable<java.util.Date> {
        
        @Override
        public java.util.Date call() throws java.lang.Exception {
            return GenericClockApp$.this.call();
        }
    }
}
```

You may recognize that the generated code is a blend of the following design patterns:

<dl>
<dt>Abstract Factory
<dd>The interface <code>GenericClockApp</code> is an abstract factory because clients get access to the clock by calling
    the abstract method <code>Callable&lt;Date&gt; clock()</code> without knowing the implementation class.
    The same is true for the method <code>Date date()</code>.
<dt>Factory Method
<dd>The class <code>GenericClockApp$</code> implements these factory methods to create, and in case of
    <code>Callable&lt;Date&gt; clock()</code> also cache, the singleton clock and its current time.
    The class is abstract and provides a static method constructor in order to control its instantiation and inhibit the
    uncontrolled proliferation of its type while still allowing for subclassing - the latter feature is required for
    advanced scenarios like modular apps.
<dt>Template Method
<dd>The interface <code>Callable&lt;T&gt;</code> defines the template method <code>T call() throws Exception</code> to
    return a completely generic instance <code>T</code>, whereby it may terminate with an exception instead of a result.
<dt>Mediator
<dd>The inner class <code>Callable$1</code> holds an implicit reference to its enclosing class
    <code>GenericClockApp$</code>.
    It uses this reference to obtain the current Date when calling its method <code>Date call() throws Exception</code>.
</dl>

If there were more dependencies, these patterns would be repeatedly applied to the abstract methods of these types.

# Advanced Features

The [sample code](samples) also showcases the following advanced features: 

+ Abstract factory methods in types annotated with [`@Module`](annotations/src/main/java/bali/Module.java) to use their
  (possible generic) parameters as dependencies.
+ Caching the return value of parameterless, overridable methods in a module or dependency type by applying the
  [`@Cache`](annotations/src/main/java/bali/Cache.java) annotation to the method.
+ Selecting a caching strategy by applying one of `@Cache(DISABLED)`, `@Cache(NOT_THREAD_SAFE)`, `@Cache(THREAD_SAFE)`
  or `@Cache(THREAD_LOCAL)`.
  The default value of the `@Cache` annotation is the thread-safe caching strategy.
+ Specifying a default caching strategy for all abstract, parameterless methods in a module or dependency type by
  applying the `@Cache` annotation to the type.
+ Taking advantage of interface segregation by applying
  [`@Make(MyImplementation.class)`](annotations/src/main/java/bali/Make.java) to methods returning `MyInterface`, where
  `MyImplementation` is a subtype of `MyInterface`. 
+ Specifying the name of accessed methods, fields or parameters by applying
  [`@Lookup("myname")`](annotations/src/main/java/bali/Lookup.java) to accessor methods.
+ Re-using modules by inheritance or composition for multi-module applications.
