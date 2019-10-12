package space.meduzza.taskverifier.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal

@Controller
class MainController {



    @RequestMapping("/me")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    fun me(principal: Principal): Principal {
        return principal
    }
}