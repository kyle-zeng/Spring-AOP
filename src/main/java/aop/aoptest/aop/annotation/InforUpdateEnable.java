package aop.aoptest.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InforUpdateEnable {
  /**
   * 如果为true，则类下面的InforUpdate启作用，否则忽略
   * @return
   */
  boolean inforUpdateEnable() default true;
}
