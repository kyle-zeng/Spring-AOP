package aop.aoptest.aop.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aop.aoptest.aop.annotation.EventType;
import aop.aoptest.aop.annotation.InforUpdate;
import aop.aoptest.aop.annotation.InforUpdateEnable;
import aop.aoptest.aop.annotation.RelationType;
import aop.aoptest.aop.biz.InforUpdateAspectBiz;
import aop.aoptest.aop.biz.RelationEntityGeneration;
import aop.aoptest.aop.entity.CrmCustRelation;


@Component
@Aspect
public class InforUpdateAspect {

	@Autowired
	private InforUpdateAspectBiz inforUpdateAspectBiz;
	
	@Autowired
	private RelationEntityGeneration relationEntityGeneration;

	/**
	 * aop入口函数
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	public Object aroundManagerInforPoint(ProceedingJoinPoint jp) throws Throwable {
		Class<?> target = jp.getTarget().getClass();
		// 获取InforUpdateEnable，是否需要做关联关系的更新
		InforUpdateEnable inforUpdateEnable = (InforUpdateEnable) target.getAnnotation(InforUpdateEnable.class);
		if(inforUpdateEnable == null || !inforUpdateEnable.inforUpdateEnable()){
			return jp.proceed();
		}
		
		// 获取类上的InforUpdate做为默认值
		InforUpdate inforUpdateClass = (InforUpdate) target.getAnnotation(InforUpdate.class);
		//获取被注解的方法
		Method method = getInvokedMethod(jp);
		if(method == null){
			return jp.proceed();
		}
		
		// 获取方法上的InforUpdate
		InforUpdate inforUpdateMethod = method.getAnnotation(InforUpdate.class);
		if(inforUpdateMethod == null){
			return jp.proceed();
		}
		
		String optEvent = inforUpdateMethod.event().getEvent();
		//关联关系类型
		String optRelation = inforUpdateMethod.relation().getRelation();
		
		if(inforUpdateClass != null){
			// 如果方法上的值为默认值，则使用全局的值进行替换
			optEvent = optEvent.equals(EventType.ADD.getEvent()) ? inforUpdateClass.event().getEvent() : optEvent;
			optRelation = optRelation.equals(RelationType.LEGALPERSON_P) ? inforUpdateClass.relation().getRelation() : optRelation;
		}
		//处理注解字段的值
		CrmCustRelation crmCustRelation = new CrmCustRelation();
		crmCustRelation.setTxtReltDesc(optRelation);
		crmCustRelation = relationEntityGeneration.processingManagerRelation(jp, method,crmCustRelation);
		//执行业务方法
		Object returnObj = jp.proceed();
		//执行一下的操作：更新关联关系
		if(optEvent.equals(EventType.ADD.getEvent())){
			inforUpdateAspectBiz.dealWithRelation(crmCustRelation);
		}else {
			
		}
		return returnObj;
	}
	
	/**
	* 获取请求方法
	*
	* @param jp
	* @return
	*/
	public Method getInvokedMethod(JoinPoint jp) {
		// 调用方法的参数
		List classList = new ArrayList();
		for (Object obj : jp.getArgs()) {
			classList.add(obj.getClass());
		}
		Class[] argsCls = (Class[]) classList.toArray(new Class[0]);
		
		// 被调用方法名称
		String methodName = jp.getSignature().getName();
		Method method = null;
		try {
			method = jp.getTarget().getClass().getMethod(methodName, argsCls);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method;
	}
}


