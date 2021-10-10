package com.github.java;

import com.github.java.antlr.JavaParser;
import com.github.java.antlr.JavaParserBaseListener;
import com.github.model.ScanResultInfo;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ScanJavaCodeListener extends JavaParserBaseListener {

    public Set<String> scanImportSet;

    public BufferedTokenStream bufferedTokenStream;

    public List<ScanResultInfo> scanResultInfoList = Lists.newArrayList();

    public ScanJavaCodeListener(Set<String> scanImportSet, BufferedTokenStream bufferedTokenStream) {
        this.scanImportSet = scanImportSet;
        this.bufferedTokenStream = bufferedTokenStream;
    }

    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        if (Objects.nonNull(ctx.qualifiedName())) {
            if (scanImportSet.contains(ctx.qualifiedName().getText())) {
                Token token = ctx.getStart();
                scanResultInfoList.add(new ScanResultInfo(token.getLine(), bufferedTokenStream.getText(ctx)));
            }
        }
    }

    @Override
    public void enterAnnotation(JavaParser.AnnotationContext ctx) {
        if (Objects.nonNull(ctx.qualifiedName())) {
            if (scanImportSet.contains(ctx.qualifiedName().getText())) {
                Token token = ctx.getStart();
                scanResultInfoList.add(new ScanResultInfo(token.getLine(), ctx.getText()));
            }
        }
    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        if (Objects.nonNull(ctx.statementExpression)
                && Objects.nonNull(ctx.statementExpression.expression())
                && ctx.statementExpression.expression().size() > 0) {
            if (scanImportSet.contains(ctx.statementExpression.expression(0).getText())) {
                Token token = ctx.statementExpression.getStart();
                scanResultInfoList.add(new ScanResultInfo(token.getLine(), ctx.getText()));
            }
        }
    }
}
