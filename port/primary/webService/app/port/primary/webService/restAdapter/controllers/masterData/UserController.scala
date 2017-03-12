package port.primary.webService.restAdapter.controllers.masterData

import com.google.inject.Inject
import domain.masterData.user.{User, UserFactory, UserRepository}
import play.api.Logger
import play.api.i18n.MessagesApi
import port.primary.webService.restAdapter.forms.UserCreateOrUpdateForm
import port.primary.webService.restAdapter.forms.UserCreateOrUpdateForm
import port.primary.webService.restAdapter.controllers.json.UserJsonSupport._
import port.primary.webService.restAdapter.controllers.commons.CrudController
import port.primary.webService.restAdapter.forms.UserCreateOrUpdateForm

import scala.util.Try

/**
  * Created by specter8x on 3/12/17.
  */
class UserController @Inject() (userFactory: UserFactory, messagesApi: MessagesApi, userRepo: UserRepository)
  extends CrudController[User, UserCreateOrUpdateForm, UserCreateOrUpdateForm] {

  protected val createForm = UserCreateOrUpdateForm.form
  protected val updateForm = UserCreateOrUpdateForm.form
  protected val jsonWriter = userWrites
  protected val message = messagesApi
  protected val repo = userRepo


  protected def createByForm(form: UserCreateOrUpdateForm): Try[User] = {
    userFactory.createUser(form.userName, form.email).map {
      case (user, password) =>
        Logger.info(s"User $user is created with password: $password")
        user
    }
  }

  protected def updateByForm(form: UserCreateOrUpdateForm, user: User): Try[User] = {
    Try(user.update(form.userName, form.email))
  }
}
