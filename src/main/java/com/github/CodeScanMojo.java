package com.github;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.common.enums.AlarmLevelEnum;
import com.github.java.ScanJavaCodeListener;
import com.github.java.antlr.JavaParser;
import com.github.model.ScanResultInfo;
import com.github.utils.AntlrUtils;
import com.github.utils.FileUtils;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mojo(name = "scan", defaultPhase = LifecyclePhase.VERIFY, requiresDependencyResolution = ResolutionScope.NONE, threadSafe = true)
public class CodeScanMojo extends AbstractMojo {

    @Parameter(property = "project.build.directory", defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    @Parameter(property = "project.build.sourceDirectory", defaultValue = "${project.build.sourceDirectory}", required = true)
    private File sourceDirectory;

    @Parameter(property = "includes", defaultValue = "${includes}", required = true)
    private String[] includes;

    @Parameter(property = "alarmLevel", defaultValue = "${alarmLevel}")
    private String alarmLevel;

    @Override
    public void execute() throws MojoExecutionException {
        String currentPath = null;
        try {
            if (!sourceDirectory.exists()) {
                throw new MojoExecutionException("scan java code error,sourceDirectory is empty");
            }
            getLog().info("sourceDirectory: " + sourceDirectory.getAbsolutePath());
            getLog().info("includes: " + Arrays.toString(includes));
            getLog().info("alarm level: " + alarmLevel);
            List<Path> sourcePathList = Lists.newArrayList();
            FileUtils.getAllFile(sourceDirectory.toPath(), sourcePathList);
            sourcePathList = sourcePathList.stream().filter(path -> path.toString().contains(".java")).collect(Collectors.toList());
            List<ScanResultInfo> scanResultInfoList = Lists.newArrayList();
            for (Path path : sourcePathList) {
                currentPath = path.toString();
                CommonTokenStream commonTokenStream = AntlrUtils.getTokenStreamFromFile(path.toString());
                JavaParser javaParser = new JavaParser(commonTokenStream);
                ParseTree parseTree = javaParser.compilationUnit();

                Set<String> includeSet = Arrays.stream(includes).collect(Collectors.toSet());
                ScanJavaCodeListener scanJavaCodeListener = new ScanJavaCodeListener(includeSet, commonTokenStream);
                ParseTreeWalker walker = new ParseTreeWalker();
                walker.walk(scanJavaCodeListener, parseTree);

                scanJavaCodeListener.scanResultInfoList.forEach(scanResultInfo -> scanResultInfo.setPath(path.toString()));
                scanResultInfoList.addAll(scanJavaCodeListener.scanResultInfoList);
            }
            if (!scanResultInfoList.isEmpty()) {
                AlarmLevelEnum alarmLevelEnum = AlarmLevelEnum.getAlarmLevel(alarmLevel);
                switch (alarmLevelEnum) {
                    case WARN:
                        scanResultInfoList.forEach(scanResultInfo -> getLog().warn(scanResultInfo.toString()));
                        break;
                    case ERROR:
                        scanResultInfoList.forEach(scanResultInfo -> getLog().error(scanResultInfo.toString()));
                        throw new MojoFailureException("?????????????????????????????????????????????????????????????????????");
                    default:
                        scanResultInfoList.forEach(scanResultInfo -> getLog().warn(scanResultInfo.toString()));
                }
            }
        } catch (MojoFailureException e) {
            throw new MojoExecutionException("scan java code error message", e);
        } catch (Exception e) {
            getLog().error(e);
            getLog().error("scan error path: " + currentPath);
            throw new MojoExecutionException("scan java code error message", e);
        }
    }
}
