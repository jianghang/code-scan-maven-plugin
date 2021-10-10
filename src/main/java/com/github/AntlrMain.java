package com.github;

import com.github.java.ScanJavaCodeListener;
import com.github.java.antlr.JavaLexer;
import com.github.java.antlr.JavaParser;
import com.google.common.collect.Sets;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Set;

public class AntlrMain {

    public static void main(String[] args) {
        String code = "package com.sunsharing.xshare.catalog.config;\n" +
                "\n" +
                "import com.alibaba.druid.pool.DruidDataSource;\n" +
                "import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "\n" +
                "import javax.sql.DataSource;\n" +
                "import java.sql.SQLException;\n" +
                "\n" +
                "@Configuration\n" +
                "@com.google.common.annotations.Beta\n" +
                "public class DatabaseConfig {\n" +
                "\n" +
                "    @Bean\n" +
                "    @ConfigurationProperties(prefix = \"spring.datasource\")\n" +
                "    public DataSource dataSource() throws SQLException {\n" +
                "        DruidDataSource datasource = DruidDataSourceBuilder.create().build();\n" +
                "        com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(\"11\");\n" +
                "        datasource.setFilters(\"stat\");\n" +
                "        return datasource;\n" +
                "    }\n" +
                "}";
        System.out.println(code);
        CharStream charStream = CharStreams.fromString(code);
        JavaLexer javaLexer = new JavaLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(javaLexer);
        JavaParser javaParser = new JavaParser(commonTokenStream);
        ParseTree parseTree = javaParser.compilationUnit();

        Set<String> scanImportSet = Sets.newHashSet("java.util.ArrayList");
        ScanJavaCodeListener scanJavaCodeListener = new ScanJavaCodeListener(scanImportSet, commonTokenStream);

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(scanJavaCodeListener, parseTree);
    }
}
