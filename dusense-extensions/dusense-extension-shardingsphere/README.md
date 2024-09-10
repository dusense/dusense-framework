# 产品VISDATA 2.0 Dashbd-Common jar
ZANZIBAR KETO
```
<relation-tuple> ::= <object>'#'relation'@'<subject>
<object> ::= namespace':'object_id
<subject> ::= subject_id | <subject_set>
<subject_set> ::= <object>'#'relation

videos:cat.mp4#view@felix

```

```
 基本数据权限支持
 777 法则  owner权限 ，所属组其他用户权限，其他组用户权限
 page:id1#owner@user1  用户1是page-id1的所有者

```

## 获取当前用户可view的page 列表
1. 当前用户可查看的列表信息
2. 当前用户所属组的列表信息
3. 其他特定权限

```azure

where scope.create_dept in (${deptId}) and category = 5

page:/*#view@groupowner
page:/?category=5#view@groupowner

videos:cat.mp4#view@(groups:admin#member)


```

## 单独view的pageId  可直接使用keto等逻辑
1. 检查当前用户与pageId 的权限所属