package org.softauto.testng;

public interface IStepListener {


     void onStepStart(IStepResult result);
     void onStepSuccess(IStepResult result);
     void onStepFailure(IStepResult result);
     void onStepSkipped(IStepResult result);
     void onStepFailedButWithinSuccessPercentage(IStepResult result) ;
     void onStart(IStepContext context);
     void onFinish(IStepContext context);


}
