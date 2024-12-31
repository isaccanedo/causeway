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
package org.apache.causeway.testing.archtestsupport.applib.classrules;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import static org.apache.causeway.testing.archtestsupport.applib.classrules.CommonPredicates.annotationOf;
import static org.apache.causeway.testing.archtestsupport.applib.classrules.CommonPredicates.haveNoArgProtectedConstructor;
import static org.apache.causeway.testing.archtestsupport.applib.classrules.CommonPredicates.ofAnEnum;

import lombok.experimental.UtilityClass;

/**
 * A library of architecture tests to ensure coding conventions are followed for classes annotated with
 * the JPA {@link Entity} annotation.
 *
 * @since 2.0 {@index}
 */
@UtilityClass
public class ArchitectureJpaRules {

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be
     * annotated with the Apache Causeway {@link DomainObject} annotation specifying that its
     * {@link DomainObject#nature() nature} is an {@link org.apache.causeway.applib.annotation.Nature#ENTITY entity}.
     */
    public static ArchRule every_jpa_Entity_must_be_annotated_with_DomainObject_nature_of_ENTITY() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(CommonPredicates.DomainObject_nature_ENTITY());
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be
     * annotated with the Apache Causeway {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter} annotation
     * with a value of {@link org.apache.causeway.applib.jaxb.PersistentEntityAdapter}<code>.class</code>.
     *
     * <p>
     * Tnis is so that entities can be transparently referenced from XML-style view models.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_be_annotated_with_XmlJavaAdapter_of_PersistentEntityAdapter() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(CommonPredicates.XmlJavaTypeAdapter_value_PersistentEntityAdapter());
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be
     * annotated with the {@link javax.persistence.EntityListeners} annotation that includes
     * a value of <code>org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener.class</code>.
     *
     * <p>
     * Tnis is so that entities can be transparently referenced from XML-style view models.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_be_annotated_as_an_CausewayEntityListener() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(EntityListeners_with_CausewayEntityListener());
    }

