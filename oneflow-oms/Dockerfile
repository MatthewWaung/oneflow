FROM anapsix/alpine-java:8_server-jre_unlimited

RUN mkdir -p /ecoflow/oms
WORKDIR /ecoflow/oms

#设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

# 添加字体
ARG MS_MINCHO="MS Mincho.ttf"
ARG Roman_Font="Times New Roman.ttf"

RUN mkdir -p /usr/share/fonts/my_fonts
ADD https://minio.ecoflow.com:9000/public/simsun.ttc /usr/share/fonts/my_fonts/simsun.ttc
ADD https://minio.ecoflow.com:9000/public/arial.ttf /usr/share/fonts/my_fonts/arial.ttf
ADD https://minio.ecoflow.com:9000/public/malgun.ttf /usr/share/fonts/my_fonts/malgun.ttf
ADD https://minio.ecoflow.com:9000/public/MS%20Mincho.ttf /usr/share/fonts/my_fonts/${MS_MINCHO}
ADD https://minio.ecoflow.com:9000/public/SourceHanSerifSC-Regular.otf /usr/share/fonts/my_fonts/SourceHanSerifSC-Regular.otf
ADD https://minio.ecoflow.com:9000/public/meiryo.ttc /usr/share/fonts/my_fonts/meiryo.ttc
ADD https://minio.ecoflow.com:9000/public/Times%20New%20Roman.ttf /usr/share/fonts/my_fonts/${Roman_Font}


EXPOSE 8888

ADD oms-service/target/oms-service.jar ./app.jar

# 设置JVM参数
ENV JAVA_OPTS="\
-Xms2048m \
-Xmx4096m \
-Xmn1024m \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=256m \
-XX:+UseConcMarkSweepGC \
-XX:+UseParNewGC \
-XX:+PrintGCDetails \
-XX:+PrintGCTimeStamps \
-XX:+PrintGCDateStamps \
-XX:+PrintHeapAtGC\
-Xloggc:gc.log"

ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar