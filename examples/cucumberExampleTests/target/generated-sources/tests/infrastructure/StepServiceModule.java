/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;
import java.util.function.Function;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import org.softauto.guice.JvmTypeListener;
import org.softauto.guice.InitializeNoParamProvider;
import com.google.inject.multibindings.Multibinder;
import org.softauto.guice.InitializeParamProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@org.apache.avro.specific.AvroGenerated
public class StepServiceModule extends AbstractModule {

    private static Logger logger = LogManager.getLogger(StepServiceModule.class);

    @Override
    protected void configure() {
    try{
    bind( org.softauto.system.SystemServiceImpl.class).toInstance(org.softauto.system.SystemServiceImpl.getInstance());
        bind(baeldung.cucumber.books.BookStore.class).toProvider(InitializeNoParamProvider.getProvider(baeldung.cucumber.books.BookStore.class));
        bind(baeldung.cucumber.books.BookCatalog.class).toProvider(InitializeNoParamProvider.getProvider(baeldung.cucumber.books.BookCatalog.class));
}catch(Exception e){
    e.printStackTrace();
}
    }
}