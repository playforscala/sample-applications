package views.html.helper

package object bootstrap {
  implicit val fieldConstructor = new FieldConstructor {
    def apply(elements: FieldElements) = bootstrap.bootstrapFieldConstructor(elements)
  } 
}