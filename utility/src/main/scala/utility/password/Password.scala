package utility.password

import java.math.BigInteger
import java.security.MessageDigest

import scala.util.Random

/**
  * Created by specter8x on 3/9/17.
  */
object Password {

  private val SALT_LENGTH = 16
  private val PASSWORD_LENGTH = 8

  private def md5(str: String): String = {
    val digest = MessageDigest.getInstance("MD5")
    digest.update(str.getBytes("UTF-8"), 0, str.length)
    new BigInteger(1, digest.digest()).toString(SALT_LENGTH)
  }

  def randomPassword = Random.alphanumeric.take(PASSWORD_LENGTH).mkString

  def hash(rawPwd: String, salt: String): String = {
    md5(md5(rawPwd) + salt)
  }
}