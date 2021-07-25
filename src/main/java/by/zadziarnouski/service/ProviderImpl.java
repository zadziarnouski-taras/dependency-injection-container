package by.zadziarnouski.service;

import by.zadziarnouski.annotation.Inject;
import by.zadziarnouski.exception.BindingNotFoundException;
import by.zadziarnouski.exception.ConstructorNotFoundException;
import by.zadziarnouski.exception.TooManyConstructorsException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ProviderImpl<T> implements Provider<T> {
    private static final Map<Class<?>, Object> SINGLETON_CACHE = new HashMap<>();
    Class<T> clazz;

    public ProviderImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getInstance() {
        if (SINGLETON_CACHE.containsKey(clazz)) return (T) SINGLETON_CACHE.get(clazz);

        T t = createObject(clazz);

        if (InjectorImpl.SINGLETON_CLASSES_ROUTER.containsValue(clazz)) SINGLETON_CACHE.put(clazz, t);

        return t;
    }

    private <S> S createObject(Class<S> clazz) {
        long count = Arrays.stream(clazz.getDeclaredConstructors()).filter(constructor -> constructor.getAnnotation(Inject.class) != null).count();

        checkConstructorsAmount(count);

        if (count == 1) {
            Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors()).filter(constr -> constr.getAnnotation(Inject.class) != null).findFirst().get();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            List<Object> fields = generateFieldsByTypes(parameterTypes);

            return createInstance(clazz, fields, parameterTypes);
        } else {
            return createInstance(clazz);
        }
    }

    private List<Object> generateFieldsByTypes(Class<?>[] parameterTypes) {
        List<Object> fields = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (SINGLETON_CACHE.containsKey(parameterType)) {
                fields.add(SINGLETON_CACHE.get(parameterType));
            } else if (InjectorImpl.SINGLETON_CLASSES_ROUTER.containsValue(parameterType)) {
                Object object = createObject(parameterType);
                SINGLETON_CACHE.put(clazz, object);
                fields.add(object);
            } else if (InjectorImpl.CLASSES_ROUTER.containsKey(parameterType)) {
                fields.add(createObject(InjectorImpl.CLASSES_ROUTER.get(parameterType)));
            } else {
                throw new BindingNotFoundException();
            }
        }
        return fields;
    }

    private <S> S createInstance(Class<S> clazz, List<Object> fields, Class<?>[] parameterTypes) {
        S instance = null;

        try {
            instance = clazz.getDeclaredConstructor(parameterTypes).newInstance(fields.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private <S> S createInstance(Class<S> clazz) {
        S instance = null;

        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new ConstructorNotFoundException();
        }

        return instance;
    }

    private void checkConstructorsAmount(long count) {
        if (count > 1) {
            throw new TooManyConstructorsException("Class must contain only one constructor with annotation @Inject");
        }
    }
}
