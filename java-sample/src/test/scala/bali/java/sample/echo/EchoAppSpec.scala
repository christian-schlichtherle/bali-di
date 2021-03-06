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
package bali.java.sample.echo

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class EchoAppSpec extends AnyWordSpec {

  "The echo app" should {
    val app = EchoApp$.new$
    import app._

    "produce 'Hello world!'" in {
      getEcho.echo("Hello", "world!") shouldBe "Hello world!"
    }
  }
}
