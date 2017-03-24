package domain.masterData.user

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.MessagesApi
import utility.exceptions.EntityNotFound

import scala.util.{Failure, Success}
import scalaz.\/
import scalaz.syntax.either._

/**
  * Created by specter8x on 3/18/17.
  */

@Singleton
class UserService @Inject() (userRepo: UserRepository, messagesApi: MessagesApi) {

  def validateUser(email: String, rawPassword: String): \/[String, User] = {
    userRepo.getByEmail(email, allowInactive = true) match {
      case Success(user) =>
        if (user.isInactive)
          messagesApi("user.error.inactive_user").left
        else if (user.checkLogin(rawPassword))
          user.right
        else
          messagesApi("user.error.invalid_password").left
      case Failure(e) => e match {
        case _: EntityNotFound => messagesApi("user.invalid.email").left
        case _ =>
          Logger.error("UserService.validateUser: " + e.getMessage)
          messagesApi("system.failed").left
      }
    }
  }
}
