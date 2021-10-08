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
import io.cucumber.java.en.When;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ParameterType;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Field;
import org.softauto.core.Utils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import java.util.List;

@org.apache.avro.specific.AvroGenerated
public class ListenerServiceCucumberImpl  extends CucumberAbstractTesterImpl{
    private static Logger logger = LogManager.getLogger(ListenerServiceCucumberImpl.class);

    public ListenerServiceCucumberImpl(){
          try {
             if( Class.forName("org.softauto.listener.ListenerObserver", false, this.getClass().getClassLoader())!= null){
                 org.softauto.tester.listener.ListenerObserver.getInstance().register("tests.infrastructure.ListenerServiceCucumberImpl", this);
             }
             registerGlueClass(this);
         }catch (Exception e){
           // e.printStackTrace();
         }
    }



 
 @ParameterType(".*")
 public String exp0 (String exp0) throws Exception{
     return exp0;
 }

@ParameterType(".*")
public String operator0 (String operator0)throws Exception{
       return operator0;
}
@ParameterType(".*")
public Object expected0 (String expected0)throws Exception{
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(expected0);
       if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(expected0);
      Object result = exp2.getValue(itemContext);

return result;
}

@Then("validate listener result of {exp0} {operator0} {expected0}")
public void validate(String exp0,String operator0,Object expected0)throws Exception{
    boolean result = getResult(exp0 + " "+ operator0 + " "+ expected0);
    Assert.assertTrue(result);
}


  /**
   */
 public  app.cucumber.books.Book  addBook_book  ;
 
@When("got addBook2 result")
public  java.lang.Object[] app_cucumber_books_BookCatalog_addBook_result(){
AtomicReference<Object[]> ref = new AtomicReference();
try {
    CountDownLatch lock = new CountDownLatch(1);
    new ListenerServiceImpl() {
        @Override
        public void app_cucumber_books_BookCatalog_addBook_result(app.cucumber.books.Book book ) {
        addBook_book  = book ;
        ref.set(new Object[]{book });
        lock.countDown();
        }
     };
    lock.await(10, TimeUnit.MINUTES);
}catch (Exception e){
   e.printStackTrace();
}
return ref.get();
}


  /**
   */


@ParameterType(".*")
public app.cucumber.books.Book book1 (String book1)throws Exception{
if(Utils.isJson(book1)){
      return new ObjectMapper().readValue(book1, app.cucumber.books.Book.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(book1);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(book1);
      app.cucumber.books.Book result = (app.cucumber.books.Book) exp2.getValue(itemContext,app.cucumber.books.Book.class);

return result;
  }

public app.cucumber.books.Book book1Value;


@ParameterType(".*")
 public String exps0 (String exps0) throws Exception{
     return exps0;
 }
 @ParameterType(".*")
 public String values0 (String values0) throws Exception{
        return values0;
 }
@When("got addBook3 result set  {exps0} with {values0}")
public  java.lang.Object[] app_cucumber_books_BookCatalog_addBook(String exps0 ,String values0){
AtomicReference<Object[]> ref = new AtomicReference();
try {
    CountDownLatch lock = new CountDownLatch(1);
    new ListenerServiceImpl() {
        @Override
        public java.lang.Object[] app_cucumber_books_BookCatalog_addBook(app.cucumber.books.Book book1 ) {
        book1Value = book1;
        String[] _exps = exps0.split(",");
        String[] _values = values0.split(",");
        for(int i=0;i<_exps.length;i++){
            Object v = evulExp(_values[i]);
            setResult(_exps[i],v);
        }
        lock.countDown();
        return new Object[]{book1 };
         };
     };
    lock.await(10, TimeUnit.MINUTES);
}catch (Exception e){
   e.printStackTrace();
}
return ref.get();
}


}