package com.jason.demo.gradle.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by daoming.lzh on 2019/3/6
 */
public class PredicateClassVisitor extends ClassVisitor {
    private boolean attemptToVisitR;

    PredicateClassVisitor() {
        super(Opcodes.ASM5);
    }

    boolean isAttemptToVisitR() {
        return attemptToVisitR;
    }


    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if (!attemptToVisitR
                && access == 0x19 /*ACC_PUBLIC | ACC_STATIC | ACC_FINAL*/
                && Utils.isRClass(name)) {
            attemptToVisitR = true;
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        if (attemptToVisitR) return null;
        return new MethodVisitor(Opcodes.ASM5, null) {

            @Override
            public void visitFieldInsn(int opcode, String owner, String fieldName,
                                       String fieldDesc) {

                if (attemptToVisitR
                        || opcode != Opcodes.GETSTATIC
                        || owner.startsWith("java/lang/")) {
                    return;
                }

                attemptToVisitR = Utils.isRClass(owner);
            }
        };
    }
}
