package domain.masterData.user

import utility.password.Password.{hash, randomPassword}

/**
  * Created by specter8x on 3/9/17.
  */
case class Credential (hashedPassword: String, salt: String) {
  def changePassword(newPassword: String) : Credential = {
    this.copy(hashedPassword = hash(newPassword, salt))
  }
  def checkPassword(rawPassword: String) : Boolean = {
    hash(rawPassword, salt) == hashedPassword
  }
}

object Credential {
  def newRandomCredential: (Credential, String) = {
    val password = randomPassword
    val salt = randomPassword
    (Credential(hash(password, salt), salt), password)
  }
}
