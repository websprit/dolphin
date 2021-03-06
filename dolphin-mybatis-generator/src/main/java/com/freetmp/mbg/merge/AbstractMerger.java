package com.freetmp.mbg.merge;

import com.freetmp.mbg.merge.comment.BlockCommentMerger;
import com.freetmp.mbg.merge.comment.JavadocCommentMerger;
import com.freetmp.mbg.merge.comment.LineCommentMerger;
import com.freetmp.mbg.merge.declaration.*;
import com.freetmp.mbg.merge.expression.*;
import com.freetmp.mbg.merge.parameter.MultiTypeParameterMerger;
import com.freetmp.mbg.merge.parameter.ParameterMerger;
import com.freetmp.mbg.merge.parameter.TypeParameterMerger;
import com.freetmp.mbg.merge.statement.*;
import com.freetmp.mbg.merge.type.*;
import com.freetmp.mbg.merge.variable.VariableDeclaratorIdMerger;
import com.freetmp.mbg.merge.variable.VariableDeclaratorMerger;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import info.debatty.java.stringsimilarity.Levenshtein;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiuPin on 2015/3/27.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMerger<M extends Node> {

  protected static ConcurrentHashMap<Class, AbstractMerger> map = new ConcurrentHashMap<>();

  static {
    // comment
    map.put(BlockComment.class, new BlockCommentMerger());
    map.put(JavadocComment.class, new JavadocCommentMerger());
    map.put(LineComment.class, new LineCommentMerger());

    // declaration
    map.put(AnnotationDeclaration.class, new AnnotationDeclarationMerger());
    map.put(AnnotationMemberDeclaration.class, new AnnotationDeclarationMerger());
    map.put(BodyDeclaration.class, new BodyDeclarationMerger());
    map.put(ClassOrInterfaceDeclaration.class, new ClassOrInterfaceDeclarationMerger());
    map.put(ConstructorDeclaration.class, new ConstructorDeclarationMerger());
    map.put(EmptyMemberDeclaration.class, new EmptyMemberDeclarationMerger());
    map.put(EmptyTypeDeclaration.class, new EmptyTypeDeclarationMerger());
    map.put(EnumConstantDeclaration.class, new EnumConstantDeclarationMerger());
    map.put(EnumDeclaration.class, new EnumDeclarationMerger());
    map.put(FieldDeclaration.class, new FieldDeclarationMerger());
    map.put(InitializerDeclaration.class, new InitializerDeclarationMerger());
    map.put(MethodDeclaration.class, new MethodDeclarationMerger());
    map.put(PackageDeclaration.class, new PackageDeclarationMerger());
    map.put(ImportDeclaration.class, new ImportDeclarationMerger());

    // expression
    map.put(Expression.class, new ExpressionMerger());
    map.put(MarkerAnnotationExpr.class, new MarkerAnnotationExprMerger());
    map.put(NormalAnnotationExpr.class, new NormalAnnotationExprMerger());
    map.put(SingleMemberAnnotationExpr.class, new SingleMemberAnnotationExprMerger());
    map.put(ArrayAccessExpr.class,new ArrayAccessExprMerger());
    map.put(ArrayCreationExpr.class, new ArrayAccessExprMerger());
    map.put(ArrayInitializerExpr.class, new ArrayInitializerExprMerger());
    map.put(AssignExpr.class, new AssignExprMerger());
    map.put(BinaryExpr.class, new BinaryExprMerger());
    map.put(BooleanLiteralExpr.class, new BooleanLiteralExprMerger());
    map.put(CastExpr.class, new CastExprMerger());
    map.put(CharLiteralExpr.class, new CharLiteralExprMerger());
    map.put(ClassExpr.class, new ClassExprMerger());
    map.put(ConditionalExpr.class, new ConditionalExprMerger());
    map.put(DoubleLiteralExpr.class, new DoubleLiteralExprMerger());
    map.put(EnclosedExpr.class, new EnclosedExprMerger());
    map.put(FieldAccessExpr.class, new FieldAccessExprMerger());
    map.put(InstanceOfExpr.class,new InstanceOfExprMerger());
    map.put(IntegerLiteralExpr.class, new IntegerLiteralExprMerger());
    map.put(IntegerLiteralMinValueExpr.class, new IntegerLiteralMinValueExprMerger());
    map.put(LambdaExpr.class, new LambdaExprMerger());
    map.put(LongLiteralExpr.class, new LongLiteralExprMerger());
    map.put(LongLiteralMinValueExpr.class, new LongLiteralMinValueExprMerger());
    map.put(MemberValuePair.class, new MemberValuePairMerger());
    map.put(MethodCallExpr.class, new MethodCallExprMerger());
    map.put(MethodReferenceExpr.class, new MethodReferenceExprMerger());
    map.put(NameExpr.class, new NameExprMerger());
    map.put(NullLiteralExpr.class, new NullLiteralExprMerger());
    map.put(ObjectCreationExpr.class, new ObjectCreationExprMerger());
    map.put(QualifiedNameExpr.class, new QualifiedNameExprMerger());
    map.put(StringLiteralExpr.class, new StringLiteralExprMerger());
    map.put(SuperExpr.class, new SuperExprMerger());
    map.put(ThisExpr.class, new ThisExprMerger());
    map.put(TypeExpr.class, new TypeExprMerger());
    map.put(UnaryExpr.class, new UnaryExprMerger());
    map.put(VariableDeclarationExpr.class, new VariableDeclarationExprMerger());

    // parameter
    map.put(Parameter.class, new ParameterMerger());
    map.put(MultiTypeParameter.class, new MultiTypeParameterMerger());
    map.put(TypeParameter.class, new TypeParameterMerger());

    // statement
    map.put(BlockStmt.class, new BlockStmtMerger());
    map.put(AssertStmt.class, new AssertStmtMerger());
    map.put(BreakStmt.class, new BreakStmtMerger());
    map.put(CatchClause.class, new CatchClauseMerger());
    map.put(ContinueStmt.class, new ContinueStmtMerger());
    map.put(DoStmt.class, new DoStmtMerger());
    map.put(EmptyStmt.class, new EmptyStmtMerger());
    map.put(ExplicitConstructorInvocationStmt.class, new ExplicitConstructorInvocationStmtMerger());
    map.put(ExpressionStmt.class, new ExpressionStmtMerger());
    map.put(ForeachStmt.class, new ForeachStmtMerger());
    map.put(ForStmt.class, new ForStmtMerger());
    map.put(IfStmt.class, new IfStmtMerger());
    map.put(LabeledStmt.class, new LabeledStmtMerger());
    map.put(ReturnStmt.class, new ReturnStmtMerger());
    map.put(SwitchEntryStmt.class, new SwitchEntryStmtMerger());
    map.put(SwitchStmt.class, new SwitchStmtMerger());
    map.put(SynchronizedStmt.class, new SynchronizedStmtMerger());
    map.put(ThrowStmt.class, new ThrowStmtMerger());
    map.put(TryStmt.class, new TryStmtMerger());
    map.put(TypeDeclarationStmt.class, new TypeDeclarationStmtMerger());
    map.put(WhileStmt.class, new WhileStmtMerger());

    // type
    map.put(ClassOrInterfaceType.class, new ClassOrInterfaceTypeMerger());
    map.put(PrimitiveType.class, new PrimitiveTypeMerger());
    map.put(ReferenceType.class, new ReferenceTypeMerger());
    map.put(VoidType.class, new VoidTypeMerger());
    map.put(WildcardType.class, new WildcardTypeMerger());

    //variable
    map.put(VariableDeclaratorId.class, new VariableDeclaratorIdMerger());
    map.put(VariableDeclarator.class, new VariableDeclaratorMerger());

    // compile unit
    map.put(CompilationUnit.class, new CompilationUnitMerger());
  }

  protected static Levenshtein levenshtein = new Levenshtein();

  public <T> boolean isAllNull(T one, T two) {
    return one == null ? two == null : false;
  }

  public <T> boolean isAllNotNull(T one, T two) {
    return one != null && two != null;
  }

  public <T> T findFirstNotNull(T... types) {
    for (T type : types) {
      if (type != null) return type;
    }
    return null;
  }

  public <T> int indexOf(int start, List<T> datas, T target) {
    int index = -1;

    for (int i = start; i < datas.size(); i++) {
      if (datas.get(i).equals(target)) {
        index = i;
        break;
      }
    }
    return index;
  }

  public <T> T mergeSelective(T one, T two) {
    T t = null;

    if (isAllNull(one, two)) {
      return t;
    }

    t = findFirstNotNull(one, two);

    return t;
  }

  public <T extends BaseParameter> boolean isParametersEquals(List<T> one, List<T> two) {

    if (one == two) return true;
    if (one == null || two == null) return false;

    if (one.size() != two.size()) return false;

    for (int i = 0; i < one.size(); i++) {

      T o = one.get(i);
      T t = two.get(i);

      AbstractMerger merger = getMerger(o.getClass());
      if (!merger.isEquals(o, t)) return false;
    }

    return true;
  }

  public boolean isTypeParameterEquals(List<TypeParameter> first, List<TypeParameter> second) {

    if (first == second) return true;
    if (first == null || second == null) return false;

    if (first.size() != second.size()) return false;

    for (int i = 0; i < first.size(); i++) {
      AbstractMerger<TypeParameter> merger = getMerger(TypeParameter.class);
      if (!merger.isEquals(first.get(i), second.get(i))) return false;
    }

    return true;
  }

  public <T extends Node> boolean isSmallerHasEqualsInBigger(List<T> first, List<T> second, boolean useOrigin) {

    if (first == second) return true;
    if (first == null || second == null) return true;

    List<T> smaller = null;
    List<T> bigger = null;

    if (first.size() > second.size()) {
      smaller = second;
      bigger = first;
    } else {
      smaller = first;
      bigger = second;
    }

    for (T st : smaller) {
      if (useOrigin) {
        if (!bigger.contains(st)) return false;
      } else {
        AbstractMerger merger = getMerger(st.getClass());
        boolean found = false;
        for (T bt : bigger) {
          if (merger.isEquals(st, bt)) {
            found = true;
            break;
          }
        }
        if (!found) return false;
      }
    }

    return true;
  }

  public int mergeModifiers(int one, int two) {
    return ModifierSet.addModifier(one, two);
  }

  public <T extends Node> List<T> mergeListNoDuplicate(List<T> one, List<T> two, boolean useMerger) {

    if (one == two) return one;
    if (one == null) return two;
    if (two == null) return one;

    List<T> results = new ArrayList<>();

    if (useMerger) {

      List<T> twoCopy = new ArrayList<>();
      twoCopy.addAll(two);

      for (T ot : one) {
        AbstractMerger merger = getMerger(ot.getClass());
        T found = null;
        for (T tt : twoCopy) {
          if (ot.getClass().equals(tt.getClass()) && merger.isEquals(ot, tt)) {
            found = tt;
            break;
          }
        }
        if (found != null) {
          twoCopy.remove(found);
          results.add((T) merger.merge(ot, found));
        } else {
          results.add(ot);
        }
      }

      results.addAll(twoCopy);

    } else {
      TreeSet<T> treeSet = new TreeSet<>();
      treeSet.addAll(one);
      treeSet.addAll(two);
      results.addAll(treeSet);
    }

    return results;
  }

  public <T> List<T> mergeListInOrder(List<T> one, List<T> two) {
    List<T> results = new ArrayList<>();

    if (isAllNull(one, two)) return null;

    if (isAllNotNull(one, two)) {

      int start = 0;
      for (int i = 0; i < one.size(); i++) {
        T t = one.get(i);
        int index = indexOf(start, two, t);
        if (index == -1 || index == start) {
          results.add(t);
          start += 1;
        } else {

          results.addAll(two.subList(start, ++index));
          start = index;
        }
      }

      if (start < two.size()) {
        results.addAll(two.subList(start, two.size()));
      }

    } else {
      results.addAll(findFirstNotNull(one, two));
    }

    return results;
  }

  /**
   * first check if mapper of the type T exist, if existed return it
   * else check if mapper of the supper type exist, then return it
   * ...
   */
  public static <T extends Node> AbstractMerger<T> getMerger(Class<T> clazz) {

    AbstractMerger<T> merger = null;

    Class<?> type = clazz;

    while (merger == null && type != null) {
      merger = map.get(type);
      type = type.getSuperclass();
    }

    return merger;
  }

  protected static <T extends Node> void register(Class<T> clazz, AbstractMerger<T> abstractMerger) {
    map.put(clazz, abstractMerger);
  }


  protected <T extends Node> List<T> mergeCollections(List<T> first, List<T> second) {

    if (first == null) return second;
    if (second == null) return first;

    List<T> nodes = new ArrayList<>();

    List<T> copies = new ArrayList<>();
    copies.addAll(second);

    for (T node : first) {

      AbstractMerger merger = getMerger(node.getClass());

      T found = null;

      for (T anotherNode : copies) {
        if (node.getClass().equals(anotherNode.getClass())) {
          if (merger.isEquals(node, anotherNode)) {
            found = anotherNode;
            break;
          }
        }
      }

      if (found != null) {
        nodes.add((T) merger.merge(node, found));
        copies.remove(found);
      } else {
        nodes.add(node);
      }

    }

    if (!copies.isEmpty()) {
      nodes.addAll(copies);
    }

    return nodes;
  }

  protected <T extends Node> List<T> mergeCollectionsInOrder(List<T> first, List<T> second) {
    if (first == null) return second;
    if (second == null) return first;

    List<T> nodes = new ArrayList<>();

    int max = Math.max(first.size(), second.size());
    for (int i = 0; i < max; i++) {
      T f = i < first.size() ? first.get(i) : null;
      T s = i < second.size() ? second.get(i) : null;
      if (isAllNotNull(f, s)) {

        AbstractMerger merger = getMerger(f.getClass());
        nodes.add((T) merger.merge(f, s));

      } else {
        nodes.add(f != null ? f : s);
      }
    }

    return nodes;
  }


  protected <T extends Node> T mergeSingle(T first, T second) {

    /**
     * ensure the parameter passed to the merge is either not null
     */
    if (first == null) return second;
    if (second == null) return first;

    if (first.getClass().equals(second.getClass())) {

      AbstractMerger merger = getMerger(first.getClass());

      if (merger.isEquals(first, second)) {
        return (T) merger.merge(first, second);
      }
    }

    return null;
  }

  protected <T extends Node> boolean isEqualsUseMerger(T first, T second){

    if(first == second) return true;
    if(first == null || second == null) return false;

    if(first.getClass().equals(second.getClass())){
      AbstractMerger merger = getMerger(first.getClass());
      return merger.isEquals(first,second);
    }

    return false;
  }

  protected <T extends Node> boolean isEqualsUseMerger(List<T> first, List<T> second){
    if(first == second) return true;
    if(first == null || second == null) return false;
    if(first.size() != second.size()) return false;

    for(int i = 0; i < first.size(); i++){
      if(!isEqualsUseMerger(first.get(i),second.get(i))){
        return false;
      }
    }

    return true;
  }

  protected <T extends Node> void mergeOrphanComments(T first, T second, T third){
    List<Comment> comments = mergeCollections(first.getOrphanComments(),second.getOrphanComments());
    if(comments != null && !comments.isEmpty()){
      for(Comment comment : comments){
        third.addOrphanComment(comment);
      }
    }
  }

  protected double similarity(String first, String second){
    if(first == null || second == null) return 0d;
    return levenshtein.similarity(first,second);
  }

  protected <T extends Node> void copyPosition(T source, T dest){
    dest.setBeginColumn(source.getBeginColumn());
    dest.setBeginLine(source.getBeginLine());
    dest.setEndColumn(source.getEndColumn());
    dest.setEndLine(source.getEndLine());
  }

  public abstract M doMerge(M first,M second);

  public M merge(M first, M second){
    if(first == null) return second;
    if(second == null) return first;

    M m = doMerge(first,second);
    m.setComment(mergeSingle(first.getComment(),second.getComment()));
    mergeOrphanComments(first, second, m);
    return m;
  }

  public abstract boolean doIsEquals(M first, M second);

  public boolean isEquals(M first, M second){
    if (first == second) return true;
    if (first == null || second == null) return false;
    return doIsEquals(first,second);
  }
}
