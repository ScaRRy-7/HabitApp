package habitapp.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggableAspect {
    @Pointcut("within(@habitapp.annotations.Loggable *) && execution(* * (..)")
    public void annotatedByLoggable() {}

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long timeForExecution = System.currentTimeMillis() - start;
        System.out.println("Method " + proceedingJoinPoint.getSignature() + " executed in " + timeForExecution + " ms");
        return result;
    }
}
