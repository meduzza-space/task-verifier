package space.meduzza.taskverifier

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Main :CommandLineRunner{
    override fun run(vararg args: String) {

    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}