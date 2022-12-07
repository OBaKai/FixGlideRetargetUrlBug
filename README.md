
### Glide遇到重定向url产生的bug（bug地址 https://github.com/bumptech/glide/issues/4652）

#### 问题描述：
正常情况：www.prohub.com/a.png -> a图
glide缓存：www.prohub.com/a.png <==> a图

重定向情况：www.prohub.com/a.png -> www.fbiwarning.com/403.png -> 403图
glide缓存：www.prohub.com/a.png <==> 403图
假设www.prohub.com/a.png恢复了正常不会重定向了，由于glide缓存，glide直接加载了403图，导致bug产生


#### 源码分析：网络加载图片 -> disk cache过程
1、SourceGenerator#startNext（轮询执行所有LoadData）
	-> SourceGenerator#startNextLoad（执行单个LoadData）
	-> LoadData#fetcher#loadData（执行具体任务，以网络加载图片为例）
	   -> DataCallback#onLoadFailed（下载失败）
		 -> DataCallback#onDataReady（下载完成）
		   -> 缓存数据到 dataToCache 属性
2、当下一次执行 SourceGenerator#startNext 的时候，如果 dataToCache 属性不为空，执行 SourceGenerator#cacheData
 2.1、生成缓存目标的key（它也是缓存图片的文件名 文件名是：key + .o）
   创建DataCacheKey(loadData.sourceKey, helper.getSignature())
    loadData.sourceKey：执行下载任务时候的GlideUrl
    helper.getSignature：开发者配置的签名（这里忽略它）
 2.2、key是根据url来生成的
    url -> SHA-256 -> sha256BytesToHex
 2.3、执行齿盘缓存（DiskLruCache）
    helper.getDiskCache().put(originalKey, writer); -> DiskLruCacheWrapper#put ...
 2.4、缓存路径在 /data/data/pkg/cache/image_manager_disk_cache


#### 解决方法：将重定向后的url更新给Glide，让Glide用正确的url来cache即可
1、直接去Glide源码改，改它喵的（比较粗暴，入侵性强）
2、通过Glide4提供的自定义组件将网络组件替换成Okhttp，在网络请求完成后判断是否发送重定向了。
   如果重定向了就将新的url更新给Glide，让Glide用重定向后的url进行缓存。
   
#### FixGlideRetargetUrlBug代码就是根据 方法2 实现的
