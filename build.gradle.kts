plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "icu.hezibalck"
version = "0.0.1-dev"

repositories {
    mavenCentral()
    maven ("https://mirrors.huaweicloud.com/repository/maven/")
//    maven ("https://s01.oss.sonatype.org/content/repositories/snapshots/" )
//    maven ("https://maven.aliyun.com/repository/public")
//    maven ("https://maven.aliyun.com/repository/central")
}

dependencies {
    //依赖
    compileOnly("cn.chahuyun:HuYanAuthorize:1.1.6")
//    compileOnly("xyz.cssxsh.mirai:mirai-hibernate-plugin:2.8.2")
//    compileOnly("xyz.cssxsh.mirai:mirai-economy-core:1.06")
    compileOnly("cn.chahuyun:HuYanEconomy:1.4.10")
    //使用库
    implementation("cn.chahuyun:hibernate-plus:1.0.15")
}

// hibernate 6 和 HikariCP 5 需要 jdk11
mirai {
    jvmTarget = JavaVersion.VERSION_11
}


