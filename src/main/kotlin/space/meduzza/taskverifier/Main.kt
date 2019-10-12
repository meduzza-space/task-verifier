package space.meduzza.taskverifier

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import space.meduzza.taskverifier.services.user.UserService

@SpringBootApplication
class Main :CommandLineRunner{
    @Autowired
    private lateinit var userService: UserService

    override fun run(vararg args: String) {

    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}