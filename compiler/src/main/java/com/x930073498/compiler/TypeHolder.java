package com.x930073498.compiler;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

public class TypeHolder {
    public Element element;
    public ClassName classNameComplete;
    public String className;
    public Set<Method> methods;

    public TypeHolder(Element element, ClassName classNameComplete, String className) {
        this.element = element;
        this.classNameComplete = classNameComplete;
        this.className = className;
        this.methods = new HashSet<>();
    }

    public void addMethod(Element child, Class<? extends Annotation> annotation,String[]permissions,int[] requestCodes,boolean isAccurate){
        methods.add(new Method(child, annotation,permissions,requestCodes,isAccurate));
    }

}
