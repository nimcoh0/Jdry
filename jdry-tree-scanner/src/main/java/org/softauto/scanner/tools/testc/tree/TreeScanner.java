package org.softauto.scanner.tools.testc.tree;


import org.softauto.scanner.source.tree.*;
import org.softauto.scanner.source.tree.Tree;
import org.softauto.scanner.source.tree.TreeVisitor;

public class TreeScanner<R,D> implements TreeVisitor<R,D> {

    public R scan(Tree tree, D d, R r) {
        return (tree == null) ? null : tree.accept(this, d,r);
    }

    public R scanAndReduce(Tree node, D d, R r) {
        return reduce(scan(node, d,r), r);
    }

    public R scan(Iterable<? extends Tree> nodes, D d,R r) {
        if (nodes != null) {
            boolean first = true;
            for (Tree node : nodes) {
                r = (first ? scan(node, d, r) : scanAndReduce(node, d, r));
                first = false;
            }
        }
        return r;
    }


    private R scanAndReduce(Iterable<? extends Tree> nodes, D d, R r) {
        return reduce(scan(nodes, d,r), r);
    }

    public R reduce(R r1, R r2) {
        return r1;
    }


    @Override
    public R visitStep(StepTree step, D d, R r) {
        return r;
    }

    @Override
    public R visitApi(ApiTree step, D d, R r) {
        return r;
    }

    @Override
    public R visitListener(ListenerTree listener, D d, R r) {
        return r;
    }

    @Override
    public R visitVerify(VerifyTree verify, D d, R r) {
        return r;
    }

    @Override
    public R visitTest(TestTree test, D d, R r) {
       return r;
    }

    @Override
    public R visitTypes(TypesTree types, D d, R r) {
        return r;
    }

    @Override
    public R visitProtocol(ProtocolTree protocol, D d, R r) {
       return r;
    }




    @Override
    public R visitOther(OtherTree context, D d, R r) {
        return r;
    }

    @Override
    public R visitAssert(AssertTree context, D d, R r) {
        return r;
    }

    @Override
    public R visitData(DataTree context, D d, R r) {
        return r;
    }

    @Override
    public R visitBeforeTest(BeforeTestTree beforeTest, D d, R r) {
       return r;
    }

    @Override
    public R visitAfterTest(AfterTestTree AfterTest, D d, R r) {
        return r;
    }

    @Override
    public R visitAuthentication(AuthenticationTree authentication, D d, R r) {
        return r;
    }

    @Override
    public R visitAnnotation(AnnotationTree annotation, D d, R r) {
       return r;
    }

    @Override
    public R visitTransceiver(TransceiverTree transceiver, D d, R r) {
        return r;
    }

    @Override
    public R visitExposeAnnotation(ExposedAnnotationTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitListenerAnnotation(ListenerAnnotationTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitVerifyAnnotation(VerifyAnnotationTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitApiAnnotation(ApiAnnotationTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitBeforeTestAnnotation(BeforeTestTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitAfterTestAnnotation(AfterTestTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitDefaultValueTree(DefaultValueTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitMethod(MethodTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitModifiers(ModifiersTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitMessage(MessageTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitClass(ClassTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitNamespace(NamespaceTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitPackage(PackageTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitRequest(RequestTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitResponse(ResponseTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitReturn(ReturnTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitVariable(VariableTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitParameters(ParametersTree node, D d, R r) {
        return r;
    }

    @Override
    public R visitDependence(DependenceTree node, D d, R r) {
        return r;
    }
}
