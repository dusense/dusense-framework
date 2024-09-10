# 产品Dusense 4.0 Dusense-Framework
this project is common jar for all dusense service ;

include commmon service & dao support redis mongo mysql etc.

### 20200226 2.0 version release notes
1. 基于Jakarta 10 构建核心抽象mvc层，包括api、service、orm dao 层的CURD抽象
2. 实现融合Spring Cloud 微服务架构的基础组件、loadbalancer 、流控 、nacos注册相关
3. 基于slf4j2 api 为机制的log 体系，
4. 实现基于国产化支持的相关逻辑
5. 实现基于字段加解密的相关逻辑处理
6. 实现API报文加解密逻辑
7. 实现基本的自动化测试体系
8. 实现orm层读写分离等逻辑
9. 实现租户的处理
10. 实现功能权限
11. 实现数据权限
12. 构建Jakarta 的缓存体系？
13. 构建1个可自动化dev生成代码的工程
14. 实现基础的反应式上下文处理
15. 自监控体系


```
dusense-framework
├── ------- 基础架构设计相关
├── dusense-core-common -- 工具类相关  - [ ✔️]
├── dusense-core-cloud -- 微服务cloud封装模块 - [ ✔️]
├── dusense-core-launcher -- 基础环境启动模块- [ ✔️]
├── dusense-core-context -- 应用上下文封装模块- [ ✔️]
├── ------- 应用架构设计相关
├── dusense-core-abmoudle -- 核心抽象模块- [ ✔️]
├── dusense-extension-docapi -- docapi拓展封装模块 - [ ✔️]
├── dusense-extension-hibernate -- Hibernate拓展封装模块- [ ✔️]
├── dusense-extension-datapermit -- 数据权限封装模块- [ ✔️]
├── dusense-extension-jcache -- local缓存系统模块 - [ ✔️]
├── dusense-extension-redis -- redis系统扩展模块、分布式缓存 - [ ✔️]
├── dusense-extension-transaction -- 分布式事务模块 - [ ✔️]
├── dusense-extension-shardingsphere -- shardingsphere读写分离模块 - [ ✔️]
├── dusense-extension-msgbus -- 消息总线模块 - [ ✔️]
├── dusense-extension-log -- 日志log4j2封装模块 - [ ✔️]
|
├── ------- 安全性能架构设计等相关
├── dusense-extension-autotest -- 单元测试封装模块 - [ ✔️]
├── dusense-extension-apicrypto -- API加密封装模块 - [ ✔️]
├── dusense-extension-monitor -- 监控相关模块 - [ X ️]
├── ------- 其他
├── dusense-boot-single -- 单服务启动包装模块 - [ ✔️]
├── dusense-boot-cluster -- 分布式服务启动包装模块 - [ ✔️]
└── dusense-core-develop -- 代码生成封装模块- [ X ️]

doc文档
    openApi 标准实现
    
统一日志
    log4j2
    
统一加载器服务
    产品统一环境变量
    
微服务相同类配置
    （nacos、sentinel、openfeign、loadblance etc..)
    
统一上下文（线程上下文等）
     未来结束ThreadContext 响应式编程
     
API请求包加解密
    前端获取加密算法后加密
        
数据层框架
    jsr persiste api  标准
    数据库mybatis
    JPA

数据读写分离  
    配置对数据源的模式
     Apache shardingsphere
       
分布式缓存架构
    JSR107 标准
    
数据权限架构 基于独立服务来处理数据权限？ 
    google zanzibar
    适应于中国区的组织架构权限
        用户-》用户组（组管理员，多层级|跨多组）
        用户角色
    多租户体系
 
    
统一测试包
    junit
    可对请求模拟操作
    
监控指标
    数据库监控
    Sentinel 监控
    线程池监控


```