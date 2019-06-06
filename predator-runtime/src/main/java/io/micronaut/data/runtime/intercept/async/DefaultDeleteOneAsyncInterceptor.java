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
import io.micronaut.data.backend.Datastore;
import io.micronaut.data.intercept.async.DeleteOneAsyncInterceptor;

import java.util.Collections;
import java.util.concurrent.CompletionStage;

/**
 * The default implementation of {@link DeleteOneAsyncInterceptor}.
 * @param <T> The declaring type
 * @author graemerocher
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class DefaultDeleteOneAsyncInterceptor<T> extends AbstractAsyncInterceptor<T, Boolean>
        implements DeleteOneAsyncInterceptor<T> {
    /**
     * Default constructor.
     *
     * @param datastore The datastore
     */
    protected DefaultDeleteOneAsyncInterceptor(@NonNull Datastore datastore) {
        super(datastore);
    }

    @Override
    public CompletionStage<Boolean> intercept(MethodInvocationContext<T, CompletionStage<Boolean>> context) {
        Object[] parameterValues = context.getParameterValues();
        if (parameterValues.length == 1) {
            Class rootEntity = getRequiredRootEntity(context);
            Object o = parameterValues[0];
            if (o != null) {
                return asyncDatastoreOperations.deleteAll(rootEntity, Collections.singleton(o));
            } else {
                throw new IllegalArgumentException("Entity to delete cannot be null");
            }
        } else {
            throw new IllegalStateException("Expected exactly one argument");
        }
    }
}
