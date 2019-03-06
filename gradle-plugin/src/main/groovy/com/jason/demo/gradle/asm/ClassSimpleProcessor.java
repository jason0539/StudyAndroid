package com.jason.demo.gradle.asm;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformOutputProvider;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daoming.lzh on 2019/3/6
 */
public class ClassSimpleProcessor implements Processor {
    private Collection<TransformInput> inputs;
    private TransformOutputProvider outputProvider;
    private Function<byte[], byte[]> transFunction;

    ClassSimpleProcessor(Collection<TransformInput> inputs, TransformOutputProvider outputProvider, Function<byte[], byte[]> transFunction) {
        this.inputs = inputs;
        this.outputProvider = outputProvider;
        this.transFunction = transFunction;
    }

    @Override
    public void proceed() {
//        Stream.concat(
//                streamOf(inputs, TransformInput::getDirectoryInputs).map(input -> {
//                    Path src = input.getFile().toPath();
//                    Path dst = Utils.getDefaultOutputPath(outputProvider,input);
//                    return new DirProcessor(transFunction, src, dst);
//                })
//                ,
//                streamOf(inputs, TransformInput::getJarInputs).map(input -> {
//                    Path src = input.getFile().toPath();
//                    Path dst = getTargetPath.apply(input);
//                    return new JarProcessor(transform, src, dst);
//                })
//        ).forEach(Processor::proceed);
        streamOf(inputs, TransformInput::getDirectoryInputs).map(input -> {
            Path src = input.getFile().toPath();
            Path dst = Utils.getDefaultOutputPath(outputProvider, input);
            return new DirProcessor(transFunction, src, dst);
        }).forEach(Processor::proceed);
    }

    private static <T extends QualifiedContent> Stream<T> streamOf(
            Collection<TransformInput> inputs,
            Function<TransformInput, Collection<T>> mapping) {
        Collection<T> list = inputs.stream()
                .map(mapping)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (list.size() >= Runtime.getRuntime().availableProcessors())
            return list.parallelStream();
        else
            return list.stream();
    }
}