    static DescribedPredicate<JavaAnnotation<?>> EntityListeners_with_CausewayEntityListener() {
        return new DescribedPredicate<JavaAnnotation<?>>("@EntityListener({CausewayEntityListener.class})") {
            @Override public boolean test(final JavaAnnotation<?> javaAnnotation) {
                if (!javaAnnotation.getRawType().isAssignableTo(EntityListeners.class)) {
                    return false;
                }
                var properties = javaAnnotation.getProperties();
                var listeners = properties.get("value");
                return listeners instanceof JavaClass[] && containsCausewayEntityListener((JavaClass[]) listeners);
            }

            private boolean containsCausewayEntityListener(final JavaClass[] classes) {
                //noinspection deprecation
                return Arrays.stream(classes)
                        .anyMatch(x -> Objects.equals(x.getFullName(), CausewayEntityListener.class.getName())
                                || x.isAssignableTo(CausewayEntityListener.class));
            }
        };
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be
     * implement {@link Comparable}.
     *
     * <p>
     * This is so that entities have a natural ordering and can safely be added to parented collections of type
     * {@link java.util.SortedSet}.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_implement_Comparable() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .should().implement(Comparable.class);
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be annotated with the
     * JPA {@link Table} annotation which includes {@link Table#uniqueConstraints() uniqueConstraints}.
     *
     * <p>
     * This is so that entities will have an alternative business key in addition to the system-defined surrogate
     * key.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_be_annotated_as_Table_with_uniqueConstraints() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .and(not(areSubtypeEntities()))
                .should().beAnnotatedWith(Table_uniqueConstraints());
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must also be annotated with the
     * JPA {@link Table} annotation which includes {@link Table#schema()}  schema}.
     *
     * <p>
     * This is so that entity tables are organised into an appropriate structure (ideally mirroring that of the
     * entities).
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_be_annotated_as_Table_with_schema() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(Table_schema());
    }

    /**
     * This rule requires that enum fields in classes annotated with the JPA {@link Entity} annotation must also be
     * annotated with the JPA {@link Enumerated} annotation indicating that they should be persisted as
     * {@link javax.persistence.EnumType#STRING string}s (rather than ordinal numbers).
     *
     * <p>
     * The rationale here is that a string is (arguably) more stable than an ordinal number, and is certainly easier
     * to work with when querying the database.  The downside is slightly more space to persist the data, and slightly
     * less performant (not that it would be noticeable).
     * </p>
     */
    public static ArchRule every_enum_field_of_jpa_Entity_must_be_annotated_with_Enumerable_STRING() {
        return fields().that()
                .areDeclaredInClassesThat(areEntities())
                .and().haveRawType(ofAnEnum())
                .should().beAnnotatedWith(annotationOf(Enumerated.class, "value",
                        "EnumType.STRING"))
                ;
    }

    /**
     * This rule requires that injected fields in classes annotated with the JPA {@link Entity} annotation must also be
     * annotated with JPA {@link Transient} annotation.
     *
     * <p>
     * The rationale here is that injected services are managed by the runtime and are not/cannot be persisted.
     * </p>
     */
    public static ArchRule every_injected_field_of_jpa_Entity_must_be_annotated_with_Transient() {
        return fields().that()
                .areDeclaredInClassesThat(areEntities())
                .and().areAnnotatedWith(Inject.class)
                .should().beAnnotatedWith(Transient.class)
                ;
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must contain an <code>id</code>
     * field that is itself annotated with {@link Id}.
     *
     * <p>
     * This is part of the standard contract for JPA entities.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_have_an_id_field() {
        return everyJpa_Entity_must_have_a_field_named_and_annotated("id", Id.class);
    }

    /**
     * This rule requires that classes annotated with the JPA {@link Entity} annotation must contain a
     * <code>version</code> field that is itself annotated with {@link javax.persistence.Version}.
     *
     * <p>
     * This is good practice for JPA entities to implement optimistic locking
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_have_a_version_field() {
        return everyJpa_Entity_must_have_a_field_named_and_annotated("version", Version.class);
    }

    static ClassesShouldConjunction everyJpa_Entity_must_have_a_field_named_and_annotated(
            final String fieldName,
            final Class<? extends Annotation> annotationClass) {
        final String fieldAnnotation = annotationClass.getSimpleName();
        return classes().that()
                .areAnnotatedWith(Entity.class)
                .should(new ArchCondition<JavaClass>(
                        String.format("have a field named '%s' annotated with '@%s'", fieldName, fieldAnnotation)) {
                    @Override public void check(final JavaClass javaClass, final ConditionEvents conditionEvents) {
                        var javaFieldIfAny = javaClass.getAllFields().stream()
                                .filter(x -> Objects.equals(x.getName(), fieldName)).findAny();
                        if(!javaFieldIfAny.isPresent()) {
                            conditionEvents.add(new SimpleConditionEvent(javaClass, false,
                                    String.format("%s does not have a field named '%s'", javaClass.getSimpleName(), fieldName)));
                            return;
                        }
                        var javaField = javaFieldIfAny.get();
                        if(!javaField.isAnnotatedWith(annotationClass)) {
                            conditionEvents.add(new SimpleConditionEvent(javaClass, false,
                                    String.format("%s has field named '%s' but it is not annotated with '@%s'",
                                            javaClass.getSimpleName(), fieldName, fieldAnnotation)));
                        }
                    }
                })
                ;
    }

    static DescribedPredicate<JavaAnnotation<?>> Table_schema() {
        return new DescribedPredicate<JavaAnnotation<?>>("@Table(schema=...)") {
            @Override public boolean test(final JavaAnnotation<?> javaAnnotation) {
                if (!javaAnnotation.getRawType().isAssignableTo(Table.class)) {
                    return false;
                }
                var properties = javaAnnotation.getProperties();
                var schema = properties.get("schema");
                return schema instanceof String &&
                        ((String) schema).length() > 0;
            }
        };
    }

    static DescribedPredicate<JavaClass> areEntities() {
        return new DescribedPredicate<JavaClass>("are entities") {
            @Override
            public boolean test(final JavaClass input) {
                return input.isAnnotatedWith(Entity.class);
            }
        };
    }

    static DescribedPredicate<JavaAnnotation<?>> Table_uniqueConstraints() {
        return new DescribedPredicate<JavaAnnotation<?>>("@Table(uniqueConstraints=...)") {
            @Override public boolean test(final JavaAnnotation<?> javaAnnotation) {
                if (!javaAnnotation.getRawType().isAssignableTo(Table.class)) {
                    return false;
                }
                var properties = javaAnnotation.getProperties();
                var uniqueConstraints = properties.get("uniqueConstraints");
                return uniqueConstraints instanceof JavaAnnotation[] &&
                        ((JavaAnnotation<?>[]) uniqueConstraints).length > 0;
            }
        };
    }

    /**
     * This rule requires that concrete classes annotated with the JPA {@link Entity} annotation have a no-arg
     * constructor with <code>protected</code> visibility.
     *
     * <p>
     * The rationale is to encourage the use of static factory methods.
     * </p>
     */
    public static ArchRule every_jpa_Entity_must_have_protected_no_arg_constructor() {
        return classes()
                .that().areAnnotatedWith(Entity.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should(haveNoArgProtectedConstructor());
    }

    static DescribedPredicate<? super JavaClass> areSubtypeEntities() {
        return new DescribedPredicate<JavaClass>("are subtype entities ") {
            @Override public boolean test(final JavaClass input) {
                var superclassIfAny = input.getSuperclass();
                if(!superclassIfAny.isPresent()) {
                    return false;
                }
                var superType = superclassIfAny.get();
                var superClass = superType.toErasure();
                var entityIfAny = superClass
                        .tryGetAnnotationOfType(Entity.class);
                return entityIfAny.isPresent();
            }
        };
    }

}
