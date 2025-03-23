这样我们可以将一些maven、ant、gradle、npm工具通过一个job模板和不同的构建命令实现。templates的好处是我们在其中定义了模板流水线，这些流水线可以直接让项目使用。当遇到个性化项目的时候就可以在当前项目创建.gitlab-ci.yml文件来引用模板文件，再进一步实现个性化需要。

模板库通常有两个目录：Jobs和templates

官方模板文档：https://docs.gitlab.cn/jh/ci/examples/

https://jihulab.com/gitlab-cn/gitlab/-/tree/master/lib/gitlab/ci/templates

**DevOps系列文章 之GitLabCI模板库的流水线：**

jobs目录用于存放作业模板。templates目录用于存放流水线模板。这次使用default-pipeline.yml作为所有作业的基础模板。【https://blog.csdn.net/Coder_Boy_/article/details/131880719】

**======【本工程ci模板参考自github上分享的工程：】======** 
https://github.com/cidevopsci/cidevops-newci-service.git

