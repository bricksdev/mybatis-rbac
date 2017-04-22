此项目是Mybatis的一个插件项目，主要用于数据权限处理
其中包括数据过滤权限、数据模糊权限
数据过滤参考的是RBAC权限模型进行设计的
参考RBAC-classes.jpg图片
1.数据过滤权限实现是将sql_mapping中定义的文件做替换，添加了数据过滤权限
2.模糊权限实现是将返回结果集的数据内容进行替换，用*号或其他信息替换

定义指定的插件
数据过滤权限插件
<plugin interceptor="cn.com.bricks.mybatis.rbac.DynamicRbacInterceptor">
</plugin>
模糊数据权限插件
<plugin interceptor="cn.com.bricks.mybatis.rbac.DynamicObscureInterceptor">
</plugin>
