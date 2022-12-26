package de.jonasmetzger.dependency;


import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {

    public static final String DEFAULT_KEY = "default";
    private final Map<Class<?>, Map<String, Object>> dependencies = new HashMap<>();

    public <T> T getDependency(Class<T> classToFetch) {
        return getDependency(classToFetch, DEFAULT_KEY);
    }

    public <T> T getDependency(Class<T> classToFetch, String key) {
        if (dependencies.containsKey(classToFetch)) {
            return (T) dependencies.get(classToFetch).get(key);
        }
        return null;
    }

    public void registerDependency(Class<?> classToRegister, Object objToRegister) {
        registerDependency(classToRegister, DEFAULT_KEY, objToRegister);
    }

    public void registerDependency(Class<?> classToRegister, String key, Object objToRegister) {
        dependencies.putIfAbsent(classToRegister, new HashMap<>());
        final Map<String, Object> classMap = dependencies.get(classToRegister);
        if (!classMap.containsKey(key)) {
            classMap.put(key, objToRegister);
        }
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
                final Inject declaredAnnotation = field.getDeclaredAnnotation(Inject.class);
                if (dependencies.containsKey(field.getType())) {
                    field.setAccessible(true);
                    try {
                        final Map<String, Object> classes = dependencies.get(field.getType());
                        if (classes.containsKey(declaredAnnotation.value())) {
                            field.set(obj, classes.get(declaredAnnotation.value()));
                        } else {
                            throw new RuntimeException(String.format("Cannot inject class %s with key %s into field %s in class %s", field.getType(), declaredAnnotation.value(), field.getName(), classToInstantiate.getCanonicalName()));
                        }
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
                final DynamicDependency declaredAnnotation = method.getDeclaredAnnotation(DynamicDependency.class);
                method.setAccessible(true);
                try {
                    final Object dynamicDependency = method.invoke(obj);
                    for (Class<?> c : dynamicDependency.getClass().getInterfaces()) {
                        registerDependency(c, declaredAnnotation.value(), dynamicDependency);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Cannot invoke 0-args postConstruct with name %s in class %s", method.getName(), classToInstantiate.getCanonicalName()), e);
                }
            }
        }
        return obj;
    }

}
