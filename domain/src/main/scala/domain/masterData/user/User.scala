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

  def update(newUserName : String, newEmail: String, newInactiveStatus: Boolean, newCrendential: Credential): User = {
    this.copy(
      userName = newUserName,
      email = newEmail,
      isInactive = newInactiveStatus,
      crendential = newCrendential
    )
  }
}
