package application.dto


object UserRecord {
  import anorm.SqlParser.{bool, int, str, long}
  import anorm.~

  private val rowParser =
    long("user_id") ~ str("username") ~ str("email") ~ bool("is_inactive") ~ str("password") ~ str("salt")
  val userRecordPaser = rowParser map {
    case (userId ~ username ~ email ~ isInactive ~ password ~ salt) =>
      UserRecord(userId, username, email, isInactive, password, salt)
  }
}
case class UserRecord (userId: Long,
                       userName: String,
                       email: String,
                       isInactive: Boolean,
                       password: String,
                       salt: String)

