/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.data.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to indicate a field or method is a relation to another type. Typically not used
 * directly but instead mapped to.
 *
 * @author graemerocher
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Relation {
    /**
     * @return The relation kind.
     */
    Kind value();

    /**
     * @return The inverse property that this relation is mapped by
     */
    String mappedBy() default "";

    /**
     * The relation kind.
     */
    enum Kind {
        /**
         * One to many association.
         */
        ONE_TO_MANY,
        /**
         * One to one association.
         */
        ONE_TO_ONE,
        /**
         * Many to many association.
         */
        MANY_TO_MANY,
        /**
         * Embedded association.
         */
        EMBEDDED,

        /**
         * Many to one association.
         */
        MANY_TO_ONE
    }
}
