package user

import application.dto.UserRecord
import com.google.inject.{Inject, Singleton}
import domain.masterData.user.{Credential, User, UserRepository}
import utility.exceptions.EntityNotFound
import utility.repository.AbstractDao

import scala.util.Try

/**
  * Created by specter8x on 3/9/17.
  */

@Singleton
class UserRepositoryImpl @Inject() (userDao: UserDao) extends UserRepository{

  override protected val dao = userDao

  override def getByEmail(email: String, allowInactive: Boolean): Try[User] = {
    getAll(allowInactive).map(_.find(_.email == email) match {
      case None => throw new EntityNotFound("Couldn't find User with Email: " + email)
      case Some(user) => user
    })
  }

  override protected def record2Entity(record: UserRecord): User = {
    User(
      record.userId,
      record.userName,
      record.email,
      record.isInactive,
      Credential(record.password,record.salt)
    )
  }

  override protected def entity2Record(entity: User): UserRecord = {
    UserRecord(
      entity.userId,
      entity.userName,
      entity.email,
      entity.isInactive,
      entity.crendential.hashedPassword,
      entity.crendential.salt
    )
  }
}
