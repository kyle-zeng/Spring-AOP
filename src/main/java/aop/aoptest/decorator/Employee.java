package aop.aoptest.decorator;

public  class Employee implements Person {

	@Override
	public void doCoding(String name, String doing) {
		System.out.println("程序员"+name+"加班写程序啊，写程序，终于写完了。。。"+doing);
	}

}
