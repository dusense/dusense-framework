# Dusense 4.0 发布 dusense-docapi
this project is dusense docapi common  jar for  all service ;

OpenApi 3.0 规范
https://blog.51cto.com/u_15127513/2684846

## 工程描述
    这是一个用于自动化处理swagger api  knife4j  文档的工程
    springboot2.6.x 引入swagger2 会存在问题
    
    actuator WebMvcEndpointHandlerMappingConfiguration  默认会指定PathPatternParser 导致condition 为null

    