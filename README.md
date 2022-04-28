[toc]

Author： lishui.lin


** 主工程及其关联工程的设计与研发，初衷是为了研究与探索当下前沿的基础架构方案 **


### 推荐架构

谷歌推荐的典型应用架构：
- 界面层：在屏幕上显示应用数据。由 View 及 ViewModel 构成。
- 网域层（可选）：简化和重复使用界面层与数据层之间的交互。负责封装复杂的业务逻辑，或者由多个 ViewModel 重复使用的简单业务逻辑。
- 数据层：包含应用的业务逻辑并公开应用数据。由与业务相关联的 Repository 构成。

#### 最佳做法

关于Android项目的编程，谷歌提出如下建议：
- 不要将数据存储在应用组件中。
- 减少对 Android 类的依赖。
- 在应用的各个模块之间设定明确定义的职责界限。
- 尽量少公开每个模块中的代码。
- 专注于应用的独特核心，以使其从其他应用中脱颖而出。
- 考虑如何使应用的每个部分可独立测试。
- 保留尽可能多的相关数据和最新数据。


### 模块设计

模块分层如下：
- 界面入口：app模块，触发应用的初始化
- 模块层：以`module`为前缀，其中`moduleMain`暂定为主界面模块
- 服务层：以`service`为前缀，其中`serviceCore`是各个service模块的基础模块
- 基础库：libBase

另外还有独立的组件：
- 组件层：以lib为前缀
- 公共UI套件：UiKit

> 模块以小驼峰形式命名，module之间严禁互相引用！

### 工程情况

待启动计划：
1. 将视频播放模块独立成工程库，支持远程打包aar。主工程依赖外部视频播放模块aar
2. 封装通用的图片加载框架，可自由切换到Coil或者Glide等三方开源实现。并将其独立成工程库，支持远程打包，主工程外部依赖。
3. 完善 flutter_demo 库，建立状态管理机制。支持远程打包aar，后续将其迁移至GitHub里。
4. 引入`Shadow`框架，实现插件化，可有效减少安装包体积和按需加载。
5. 研究当下H5与原生的通信方案，与现有的`jsbridge`横向对比，引入合适的通信方案，并封装成库，支持远程打包。
6. JNI与NDK实践，基于视频播放模块进行改造，将 `ExoPlayer`更换为`FFmpeg`的播放实现。

持续优化项：
1. 基础库下沉，代码通用化和基础化，支持远程打包，用于外部工程依赖。
2. 在多模块多工程情况下，设计出一套优雅的依赖管理方案。
3. 性能监控方案（启动、卡顿、内存、网络等）、调试工具和日志管理。

里程牌子项：
1. 模块化和组件化设计，组件通信基于SPI路由方案：[Router](https://github.com/linlishui/Router)，各个模块使用start-up库进行初始化。
2. flutter_demo 单独建库，通过导出aar的形式给到主工程。
3. gradle插件，主工程插件统一放入plugins文件目录内。已实现的插件工程`pluginGradle`，内含ASM修改字节码完成方法耗时统计。
4. 提供基本的组件，有：视频播放、网络请求、浏览器、数据持久化（内含room和datastore）等。


### Hilt[已移除]

> 暂未找到多模块跨库的依赖管理方案，故废弃。

#### 1. 在 Hilt 不支持的类中注入依赖项

可以使用 `@EntryPoint` 注释创建入口点，然后添加 ``@InstallIn` 以指定要在其中安装入口点的组件，如下例子：
```kotlin
// 1. 声明依赖的类
@Singleton
class Dependency @Inject constructor() {
    fun doDependency() {
        LogUtils.d("Dependency doDependency")
    }
}

// 2. 定义注入依赖的模块
@EntryPoint
@InstallIn(ApplicationComponent::class)
interface CustomModule {
    fun getDependency(): Dependency
}

// 3. 使用依赖
val customModule = EntryPointAccessors.fromApplication(application, CustomModule::class.java)
customModule.getDependency().doDependency()
```