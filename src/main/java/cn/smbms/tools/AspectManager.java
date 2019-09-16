package cn.smbms.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * 实现日志增强
 */
@Aspect
public class AspectManager {
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    //定义统一切入点
    @Pointcut("execution(* cn.smbms.service..*.*(..))")
    public void pointcut(){}

    //定义前置增强
    @Before("pointcut()")
    public void before(JoinPoint jp){
        LOGGER.info("调用:"+jp.getTarget()+"的"+jp.getSignature().getName()
                +"方法;方法入参:"+ Arrays.toString(jp.getArgs()));
    }

    //定义后置增强
    @AfterReturning(value = "pointcut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result){
        LOGGER.info("调用:"+jp.getTarget()+"的"+
                jp.getSignature().getName()+"方法;方法返回值:"+result);
    }

    //定义最终增强
    @After("pointcut()")
    public void afterLogger(JoinPoint jp){
        LOGGER.info(jp.getSignature().getName()+"方法执行结束.");
    }

    //定义异常增强
    @AfterThrowing(value = "pointcut()",throwing = "e")
    public void afterThrowing(JoinPoint jp,RuntimeException e){
        LOGGER.error(jp.getSignature().getName()+"方法发生异常:"+e);
    }
}
