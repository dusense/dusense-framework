# 工程启动核心
order : 3

    tools  -> auto -> launcher

    -> auto -> launcher

工程为应用类工程启动服务核心，包括启动扩展、公共属性工程

## 工程核心
1. ThreadLocal 上下文
2. Log MOC 上下文
3. 未来的响应式编程上下文逻辑，自动同步本地线程ThreadLocal 的上下文
4. 支持分布式上下文数据逻辑（Redis缓存结构模型）ThreadLocal 支持分布式线程逻辑~
5. 环境变量启动的上下文

## 参考



