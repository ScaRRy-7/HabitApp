package habitapp.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания того, что метод или класс должны быть логированы.
 * <p>
 * Эта аннотация может применяться к методам и классам. При использовании
 * аннотации ожидается, что система логирования будет захватывать и
 * записывать информацию о вызовах методов или событиях, связанных с
 * классами, помеченными этой аннотацией.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Loggable {
}
