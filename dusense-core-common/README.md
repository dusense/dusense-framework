# 核心公共相关
order : 1   common

## 工程核心
1. 工具类层
2. 自动化auto 层
3. 公共核心定义层
```
    apache commons-lang3、
    commons-io、
    commons-codec、
    commons-digester3、
    commons-collections4
    
    jackson jsr310
    
```
## 关键用例实现
### 1.excel 通用化工具操作
    解析excel文件为二维数组，标记头信息
    解析excel文件流数据，和bean 结构
    导出文件流 用于下载功能

### 2.Jackson 工具
    解析Json Str
    转换Json Object

### 3.Exception
    应用异常
    Web服务异常
    业务异常
    安全、性能、系统级别等异常区分

### 4.SSL Http 支持 NOSSL
    增强SSL 的连接不验证证书

### 5.时间类工具
    本土化，中国类型

### 6.加密算法工具支持
    国密支持
    MD5 对称加密
    RSA 不对称加密

### 7.ThreadLocal 增强
    相关应用的线程绑定数据均不在使用默认工具，而是一个上下文操作
    ThreadContextUtil