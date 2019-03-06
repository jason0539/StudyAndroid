package com.jason.demo.gradle.asm;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Created by daoming.lzh on 2019/3/4
 */
public class AsmPlugin implements Plugin<Project> {
    public static final Logger logger = Logging.getLogger(AsmPlugin.class);

    @Override
    public void apply(Project project) {
        logger.lifecycle("asm============================AsmPlugin.apply");
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new AsmPluginTransform(project));
    }
}
