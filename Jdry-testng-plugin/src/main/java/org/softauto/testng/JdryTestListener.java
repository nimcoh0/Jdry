package org.softauto.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.softauto.core.Context;
import org.softauto.core.TestLifeCycle;
import org.softauto.listener.server.ListenerObserver;
import org.softauto.listener.server.ListenerServerProviderImpl;
import org.softauto.tester.SystemState;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class JdryTestListener implements ITestListener {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JdryTestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        try {
            Context.setTestState(TestLifeCycle.START);
            SystemState.getInstance().startTest(result.getName(), res -> {
                if (res.succeeded()) {
                    logger.debug("successfully start test " + result.getName());
                } else {
                    logger.error("fail start test "+result.getName(), res.cause());
                }
            });
        }catch (Exception e){
            logger.error("fail onTestStart "+result.getName(),e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.debug(result.getName()+" end successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.debug(result.getName()+" fail",result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.debug(result.getName()+" skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            Context.setTestState(TestLifeCycle.STOP);
            SystemState.getInstance().endTest(context.getName(), res -> {
                if (res.succeeded()) {
                    logger.debug("successfully end test ");
                } else {
                    logger.error("fail end test" , res.cause());
                }
            });
            ListenerObserver.getInstance().reset();
            ListenerServerProviderImpl.getInstance().shutdown();
            SystemState.getInstance().shutdown(res -> {
                if (res.succeeded()) {
                    logger.debug("successfully shutdown ");
                } else {
                    logger.error("fail shutdown ", res.cause());
                }
            });
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();
            config.getRootLogger().removeAppender("console");
            ctx.updateLoggers();
        }catch (Exception e){
            logger.error("fail onFinish ",e);
        }
    }
}
