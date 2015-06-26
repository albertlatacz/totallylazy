package com.googlecode.totallylazy.functions;

public class Compose<T> implements CurriedMonoid<Function1<T,T>> {
    private Compose() {}

    public static <T> Compose<T> compose() {
        return new Compose<T>();
    }

    public static <T> Compose<T> compose(Class<T> aClass) {
        return compose();
    }

    @Override
    public Function1<T, T> call(Function1<T, T> a, Function1<T, T> b) throws Exception {
        return Callables.compose(a, b);
    }

    @Override
    public Function1<T, T> identity() {
        return Functions.identity();
    }

}