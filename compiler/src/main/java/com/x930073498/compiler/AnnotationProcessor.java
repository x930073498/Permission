package com.x930073498.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x930073498.annotations.PermissionsDenied;
import com.x930073498.annotations.PermissionsGranted;
import com.x930073498.permission.PermissionsProxy;
import com.x930073498.permission.ProxyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({
        "com.x930073498.annotations.PermissionsCustomRationale",
        "com.x930073498.annotations.PermissionsDenied",
        "com.x930073498.annotations.PermissionsGranted",
        "com.x930073498.annotations.PermissionsNonRationale",
        "com.x930073498.annotations.PermissionsRationale"

})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private Filer filer;
    private Map<ClassName, TypeHolder> typeHolders = new HashMap<>();
    private ProcessUtils processUtils = new ProcessUtils();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processAnnotations(roundEnv);
        writeHoldersOnJavaFile();
        return true;
    }

    private void writeHoldersOnJavaFile() {
        for (TypeHolder holder : typeHolders.values()) {
            construct(holder);
        }
    }

    private void construct(TypeHolder holder) {
        final TypeName target = holder.classNameComplete;

        final TypeSpec.Builder builder = TypeSpec.classBuilder(String.format(Locale.CHINA, "%s$$Proxy", holder.className))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(PermissionsProxy.class), target));


        MethodSpec.Builder deny = MethodSpec.methodBuilder("denied")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(TypeName.VOID)
                .addParameter(target, "target")
                .addParameter(ParameterSpec.builder(String[].class, "permissions").build())
                .addParameter(ParameterSpec.builder(int[].class, "requestCodes").build());

        MethodSpec.Builder grant = MethodSpec.methodBuilder("granted")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(TypeName.VOID)
                .addParameter(target, "target")
                .addParameter(ParameterSpec.builder(String[].class, "permissions").build())
                .addParameter(ParameterSpec.builder(int[].class, "requestCodes").build());

        boolean isInDenyControl = false;

        boolean inInGrantControl = false;
        StringBuilder permissionBuilder, requestBuilder;
        for (Method method : holder.methods) {
            String[] permissions = method.permissions;
            int[] requestCodes = method.requestCodes;
            permissionBuilder = new StringBuilder();
            for (int i = 0; i < permissions.length; i++) {
                if (i == permissions.length - 1) permissionBuilder.append(permissions[i]);
                else permissionBuilder.append(permissions[i]).append(",");
            }
            requestBuilder = new StringBuilder();
            for (int i = 0; i < requestCodes.length; i++) {
                if (i == permissions.length - 1) requestBuilder.append(requestCodes[i]);
                else requestBuilder.append(requestCodes[i]).append(",");
            }
            if (PermissionsGranted.class == method.annotation) {
                if (inInGrantControl) {
                    grant.nextControlFlow("\nif($T.isMatch(permissions,requestCodes,new $T[]{$S},new $T[]{$L},$L))",
                            TypeName.get(ProxyUtils.class)
                            , TypeName.get(String.class), permissionBuilder.toString(), TypeName.INT, requestBuilder.toString(), String.valueOf(method.isAccurate))
                            .addCode("target.$L();\n", method.element.getSimpleName().toString());
                } else {
                    inInGrantControl = true;
                    grant.beginControlFlow("if($T.isMatch(permissions,requestCodes,new $T[]{$S},new $T[]{$L},$L))", TypeName.get(ProxyUtils.class)
                            , TypeName.get(String.class), permissionBuilder.toString(), TypeName.INT, requestBuilder.toString(), String.valueOf(method.isAccurate)
                    )
                            .addCode("target.$L();\n", method.element.getSimpleName().toString());
                }


            } else if (PermissionsDenied.class == method.annotation) {
                if (isInDenyControl) {
                    deny.nextControlFlow("\nif($T.isMatch(permissions,requestCodes,new $T[]{$S},new $T[]{$L},$L))",
                            TypeName.get(ProxyUtils.class)
                            , TypeName.get(String.class), permissionBuilder.toString(), TypeName.INT, requestBuilder.toString(), String.valueOf(method.isAccurate))
                            .addCode("target.$L();\n", method.element.getSimpleName().toString());
                } else {
                    isInDenyControl = true;
                    deny.beginControlFlow("if($T.isMatch(permissions,requestCodes,new $T[]{$S},new $T[]{$L},$L))", TypeName.get(ProxyUtils.class)
                            , TypeName.get(String.class), permissionBuilder.toString(), TypeName.INT, requestBuilder.toString(), String.valueOf(method.isAccurate)
                    )
                            .addCode("target.$L();\n", method.element.getSimpleName().toString());
                }
            }

        }
        if (inInGrantControl)
            grant.endControlFlow();
        if (isInDenyControl)
            deny.endControlFlow();
        builder.addMethod(grant.build())
                .addMethod(deny.build())
        ;
        final TypeSpec newClass = builder.build();
        final JavaFile javaFile = JavaFile.builder(holder.classNameComplete.packageName(), newClass).build();
        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processAnnotations(RoundEnvironment env) {
        for (Class<? extends Annotation> annotation : Arrays.asList(PermissionsDenied.class, PermissionsGranted.class)) {
            for (Element element : env.getElementsAnnotatedWith(annotation)) {
                processObserveAnnotated(element.getEnclosingElement(), annotation);
            }
        }
    }

    private void processObserveAnnotated(Element element, Class<? extends Annotation> annotation) {
        final ClassName classFullName = ClassName.get((TypeElement) element); //com.github.florent37.sample.TutoAndroidFrance
        if (!typeHolders.containsKey(classFullName)) {
            final String className = element.getSimpleName().toString(); //TutoAndroidFrance

            final TypeHolder observeHolder = new TypeHolder(element, classFullName, className);

            for (Element method : processUtils.getMethods(element)) {
                Annotation a;
                if ((a = method.getAnnotation(annotation)) != null) {
                    if (a instanceof PermissionsGranted) {
                        observeHolder.addMethod(method, annotation, ((PermissionsGranted) a).permission(), ((PermissionsGranted) a).requestCode(), ((PermissionsGranted) a).isAccurate());
                    } else if (a instanceof PermissionsDenied) {
                        observeHolder.addMethod(method, annotation, ((PermissionsDenied) a).permission(), ((PermissionsDenied) a).requestCode(), ((PermissionsDenied) a).isAccurate());
                    }

                }
            }

            typeHolders.put(classFullName, observeHolder);
        } else {
            final TypeHolder observeHolder = typeHolders.get(classFullName);
            for (Element method : processUtils.getMethods(element)) {
                Annotation a;
                if ((a = method.getAnnotation(annotation)) != null) {
                    if (a instanceof PermissionsGranted) {
                        observeHolder.addMethod(method, annotation, ((PermissionsGranted) a).permission(), ((PermissionsGranted) a).requestCode(), ((PermissionsGranted) a).isAccurate());
                    } else if (a instanceof PermissionsDenied) {
                        observeHolder.addMethod(method, annotation, ((PermissionsDenied) a).permission(), ((PermissionsDenied) a).requestCode(), ((PermissionsDenied) a).isAccurate());
                    }
                }
            }

        }
    }

}
