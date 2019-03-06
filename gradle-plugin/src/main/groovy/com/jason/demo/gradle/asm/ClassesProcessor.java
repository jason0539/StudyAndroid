package com.jason.demo.gradle.asm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * Created by daoming.lzh on 2019/3/6
 */
abstract class ClassesProcessor implements Processor {
    protected final Function<byte[], byte[]> classTransform;
    protected final Path src;
    protected final Path dst;

    /**
     * @param src Source path, can be resolved as a directory or a jar file
     * @param dst Destination path
     */
    ClassesProcessor(Function<byte[], byte[]> classTransform,
                     Path src, Path dst) {
        this.classTransform = classTransform;
        this.src = src;
        this.dst = dst;
        if (Files.notExists(src)) {
            throw new IllegalArgumentException("No such file " + src);
        }
    }
}
