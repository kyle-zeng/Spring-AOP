package aop.aoptest.aop.biz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import aop.aoptest.aop.annotation.RelationEntity;
import aop.aoptest.aop.annotation.RelationParameter;
import aop.aoptest.aop.entity.CrmCustRelation;


@Component
public class RelationEntityGeneration {

	public CrmCustRelation processingManagerRelation(ProceedingJoinPoint jp, Method method,CrmCustRelation crmCustRelation) throws Exception {
        //获取注解方法的参数信息
		Object[] args = jp.getArgs();
        if(args.length > 0){
            for(int i = 0; i < args.length; i++){
                Object arg = args[i];
                //获取该参数对应对象的所有字段（包括父类）
                List<Field> fieldList = new ArrayList<>() ;
                Class<?> tempClass = arg.getClass();
                while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                      fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
                      tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
                }
                //处理字段值
                crmCustRelation = dealWithFields(crmCustRelation, fieldList, arg);
            }
        }
        return crmCustRelation;
    }

	/**
	 * 处理字段值
	 * @param crmCustRelation
	 * @param fields
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public CrmCustRelation dealWithFields(CrmCustRelation crmCustRelation, List<Field> fields, Object value) throws Exception{
		String keyMain = "";
		String keyRelt = "";
		String relationDesc = crmCustRelation.getTxtReltDesc();
        for (Field f : fields) {
            boolean hasRelationEntity=  f.isAnnotationPresent(RelationEntity.class);
          //如果请求参数还封装了一层entity，取出该entity的值，并取出所有的field字段，继续递归
            if(hasRelationEntity){    
            	f.setAccessible(true);
            	Object nextObject = f.get(value);
            	List<Field> fieldList = new ArrayList<>() ;
                Class<?> tempClass = f.getType();
                while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                      fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
                      tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
                }
            	crmCustRelation = dealWithFields(crmCustRelation, fieldList, nextObject);
            }else{
            	//取出字段上的注解，如果有自定义的RelationParameter注解，则处理该字段值，否则跳过
            	Annotation[] ans = f.getAnnotations();
            	for (Annotation an : ans) {
					if ((an instanceof RelationParameter)) {
						String fieldNameInforkey = ((RelationParameter)an).value();
						f.setAccessible(true);
						Object fieldValue = f.get(value);
						if(fieldValue==null){
							continue;
						}
						if("keyMain".equals(fieldNameInforkey)){
							keyMain = (String)fieldValue;
						}else if("keyRelt".equals(fieldNameInforkey)){
							keyRelt = (String)fieldValue;
						}
					}
            	}
            }
        }
        relationDesc =  MessageFormat.format(relationDesc, keyMain,keyRelt);
        crmCustRelation.setTxtReltDesc(relationDesc);
		return crmCustRelation;
	}
}


