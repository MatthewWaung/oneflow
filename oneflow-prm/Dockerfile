# 使用 frolvlad/alpine-java 仓库中的 jdk8-slim 镜像作为基础镜像
# 该镜像基于 Alpine Linux 并包含 Java 8 的精简版本
FROM frolvlad/alpine-java:jdk8-slim

# 定义一个构建参数 JAR_FILE，默认值为 target 目录下的所有 .jar 文件
# 用于后续将本地的 JAR 文件复制到镜像中
ARG JAR_FILE=target/*.jar

# 定义一个构建参数 MS_MINCHO，指定字体文件 MS Mincho.ttf 在镜像中的路径
ARG MS_MINCHO="/usr/share/fonts/my_fonts/MS Mincho.ttf"

# 将本地的 JAR 文件复制到镜像中，并命名为 app.jar
COPY ${JAR_FILE} app.jar

# 创建一个软链接，将系统的时区设置为亚洲/上海时区
# 确保容器内的时间与上海时区一致
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 将时区信息写入 /etc/timezone 文件
RUN echo 'Asia/Shanghai' > /etc/timezone

# 在镜像中创建一个目录，用于存放字体文件
RUN mkdir -p /usr/share/fonts/my_fonts

ARG Roman_Font="Times New Roman.ttf"
ADD https://minio.oneflow.com:9000/public/simsun.ttc /usr/share/fonts/my_fonts/simsun.ttc
ADD https://minio.oneflow.com:9000/public/arial.ttf /usr/share/fonts/my_fonts/arial.ttf
ADD https://minio.oneflow.com:9000/public/malgun.ttf /usr/share/fonts/my_fonts/malgun.ttf
ADD https://minio.oneflow.com:9000/public/MS%20Mincho.ttf ${MS_MINCHO}
ADD https://minio.oneflow.com:9000/public/SourceHanSerifSC-Regular.otf /usr/share/fonts/my_fonts/SourceHanSerifSC-Regular.otf
ADD https://minio.oneflow.com:9000/public/meiryo.ttc /usr/share/fonts/my_fonts/meiryo.ttc
ADD https://minio.oneflow.com:9000/public/Meiryo.ttf /usr/share/fonts/my_fonts/Meiryo.ttf
ADD https://minio.oneflow.com:9000/public/Times%20New%20Roman.ttf /usr/share/fonts/my_fonts/${Roman_Font}

# 定义一个环境变量 JAVA_OPTS，用于设置 JVM 的参数
# -Xms2048m：设置 JVM 堆的初始大小为 2048MB
# -Xmx6144m：设置 JVM 堆的最大大小为 6144MB
# -XX:NewRatio=3：设置新生代和老年代的比例为 1:3
# -XX:SurvivorRatio=8：设置 Eden 区和 Survivor 区的比例为 8:1
# -XX:MetaspaceSize=256m：设置元空间的初始大小为 256MB
# -XX:MaxMetaspaceSize=512m：设置元空间的最大大小为 512MB
# -XX:+UseParallelGC：使用并行垃圾收集器
# -XX:+UseParallelOldGC：使用并行老年代垃圾收集器
# -XX:+PrintGCDetails：打印详细的垃圾回收信息
# -XX:+PrintGCTimeStamps：打印垃圾回收的时间戳
# -XX:+PrintGCDateStamps：打印垃圾回收的日期戳
# -XX:+PrintHeapAtGC：在垃圾回收时打印堆的信息
# -Xloggc:gc.log：将垃圾回收信息记录到 gc.log 文件中
ENV JAVA_OPTS="\
-Xms2048m \
-Xmx6144m \
-XX:NewRatio=3 \
-XX:SurvivorRatio=8 \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m \
-XX:+UseParallelGC \
-XX:+UseParallelOldGC \
-XX:+PrintGCDetails \
-XX:+PrintGCTimeStamps \
-XX:+PrintGCDateStamps \
-XX:+PrintHeapAtGC \
-Xloggc:gc.log"

# 定义容器启动时执行的命令
# 使用之前设置的 JVM 参数启动 Java 应用，并指定 Spring 的配置文件
# ${SPRING_PROFILES_ACTIVE} 是一个环境变量，用于指定 Spring 的配置文件
ENTRYPOINT java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar