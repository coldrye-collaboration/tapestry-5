// Copyright 2015 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.jpa;

import java.lang.reflect.Method;

import javax.persistence.PersistenceContext;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;

import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.jpa.annotations.CommitAfter;

public class JpaTransactionAdvisorImpl implements JpaTransactionAdvisor
{
    private final JpaAdvisorProvider jpaAdvisorProvider;

    public JpaTransactionAdvisorImpl(JpaAdvisorProvider jpaAdvisorProvider)
    {
        this.jpaAdvisorProvider = jpaAdvisorProvider;
    }

    @Override
    public void addTransactionCommitAdvice(MethodAdviceReceiver receiver)
    {
        for (final Method m : receiver.getInterface().getMethods())
        {
            if (m.getAnnotation(CommitAfter.class) != null)
            {
                PersistenceContext annotation = receiver.getMethodAnnotation(m, PersistenceContext.class);
                receiver.adviseMethod(m, jpaAdvisorProvider.getAdvice(annotation));
            }
        }
    }

}
