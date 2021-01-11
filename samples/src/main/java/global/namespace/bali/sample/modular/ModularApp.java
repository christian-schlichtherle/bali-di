/*
 * Copyright © 2021 Schlichtherle IT Services
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
package global.namespace.bali.sample.modular;

import bali.Cache;
import bali.Module;
import global.namespace.bali.sample.modular.formatter.FormatterModule;
import global.namespace.bali.sample.modular.greeting.GreetingModule;

import static java.lang.System.out;

@Cache
@Module
public interface ModularApp extends FormatterModule, GreetingModule, Runnable {

    default String format() {
        return "Hello %s!";
    }

    @Override
    default void run() {
        out.println(greeting().message("world"));
    }

    static void main(String... args) {
        ModularApp$.new$().run();
    }
}
