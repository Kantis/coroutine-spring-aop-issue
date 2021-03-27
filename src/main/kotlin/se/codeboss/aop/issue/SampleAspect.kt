package se.codeboss.aop.issue

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

@Aspect
class SampleAspect {

    init {
        println("Initialized ${this::class.simpleName}")
    }

    class Callstack : CoroutineContext.Element {
        companion object Key : CoroutineContext.Key<Callstack>

        override val key: CoroutineContext.Key<*>
            get() = Key

        val data = LinkedList<String>()
    }


    @Around("@annotation(se.codeboss.aop.issue.SampleAnnotation)")
    fun intercept(joinPoint: ProceedingJoinPoint): Any {
        val codeSignature = joinPoint.signature as MethodSignature

        val signature = if (joinPoint.args.size == 1) codeSignature.toShortString().replace("..", "") else codeSignature.toShortString()

        val start = Instant.now()

        @Suppress("UNCHECKED_CAST")
        val interceptedContinuation = joinPoint.args.last() as Continuation<Any>

        val hasStack = interceptedContinuation.context[Callstack] != null
        val context =
            if (!hasStack) interceptedContinuation.context + Callstack()
            else interceptedContinuation.context
        val stack = context[Callstack]!!

        stack.data.push(signature)

        val newContinuation = Continuation<Any>(context) { result ->
            println("${stack.data.pop()} finished after ${Duration.between(start, Instant.now()).toMillis()}ms")


            interceptedContinuation.resumeWith(result)
        }

        val args = (joinPoint.args.dropLast(1) + newContinuation).toTypedArray()
        return try {
            val result = joinPoint.proceed(args)

            if (result.toString() == "COROUTINE_SUSPENDED") {
                result
            } else {
                // Resuming continuation
                newContinuation.resume(result as Any)
            }
        } catch (e: Exception) {
            newContinuation.resumeWith(Result.failure(e))
            throw e
        }
    }

}
