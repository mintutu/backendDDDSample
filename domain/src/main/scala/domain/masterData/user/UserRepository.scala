package domain.masterData.user

import application.dto.UserRecord
import utility.repository.AbstractRepository

import scala.util.Try

/**
  * Created by specter8x on 3/9/17.
  */
trait UserRepository extends AbstractRepository[User, UserRecord]{
  def getByEmail(email: String, allowInactive: Boolean = false): Try[User]
}
