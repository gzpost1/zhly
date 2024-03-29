#智慧楼宇后端

   #技术架构
    Spring Cloud Alibaba
    
   #模块
    common 通用的配置，代码
    domain 领域驱动设计的模型对象、仓储服务
    gateway 网关提供统一的路由访问
    system 系统模块，前端访问的主服务
    
   #依赖组件
    nacos mysql kafka redis zookeeper
    
   #运行环境
    安装jdk1.8并配置环境变量
    
   #配置
    修改gateway和system下的bootstrap.yml相应的服务器地址、端口号、用户名、密码等
    
   #项目启动
    idea运行gateway和system的Application