package com.gt.plugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * time 2020/8/21 0021
 * author GT
 * task 有输入和输出 doFirst 和 doLast可以拦截输入输出的文件 做自己的修改操作
 */
class PluginTest implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        //在 gradle中apply plugin 就会回调这个apply方法
        PluginEx ex = project.getExtensions().create("plugin_test",PluginEx.class);
        project.afterEvaluate(project1 -> {
            //解析完之后才能拿到ex中的属性
            System.out.println(ex.getPluginName());
            System.out.println(ex.getPluginNumber());
        });
    }
}
