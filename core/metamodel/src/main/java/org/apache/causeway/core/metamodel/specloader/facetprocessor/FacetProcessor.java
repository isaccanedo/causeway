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
package org.apache.causeway.core.metamodel.specloader.facetprocessor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.causeway.applib.annotation.Introspection.IntrospectionPolicy;
import org.apache.causeway.commons.internal.base._Lazy;
import org.apache.causeway.commons.internal.collections._Lists;
import org.apache.causeway.commons.internal.collections._Maps;
import org.apache.causeway.commons.internal.collections._Multimaps;
import org.apache.causeway.commons.internal.collections._Multimaps.ListMultimap;
import org.apache.causeway.commons.internal.collections._Sets;
import org.apache.causeway.commons.internal.reflection._GenericResolver.ResolvedMethod;
import org.apache.causeway.commons.internal.reflection._MethodFacades.MethodFacade;
import org.apache.causeway.core.metamodel.context.HasMetaModelContext;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facetapi.MethodRemover;
import org.apache.causeway.core.metamodel.facets.FacetFactory;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessClassContext;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessParameterContext;
import org.apache.causeway.core.metamodel.facets.FacetedMethod;
import org.apache.causeway.core.metamodel.facets.FacetedMethodParameter;
import org.apache.causeway.core.metamodel.facets.ObjectTypeFacetFactory;
import org.apache.causeway.core.metamodel.facets.ObjectTypeFacetFactory.ProcessObjectTypeContext;
import org.apache.causeway.core.metamodel.facets.PropertyOrCollectionIdentifyingFacetFactory;
import org.apache.causeway.core.metamodel.methods.MethodFilteringFacetFactory;
import org.apache.causeway.core.metamodel.methods.MethodPrefixBasedFacetFactory;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel;
import org.apache.causeway.core.metamodel.spec.feature.ObjectMember;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacetProcessor
implements HasMetaModelContext, AutoCloseable{

    private final @NonNull ProgrammingModel programmingModel;

    @Getter(onMethod_ = {@Override})
    private final @NonNull MetaModelContext metaModelContext;

    /**
     * Class<FacetFactory> => FacetFactory
     */
    private final Map<Class<? extends FacetFactory>, FacetFactory> factoryByFactoryType = _Maps.newHashMap();

    /**
     * {@link FacetFactory Facet factories}, in order they were
     * {@link #registerFactory(FacetFactory) registered}.
     */
    private final List<FacetFactory> factories = _Lists.newArrayList();

    /**
     * All method prefixes to check in {@link #recognizes(Method)}.
     *
     * <p>
     * Derived from factories that implement
     * {@link MethodPrefixBasedFacetFactory}.
     *
     */
    private final _Lazy<Set<String>> methodPrefixes =
            _Lazy.threadSafe(this::init_methodPrefixes);

    /**
     * All registered {@link FacetFactory factories} that implement
     * {@link MethodFilteringFacetFactory}.
     *
     * <p>
     * Used within {@link #recognizes(Method)}.
     *
     */
    private final _Lazy<List<MethodFilteringFacetFactory>> methodFilteringFactories =
            _Lazy.threadSafe(this::init_methodFilteringFactories);

    /**
     * All registered {@link FacetFactory factories} that implement
     * {@link PropertyOrCollectionIdentifyingFacetFactory}.
     *
     * <p>
     * Used within {@link #recognizes(Method)}.
     */
    private final _Lazy<List<PropertyOrCollectionIdentifyingFacetFactory>> propertyOrCollectionIdentifyingFactories =
            _Lazy.threadSafe(this::init_propertyOrCollectionIdentifyingFactories);

    /**
     * ObjectFeatureType => List<FacetFactory>
     *
     * <p>
     * Lazily initialized, then cached. The lists remain in the same order that
     * the factories were {@link #registerFactory(FacetFactory) registered}.
     */
    private final _Lazy<ListMultimap<FeatureType, FacetFactory>> factoryListByFeatureType =
            _Lazy.threadSafe(this::init_factoriesByFeatureType);

    // -- LIFECYCLE

    public void init() {
        cleanUp();
        programmingModel.streamFactories()
        .forEach(this::registerFactory);
    }

    @Override
    public void close() {
        cleanUp();
    }

    private void cleanUp() {
        clearCaches();
        factories.clear();
        factoryByFactoryType.clear();
    }

    private void registerFactory(final FacetFactory factory) {
        factoryByFactoryType.put(factory.getClass(), factory);
        factories.add(factory);
        injectDependenciesInto(factory);
    }

    /**
     * This is <tt>public</tt> so that can be used for <tt>@Facets</tt>
     * processing.
     */
    public void injectDependenciesInto(final FacetFactory factory) {
        metaModelContext.getServiceInjector().injectServicesInto(factory);
    }

    /**
     * Appends to the supplied {@link Set} all of the {@link Method}s that may
     * represent a property or collection.
     *
     * <p>
     * Delegates to all known
     * {@link PropertyOrCollectionIdentifyingFacetFactory}s.
     */
    public void findAssociationCandidateGetters(
            final Stream<ResolvedMethod> methodStream,
            final Consumer<ResolvedMethod> onCandidate) {

        var factories = propertyOrCollectionIdentifyingFactories.get();

        methodStream
        .forEach(method->{
            for (var facetFactory : factories) {
                if (facetFactory.isPropertyOrCollectionGetterCandidate(method)) {
                    onCandidate.accept(method);
                }
            }
        });
    }

    /**
     * Use the provided {@link MethodRemover} to have all known
     * {@link PropertyOrCollectionIdentifyingFacetFactory}s to remove all
     * property accessors, and append them to the supplied methodList.
     *
     * <p>
     * Intended to be called after {@link #findAndRemovePropertyAccessors(MethodRemover, java.util.List)} once only reference properties remain.
     */
    public void findAndRemovePropertyAccessors(
            final MethodRemover methodRemover,
            final List<ResolvedMethod> methodListToAppendTo) {

        for (var facetFactory : propertyOrCollectionIdentifyingFactories.get()) {
            facetFactory.findAndRemovePropertyAccessors(methodRemover, methodListToAppendTo);
        }
    }

    /**
     * Use the provided {@link MethodRemover} to have all known
     * {@link PropertyOrCollectionIdentifyingFacetFactory}s to remove all
     * property accessors, and append them to the supplied methodList.
     *
     * @see PropertyOrCollectionIdentifyingFacetFactory#findAndRemoveCollectionAccessors(MethodRemover,
     *      List)
     */
    public void findAndRemoveCollectionAccessors(
            final MethodRemover methodRemover,
            final List<ResolvedMethod> methodListToAppendTo) {

        for (var facetFactory : propertyOrCollectionIdentifyingFactories.get()) {
            facetFactory.findAndRemoveCollectionAccessors(methodRemover, methodListToAppendTo);
        }
    }

    /**
     * Whether this {@link Method method} is recognized by any of the
     * {@link FacetFactory}s.
     *
     * <p>
     * Typically this is when method has a specific prefix, such as
     * <tt>validate</tt> or <tt>hide</tt>. Specifically, it checks:
     * <ul>
     * <li>the method's prefix against the prefixes supplied by any
     * {@link MethodPrefixBasedFacetFactory}</li>
     * <li>the method against any {@link MethodFilteringFacetFactory}</li>
     * </ul>
     *
     * <p>
     * The design of {@link MethodPrefixBasedFacetFactory} (whereby this facet
     * factory set does the work) is a slight performance optimization for when
     * there are multiple facet factories that search for the same prefix.
     */
    public boolean recognizes(final ResolvedMethod method) {
        var methodName = method.name();
        for (var prefix : methodPrefixes.get()) {
            if (methodName.startsWith(prefix)) {
                return true;
            }
        }

        for (var factory : methodFilteringFactories.get()) {
            if (factory.recognizes(method)) {
                return true;
            }
        }

        return false;
    }

    public void processObjectType(final Class<?> cls, final FacetHolder facetHolder) {
        var factoryList = getObjectSpecIfFacetFactoryList();
        for (var facetFactory : factoryList) {
            facetFactory.process(new ProcessObjectTypeContext(cls, facetHolder));
        }
    }

    private List<ObjectTypeFacetFactory> objectSpecIfFacetFactoryList = null;

    private List<ObjectTypeFacetFactory> getObjectSpecIfFacetFactoryList() {
        if(objectSpecIfFacetFactoryList == null) {
            var facetFactories = _Lists.<ObjectTypeFacetFactory>newArrayList();

            factoryListByFeatureType.get().getOrElseEmpty(FeatureType.OBJECT)
            .forEach(facetFactory->{
                if (facetFactory instanceof ObjectTypeFacetFactory) {
                    facetFactories.add((ObjectTypeFacetFactory) facetFactory);
                }
            });

            objectSpecIfFacetFactoryList = Collections.unmodifiableList(facetFactories);
        }
        return objectSpecIfFacetFactoryList;
    }

    /**
     * Attaches all facets applicable to the provided {@link FeatureType#OBJECT
     * object}) to the supplied {@link FacetHolder}.
     *
     * <p>
     * Delegates to {@link FacetFactory#process(FacetFactory.ProcessClassContext)} for each
     * appropriate factory.
     *
     * @see FacetFactory#process(ProcessClassContext)
     *
     * @param cls
     *            - class to process
     * @param facetHolder
     *            - holder to attach facets to.
     */
    public void process(
            final Class<?> cls,
            final IntrospectionPolicy introspectionPolicy,
            final MethodRemover methodRemover,
            final FacetHolder facetHolder) {

        var ctx = new ProcessClassContext(
                cls,
                introspectionPolicy,
                removerElseNoopRemover(methodRemover),
                facetHolder);

        factoryListByFeatureType.get().getOrElseEmpty(FeatureType.OBJECT)
        .forEach(facetFactory->facetFactory.process(ctx));
    }

    /**
     * Attaches all facets applicable to the provided {@link FeatureType type of
     * feature} to the supplied {@link FacetHolder}.
     *
     * <p>
     * Delegates to {@link FacetFactory#process(FacetFactory.ProcessMethodContext)} for each
     * appropriate factory.
     *
     * @param cls
     *            - class in which introspect; allowing the helper methods to be
     *            found is subclasses of that which the method was originally
     *            found.
     * @param method
     *            - method to process
     * @param facetedMethod
     *            - holder to attach facets to.
     * @param featureType
     *            - what type of feature the method represents (property,
     *            action, collection etc)
     * @param isMixinMain
     *            - Whether we are currently processing a mixin type AND this context's method
     *            can be identified as the main method of the processed mixin class. (since 2.0)
     */
    public void process(
            final Class<?> cls,
            final IntrospectionPolicy introspectionPolicy,
            final MethodFacade method,
            final MethodRemover methodRemover,
            final FacetedMethod facetedMethod,
            final FeatureType featureType,
            final boolean isMixinMain) {

        var processMethodContext =
                new ProcessMethodContext(
                        cls,
                        introspectionPolicy,
                        featureType,
                        method,
                        removerElseNoopRemover(methodRemover), facetedMethod, isMixinMain);

        for (FacetFactory facetFactory : factoryListByFeatureType.get().getOrElseEmpty(featureType)) {

            facetFactory.process(processMethodContext);
        }
    }

    public void processMemberOrder(final ObjectMember facetHolder) {

    }

    /**
     * Attaches all facets applicable to the provided parameter to the supplied
     * {@link FacetHolder}.
     *
     * <p>
     * Delegates to {@link FacetFactory#processParams(ProcessParameterContext)}
     * for each appropriate factory.
     *
     * @see FacetFactory#processParams(ProcessParameterContext)
     *
     * @param introspectedClass
     * @param method
     *            - action method to process
     * @param methodRemover
     * @param facetedMethodParameter
     */
    public void processParams(
            final Class<?> introspectedClass,
            final IntrospectionPolicy introspectionPolicy,
            final MethodFacade method,
            final MethodRemover methodRemover,
            final FacetedMethodParameter facetedMethodParameter) {

        var processParameterContext =
                new ProcessParameterContext(introspectedClass, introspectionPolicy,
                        method, methodRemover, facetedMethodParameter);

        var factoryCache = factoryListByFeatureType.get();

        FeatureType.PARAMETERS_ONLY.stream()
        .map(factoryCache::getOrElseEmpty)
        .flatMap(List::stream)
        .collect(Collectors.toSet())
        .forEach(facetFactory->facetFactory.processParams(processParameterContext));
    }

    private void clearCaches() {
        factoryListByFeatureType.clear();
        methodPrefixes.clear();
        methodFilteringFactories.clear();
        propertyOrCollectionIdentifyingFactories.clear();
    }

    // -- INITIALIZERS

    private ListMultimap<FeatureType, FacetFactory> init_factoriesByFeatureType() {
        var factoryListByFeatureType = _Multimaps.<FeatureType, FacetFactory>newListMultimap();
        for (var factory : factories) {
            factory.getFeatureTypes().forEach(featureType->
                factoryListByFeatureType.putElement(featureType, factory));
        }
        return factoryListByFeatureType;
    }

    private Set<String> init_methodPrefixes() {
        var cachedMethodPrefixes = _Sets.<String>newHashSet();
        for (var facetFactory : factories) {
            if (facetFactory instanceof MethodPrefixBasedFacetFactory) {
                var methodPrefixBasedFacetFactory = (MethodPrefixBasedFacetFactory) facetFactory;
                methodPrefixBasedFacetFactory.getPrefixes().forEach(cachedMethodPrefixes::add);
            }
        }
        return cachedMethodPrefixes;
    }

    private List<MethodFilteringFacetFactory> init_methodFilteringFactories() {
        var methodFilteringFactories = _Lists.<MethodFilteringFacetFactory>newArrayList();
        for (var factory : factories) {
            if (factory instanceof MethodFilteringFacetFactory) {
                var methodFilteringFacetFactory = (MethodFilteringFacetFactory) factory;
                methodFilteringFactories.add(methodFilteringFacetFactory);
            }
        }
        return methodFilteringFactories;
    }

    private List<PropertyOrCollectionIdentifyingFacetFactory> init_propertyOrCollectionIdentifyingFactories() {
        var propertyOrCollectionIdentifyingFactories = _Lists.<PropertyOrCollectionIdentifyingFacetFactory>newArrayList();
        for (var factory : factories) {
            if (factory instanceof PropertyOrCollectionIdentifyingFacetFactory) {
                var identifyingFacetFactory = (PropertyOrCollectionIdentifyingFacetFactory) factory;
                propertyOrCollectionIdentifyingFactories.add(identifyingFacetFactory);
            }
        }
        return propertyOrCollectionIdentifyingFactories;
    }

    // -- HELPER

    private static MethodRemover removerElseNoopRemover(final MethodRemover methodRemover) {
        return methodRemover != null ? methodRemover : MethodRemover.NOOP;
    }

}
