package utility.entity

trait Entity {
  val identity: Long
  val isInactive: Boolean

  def isSameAs(that: Entity): Boolean =
    (this.getClass == that.getClass) && (this.identity == that.identity)
}

