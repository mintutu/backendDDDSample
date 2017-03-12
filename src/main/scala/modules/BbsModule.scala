package modules

import com.google.inject.AbstractModule
import domain.masterData.user.UserRepository
import port.secondary.database.user.UserRepositoryImpl

/**
  * Created by specter8x on 3/12/17.
  */
class BbsModule extends AbstractModule{

  def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).asEagerSingleton()
  }
}
