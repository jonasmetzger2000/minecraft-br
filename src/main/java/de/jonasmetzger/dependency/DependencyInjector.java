package de.jonasmetzger.dependency;


import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    public void registerDependency(Class<?> classToRegister, Object objToRegister) {
        dependencies.put(classToRegister, objToRegister);
    }

    @SneakyThrows
    public <T> T instantiate(Class<T> classToInstantiate) {
        final Constructor<T> constructor;
        try {
            constructor = classToInstantiate.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Classes to instantiate with Dependencies, should have a 0-args constructor", e);
        }
        final T obj = constructor.newInstance();
        for (Field field : classToInstantiate.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (dependencies.containsKey(field.getType())) {
                    field.setAccessible(true);
                    field.set(obj, dependencies.get(field.getType()));
                } else {
                    throw new RuntimeException(String.format("Missing Dependency of Type %s for class %s", field.getType().getCanonicalName(), classToInstantiate.getCanonicalName()));
                }
            }
        }
        return obj;
    }


}
