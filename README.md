# datax-admin

该项目[二开DataX](https://github.com/thestyleofme/DataX.git) 实现DataX集群，为了方便使用，故开发了datax-admin服务统一管理。

DataX分布式集群与负载均衡、任务执行/统计，基于DataX的通用数据同步微服务，一个Restful接口搞定所有通用数据同步

- **DataX分布式集群负载均衡**
- **Datax集群动态可伸缩**
- **支持http方式提交DataX任务到集群运行**
- **统计DataX执行信息以及本次执行脏数据**
- **DataX分布式日志**
- **DataX对源端分片执行，提高同步效率**
- **基于多数据源自带接口可创表查表结构等接口，可快速开发web页面创建Datax任务**
- **Datax任务管理，jobId全局唯一**
- **Datax任务敏感信息脱敏，如密码**

了解二开DataX详情，请移步[DataX](https://github.com/thestyleofme/DataX.git)

## Get Started

按照说明文档[DataX](https://github.com/thestyleofme/DataX/blob/master/datax-cluster.md) 启动DataX集群即可。

该项目需要[动态数据源(数据中台多数据源统一接口)](https://github.com/thestyleofme/plugin-driver-parent.git) 支持，可clone下来，按照说明文档进行打包，
放到此项目dist/plugin目录（配置文件可配置）下，即${your_path}/data-audit-parent/dist/plugins/driver-xxx.jar

## Introduction

TODO
- 接口介绍以及使用