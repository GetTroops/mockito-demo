package demo

trait Repository {
  def load(id: Int): Option[Model]
  def save(model: Model): Boolean
}
