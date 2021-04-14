package io.ashimjk.annotationprocessor.part1;

import io.ashimjk.annotationprocessor.sample.BuilderProperty;

public class Sample {

    private int age;
    private String name;

    @BuilderProperty
    public void setAge(int age) {
        this.age = age;
    }

    @BuilderProperty
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

}