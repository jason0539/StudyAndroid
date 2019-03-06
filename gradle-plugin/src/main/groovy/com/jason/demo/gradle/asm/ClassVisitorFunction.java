package com.jason.demo.gradle.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.function.Function;

import static org.objectweb.asm.ClassReader.SKIP_DEBUG;
import static org.objectweb.asm.ClassReader.SKIP_FRAMES;

/**
 * Created by daoming.lzh on 2019/3/6
 */
public class ClassVisitorFunction implements Function<byte[], byte[]> {
    @Override
    public byte[] apply(byte[] origin) {
        ClassReader reader = new ClassReader(origin);

        PredicateClassVisitor precondition = new PredicateClassVisitor();
        reader.accept(precondition, SKIP_DEBUG | SKIP_FRAMES);
        if (precondition.isAttemptToVisitR()) {
            AsmPlugin.logger.lifecycle( "当前的类访问了R文件");
        }
        return origin;
        //如何使用asm对class做编辑，参考https://github.com/yrom/shrinker和https://asm.ow2.io/
    }
}