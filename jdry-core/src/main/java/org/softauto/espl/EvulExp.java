package org.softauto.espl;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EvulExp {

    ExpressionBuilder exp;
    Map<String, Object> localParams;

    public EvulExp setExp(ExpressionBuilder exp) {
        this.exp = exp;
        return this;
    }

    public EvulExp setLocalParams(Map<String, Object> localParams) {
        this.localParams = localParams;
        return this;
    }

    public static Object evulExp (String exp,Object ctx)throws Exception{
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext(ctx);
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        Object res = (java.lang.Object) exp2.getValue(itemContext,Object.class);
        return res;
    }

    public static boolean evulExp (String exp)throws Exception{
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext();
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        boolean res = (java.lang.Boolean) exp2.getValue(itemContext,Boolean.class);
        return res;
    }

    public boolean evaluate(){
        try{
            List<Object> objects = new ArrayList<>();
            for(org.softauto.espl.ExpressionBuilder.Expression exp : exp.getExpressions()){
                Object ctx = exp.getContext();
                if(exp.getContext() instanceof String){
                    ctx = localParams.get(exp.getContext());
                }
                objects.add(evulExp(exp.getStatement(),ctx));
            }
            if(objects.size() > 1){
              return  evulExp(objects.get(0).toString()+" "+exp.getOperator()+" "+objects.get(01).toString());
            }else if(objects.size() > 0) {
                return (Boolean)objects.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
