package org.softauto.testng;

import org.testng.IClass;
import org.testng.ITestNGMethod;

public interface IStepResult {

    // Step status
    int CREATED = -1;
    int SUCCESS = 1;
    int FAILURE = 2;
    int SKIP = 3;
    int SUCCESS_PERCENTAGE_FAILURE = 4;
    int STARTED= 16;

    /**
     * @return The status of this result, using one of the constants
     * above.
     */
    int getStatus();
    void setStatus(int status);

    /**
     * @return The test method this result represents.
     */
    ITestNGMethod getMethod();

    /**
     * @return The parameters this method was invoked with.
     */
    Object[] getParameters();
    void setParameters(Object[] parameters);

    /**
     * @return The Step class used this object is a result for.
     */
    IClass getStepClass();

    /**
     * @return The throwable that was thrown while running the
     * method, or null if no exception was thrown.
     */
    Throwable getThrowable();
    void setThrowable(Throwable throwable);

    /**
     * @return the start date for this Step, in milliseconds.
     */
    long getStartMillis();

    /**
     * @return the end date for this Step, in milliseconds.
     */
    long getEndMillis();
    void setEndMillis(long millis);

    /**
     * @return The name of this StepResult, typically identical to the name
     * of the method.
     */
    String getName();

    /**
     * @return true if if this Step run is a SUCCESS
     */
    boolean isSuccess();

    /**
     * @return The host where this Step was run, or null if it was run locally.  The
     * returned string has the form:  host:port
     */
    String getHost();

    /**
     * The instance on which this method was run.
     */
    Object getInstance();

    /**
     * If this result's related instance implements IStep or use @Step(testName=...), returns its Step name, otherwise returns null.
     */
    String getStepName();

    String getInstanceName();

    /**
     * @return the {@link IStepContext} for this step result.
     */
    IStepContext getStepContext();
}
