/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.verifier.resolver;

import com.facebook.presto.jdbc.QueryStats;
import com.facebook.presto.verifier.framework.QueryBundle;
import com.facebook.presto.verifier.framework.QueryException;

import java.util.Optional;

import static com.facebook.presto.spi.StandardErrorCode.EXCEEDED_GLOBAL_MEMORY_LIMIT;
import static com.facebook.presto.verifier.framework.QueryStage.TEST_MAIN;
import static com.facebook.presto.verifier.resolver.FailureResolverUtil.mapMatchingPrestoException;

public class ExceededGlobalMemoryLimitFailureResolver
        implements FailureResolver
{
    public static final String NAME = "exceeded-global-memory-limit";

    @Override
    public Optional<String> resolve(QueryStats controlQueryStats, QueryException queryException, Optional<QueryBundle> test)
    {
        return mapMatchingPrestoException(queryException, TEST_MAIN, EXCEEDED_GLOBAL_MEMORY_LIMIT,
                e -> e.getQueryStats().isPresent() && controlQueryStats.getPeakMemoryBytes() > e.getQueryStats().get().getPeakMemoryBytes()
                        ? Optional.of("Control query uses more memory than the test cluster memory limit")
                        : Optional.empty());
    }
}
