package domain.masterData.user

import utility.entity.Entity

/**
  * Created by specter8x on 3/9/17.
  */
case class User (userId: Long,
                 userName: String,
                 email: String,
                 isInactive: Boolean,
                 crendential: Credential
                 ) extends Entity {
  override val identity: Long = userId

  def checkLogin(rawPassword: String) : Boolean = {
    crendential.checkPassword(rawPassword)
  }

  def update(newUserName : String, newEmail: String): User = {
    this.copy(
      userName = newUserName,
      email = newEmail
    )
  }

  def updatePassword(newPassword: String): User = {
    this.copy(crendential = crendential.changePassword(newPassword))
  }

  def updateEmail(newEmail: String): User = {
    this.copy(email = newEmail)
  }
}
