package habitapp.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Аспект для логирования вызовов методов, аннотированных @Loggable.
 * Предоставляет возможность отслеживать время выполнения методов.
 */
@Aspect
public class LoggableAspect {

    /**
     * Определяет точку среза для методов, аннотированных @Loggable.
     */
    @Pointcut("within(@habitapp.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {}

    /**
     * Оборачивает вызовы методов, аннотированных @Loggable, для логирования.
     *
     * @param proceedingJoinPoint Объект, представляющий соединение с методом.
     * @return Результат выполнения метода.
     * @throws Throwable Если возникло исключение во время выполнения метода.
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Вызов метода " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed(); // Выполнение метода
        long timeForExecution = System.currentTimeMillis() - start;
        System.out.println("Метод " + proceedingJoinPoint.getSignature() + " выполнен за " + timeForExecution + " мс");
        return result;
    }
}