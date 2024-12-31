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
package org.apache.causeway.core.config.beans;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import javax.persistence.Entity;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.id.LogicalType;
import org.apache.causeway.applib.services.metamodel.BeanSort;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.reflection._Annotations;
import org.apache.causeway.commons.internal.reflection._ClassCache;
import org.apache.causeway.commons.semantics.CollectionSemantics;
import org.apache.causeway.core.config.progmodel.ProgrammingModelConstants.TypeExcludeMarker;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
//@Log4j2
final class CausewayBeanTypeClassifierDefault
implements CausewayBeanTypeClassifier {

    private final Can<String> activeProfiles;
    private final Can<CausewayBeanTypeClassifier> classifierPlugins = CausewayBeanTypeClassifier.get();

    private final _ClassCache classCache = _ClassCache.getInstance();

    // handle arbitrary types ...
    @Override
    public CausewayBeanMetaData classify(
            final @NonNull Class<?> type) {

        //debug
//        _Debug.onClassSimpleNameMatch(type, "class of interest", ()->{
//            System.err.printf("classifying %s%n", type);
//        });

        if(ClassUtils.isPrimitiveOrWrapper(type)
                || type.isEnum()) {
            return CausewayBeanMetaData.notManaged(BeanSort.VALUE, type);
        }

        if(CollectionSemantics.valueOf(type).isPresent()) {
            return CausewayBeanMetaData.causewayManaged(BeanSort.COLLECTION, type);
        }

        if(type.isInterface()
                // modifier predicate must be called after testing for non-scalar type above,
                // otherwise we'd get false positives
                || Modifier.isAbstract(type.getModifiers())) {

            // apiNote: abstract types and interfaces cannot be vetoed
            // and should also never be identified as ENTITY, VIEWMODEL or MIXIN
            // however, concrete types that inherit abstract ones with vetoes,
            // will effectively be vetoed through means of annotation synthesis
            return CausewayBeanMetaData.indifferent(BeanSort.ABSTRACT, type);
        }

        // handle vetoing ...
        if(TypeExcludeMarker.anyMatchOn(type)) {
            return CausewayBeanMetaData.notManaged(BeanSort.VETOED, type); // reject
        }

        var profiles = Can.ofArray(_Annotations.synthesize(type, Profile.class)
                .map(Profile::value)
                .orElse(null));
        if(profiles.isNotEmpty()
                && !profiles.stream().anyMatch(this::isProfileActive)) {
            return CausewayBeanMetaData.notManaged(BeanSort.VETOED, type); // reject
        }

        // handle value types ...

        var aValue = _Annotations.synthesize(type, org.apache.causeway.applib.annotation.Value.class)
                .orElse(null);
        if(aValue!=null) {
            return CausewayBeanMetaData.notManaged(BeanSort.VALUE, type);
        }

        // handle actual bean types ...

        var aDomainService = _Annotations.synthesize(type, DomainService.class);
        if(aDomainService.isPresent()) {
            var logicalType = LogicalType.infer(type);
            Attributes.HAS_DOMAIN_SERVICE_SEMANTICS.set(classCache, type, "true");
            return CausewayBeanMetaData
                        .injectable(BeanSort.MANAGED_BEAN_CONTRIBUTING, logicalType);
        }

        //[CAUSEWAY-3585] when implements ViewModel, then don't consider alternatives, yield VIEW_MODEL
        if(org.apache.causeway.applib.ViewModel.class.isAssignableFrom(type)) {
            return CausewayBeanMetaData.causewayManaged(BeanSort.VIEW_MODEL, type);
        }

        // allow ServiceLoader plugins to have a say, eg. when classifying entity types
        for(var classifier : classifierPlugins) {
            var classification = classifier.classify(type);
            if(classification!=null) {
                return classification;
            }
        }

        var entityAnnotation = _Annotations.synthesize(type, Entity.class).orElse(null);
        if(entityAnnotation!=null) {
            return CausewayBeanMetaData.entity(PersistenceStack.JPA, LogicalType.infer(type));
        }

        var aDomainObject = _Annotations.synthesize(type, DomainObject.class).orElse(null);
        if(aDomainObject!=null) {
            switch (aDomainObject.nature()) {
            case BEAN:
                return CausewayBeanMetaData
                        .indifferent(BeanSort.MANAGED_BEAN_CONTRIBUTING, type);
            case MIXIN:
                // memoize mixin main name
                Attributes.MIXIN_MAIN_METHOD_NAME.set(classCache, type, aDomainObject.mixinMethod());
                return CausewayBeanMetaData.causewayManaged(BeanSort.MIXIN, type);
            case ENTITY:
                return CausewayBeanMetaData.entity(PersistenceStack.UNSPECIFIED, LogicalType.infer(type));
            case VIEW_MODEL:
            case NOT_SPECIFIED:
                //because object is not associated with a persistence context unless discovered above
                return CausewayBeanMetaData.causewayManaged(BeanSort.VIEW_MODEL, type);
            }
        }

        if(_ClassCache.getInstance().hasJaxbRootElementSemantics(type)) {
            return CausewayBeanMetaData.causewayManaged(BeanSort.VIEW_MODEL, type);
        }

        if(_Annotations.isPresent(type, Component.class)) {
            return CausewayBeanMetaData.indifferent(BeanSort.MANAGED_BEAN_NOT_CONTRIBUTING, type);
        }

        if(Serializable.class.isAssignableFrom(type)) {
            return CausewayBeanMetaData.indifferent(BeanSort.VALUE, type);
        }

        return CausewayBeanMetaData.indifferent(BeanSort.UNKNOWN, type);
    }

    // -- HELPER

    //XXX yet this is a naive implementation, not evaluating any expression logic like eg. @Profile("!dev")
    //either we find a Spring Boot utility class that does this logic for us, or we make it clear with the
    //docs, that we have only limited support for the @Profile annotation
    private boolean isProfileActive(final String profile) {
        return activeProfiles.contains(profile);
    }

}
