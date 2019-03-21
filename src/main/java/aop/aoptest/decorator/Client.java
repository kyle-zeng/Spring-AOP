package aop.aoptest.decorator;

public class Client {
	public static void main(String args[]){
        Person employee = new Employee();
        ManagerA employee2 = new ManagerA(employee);//赋予程序猿项目经理A职责
        RequestDTO re = new RequestDTO();
        re.setName("kyle");
        re.setDoing(1);
        employee2.doCoding(re);
        employee2.doCoding2(re);
	}
}
