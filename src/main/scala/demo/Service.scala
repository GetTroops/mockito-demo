package demo

trait Service {
  def createNextModel(id: Int): Option[Model]
}

object Service {
  def apply(repository: Repository): Service = new Service {
    override def createNextModel(id: Int): Option[Model] = {
      val baseModelO = repository.load(id)
      baseModelO.flatMap { baseModel =>
        val newId = baseModel.id + 1
        if (newId < 10) {
          val newModel = Model(newId)
          repository.save(newModel)
          Some(newModel)
        } else {
          None
        }
      }
    }
  }
}
