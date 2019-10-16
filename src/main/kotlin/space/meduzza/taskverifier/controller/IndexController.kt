package space.meduzza.taskverifier.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import space.meduzza.taskverifier.CompilationServiceFacade
import space.meduzza.taskverifier.services.comp.JavaCompilationService
import space.meduzza.taskverifier.services.task.TaskService
import space.meduzza.taskverifier.services.user.UserService
import java.io.File
import java.security.Principal

@Controller
class IndexController {

    @Autowired
    private lateinit var userService: UserService
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
        return "pages/main"
    }

    @PostMapping("/check-task")
    @PreAuthorize("hasRole('USER')")
    fun checkTask(code: MultipartFile, model: Model, principal: Principal): String {

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

        return "pages/task-check-status"
    }

    @RequestMapping("/tasks")
    @PreAuthorize("hasRole('USER')")
    fun tasks(principal: Principal, model: Model): String {
        val user = userService.getUserByUsername(principal.name)
        model.addAttribute("tasks", userService.getUserTasks(user))
        return "pages/tasks"
    }

    @RequestMapping("/task")
    @PreAuthorize("hasRole('USER')")
    fun task(@RequestParam(name = "task-id") taskId: Long, principal: Principal, model: Model): String {
        model.addAttribute("task", taskService.getTask(taskId))
        return "pages/task-detail"
    }

    @RequestMapping("/settings")
    @PreAuthorize("hasRole('USER')")
    fun settings(principal: Principal, model: Model): String {
        return "pages/settings"
    }
}
