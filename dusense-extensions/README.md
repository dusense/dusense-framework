# Dusense Framework 4.0 
this project is common jar for all dusense framewok ;

include commmon service & dao support mysql and extension like quarkus etc.

### 20200226 2.0 version release notes
1.0 增加dusense-core-common 模块，包含工具类
2.0 增加dusense-core-cloud 模块，包含微服务cloud封装
3.0 增加dusense-core-launcher 模块，包含基础环境启动模块
4.0 增加dusense-core-context 模块，包含应用上下文封装
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