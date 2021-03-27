package se.codeboss.aop.issue

import kotlinx.coroutines.delay
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service("kotlinTestCoroutineService")
class Service @Autowired constructor(val repository: Repository){
    @SampleAnnotation
    suspend fun date(date: LocalDate): LocalDate {
        delay(10)
        return date
    }

    @SampleAnnotation
    suspend fun hello(name: String?, age: Int): String {
        return repository.hello(name, age)
    }

    @SampleAnnotation
    suspend fun exception(name: String): String {
        return repository.error(name)
    }
}
