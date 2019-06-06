/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.data.runtime.intercept.async;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.backend.Datastore;
import io.micronaut.data.intercept.async.FindSliceAsyncInterceptor;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.PreparedQuery;
import io.micronaut.data.model.Slice;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Default implementation of {@link FindSliceAsyncInterceptor}.
 *
 * @param <T> The declaring type
 * @author graemerocher
 * @since 1.0.0
 */
public class DefaultFindSliceAsyncInterceptor<T> extends AbstractAsyncInterceptor<T, Slice<Object>> implements FindSliceAsyncInterceptor<T> {
    /**
     * Default constructor.
     *
     * @param datastore The datastore
     */
    protected DefaultFindSliceAsyncInterceptor(@NonNull Datastore datastore) {
        super(datastore);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletionStage<Slice<Object>> intercept(MethodInvocationContext<T, CompletionStage<Slice<Object>>> context) {
        if (context.hasAnnotation(Query.class)) {
            PreparedQuery<?, ?> preparedQuery = prepareQuery(context);
            Pageable pageable = preparedQuery.getPageable();
            return asyncDatastoreOperations.findAll(preparedQuery)
                    .thenApply(objects ->
                            Slice.of((List<Object>) CollectionUtils.iterableToList(objects), pageable)
                    );

        } else {
            Class<?> rootEntity = getRequiredRootEntity(context);
            Pageable pageable = getPageable(context);
            return asyncDatastoreOperations.findAll(rootEntity, pageable != null ? pageable : Pageable.unpaged()).thenApply(objects ->
                    Slice.of((List<Object>) CollectionUtils.iterableToList(objects), pageable)
            );
        }
    }
}

