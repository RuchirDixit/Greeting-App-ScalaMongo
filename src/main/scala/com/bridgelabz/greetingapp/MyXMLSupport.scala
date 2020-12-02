package com.bridgelabz.greetingapp

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.unmarshalling.Unmarshaller

import scala.xml.NodeSeq

trait MyXMLSupport extends ScalaXmlSupport{
  implicit val personUnmarshaller = Unmarshaller.strict[NodeSeq, Greeting] { xml ⇒
    Greeting((xml \\ "msg").text, (xml \\ "name").text)
  }
}
