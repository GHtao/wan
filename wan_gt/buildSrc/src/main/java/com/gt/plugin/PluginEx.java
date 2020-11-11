package com.gt.plugin;

/**
 * time 2020/8/21 0021
 * author GT
 *
 * 扩展bean必须有get set方法 和空的构造函数
 */
class PluginEx {
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public int getPluginNumber() {
        return pluginNumber;
    }

    public void setPluginNumber(int pluginNumber) {
        this.pluginNumber = pluginNumber;
    }

    public PluginEx(){

    }
    private String pluginName;
    private int pluginNumber;
}
