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

package org.example.app5.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.jpa.JpaSymbols;
import org.apache.tapestry5.jpa.integration.app5.DummyPersistenceProvider;
import org.apache.tapestry5.jpa.modules.JpaModule;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.Arrays;
import java.util.List;

@ImportModule(JpaModule.class)
public class AppModule
{

    static
    {

        //This will also affect test suites run after this one.Its better to run this suite last
        PersistenceProviderResolverHolder.setPersistenceProviderResolver(
                new PersistenceProviderResolver()
                {
                    @Override
                    public List<PersistenceProvider> getPersistenceProviders()
                    {
                        return Arrays.<PersistenceProvider>asList(new DummyPersistenceProvider(), new org.eclipse.persistence.jpa.PersistenceProvider());
                    }

                    @Override
                    public void clearCachedProviders()
                    {
                    }
                }
        );
    }

    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void provideApplicationDefaults(
            final MappedConfiguration<String, String> configuration)
    {
        configuration.add(JpaSymbols.PROVIDE_ENTITY_VALUE_ENCODERS, "false");
        configuration.add(JpaSymbols.PERSISTENCE_DESCRIPTOR, "/explicit-persistence-provider-class-persistence-unit.xml");
    }
}
