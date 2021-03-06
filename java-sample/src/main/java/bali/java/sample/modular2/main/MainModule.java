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
package bali.java.sample.modular2.main;

import bali.Cache;
import bali.Module;
import bali.java.sample.modular2.formatter.Formatter;
import bali.java.sample.modular2.formatter.FormatterModule$;
import bali.java.sample.modular2.greeting.Greeting;
import bali.java.sample.modular2.greeting.GreetingModule$;

@Module
public interface MainModule extends FormatterModule$, GreetingModule$ {

    @Cache
    @Override
    default String getFormat() {
        return "Hello %s!";
    }

    @Cache
    @Override
    default Formatter getFormatter() {
        return FormatterModule$.super.getFormatter();
    }

    @Cache
    @Override
    default Greeting getGreeting() {
        return GreetingModule$.super.getGreeting();
    }
}
