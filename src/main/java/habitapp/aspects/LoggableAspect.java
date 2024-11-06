//package habitapp.aspects;
//
//import habitapp.annotations.Loggable;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * Аспект для логирования вызовов методов, аннотированных @Loggable.
// * Предоставляет возможность отслеживать время выполнения методов.
// */
//@Aspect
//@Component
//public class LoggableAspect {
//
//    private final Logger logger = LoggerFactory.getLogger(LoggableAspect.class);
//
//    /**
//     * Определяет точку среза для методов, аннотированных @Loggable.
//     */
//    @Around("@annotation(habitapp.annotations.Loggable)")
//    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
//        System.out.println("Executing method: " + signature.getMethod().getName());
//
//        long start = System.currentTimeMillis();
//        Object result = proceedingJoinPoint.proceed(); // Выполнение метода
//        long timeForExecution = System.currentTimeMillis() - start;
//
//        logger.debug("Method {} executed in {} ms", signature.getMethod().getName(), timeForExecution);
//        return result;
//    }
//}