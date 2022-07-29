package org.softauto.scanner.source.tree;


public interface MessageVisitor<R,P> {

    R visitMethod(MethodTree node,P p,R r);

    R visitModifiers(ModifiersTree node, P p,R r);

    R visitMessage(MessageTree node, P p,R r);

    R visitTypes(TypesTree node , P p, R r);

    R visitAnnotation(AnnotationTree node,P  p,R r);

    R visitClass(ClassTree node, P p,R r);

    R visitNamespace(NamespaceTree node , P p,R r);

    R visitPackage(PackageTree node, P p, R r);

    R visitRequest(RequestTree node, P  p, R r);

    R visitResponse(ResponseTree node, P p, R r);

    R visitReturn(ReturnTree node , P p, R r);

    R visitVariable(VariableTree node, P  p, R r);

    R visitParameters(ParametersTree node, P p, R r);


}
