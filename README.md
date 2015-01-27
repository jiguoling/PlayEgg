# PlayEgg
仿微信彩蛋,触发某些关键词时很多图片下落的动画
参考文章http://blog.csdn.net/victoryckl/article/details/8185173
基于http://download.csdn.net/detail/zgz345/8247563该代码,主要修改地方:
1.修复初始位置异常的问题
2.通过AnimatorEndListener监听动画结束事件
3.删除无用代码,增加部分注释

数量30个
初始位置随机整个屏幕
下降速度随机 最大1500px/s最小500px/s