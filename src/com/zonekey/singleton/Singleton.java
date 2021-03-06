package com.zonekey.singleton;

public class Singleton {

	/* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
	private static Singleton instance = null;

	/* 私有构造方法，防止被实例化 */
	private Singleton() {
	}

	/* 静态工程方法，创建实例 */
	public static Singleton getInstance() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}

	/* 线程安全模式 */
	public static synchronized Singleton getInstance2() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}

	/* 只有创建的时候需要加锁，之后就不需要了 */
	/* a>A、B线程同时进入了第一个if判断 */
	/* b>A首先进入synchronized块，由于instance为null，所以它执行instance = new Singleton(); */
	/*
	 * c>由于JVM内部的优化机制，JVM先画出了一些分配给Singleton实例的空白内存，并赋值给instance成员（注意此时JVM没有开始初始化这个实例
	 * ），然后A离开了synchronized块。
	 */
	/*
	 * d>B进入synchronized块，由于instance此时不是null，因此它马上离开了synchronized块并将结果返回给调用该方法的程序
	 * 。
	 */
	public static Singleton getInstance3() {
		if (instance == null) {
			synchronized (Singleton.class) {
				if (instance == null) {
					instance = new Singleton();
				}
			}
		}
		return instance;
	}

	/* 此处使用一个内部类来维护单例 */
	private static class SingletonFactory {
		private static Singleton instance = new Singleton();
	}

	/* 获取实例 */
	public static Singleton getInstance4() {
		return SingletonFactory.instance;
	}

	/* 如果该对象被用于序列化，可以保证对象在序列化前后保持一致 */
	public Object readResolve() {
		return instance;
	}
}