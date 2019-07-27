# MyRxJavaDemo
My RxJava demo with myOperator and Aspectj

此Demo为我的博客《一起读RxJava源码》系列的事例，具体的相关细节欢迎看我的博客。

由于RxJava在“上行”事件触发流中执行的操作符主要是subscribeOn()，绝大部分操作符是在事件下发流中执行的。（一般事件下发前也没什么意义）

本Demo主要是为了在“上行”事件触发流中执行操作提供几种思路，顺便验证了subscribeOn()是在“上行”流中切换的线程。


## 主要的三种思路

可以看做是向别人写的类中添加/修改方法的问题。一般思路如下：

### 1.继承
最简单的面向对象的思路，但是问题也有很多，比如目标类/方法final的问题，以及向下转型的问题等等。本demo中有此方法实现的相关代码。

### 2.代理
静态代理和动态代理都可以实现向被代理类的方法添加代码的功能，但是问题是两种代理都要基于接口，也就是代理类和被代理类要实现相同接口。

RxJava中目标类Observable没实现任何接口，当然也可以在自己定义的实现接口的类中使用反射，反射Observable中的方法，但是此思路过于复杂，暂不在考虑范围。

### 3.AspectJ实现代码注入

可以用AOP思想，无需接口的情况下，利用AspectJ在“上行”事件触发流中注入相关代码。
本例使用的是沪江的[Aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)，有《Android群英传》一书作者徐宜生大佬的博客[《看AspectJ在Android中的强势插入》](https://www.jianshu.com/p/5c9f1e8894ec)以供参考。
