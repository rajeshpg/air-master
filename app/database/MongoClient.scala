package database

import com.google.inject.{Inject, Singleton}
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

@Singleton
class MongoClient @Inject()(lifecycle: ApplicationLifecycle, val reactiveMongoApi: ReactiveMongoApi) extends ReactiveMongoComponents {

  def getCollection(collectionName: String): Future[JSONCollection] = {
    reactiveMongoApi.database.map(_.collection[JSONCollection](collectionName))
  }


  lifecycle.addStopHook(() => Future.successful(reactiveMongoApi.connection.close()))

}
