package utility.repository

import play.api.db.Database
import java.sql.Connection

import utility.exceptions.EntityNotFound

import scala.util.Try
/**
  * Created by specter8x on 3/9/17.
  */
trait AbstractDaoHelper[T] {
  protected val database: Database
  protected def tryWithConnection[A](block: (Connection) => A): Try[A] = Try (database.withConnection(block))
  protected val entityNotFound : Exception = new EntityNotFound
}
