package com.taylor.common.lang;

import java.io.Serializable;

/**
 * Created by carl.zhao on 2016/5/27.
 */
public class Pair<F, S> implements Serializable {

    private static final long serialVersionUID = 6114448101229570993L;
    private F first;
    private S second;

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> newInstance(F first, S second) {
        return new Pair<F, S>(first, second);
    }

    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }
}
