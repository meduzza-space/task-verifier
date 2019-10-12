package space.meduzza.taskverifier.services.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Service
class UserService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var userEntityRepository: UserEntityRepository

    fun getUser(userId: Long) {
        userEntityRepository.findById(userId).orElseThrow{NoSuchElementException()}
    }

    fun createUser(fname: String, lname: String, username: String, password: String): UserEntity {
        return userEntityRepository.save(
                UserEntity(
                        fname = fname,
                        lname = lname,
                        username = username,
                        password = passwordEncoder.encode(password),
                        authorities = "ROLE_USER"
                )
        )
    }
}

@Entity
data class UserEntity(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val fname: String,
        val lname: String
//        val bithday: Timestamp
)

@Repository
interface UserEntityRepository: JpaRepository<UserEntity, Long>