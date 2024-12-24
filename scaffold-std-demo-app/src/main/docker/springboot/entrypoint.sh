#!/bin/bash
set -e

APP_HOME=${APP_HOME:-${HOME}}
echo "APP_HOME is $APP_HOME"
DATA_HOME=${DATA_HOME:-${HOME}/data}
echo "DATA_HOME is $DATA_HOME"

# 获取 Java 主版本
JAVA_MAJOR_VERSION=$(java -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')

# 默认 Java 参数设置
JAVA_OPTS="${JAVA_OPTS} -server"
JAVA_OPTS="${JAVA_OPTS} -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions"
JAVA_OPTS="${JAVA_OPTS} -XX:+AlwaysPreTouch -XX:+PrintFlagsFinal -XX:-DisplayVMOutput -XX:-OmitStackTraceInFastThrow"
JAVA_OPTS="${JAVA_OPTS} -Xms${JVM_XMS:-1G} -Xmx${JVM_XMX:-1G} -Xss${JVM_XSS:-256K}"
JAVA_OPTS="${JAVA_OPTS} -XX:MetaspaceSize=${METASPACE_SIZE:-128M} -XX:MaxMetaspaceSize=${MAX_METASPACE_SIZE:-256M}"
JAVA_OPTS="${JAVA_OPTS} -XX:MaxGCPauseMillis=${MAX_GC_PAUSE_MILLIS:-200}"

# 垃圾回收模式设置
if [[ "${GC_MODE}" == "ShenandoahGC" ]]; then
  echo "GC mode is ShenandoahGC"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseShenandoahGC"
elif [[ "${GC_MODE}" == "ZGC" ]]; then
  echo "GC mode is ZGC"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseZGC"
elif [[ "${GC_MODE}" == "G1" ]]; then
  echo "GC mode is G1"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseG1GC"
  JAVA_OPTS="${JAVA_OPTS} -XX:InitiatingHeapOccupancyPercent=${INITIATING_HEAP_OCCUPANCY_PERCENT:-45}"
  JAVA_OPTS="${JAVA_OPTS} -XX:G1ReservePercent=${G1_RESERVE_PERCENT:-10} -XX:G1HeapWastePercent=${G1_HEAP_WASTE_PERCENT:-5} "
  JAVA_OPTS="${JAVA_OPTS} -XX:G1NewSizePercent=${G1_NEW_SIZE_PERCENT:-50} -XX:G1MaxNewSizePercent=${G1_MAX_NEW_SIZE_PERCENT:-60}"
  JAVA_OPTS="${JAVA_OPTS} -XX:G1MixedGCCountTarget=${G1_MIXED_GCCOUNT_TARGET:-8}"
  JAVA_OPTS="${JAVA_OPTS} -XX:G1MixedGCLiveThresholdPercent=${G1_MIXED_GCLIVE_THRESHOLD_PERCENT:-65}"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled"
elif [[ "${GC_MODE}" == "CMS" ]]; then
  echo "GC mode is CMS"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -Xmn${XMN:-512m}"
  JAVA_OPTS="${JAVA_OPTS} -XX:ParallelGCThreads=${PARALLEL_GC_THREADS:-2} -XX:ConcGCThreads=${CONC_GC_THREADS:-1}"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=${CMS_INITIATING_HEAP_OCCUPANCY_PERCENT:-92}"
  JAVA_OPTS="${JAVA_OPTS} -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark"
  if [[ "$JAVA_MAJOR_VERSION" -le "8" ]]; then
    JAVA_OPTS="${JAVA_OPTS} -XX:+CMSIncrementalMode -XX:CMSFullGCsBeforeCompaction=${CMS_FULL_GCS_BEFORE_COMPACTION:-5}"
    JAVA_OPTS="${JAVA_OPTS} -XX:+ExplicitGCInvokesConcurrent -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses"
  fi
fi

# GC 日志设置
if [[ "${USE_GC_LOG}" == "Y" ]]; then
  JAVA_OPTS="${JAVA_OPTS} -XX:+PrintVMOptions"
  gc_log_path=${GC_LOG_PATH:-${DATA_HOME}/logs}
  echo "GC log path is '${gc_log_path}/jvm_gc.log'."
  if [[ "$JAVA_MAJOR_VERSION" -gt "8" ]]; then
    JAVA_OPTS="${JAVA_OPTS} -Xlog:gc:file=${gc_log_path}/jvm_gc-%p-%t.log:tags,uptime,time,level:filecount=${GC_LOG_FILE_COUNT:-10},filesize=${GC_LOG_FILE_SIZE:-100M}"
  else
    JAVA_OPTS="${JAVA_OPTS} -Xloggc:${gc_log_path}/jvm_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps"
    JAVA_OPTS="${JAVA_OPTS} -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=${GC_LOG_FILE_COUNT:-10} -XX:GCLogFileSize=${GC_LOG_FILE_SIZE:-100M}"
    JAVA_OPTS="${JAVA_OPTS} -XX:+PrintGCCause -XX:+PrintGCApplicationStoppedTime"
    JAVA_OPTS="${JAVA_OPTS} -XX:+PrintTLAB -XX:+PrintReferenceGC -XX:+PrintHeapAtGC"
    JAVA_OPTS="${JAVA_OPTS} -XX:+FlightRecorder -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1"
    JAVA_OPTS="${JAVA_OPTS} -XX:+DebugNonSafepoints -XX:+SafepointTimeout -XX:SafepointTimeoutDelay=500"
  fi
fi

# 创建日志目录
if [ ! -d "${DATA_HOME}/logs" ]; then
  mkdir "${DATA_HOME}"/logs
fi

# 堆转储设置
if [[ "${USE_HEAP_DUMP}" == "Y" ]]; then
  heap_dump_path=${HEAP_DUMP_PATH:-${DATA_HOME}/logs}
  echo "Heap dump path is '${heap_dump_path}/jvm_heap_dump.hprof'."
  JAVA_OPTS="${JAVA_OPTS} -XX:HeapDumpPath=${heap_dump_path}/jvm_heap_dump.hprof -XX:+HeapDumpOnOutOfMemoryError"
fi

# 大页设置
if [[ "${USE_LARGE_PAGES}" == "Y" ]]; then
  echo "Use large pages."
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseLargePages"
fi

# JDWP调试设置
if [[ "${JDWP_DEBUG:-N}" == "Y" ]]; then
  echo "Attach to remote JVM using port ${JDWP_PORT:-5005}."
  JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xrunjdwp:transport=dt_socket,address=${JDWP_PORT:-5005},server=y,suspend=n"
fi

# 处理外部配置：如果 /config 目录存在且非空，则将其作为额外的配置位置
EXTERNAL_CONFIG_HOME=${EXTERNAL_CONFIG_HOME:-${APP_HOME}/config}
if [[ -d "$EXTERNAL_CONFIG_HOME" && "$(ls -A "$EXTERNAL_CONFIG_HOME")" ]]; then
  echo "Using external configuration from $EXTERNAL_CONFIG_HOME"
  JAVA_OPTS="${JAVA_OPTS} --spring.config.additional-location=file:${EXTERNAL_CONFIG_HOME}/"
fi

# 设置 Spring Boot 端口
JAVA_OPTS="${JAVA_OPTS} -Dserver.port=${SERVER_PORT} -Dmanagement.server.port=${MANAGEMENT_SERVER_PORT}"

# 执行 Java 应用
# 方式一: spring-boot-maven 分层构建
exec java ${JAVA_OPTS} -noverify -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher "$@"
# 方式二: jib-maven-plugin
#exec java ${JAVA_OPTS} -cp $(cat "$APP_HOME"/jib-classpath-file) $(cat "$APP_HOME"/jib-main-class-file)
