#include <jni.h>
#include <string>

// 会报错，必须混合编译
/*#include <libavutil/avutil.h>*/


extern "C" {
#include <libavutil/avutil.h>
}


/**
 * 拿到 ffmpeg 当前版本
 * @return
 */
const char *getFFmpegVer() {
    return av_version_info();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_ffmpegdemo_MainActivity_getFFmpegVersion(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(getFFmpegVer());
}