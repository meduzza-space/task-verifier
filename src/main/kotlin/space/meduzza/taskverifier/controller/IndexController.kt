package space.meduzza.taskverifier.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.multipart.MultipartFile
import space.meduzza.taskverifier.CompilationServiceFacade
import space.meduzza.taskverifier.services.comp.JavaCompilationService
import space.meduzza.taskverifier.services.task.TaskService
import java.io.File
import java.security.Principal

@Controller
class IndexController {

    @Autowired
    private lateinit var compilationServiceFacade: CompilationServiceFacade
    @Autowired
    private lateinit var javaCompilationService: JavaCompilationService
    @Autowired
    private lateinit var taskService: TaskService
    val logger = LoggerFactory.getLogger(IndexController::class.java)

    @RequestMapping("/", method = [RequestMethod.GET])
    @PreAuthorize("hasRole('USER')")
    fun index(model: Model): String {
        return "index"
    }

    @PostMapping("/check-code")
    @PreAuthorize("hasRole('USER')")
    fun checkCode(code: MultipartFile, model: Model, principal: Principal): String {

        val srcFile = File(code.originalFilename!!)
        try {
            if (srcFile.exists())
                srcFile.delete()
            srcFile.createNewFile()

            srcFile.outputStream().use {
                it.write(code.bytes)
                it.close()
            }

            val task = taskService.getTask(4)
            val result = compilationServiceFacade.compileAndExecute(srcFile, task.input)
            val taskResult = taskService.checkTask(task.id!!, srcFile.readText(), result)
            model.addAttribute("result", taskResult)
            logger.info("{} {}", principal.name, taskResult)
        } finally {
            if (srcFile.exists()) srcFile.delete()
        }

        return "index"
    }
}
