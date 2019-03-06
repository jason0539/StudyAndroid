package com.jason.demo.gradle.asm;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformOutputProvider;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * Created by daoming.lzh on 2019/3/6
 */
public class Utils {

    public static final Path getDefaultOutputPath(TransformOutputProvider outputProvider, QualifiedContent qualifiedContent) {
        //函数式编程
        Function<QualifiedContent, Path> getOutDirFunction = input -> {
            Format format;
            if (input instanceof DirectoryInput) {
                format = Format.DIRECTORY;
            } else if (input instanceof JarInput) {
                format = Format.JAR;
            } else {
                throw new UnsupportedOperationException("unknow format " + input);
            }
            File file = outputProvider.getContentLocation(input.getName(), input.getContentTypes(), input.getScopes(), format);
            return file.toPath();
        };
        Path path = getOutDirFunction.apply(qualifiedContent);
        return path;
    }

    public static boolean isRClass(String className) {
        int $ = className.lastIndexOf('$');
        int slash = className.lastIndexOf('/', $);
        return $ > slash && $ < className.length() && (className.charAt(slash + 1) | className.charAt($ - 1)) == 'R';
    }
}
