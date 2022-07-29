package org.softauto.scanner.source.tree;

public interface Tree {

    public enum Kind {
        LISTENER(ListenerTree.class),
        STEP(StepTree.class),
        API(ApiTree.class),
        TEST(TestTree.class),
        TYPES(TypesTree.class),
        PROTOCOL(ProtocolTree.class),
        REFERENCE(ReferenceTree.class),
        ASSERT(AssertTree.class),
        AUTHENTICATION(AuthenticationTree.class),
        BEFORE_TEST(BeforeTestTree.class),
        VERIFY(VerifyTree.class),
        LISTENER_ANNOTATION(ListenerAnnotationTree.class),
        VERIFY_ANNOTATION(VerifyAnnotationTree.class),
        EXPOSED_ANNOTATION(ExposedAnnotationTree.class),
        API_ANNOTATION(ApiAnnotationTree.class),
        AFTER_TEST(AfterTestTree.class),
        TRANSCEIVER(TransceiverTree.class),
        ANNOTATION(AnnotationTree.class),
        //CONTEXT(ContextTree.class),
        DATA(DataTree.class),
        OTHER(OtherTree.class),
        DEFAULT_VALUE(DefaultValueTree.class),
        METHOD(MethodTree.class),
        MESSAGE(MessageTree.class),
        DEPENDENCE(DependenceTree.class),
        CLASS(ClassTree.class),

        MODIFIERS(ModifiersTree.class),
        NAMESPACE(NamespaceTree.class),
        PACKAGE(PackageTree.class),
        REQUEST(RequestTree.class),
        RESPONSE(ResponseTree.class),
        RETURN(ReturnTree.class),

        PARAMETER(ParametersTree.class),

        VARIABLE(VariableTree.class);
        //OTHER(null);

        Kind(Class<? extends Tree> intf) {
            associatedInterface = intf;
        }



        public Class<? extends Tree> asInterface() {
            return associatedInterface;
        }

        private final Class<? extends Tree> associatedInterface;
    }

    Kind getKind();

    /**
     * Accept method used to implement the visitor pattern.  The
     * visitor pattern is used to implement operations on trees.
     *
     * @param <R> result type of this operation.
     * @param <D> type of additional data.
     * @param visitor the visitor to be called
     * @param data a value to be passed to the visitor
     * @return the result returned from calling the visitor
     */
    //<R,D> R accept(TreeVisitor<R,D> visitor, D data);

    //public <R> R accept(TreeVisitor v);

    public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) ;
}
