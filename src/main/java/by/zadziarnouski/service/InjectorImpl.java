package by.zadziarnouski.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectorImpl implements Injector {
    final static Map<Class, Class> CLASSES_ROUTER = new ConcurrentHashMap<>();
    final static Map<Class, Class> SINGLETON_CLASSES_ROUTER = new ConcurrentHashMap<>();

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        if (SINGLETON_CLASSES_ROUTER.containsKey(type)) {
            return new ProviderImpl<>(SINGLETON_CLASSES_ROUTER.get(type));
        } else if (CLASSES_ROUTER.containsKey(type)) {
            return new ProviderImpl<>(CLASSES_ROUTER.get(type));
        } else {
            return null;
        }
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        CLASSES_ROUTER.put(intf, impl);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        SINGLETON_CLASSES_ROUTER.put(intf, impl);
    }
}