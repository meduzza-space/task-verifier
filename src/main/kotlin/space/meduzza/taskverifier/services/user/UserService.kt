package space.meduzza.taskverifier.services.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Service
class UserService : UserDetailsService {


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

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userEntityRepository.findByUsername(username).orElseThrow { UsernameNotFoundException(username) }
        return User(user.username, user.password, user.authorities.split(",").map { SimpleGrantedAuthority(it) })
    }
}

@Entity
data class UserEntity(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val fname: String,
        val lname: String,
        val username: String,
        val password: String,
        val authorities: String
//        val bithday: Timestamp
)

@Repository
interface UserEntityRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): Optional<UserEntity>
}