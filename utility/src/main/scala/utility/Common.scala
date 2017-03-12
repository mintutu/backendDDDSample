package utility

import java.util.Date

/**
  * Created by specter8x on 3/11/17.
  */
object Common {
  def currentMilliseconds: Long = new Date().getTime()
}
