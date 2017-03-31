package demo

import org.scalatest._
import Matchers._
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class ServiceSpec extends FlatSpec with MockitoSugar {
  class NegativeId extends ArgumentMatcher[Integer] {
    override def matches(x: Integer): Boolean = x < 0
  }

  class TooBigModel extends ArgumentMatcher[Model] {
    override def matches(x: Model): Boolean = x.id > 9
  }

  val mockRepository: Repository = mock[Repository]


  when(mockRepository.load(8)).thenReturn(Some(Model(8)))
  when(mockRepository.load(8)).thenReturn(Some(Model(9)))
  when(mockRepository.load(9)).thenReturn(Some(Model(9)))
  when(mockRepository.load(9)).thenReturn(Some(Model(3)))
  when(mockRepository.save(Model(9))).thenReturn(true)
  when(mockRepository.load(intThat(new NegativeId))).thenThrow(new NoNegativeModelException)
  when(mockRepository.save(argThat(new TooBigModel))).thenThrow(new TooManyModelsException)

  val service = Service(mockRepository)

  "createNexModel" should "not create Model(10)" in {
    service.createNextModel(9) should be(None)
  }

  "createNexModel" should "not even try to save Model(10)" in {
    service.createNextModel(9)
    verify(mockRepository, never()).save(Model(10))
  }

  it should "not catch the problem with Model(-1)" in {
    a [NoNegativeModelException] should be thrownBy service.createNextModel(-1)
  }
}
