package space.meduzza.taskverifier.services.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Service
class TaskService {

    @Autowired
    lateinit var taskRepository: TaskRepository
    @Autowired
    lateinit var userTryCodeRepository: UserTryCodeRepository

    fun getTask(taskId: Long): TaskEntity {
        return taskRepository.findById(taskId).orElseThrow { NoSuchElementException() }
    }

    fun getTasks(page: Int, size: Int): List<TaskEntity> {
        return taskRepository.findAll(PageRequest.of(page, size)).content
    }

    fun createTask(title: String, desc: String, weight: Int, output: String, input: String): TaskEntity {
        return taskRepository.save(
                TaskEntity(
                        title = title,
                        description = desc,
                        weight = weight,
                        output = output,
                        input = input
                )
        )
    }

    fun checkTask(taskId: Long, userCode: String, userOutput: String): Boolean {
        userTryCodeRepository.save(
                UserTryCodeEntity(
                        taskId = taskId,
                        userCode = userCode,
                        output = userOutput
                )
        )
        return taskRepository.findById(taskId).orElseThrow { NoSuchElementException() }.output == userOutput
    }
}

@Entity
data class TaskEntity(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val title: String,
        val description: String,
        val weight: Int,
        val input: String,
        val output: String
)

@Entity
data class UserTryCodeEntity(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val taskId: Long,
        val userCode: String,
        val output: String
)

@Repository
interface TaskRepository : JpaRepository<TaskEntity, Long>

@Repository
interface UserTryCodeRepository : JpaRepository<UserTryCodeEntity, Long>