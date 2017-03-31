package demo

final case class Model(id: Int)

class TooManyModelsException extends RuntimeException("There can be only 10 models")
class NoNegativeModelException extends RuntimeException("The ID must be positive")
