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
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(EvulExp.class);

    public EvulExp setExp(ExpressionBuilder exp) {
        this.exp = exp;
        return this;
    }

    public EvulExp setLocalParams(Map<String, Object> localParams) {
        this.localParams = localParams;
        return this;
    }

    public static Object evulExp (String exp,Object ctx)throws Exception{
        logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext(ctx);
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        Object res = (java.lang.Object) exp2.getValue(itemContext,Object.class);
        logger.debug("evaluate "+exp +" to "+ res);
        return res;
    }

    public static boolean evulExp (String exp)throws Exception{
        logger.debug("evaluate "+exp);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext();
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        boolean res = (java.lang.Boolean) exp2.getValue(itemContext,Boolean.class);
        logger.debug("evaluate "+exp +" to boolean "+ res);
        return res;
    }

    public boolean evaluate(){
        try{
            List<Object> objects = new ArrayList<>();
            for(org.softauto.espl.ExpressionBuilder.Expression exp : exp.getExpressions()){
                Object ctx = exp.getContext();
                if(exp.getContext() instanceof String){
                    logger.debug("context is string ."+exp.getContext()+" getting Object from local param");
                    ctx = localParams.get(exp.getContext());
                }
                logger.debug("adding expression to the list "+ exp.getStatement() +" with context "+ ctx.getClass().getName());
                objects.add(evulExp(exp.getStatement(),ctx));
            }
            if(objects.size() <= 2) {
                if (objects.size() > 1) {
                    logger.debug("evaluate expression " +exp.getExpressions().get(0).toString()+" evaluate to "+objects.get(0).toString() + " " + exp.getOperator()+" "+exp.getExpressions().get(1).toString()+" evaluate to "+objects.get(1).toString());
                    return evulExp(objects.get(0).toString() + " " + exp.getOperator() + " " + objects.get(01).toString());
                } else if (objects.size() > 0) {
                    logger.debug("evaluate expression " + exp.getExpressions().get(0).toString()+ "result: "+ objects.get(0));
                    return (Boolean) objects.get(0);
                }
            }else{
                logger.error("number of expressions > 2 not supported ");
            }
        }catch (Exception e){
            logger.error("fail evaluate expression ", e);
        }
        return false;
    }
}
