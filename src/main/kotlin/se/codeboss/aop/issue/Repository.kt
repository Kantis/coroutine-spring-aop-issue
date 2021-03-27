package se.codeboss.aop.issue

import org.springframework.stereotype.Repository
import se.codeboss.aop.issue.SampleAnnotation

@Repository
class Repository {


    @SampleAnnotation
    suspend fun error(name: String): String {
        throw RuntimeException("This is wrong!")
    }

    @SampleAnnotation
    suspend fun hello(name: String?, age: Int?): String {
        return "hello:$name are you really $age?"
    }

    @SampleAnnotation
    suspend fun password(password: String): String {
        return "your password: $password"
    }

}
