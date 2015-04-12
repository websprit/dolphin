package com.freetmp.mbg.merge;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * Created by LiuPin on 2015/3/27.
 */
public class ClassOrInterfaceMerger extends AbstractMerger<ClassOrInterfaceDeclaration> {

    private ClassOrInterfaceMerger(){}

    static {
        if(getMerger(ClassOrInterfaceDeclaration.class) == null){
            register(ClassOrInterfaceDeclaration.class,new ClassOrInterfaceMerger());
        }
    }

    @Override
    public ClassOrInterfaceDeclaration merge(ClassOrInterfaceDeclaration first, ClassOrInterfaceDeclaration second) {

        if(first.isInterface() != second.isInterface()) return null;

        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();

        declaration.setInterface(first.isInterface());
        declaration.setName(first.getName());

        declaration.setModifiers(mergeModifiers(first.getModifiers(),second.getModifiers()));
        declaration.setJavaDoc(mergeSingle(first.getJavaDoc(),second.getJavaDoc()));
        declaration.setTypeParameters(mergeCollcetions(first.getTypeParameters(),second.getTypeParameters()));

        //TODO

        return declaration;
    }

    @Override
    public boolean isEquals(ClassOrInterfaceDeclaration first, ClassOrInterfaceDeclaration second) {

        if(first.getName().equals(second.getName())) return true;

        return false;
    }
}
