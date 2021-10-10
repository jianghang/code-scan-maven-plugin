package com.github.utils;

import com.github.java.antlr.JavaLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class AntlrUtils {

    public static CommonTokenStream getTokenStreamFromFile(String fileName) throws IOException {
        CharStream charStream = CharStreams.fromFileName(fileName);
        JavaLexer javaLexer = new JavaLexer(charStream);
        return new CommonTokenStream(javaLexer);
    }
}
