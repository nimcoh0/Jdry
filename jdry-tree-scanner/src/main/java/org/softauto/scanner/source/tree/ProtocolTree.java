package org.softauto.scanner.source.tree;


import org.apache.avro.Protocol;
import org.apache.avro.Suite;


import java.util.List;

public interface ProtocolTree extends Tree {


        Protocol getProtocol();

        VerifyTree getVerifyTree();

        // Suite.Verify getVerify();

        StepTree getStepTree();

        ApiTree getApiTree();

        //Suite.Step getStep();

        ListenerTree getListenerTree();

        //Suite.Listener getListener();

        Suite.Verify getVerify();

        List<Suite.Step> getSteps();

        Suite.Api getApi();

        List<Suite.Listener> getListeners();

        TypesTree getTypesTree();

        //Context getContext();

        //ContextTree getContextTree();

        OtherTree getOtherTree();

        AssertTree getAssertTree();

        Suite.Assert getAssert();

        BeforeTestTree getBeforeTestTree();

        ProtocolTree getProtocolTree();
}
