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
package bali.sample.tuple;

import bali.Lookup;

import java.util.Objects;
import java.util.function.Function;

abstract class ImmutableTuple2<T1, T2> implements Tuple2<T1, T2> {

    @Lookup("t1")
    @Override
    public abstract T1 getT1();

    @Lookup(param = "t2")
    @Override
    public abstract T2 getT2();

    abstract TupleFactory factory();

    @Override
    public <R> Tuple2<R, T2> mapT1(Function<T1, R> f) {
        return factory().tuple(f.apply(getT1()), getT2());
    }

    @Override
    public <R> Tuple2<T1, R> mapT2(Function<T2, R> f) {
        return factory().tuple(getT1(), f.apply(getT2()));
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ImmutableTuple2)) {
            return false;
        }
        final ImmutableTuple2<?, ?> that = (ImmutableTuple2<?, ?>) obj;
        return Objects.equals(this.getT1(), that.getT1()) && Objects.equals(this.getT2(), that.getT2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getT1(), getT2());
    }

    @Override
    public String toString() {
        return "ImmutableTuple2(t1=" + getT1() + ", t2=" + getT2() + ")";
    }
}
