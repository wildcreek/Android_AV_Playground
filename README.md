## 整理自[卢俊](https://github.com/Jhuster)的[相关博客](http://blog.51cto.com/ticktick/1956269)
## 音视频开发的范畴
+ 采集：它解决的是，数据从哪里来的问题
+ 渲染（绘制/播放）：它解决的是，数据怎么展现的问题
+ 处理：它解决的是，数据怎么加工的问题
+ 传输：它解决的是，数据怎么共享的问题

### 视频
1. 采集
~~~
采集接口：Camera
采集参数：分辨率、帧率、预览方向、对焦、闪光灯 等
采集格式：图片：JPEG，视频数据：NV21，NV12，I420 等
~~~
2. 绘制接口：ImageView，SurfaceView，TextureView，OpenGL 等

3. 图像处理（裁剪、缩放、旋转、叠加）：OpenGL，OpenCV，libyuv，ffmpeg 等
4. 视频编解码：x264，OpenH264，ffmpeg 等

### 音频
* 延时标准: ITU-TG.114规定，对于高质量语音可接受的时延是300ms。一般来说，如果时延在300～400ms，通话的交互性比较差，但还可以接受。时延大于400ms时，则交互通信非常困难。
* 常见的音频编码方式：PCM , ADPCM 等
* 常见的音频压缩格式: MP3，AAC，OGG，WMA，Opus，FLAC，APE，m4a，AMR 等
1. 采集
~~~
采集接口：AudioRecord , MediaRecoder
采集参数：采样率，通道号，量化精度(位宽) 等
采集格式：PCM 

~~~

2. 播放接口：SoundPool，MediaPlayer，AudioTrack  等

3. 音频处理（重采样、降噪、回声消除、混音）：speexdsp，ffmpeg ,webrtc audio module（NS、VAD、AECM、AGC）等

4. 音频编解码：MediaCodec,libfaac，opus，speex，ffmpeg 等

5. NDK API：OpenSL ES

### 传输
1. 音视频在传输前，怎么打包的，如：FLV，ts，mpeg4 等

2. 直播推流，有哪些常见的协议，如：RTMP，RSTP 等

3. 直播拉流，有哪些常见的协议，如：RTMP，HLS，HDL，RTSP 等

4. 基于 UDP 的协议有哪些？如：RTP/RTCP，QUIC 等

## 音视频开发学习任务列表

- [X] 1.在 Android 平台绘制一张图片，使用至少 3 种不同的 API，ImageView，SurfaceView，自定义 View
        a. ImageView 直接展示
        b. SurfaceView 获取Holder，在callback surfaceCreated中调用lockCanvas获取Canavas对象绘制，绘制完成调用unlockCanvasAndPost
        c. 自定义View继承View，重写onDraw方法 调用canvas.drawBitmap绘制。

- [ ] 2. 在 Android 平台使用 AudioRecord 和 AudioTrack API 完成音频 PCM 数据的采集和播放，并实现读写音频 wav 文件
        a. AudioRecord 通过采样率、通道、位宽和上述参数计算的合适buffer构造，启动后，调用write将硬件采集数据读入该buffer，通过流将该buffer循环写入文件。
        b. AudioTrack 通过采样率、通道、位宽、buffer、播放模式、采集类型等构造，播放后，通过文件流循环读取buffer，并写入Audio Sink。
        c. WAV 是RIFF文件的子集，WAV文件包含一个wave chunk 和fmt 、data 子chunk，上述 pcm无损压缩编码数据 可作为data数据。
        AudioRecord 写入 wav 文件可以先构造 wav文件，写入文件头，再获取buffer写入 data，结束采集时修改data size ;AudioTrack 读取wav文件可以直接读取data数据到buffer，再调用write。

- [ ] 3. 在 Android 平台使用 Camera API 进行视频的采集，分别使用 SurfaceView、TextureView 来预览 Camera 数据，取到 NV21 的数据回调

- [ ] 4. 学习 Android 平台的 MediaExtractor 和 MediaMuxer API，知道如何解析和封装 mp4 文件

- [ ] 5. 学习 Android 平台 OpenGL ES API，了解 OpenGL 开发的基本流程，使用 OpenGL 绘制一个三角形

- [ ] 6. 学习 Android 平台 OpenGL ES API，学习纹理绘制，能够使用 OpenGL 显示一张图片

- [ ] 7. 学习 MediaCodec API，完成音频 AAC 硬编、硬解

- [ ] 8. 学习 MediaCodec API，完成视频 H.264 的硬编、硬解

- [ ] 9. 串联整个音视频录制流程，完成音视频的采集、编码、封包成 mp4 输出

- [ ] 10. 串联整个音视频播放流程，完成 mp4 的解析、音视频的解码、播放和渲染

- [ ] 11. 进一步学习 OpenGL，了解如何实现视频的剪裁、旋转、水印、滤镜，并学习 OpenGL 高级特性，如：VBO，VAO，FBO 等等

- [ ] 12. 学习 Android 图形图像架构，能够使用 GLSurfaceviw 绘制 Camera 预览画面

- [ ] 13. 深入研究音视频相关的网络协议，如 rtmp，hls，以及封包格式，如：flv，mp4

- [ ] 14. 深入学习一些音视频领域的开源项目，如 webrtc，ffmpeg，ijkplayer，librtmp 等等

- [ ] 15. 将 ffmpeg 库移植到 Android 平台，结合上面积累的经验，编写一款简易的音视频播放器

- [ ] 16. 将 x264 库移植到 Android 平台，结合上面积累的经验，完成视频数据 H264 软编功能

- [ ] 17. 将 librtmp 库移植到 Android 平台，结合上面积累的经验，完成 Android RTMP 推流功能

- [ ] 18. 上面积累的经验，做一款短视频 APP，完成如：断点拍摄、添加水印、本地转码、视频剪辑、视频拼接、MV 特效等功能
    
## 音视频开发参考资料 
    1. 《雷霄骅的专栏》：http://blog.csdn.net/leixiaohua1020
    2. 《Android音频开发》：http://ticktick.blog.51cto.com/823160/d-15
    3. 《FFMPEG Tips》：http://ticktick.blog.51cto.com/823160/d-17
    4. 《Learn OpenGL 中文》：https://learnopengl-cn.readthedocs.io/zh/latest/
    5. 《Android Graphic 架构》：https://source.android.com/devices/graphics/
    6. 技术博客：https://github.com/hejunlin2013/AVBlog
    7. 《Android 音视频开发入门指南》 http://blog.51cto.com/ticktick/1956269
    8. 《从开发小白到音视频专家》 https://mp.weixin.qq.com/s/pqa_llT4DlSBZ_Qvhpypjw



