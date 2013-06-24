package test

import play.api.test.Helpers._
import org.specs2.mutable.{Around, SpecificationFeatures}
import org.specs2.execute.Result
import play.api.test.FakeApplication
import models.Product

trait DatabaseHelpers { this: SpecificationFeatures =>
  val product = Product(1, 342545645l, "P1", "simple paperclip")
  val products = 0 to 10 map { i => Product(i, 9384928173l + i, "P" + i, "paperclip " + i)}

  trait Schema extends Around {
    def around[T <% Result](test: => T) = running(FakeApplication(additionalConfiguration =  inMemoryDatabase())) {
      test
    }
  }

  object EmptySchema extends Schema

  object SingleProduct extends Schema {
    override def around[T <% Result](test: => T) = super.around {
      Product.insert(product)

      test
    }
  }

  object SeveralProducts extends Schema {
    override def around[T <% Result](test: => T) = super.around {
      products.foreach { Product.insert(_) }

      test
    }
  }
}
