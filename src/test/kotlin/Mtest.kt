
import com.zzz.user.User
import com.zzz.user.Users
import com.zzz.user.Users.runinsession
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:mysql://localhost:3306/zzz", "com.mysql.jdbc.Driver", "root", "")
    transaction {
        addLogger(StdOutSqlLogger)

    }
}