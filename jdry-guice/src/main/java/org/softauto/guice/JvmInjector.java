package org.softauto.guice;

import com.google.inject.*;
import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.TypeConverterBinding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JvmInjector implements Injector{

    private Class field;

    public JvmInjector(){
       // this.field = field;
    }

    @Override
    public void injectMembers(Object o) {

    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return null;
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> aClass) {
        return null;
    }

    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        return null;
    }

    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        return null;
    }

    @Override
    public <T> Binding<T> getBinding(Key<T> key) {
        return null;
    }

    @Override
    public <T> Binding<T> getBinding(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> Binding<T> getExistingBinding(Key<T> key) {
        return null;
    }

    @Override
    public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> typeLiteral) {
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return null;
    }

    @Override
    public <T> T getInstance(Class<T> aClass) {
        return null;
    }

    @Override
    public Injector getParent() {
        return null;
    }

    @Override
    public Injector createChildInjector(Iterable<? extends Module> iterable) {
        return null;
    }

    @Override
    public Injector createChildInjector(Module... modules) {
        return null;
    }

    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return null;
    }

    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        return null;
    }

    @Override
    public List<Element> getElements() {
        return null;
    }

    @Override
    public Map<TypeLiteral<?>, List<InjectionPoint>> getAllMembersInjectorInjectionPoints() {
        return null;
    }
}
