# Android_Frame_AOP
* 说明
* 明确需求
* 运行时权限检查
### 说明
一个基于java语言的模块化Android项目。 
### 明确需求
> 注:该标题下的内容，随着内容的不断扩充，后期需要单独成文件（目前由于内容较少，可以放置再此处）

面向切面编程的小案例，目前只实现了**检测token是否过期**，其新需求如下：
  1. 日志上传功能;
  2. 权限检查；
  3. 自定义UI（包括标题（toolbar）和布局）
  4. 弹出弹窗（popwidow和dialog）
  
> 目前处于起初开发状态，即第一阶段，着重考虑权限的设计及实现；但是依然有必要将1，3，4的标题列出；因为我们有可能有**好的需求或实现**涌上心头，可以在下面记录。当然下面的功能**有何风险**也可写上。
#### 日志上传功能
  
  1. 我们应该明确是哪里的日志，是Logcat中的输出，还是JVM（DVM）抛出的Throwable，还是埋点（UMENG之类的商品功能）
  
#### 权限检查功能
> 权限的逻辑和其他功能的处理还是有差别，相对于其他来说，它是"懒加载"。
  1. 确保实现DI(以来注入)，注入后的业务逻辑可以第三方申请暂态，后期替换；（运行时权限这一块，不缺乏"成名框架"，可选取一个）
  2. 替换第三方的运行时权限。

#### 自定义UI

#### 自定义弹窗

### 运行时权限检查