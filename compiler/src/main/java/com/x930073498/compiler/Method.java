package com.x930073498.compiler;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

import javax.lang.model.element.Element;

/**
 * Created by florentchampigny on 04/04/2017.
 */

public class Method {
    public final Element element;
    public final Class<? extends Annotation> annotation;
    public final String[] permissions;
    public final int[] requestCodes;
    public final boolean isAccurate;


    public Method(Element element, Class<? extends Annotation> annotation, String[] permissions, int[] requestCodes, boolean isAccurate) {
        this.element = element;
        this.annotation = annotation;
        this.requestCodes = requestCodes;
        this.permissions = permissions;
        this.isAccurate = isAccurate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return Objects.equals(element, method.element) &&
                Objects.equals(annotation, method.annotation) &&
                Arrays.equals(permissions, method.permissions) &&
                Arrays.equals(requestCodes, method.requestCodes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(element, annotation);
        result = 31 * result + Arrays.hashCode(permissions);
        result = 31 * result + Arrays.hashCode(requestCodes);
        return result;
    }
}
