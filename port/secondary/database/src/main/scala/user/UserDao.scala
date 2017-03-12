package user

import java.sql.Connection

import application.dto.UserRecord
import com.google.inject.{Inject, Singleton}
import play.api.db.Database
import utility.exceptions.EntityNotFound
import utility.repository.AbstractDao
import anorm._

import scala.util.Try

/**
  * Created by specter8x on 3/9/17.
  */
@Singleton
class UserDao @Inject() (db: Database) extends AbstractDao[UserRecord]{

  protected val database = db

  def getById(userId: Long, allowInactive: Boolean = false): Try[UserRecord] = tryWithConnection {
    implicit connection =>
      SQL"""
        SELECT * FROM users WHERE user_id = $userId AND is_inactive <= $allowInactive
        """.as(UserRecord.userRecordPaser.singleOpt) match {
        case Some(record) => record
        case None => throw new EntityNotFound(s"Could'nt found user record with id = $userId")
      }
  }

  def getAll(allowInactive: Boolean = false) : Try[Seq[UserRecord]] = tryWithConnection {
    implicit connection =>
      SQL"""
        SELECT * FROM users WHERE is_inactive <= $allowInactive
        """.as(UserRecord.userRecordPaser.*)
  }

  def insert(userRecord: UserRecord) : Try[Long] = tryWithConnection {
    implicit connection =>
      SQL"""
        INSERT INTO users(username, email, password, salt, is_inactive) VALUES
        (${userRecord.userName}, ${userRecord.email}, ${userRecord.password}, ${userRecord.salt}, ${userRecord.isInactive})
        """.executeInsert[Option[Long]]() match {
        case Some(id) => id
        case None => throw new RuntimeException("Error while inserting new user" + userRecord.toString)
      }
  }

  def delete(userId: Long) : Try[Int] = tryWithConnection { implicit connection =>
    SQL"""
        UPDATE users
        SET is_inactive = 1
        WHERE user_id = $userId
      """.executeUpdate()
  }

  def restore(userId: Long): Try[Int] = tryWithConnection { implicit conn =>
    SQL"""
        UPDATE users
        SET is_inactive = 0
        WHERE user_id = $userId
      """.executeUpdate()
  }

  def update(record: UserRecord): Try[Int] = tryWithConnection { implicit conn =>
    SQL"""
        UPDATE users
        SET
          username = ${record.userName},
          password = ${record.password},
          salt = ${record.salt},
          email = ${record.email},
        WHERE user_id = ${record.userId}
      """.executeUpdate()
  }
}
