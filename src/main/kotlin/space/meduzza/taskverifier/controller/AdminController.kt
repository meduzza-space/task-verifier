package space.meduzza.taskverifier.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import space.meduzza.taskverifier.services.user.UserEntity
import space.meduzza.taskverifier.services.user.UserEntityRepository

@RequestMapping("/admin")
@Controller
class AdminController {


    @Qualifier("userEntityRepository")
    @Autowired
    private lateinit var userEntityRepository: UserEntityRepository

    @RequestMapping("/users/all")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    fun allUser(): MutableList<UserEntity> {
        return userEntityRepository.findAll()
    }
}