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
package bali.sample.genericclock

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class GenericClockAppSpec extends AnyWordSpec {

  "The generic clock app" should {
    val app = GenericClockApp$.new$
    import app._

    "cache the clock" in {
      clock shouldBe theSameInstanceAs(clock)
    }

    "not cache the time" in {
      call shouldNot be theSameInstanceAs call
    }

    "not cache the time of the clock" in {
      clock.call shouldNot be theSameInstanceAs clock.call
    }
  }
}
