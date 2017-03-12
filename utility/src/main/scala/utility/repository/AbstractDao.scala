package utility.repository

import scala.util.Try

/**
  * Created by specter8x on 3/9/17.
  */
trait AbstractDao[T] extends AbstractDaoHelper[T]{

  def getById(id: Long, allowInactive: Boolean = false) : Try[T]
  def getAll(allowInactive: Boolean = false) : Try[Seq[T]]
  def insert(t: T) : Try[Long]
  def update(t: T) : Try[Int]
  def delete(t: Long) : Try[Int]
  def restore(t: Long) : Try[Int]
}
