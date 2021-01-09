/*
 * Copyright © 2020 Schlichtherle IT Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package global.namespace.bali.sample.nested;

import bali.Module;

import java.util.function.Supplier;

import static java.lang.System.out;

public final class NestedApp {

    public static void main(String... args) {
        StaticModule.instance.run();
    }

    @Module
    @SuppressWarnings("experimental")
    interface StaticModule extends Runnable {

        StaticModule instance = StaticModule$.new$();

        String get = "Hello world!";

        Supplier<String> message();

        @Override
        default void run() {
            out.println(message().get());
        }
    }
}
