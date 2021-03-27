package se.codeboss.aop.issue

import kotlinx.coroutines.delay
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping
class Controller @Autowired constructor(
    private val service: Service
) {

    @GetMapping("/date")
    @SampleAnnotation
    suspend fun date(): LocalDate {
        return service.date(LocalDate.parse("2018-12-12"))
    }


    @GetMapping("/hello/{name}")
    @SampleAnnotation
    suspend fun hello(@PathVariable name: String): String {
        return service.hello(name, 20)
    }

    @GetMapping("/delayed-return")
    @SampleAnnotation
    suspend fun delayedReturn(): String {
        delay(10)
        return "delayed hello!"
    }


    @GetMapping("/exception")
    @SampleAnnotation
    suspend fun exception(): String? {
        return service.hello("okName", 18) + service.exception("errorName")
    }


    @GetMapping("/sync")
    @SampleAnnotation
    suspend fun sync(): String? {
        return (service.hello("syncName1", 18)
            + service.hello("syncName2", 65))
    }

    @GetMapping("/null")
    @SampleAnnotation
    suspend fun nameNull(): String? {
        return service.hello(null, 18)
    }

}
