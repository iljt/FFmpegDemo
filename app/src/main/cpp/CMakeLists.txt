#指定 Cmake 最低版本
cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.
project("ffmpegdemo")

#打印当前 cmakeLists 的路径
message("CMakeLists.txt ===== ${CMAKE_SOURCE_DIR}     ARCH_ABI===== ${CMAKE_ANDROID_ARCH_ABI}")
#找到包含所有的cpp文件
file(GLOB allCpp ${CMAKE_SOURCE_DIR}/*.cpp)
#生成总库的名称
add_library( # Sets the name of the library.
        ffmpegdemo
        SHARED
        ${allCpp})

#寻找系统log库
find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

#打印当前 CMAKE_CXX_FLAGS
message("CMAKE_CXX_FLAGS===== ${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/${CMAKE_ANDROID_API}")
# 引入FFmpeg的头文件
include_directories(${CMAKE_SOURCE_DIR}/include)

message("PROJECT_SOURCE_DIR=== ${PROJECT_SOURCE_DIR}")
message("CMAKE_SOURCE_DIR=== ${CMAKE_SOURCE_DIR}")

# 1、so直接放到app/libs目录下 build.gradle 不用设置sourceSets.main.jniLibs.srcDirs = ['libs']   ../上一级目录
#set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libavutil.so)
# 2、so直接放到main/xx目录下 ,xx目录只要不是jniLibs就可以了,build.gradle 还是不用设置sourceSets.main.jniLibs.srcDirs = ['libs']
# 3、so直接放到main/jniLibs目录下 build.gradle 必须设置sourceSets.main.jniLibs.srcDirs = ['libs'] 否则运行报错如下:
#[[2 files found with path 'lib/armeabi-v7a/libavutil.so'.
If you are using lib and CMake IMPORTED targets, see
https://developer.android.com/r/tools/jniLibs-vs-imported-targets]]
#原因：在gradle plugln 4.0及以上build.gradle自动会帮助我们寻找src/main/jniLibs文件夹(相当于build.gradle默认设置了sourceSets.main.jniLibs.srcDirs = ['src/main/jniLibs'])，找到文件夹下的相应abi下的.a或者.so去加载和优化,而在这里我们又重新指定src/main/jniLibs文件夹去加载第三方库，重复了
#解决办法：使用上面说的方式，详细看build.gradle里面的解释
#set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libavutil.so)
# 导入FFmpeg动态库 可读性更强，代码多
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libavcodec.so)
add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libavfilter.so)
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libavformat.so)
add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libavutil.so)
add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libswresample.so)
add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../aaa/${CMAKE_ANDROID_ARCH_ABI}/libswscale.so)

target_link_libraries(
        ffmpegdemo
        ${log-lib}
        # 具体的库文件，链接到总库
        avcodec avfilter avformat avutil swresample swscale
)

