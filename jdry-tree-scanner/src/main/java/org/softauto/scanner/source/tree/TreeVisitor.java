package org.softauto.scanner.source.tree;

public interface TreeVisitor<R,P> {

    R visitStep(StepTree step, P p, R r);

    R visitApi(ApiTree step, P p, R r);

    R visitListener(ListenerTree listener, P p, R r);

    R visitVerify(VerifyTree verify, P p, R r);

    R visitTest(TestTree test , P p, R r);

    R visitTypes(TypesTree types , P p, R r);

    R visitProtocol(ProtocolTree protocol ,P p,R r);

    //R visitContext(ContextTree context , P p, R r);

    R visitOther(OtherTree other , P p, R r);

    R visitAssert(AssertTree assertTree , P p, R r);

    R visitData(DataTree data , P p, R r);

    R visitBeforeTest(BeforeTestTree beforeTest , P p, R r);

    R visitAfterTest(AfterTestTree AfterTest , P p, R r);

    R visitAuthentication(AuthenticationTree authentication , P p, R r);

    R visitAnnotation(AnnotationTree annotation , P p, R r);

    R visitTransceiver(TransceiverTree transceiver , P p, R r);

    R visitExposeAnnotation(ExposedAnnotationTree node, P p, R r);

    R visitListenerAnnotation(ListenerAnnotationTree node, P p, R r);

    R visitVerifyAnnotation(VerifyAnnotationTree node, P p, R r);

    R visitApiAnnotation(ApiAnnotationTree node, P p, R r);

    R visitBeforeTestAnnotation(BeforeTestTree node, P p, R r);

    R visitAfterTestAnnotation(AfterTestTree node, P p, R r);

    R visitDefaultValueTree(DefaultValueTree node, P p, R r);

    R visitMethod(MethodTree node,P p,R r);

    R visitModifiers(ModifiersTree node, P p,R r);

    R visitMessage(MessageTree node, P p,R r);

    R visitClass(ClassTree node, P p,R r);

    R visitNamespace(NamespaceTree node , P p,R r);

    R visitPackage(PackageTree node, P p, R r);

    R visitRequest(RequestTree node, P  p, R r);

    R visitResponse(ResponseTree node, P p,R r);

    R visitReturn(ReturnTree node , P p,R r);

    R visitVariable(VariableTree node, P  p, R r);

    R visitParameters(ParametersTree node, P p, R r);

    R visitDependence(DependenceTree node, P p, R r);
}
