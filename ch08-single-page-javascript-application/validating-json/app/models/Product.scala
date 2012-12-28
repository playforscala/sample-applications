package models

case class Contact(email: Option[String], fax: Option[String], phone: Option[String])

case class Company(name: String, contactDetails: Contact)

/**
 * An entry in the product catalogue.
 *
 * @param ean EAN-13 code - a unique product identifier
 * @param name Product name
 * @param description Product description
 */
case class Product(
  ean: Long,
  name: String,
  description: String,
  pieces: Option[Int],
  manufacturer: Company,
  tags: List[String],
  active: Boolean)
