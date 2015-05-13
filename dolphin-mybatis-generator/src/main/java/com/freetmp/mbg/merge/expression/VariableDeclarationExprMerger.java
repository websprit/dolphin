package com.freetmp.mbg.merge.expression;

import com.freetmp.mbg.merge.AbstractMerger;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

/**
 * Created by LiuPin on 2015/5/13.
 */
public class VariableDeclarationExprMerger extends AbstractMerger<VariableDeclarationExpr> {

  @Override public VariableDeclarationExpr merge(VariableDeclarationExpr first, VariableDeclarationExpr second) {
    VariableDeclarationExpr vde = new VariableDeclarationExpr();

    vde.setComment(mergeSingle(first.getComment(),second.getComment()));
    vde.setType(mergeSingle(first.getType(),second.getType()));
    vde.setAnnotations(mergeCollections(first.getAnnotations(),second.getAnnotations()));
    vde.setModifiers(mergeModifiers(first.getModifiers(),second.getModifiers()));
    vde.setVars(mergeCollectionsInOrder(first.getVars(),second.getVars()));

    return vde;
  }

  @Override public boolean isEquals(VariableDeclarationExpr first, VariableDeclarationExpr second) {
    if(first == second) return true;

    if(!isEqualsUseMerger(first.getType(),second.getType())) return false;
    if(!isEqualsUseMerger(first.getVars(),second.getVars())) return false;

    return true;
  }
}
