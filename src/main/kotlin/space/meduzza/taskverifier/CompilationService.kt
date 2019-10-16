package space.meduzza.taskverifier

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

interface CompilationService {
    val language: Language
    fun compile(file: File): File
    fun execute(file: File, args: String): String
    fun getVersion(): String

    enum class Language {
        JAVA,
        C,
        CPP,
        CS,
        PYTHON
    }
}

@Component
class CompilationServiceFacade : ApplicationContextAware {
    private lateinit var context: ApplicationContext
    private val services = HashMap<CompilationService.Language, CompilationService>()
    private val logger = LoggerFactory.getLogger(CompilationServiceFacade::class.java)
    @PostConstruct
    private fun init() {
        context.getBeansOfType<CompilationService>()
                .forEach { (n, c) ->
                    services[c.language] = c
                    logger.info("Compilation service \"{}\" (${c.getVersion()}) loaded successfully", n)
                }
    }

    fun compileAndExecute(file: File, input: String): String {
        val compilationService = services[getLang(file)]!!
        val compFile = compilationService.compile(file)
        val result = compilationService.execute(compFile, input)
        compFile.delete()
        return result
    }

    private fun getLang(file: File): CompilationService.Language {
        return when (file.extension) {
            "java" -> CompilationService.Language.JAVA
            "c" -> CompilationService.Language.C
            "cpp" -> CompilationService.Language.CPP
            "cs" -> CompilationService.Language.CS
            "py" -> CompilationService.Language.PYTHON
            else -> throw IllegalArgumentException()
        }
    }


    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }
}