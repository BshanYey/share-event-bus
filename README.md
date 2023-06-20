# 项目描述
一个事件总线，包含功能如下：
- 生产节点（ProductionNode）
  - 定义和规范
  - 注册
- 生产线（ProductionLine）
  - 动态编排和发布
  - 版本管理和同步
  - 生产节点流转
  - 生产线链路追踪

# 项目功能
## 客户端
使用spring Boot Starter的形式，包含功能：
- 生产节点（ProductionNode）的规范和定义
- 总线客户端功能
  - 生产节点（ProductionNode）注册
  - 生产线（ProductionLine）同步


## 服务端
主要具有如下功能：
- 生产节点（ProductionNode）注册
- 生产线（ProductionLine）编排
- 生产节点（ProductionNode）动态调度
- 生产线（ProductionLine）同步

# 项目结构
```
share-event-bus
    |- boot-starter
        |- event-bus-client
    |- event-bus-server
        |- adapter
        |- application
        |- domain
        |- infrastructure
```
# 开发计划
客户端
服务端
  编排
  调度
