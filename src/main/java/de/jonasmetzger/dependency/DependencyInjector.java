package de.jonasmetzger.dependency;


import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    public <T> T getDependency(Class<T> classToFetch) {
        return (T) dependencies.get(classToFetch);
    }

    public void registerDependency(Class<?> classToRegister, Object objToRegister) {
        dependencies.put(classToRegister, objToRegister);
    }

    public <T> T instantiate(Class<T> classToInstantiate) {
        final Constructor<T> constructor;
        try {
            constructor = classToInstantiate.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Classes to instantiate with Dependencies, should have a 0-args constructor", e);
        }
        final T obj;
        try {
            obj = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Cannot construct instance of type %s", classToInstantiate.getCanonicalName()), e);
        }
        // inject fields
        for (Field field : classToInstantiate.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (dependencies.containsKey(field.getType())) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, dependencies.get(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(String.format("Cannot set field %s in class %s", field.getType().getCanonicalName(), classToInstantiate.getCanonicalName()), e);
                    }
                } else {
                    throw new RuntimeException(String.format("Missing Dependency of Type %s for class %s", field.getType().getCanonicalName(), classToInstantiate.getCanonicalName()));
                }
            }
        }
        // find dynamic dependencies
        for (Method method : classToInstantiate.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DynamicDependency.class)) {
                method.setAccessible(true);
                try {
                    final Object dynamicDependency = method.invoke(obj);
                    dependencies.put(dynamicDependency.getClass(), dynamicDependency);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Cannot invoke 0-args postConstruct with name %s in class %s", method.getName(), classToInstantiate.getCanonicalName()), e);
                }
            }
        }
        return obj;
    }
}
