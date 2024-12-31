/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.commons.collections;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.lang.Nullable;

import org.apache.causeway.commons.internal.base._Casts;
import org.apache.causeway.commons.internal.collections._Lists;
import org.apache.causeway.commons.internal.exceptions._Exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value @NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Can_Empty<T> implements Can<T> {

    private static final long serialVersionUID = 1L;

    static final Can_Empty<?> INSTANCE = new Can_Empty<>();

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ZERO;
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public Stream<T> parallelStream() {
        return Stream.empty();
    }

    @Override
    public Optional<T> getSingleton() {
        return Optional.empty();
    }

    @Override
    public Optional<T> getFirst() {
        return Optional.empty();
    }

    @Override
    public Optional<T> getLast() {
        return Optional.empty();
    }

    @Override
    public Optional<T> get(final int elementIndex) {
        return Optional.empty();
    }

    @Override
    public boolean contains(final @Nullable T element) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<T> iterator(final int skip, final int limit) {
        return iterator(); // empty iterator no matter what the arguments
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.<T>emptyList().iterator();
    }

    @Override
    public Can<T> sorted(@NonNull final Comparator<? super T> c) {
        return this;
    }

    @Override
    public Can<T> distinct() {
        return this;
    }

    @Override
    public Can<T> distinct(@NonNull final BiPredicate<T, T> equality) {
        return this;
    }

    @Override
    public Can<T> reverse() {
        return this;
    }

    @Override
    public Iterator<T> reverseIterator() {
        return iterator();
    }

    @Override
    public Can<T> reduce(@NonNull final BinaryOperator<T> accumulator) {
        return this; // reduction of empty yields empty
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
    }

    @Override
    public Can<T> filter(final @Nullable Predicate<? super T> predicate) {
        return this; // identity
    }

    @Override
    public <R> void zip(final Iterable<R> zippedIn, final BiConsumer<? super T, ? super R> action) {
        // no-op
    }

    @Override
    public <R, Z> Can<R> zipMap(final Iterable<Z> zippedIn, final BiFunction<? super T, ? super Z, R> mapper) {
        return Can.empty();
    }

    @Override
    public <R, Z> Stream<R> zipStream(final Iterable<Z> zippedIn, final BiFunction<? super T, ? super Z, R> mapper) {
        return Stream.empty();
    }

    @Override
    public Can<T> add(final @Nullable T element) {
        return element != null
                ? Can.ofSingleton(element)
                : this;
    }

    @Override
    public Can<T> addUnique(final @Nullable T element) {
        return add(element);
    }

    @Override
    public Can<T> addAll(final @Nullable Can<T> other) {
        return other != null
                ? other
                : this;
    }

    @Override
    public Can<T> add(final int index, final @Nullable T element) {
        if(index!=0) {
            throw new IndexOutOfBoundsException(
                    "cannot add to empty can with index other than 0; got " + index);
        }
        return add(element);
    }

    @Override
    public Can<T> replace(final int index, final @Nullable T element) {
        throw _Exceptions.unsupportedOperation("cannot replace an element in an empty Can");
    }

    @Override
    public Can<T> remove(final int index) {
        throw new IndexOutOfBoundsException("cannot remove anything from an empty Can");
    }

    @Override
    public Can<T> remove(final @Nullable T element) {
        return this; // on an empty can this is a no-op
    }

    @Override
    public Can<T> pickByIndex(final @Nullable int... indices) {
        return Can.empty();
    }

    @Override
    public Can<T> pickByIndex(final @Nullable IntStream intStream) {
        return Can.empty();
    }

    @Override
    public Can<T> subCan(final int startInclusive) {
        return Can.empty();
    }

    @Override
    public Can<T> subCan(final int startInclusive, final int endExclusive) {
        return Can.empty();
    }

    @Override
    public Can<Can<T>> partitionInnerBound(final int maxInnerSize) {
        return Can.empty();
    }

    @Override
    public Can<Can<T>> partitionOuterBound(final int outerSizeYield) {
        return Can.empty();
    }

    @Override
    public int indexOf(final @Nullable T element) {
        return -1;
    }

    @Override
    public String toString() {
        return "Can[]";
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if(INSTANCE == obj) {
            return true; // optimization not strictly necessary
        }
        return (obj instanceof Can)
                ? ((Can<?>)obj).isEmpty()
                : false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int compareTo(final @Nullable Can<T> other) {
        if(other==null) {
            return 0;
        }
        // when returning
        // -1 ... this is before other
        // +1 ... this is after other
        return Integer.compare(0, other.size()); // all empty Cans are same and come first
    }

    @Override
    public List<T> toList() {
        return Collections.emptyList(); // serializable and immutable
    }

    @Override
    public List<T> toArrayList() {
        return _Lists.newArrayList();
    }

    @Override
    public Set<T> toSet() {
        return Collections.emptySet(); // serializable and immutable
    }

    @Override
    public Set<T> toSet(@NonNull final Consumer<T> onDuplicated) {
        return Collections.emptySet(); // serializable and immutable
    }

    @Override
    public T[] toArray(@NonNull final Class<T> elementType) {
        var array = _Casts.<T[]>uncheckedCast(Array.newInstance(elementType, 0));
        return array;
    }

    @Override
    public <K> Map<K, T> toMap(
            @NonNull final Function<? super T, ? extends K> keyExtractor) {
       return Collections.emptyMap();
    }
    @Override
    public <K, M extends Map<K, T>> Map<K, T> toMap(
            @NonNull final Function<? super T, ? extends K> keyExtractor,
            @NonNull final BinaryOperator<T> mergeFunction,
            @NonNull final Supplier<M> mapFactory) {
        return Collections.emptyMap();
    }

    @Override
    public <R, A> R collect(@NonNull final Collector<? super T, A, R> collector) {
        return collector.finisher().apply(collector.supplier().get());
    }

    @Override
    public <K> Map<K, Can<T>> groupBy(
            @NonNull final Function<? super T, ? extends K> classifier) {
        return Collections.emptyMap();
    }

    @Override
    public <K, M extends Map<K, Can<T>>> Map<K, Can<T>> groupBy(
            @NonNull final Function<? super T, ? extends K> classifier,
            @NonNull final Supplier<M> mapFactory) {
        return Collections.emptyMap();
    }

    @Override
    public String join(@NonNull final String delimiter) {
        return "";
    }

    @Override
    public String join(@NonNull final Function<? super T, String> toStringFunction, @NonNull final String delimiter) {
        return "";
    }

}
