package org.softauto.testng;

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
        SystemState.getInstance().startTest(result.getName(),res ->{
            if(res.succeeded()){
                logger.debug("successfully start test ");
            }else {
                logger.error("fail start test ",res.cause());
            }
        });

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {
        SystemState.getInstance().endTest(context.getName(),res ->{
            if(res.succeeded()){
                logger.debug("successfully end test");
            }else {
                logger.error("fail end test",res.cause());
            }
        });
        ListenerObserver.getInstance().reset();
        ListenerServerProviderImpl.getInstance().shutdown();
        SystemState.getInstance().shutdown(res ->{
            if(res.succeeded()){
                logger.debug("successfully shutdown");
            }else {
                logger.error("fail shutdown",res.cause());
            }
        });

    }
}
