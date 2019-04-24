### 网络监测工具类

一般做法是通过广播接收系统网络变化的通知，然后在```UI```页面中做相应的提示，但是这里使用```NetworkRequest.Builder```来实现，向下兼容到 Android 5.0，具体使用如下：
* 注册全局网络监测（建议在```APPlication```中实例化）

```
// 网络监听
        NetUtils.netWorkListener(this)
```
* 进入```UI```（可见状态）前检查网络
```
override fun onResume() {
        super.onResume()
        checkNetWork(NetUtils.isConnected)
    }
 
// 检查网络
private fun checkNetWork(isConnected: Boolean) {
        if (isConnected) {
        // 当网络连接时显示UI等操作
            if (netErrorView.parent != null) {
                mWindowManager.removeViewImmediate(netErrorView)
                netErrorView.setOnClickListener(null)
            }
        } else {
         // 当网络断开时显示UI等操作
            if (netErrorView.parent == null) {
                mWindowManager.addView(netErrorView, mLayoutParams)
				initErrorViewEvent()
            }
        }
    }   

```

* 实时检测
```

@Subscribe(threadMode = ThreadMode.MAIN)
fun onNetworkChangeEvent(event: NetworkChangeEvent) {
    this.checkNetWork(event.isConnected)
}
    

```