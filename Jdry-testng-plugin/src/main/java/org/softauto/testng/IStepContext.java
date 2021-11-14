package org.softauto.testng;

import org.testng.IClass;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IStepContext {

    /**
     * The name of this Step.
     */
    public String getName();

    /**
     * When this Step started running.
     */
    public Date getStartDate();

    /**
     * When this Step stopped running.
     */
    public Date getEndDate();

    /**
     * @return A list of all the Step that run successfully.
     */
    public IResultMap getPassedSteps();

    /**
     * @return A list of all the Step that were skipped
     */
    public IResultMap  getSkippedSteps();

    /**
     * @return A list of all the Step that failed but are being ignored because
     * annotated with a successPercentage.
     */
    public IResultMap  getFailedButWithinSuccessPercentageStep();

    /**
     * @return A map of all the Steps that passed, indexed by
     * their IStepMethor.
     *
     * @see org.testng.ITestNGMethod
     */
    public IResultMap getFailedSteps();

    /**
     * @return All the groups that are included for this Step run.
     */
    public String[] getIncludedGroups();

    /**
     * @return All the groups that are excluded for this Step run.
     */
    public String[] getExcludedGroups();

    /**
     * @return Where the reports will be generated.
     */
    public String getOutputDirectory();

    /**
     * @return The Suite object that was passed to the runner
     * at start-up.
     */
    public ISuite getSuite();

    /**
     * @return All the Step methods that were run.
     */
    public ITestNGMethod[] getAllStepMethods();

    /**
     * @return The host where this Step was run, or null if it was run locally.  The
     * returned string has the form:  host:port
     */
    public String getHost();

    /**
     * @return All the methods that were not included in this Step run.
     */
    public Collection<ITestNGMethod> getExcludedMethods();

    /**
     * Retrieves information about the successful configuration method invocations.
     */
    public IResultMap getPassedConfigurations();

    /**
     * Retrieves information about the skipped configuration method invocations.
     */
    public IResultMap getSkippedConfigurations();

    /**
     * Retrieves information about the failed configuration method invocations.
     */
    public IResultMap getFailedConfigurations();



}
