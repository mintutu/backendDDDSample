package utility.exceptions

/**
  * Created by specter8x on 3/11/17.
  */
class UnauthorizedException (message: String = "User does not have the permissions to update") extends Exception(message){

}
