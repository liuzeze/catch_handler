package com.lz.httplib.http;


public interface ConfigModule {
    /**
     * 使用{@link GlobalConfigBuild.Builder}给框架配置一些配置参数
     *
     * @param builder
     */
    void applyOptions(GlobalConfigBuild.Builder builder);

}
