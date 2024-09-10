# 工程启动核心
order : 3   tools  -> auto -> launcher

工程为应用类工程启动服务核心，包括启动扩展、公共属性工程

## 工程核心
1. 启动类扩展加载 基于SPI 方式
2. 公共工程启动
   1. 应用启动属性配置
   2. 日志级别动态监管
   3. 自动封装对象统一返回 
   4. 统一异常处理

## 参考

![Spring Boot生命周期](https://img2020.cnblogs.com/blog/568153/202108/568153-20210809182954565-647364613.png)

https://www.cnblogs.com/yourbatman/p/13257999.html

#### springboot 优先级配置
   https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config


### 20200226 2.0 version release notes
1.mongo dao  add eq neq neq gt lt bt operations