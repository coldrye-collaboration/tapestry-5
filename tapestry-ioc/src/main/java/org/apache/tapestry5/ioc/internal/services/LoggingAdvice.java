// Copyright 2008, 2011 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.services.ExceptionTracker;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.slf4j.Logger;

public class LoggingAdvice implements MethodAdvice
{
    private final MethodLogger methodLogger;

    public LoggingAdvice(Logger logger, ExceptionTracker exceptionTracker)
    {
        methodLogger = new MethodLogger(logger, exceptionTracker);
    }

    @Override
    public void advise(MethodInvocation invocation)
    {
        boolean debug = methodLogger.isDebugEnabled();

        if (!debug)
        {
            invocation.proceed();
            return;
        }

        methodLogger.entry(invocation);

        try
        {
            invocation.proceed();
        }
        catch (RuntimeException ex)
        {
            methodLogger.fail(invocation, ex);

            throw ex;
        }

        if (invocation.didThrowCheckedException())
        {
            Exception thrown = invocation.getCheckedException(Exception.class);

            methodLogger.fail(invocation, thrown);

            return;
        }

        methodLogger.exit(invocation);
    }
}
