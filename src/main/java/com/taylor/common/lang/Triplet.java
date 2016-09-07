package com.taylor.common.lang;

import java.io.Serializable;

/**
 * Created by carl.zhao on 2016/5/27.
 */
public class Triplet<F, S, T> implements Serializable {

    private static final long serialVersionUID = -4454395912991858631L;
    private F first;
    private S second;
    private T third;

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

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }

    public Triplet() {
    }

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F, S, T> Triplet<F, S, T> newInstance(F first, S second, T third) {
        return new Triplet<F, S, T>(first, second, third);
    }

    @Override
    public String toString() {
        return "Triplet [first=" + first + ", second=" + second + ", third=" + third + "]";
    }
}
