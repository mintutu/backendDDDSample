package domain.masterData.user

import com.google.inject.Inject

import scala.util.Try

/**
  * Created by specter8x on 3/12/17.
  */
class UserFactory @Inject() (userRepo: UserRepository){

  def createUser(userName: String,
                 email: String): Try[(User, String)] = {
    val (newCredential, generatedPassword) = Credential.newRandomCredential
    Try(User(-1, userName, email, isInactive = true, newCredential), generatedPassword)
  }
}
