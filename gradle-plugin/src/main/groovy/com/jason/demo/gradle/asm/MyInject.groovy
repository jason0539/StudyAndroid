package com.jason.demo.gradle

import com.jason.demo.gradle.asm.AsmPlugin
import javassist.ByteArrayClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
public class MyInject {

    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "System.out.println(\" lzh ====== inject from gradle plugin : A haha A haha %s\" ); "

    public static void injectDir(String path, String packageName) {
        AsmPlugin.logger.lifecycle("=============inject dir,"+packageName)
        pool.appendClassPath(path)
        //避免某些系统类找不着报错，特定添加系统的类的路径
        CtClass contextClass = pool.makeClass("android.content.Context")
        CtClass attrSetClass = pool.makeClass("android.util.AttributeSet")
        byte[] contextClassByte = contextClass.toBytecode()
        byte[] attrSetClassByte = attrSetClass.toBytecode()
        contextClass.defrost()
        attrSetClass.defrost()
        pool.insertClassPath(new ByteArrayClassPath("android.content.Context",contextClassByte))
        pool.insertClassPath(new ByteArrayClassPath("android.util.AttributeSet",attrSetClassByte))

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                //确保当前文件是class文件，并且不是系统自动生成的class文件
//                if (filePath.endsWith(".class")
//                        && !filePath.contains('R$')
//                        && !filePath.contains('R.class')
//                        && !filePath.contains("BuildConfig.class")) {
                //因为当前工程类比较繁多，导致往构造函数插入有问题，限制com/jason/workdemo/touchevent包里的类做inject
                if (filePath.endsWith(".class")
                        && !filePath.contains('$')
                        && filePath.contains('com/jason/workdemo/demo')) {
                    AsmPlugin.logger.lifecycle("=============inject dir,package is "+packageName+",path is " + filePath)
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName);
                    boolean isMyPackage = index != -1;
                    if (isMyPackage) {
                        int end = filePath.length() - 6 // .class = 6
                        String className = filePath.substring(index, end).replace('\\', '.').replace('/', '.')
                        AsmPlugin.logger.lifecycle("=============inject dir " + className)
                        //开始修改class文件
                        CtClass c = pool.getCtClass(className)

                        if (c.isFrozen()) {
                            c.defrost()
                        }

                        CtConstructor[] cts = c.getDeclaredConstructors()
                        pool.importPackage("android.util.Log")
                        String logMsg = String.format(injectStr,className)
                        if (cts == null || cts.length == 0) {
                            //手动创建一个构造函数
                            CtConstructor constructor = new CtConstructor(new CtClass[0], c)
                            constructor.insertBeforeBody(logMsg)
                            c.addConstructor(constructor)
                        } else {
                            cts[0].insertBeforeBody(logMsg)
                        }
                        c.writeFile(path)
                        c.detach()
                    }else{
                        AsmPlugin.logger.lifecycle("=============inject not my package,break")
                    }
                }
            }
        }
    }


}