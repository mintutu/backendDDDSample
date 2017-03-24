package utility.repository

import utility.exceptions.DataIntegrityViolationException

import scala.util.{Failure, Try}


trait AbstractRepository[E, R] {

  protected val dao: AbstractDao[R]
  protected def record2Entity(record: R): E
  protected def entity2Record(entity: E): R
  protected val CONSTRAINT_VIOLATION_MSG: String = ""
  protected def checkInvariant(entity: E): Boolean = true

  def getById(id: Long, allowInactive: Boolean = false) : Try[E] =
    dao.getById(id, allowInactive).map(record2Entity)
  def getAll(allowInactive: Boolean = false) : Try[Seq[E]] =
    dao.getAll(allowInactive).map(_.map(record2Entity))
  def insert(entity: E) : Try[Long] = {
    if (checkInvariant(entity))
      dao.insert(entity2Record(entity))
    else
      Failure(new DataIntegrityViolationException(CONSTRAINT_VIOLATION_MSG))
  }
  def update(entity: E) : Try[Boolean] = {
    if (checkInvariant(entity))
      dao.update(entity2Record(entity)) map (rowAffected => rowAffected == 1)
    else
      Failure(new DataIntegrityViolationException(CONSTRAINT_VIOLATION_MSG))
  }

  def delete(id: Long) : Try[Boolean] = dao.delete(id) map (rowAffected => rowAffected == 1)



}
