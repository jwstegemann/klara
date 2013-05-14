package klara.mongo.bson

import reactivemongo.bson._
import scala.reflect.runtime.universe._
import klara.entity._

import klara.utils.ReflectionUtils

import klara.system._


trait ProductConverters extends StandardConverters with CollectionConverters {

  //FIXME: move this to entity
  implicit object MongoIdConverter extends BSONConverter[MongoId] {
    def toBSON(element: MongoId) = element.id
    def fromBSON(value: BSONValue) = value match {
      case id : BSONObjectID => new MongoId(id)
      case x => throw BSONDeserializationError("Expected BSONObjectID to deserialize to id of Product but found " + x.getClass)
    }
  }

  implicit object MongoVersionConverter extends BSONConverter[MongoVersion] {
    def toBSON(element: MongoVersion) = BSONLong(element.version)
    def fromBSON(value: BSONValue) = value match {
      case version : BSONLong => new MongoVersion(version.value)
      case x => throw BSONDeserializationError("Expected BSONLong to deserialize to version of Product but found " + x.getClass)
    }
  }

  implicit class KlaraAppendableBSONDocument(doc: AppendableBSONDocument) {
    def appendValue[T](label: String, value: T)(implicit converter: BSONConverter[T]) = {
      converter.toBSON(value) match {
        case BSONNull => doc
        case convertedValue => doc.append(label -> convertedValue)
      }
    }
  }

  implicit class KlaraTraversableBSONDocument(doc: TraversableBSONDocument) {
    def readValue[T](label: String)(implicit converter: BSONConverter[T]) : T = {
      converter.fromBSON(doc.get(label).get)
    }
  }
  
  /*
   * ProductConverters
   */

  def productConverter0[T <: Product : TypeTag](construct: () => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter0WithNames(construct)
  }
  
  def productConverter1[A :BSONConverter, T <: Product : TypeTag](construct: (A) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter1WithNames(construct, fieldNames(0))
  }
  
  def productConverter2[A :BSONConverter,B :BSONConverter, T <: Product : TypeTag](construct: (A, B) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter2WithNames(construct,
        fieldNames(0), fieldNames(1))
  }

  def productConverter3[A :BSONConverter,B :BSONConverter,C :BSONConverter, T <: Product : TypeTag](construct: (A, B, C) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter3WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2))
  }

  def productConverter4[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter, T <: Product : TypeTag](construct: (A, B, C, D) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter4WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3))
  }

  def productConverter5[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter,E :BSONConverter, T <: Product : TypeTag](construct: (A, B, C, D, E) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter5WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4))
  }

  def productConverter6[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter6WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5))
  }

  def productConverter7[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter7WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6))
  }

  def productConverter8[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter8WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7))
  }

  def productConverter9[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter9WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8))
  }

  def productConverter10[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter10WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9))
  }

  def productConverter11[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J, K) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter11WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9),
        fieldNames(10))
  }

  def productConverter12[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter12WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9),
        fieldNames(10),
        fieldNames(11))
  }

  def productConverter13[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter13WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9),
        fieldNames(10),
        fieldNames(11),
        fieldNames(12))
  }

  def productConverter14[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter,N:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter14WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9),
        fieldNames(10),
        fieldNames(11),
        fieldNames(12),
        fieldNames(13))
  }

  def productConverter15[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter,N:BSONConverter,O:BSONConverter, T <: Product : TypeTag]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter15WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3),
        fieldNames(4),
        fieldNames(5),
        fieldNames(6),
        fieldNames(7),
        fieldNames(8),
        fieldNames(9),
        fieldNames(10),
        fieldNames(11),
        fieldNames(12),
        fieldNames(13),
        fieldNames(14))
  }

  /*
   * ProductConverters with names
   */

  def productConverter0WithNames[T <: Product](construct: () => T): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        BSONDocument()
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct()
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
  }

  def productConverter1WithNames[A :BSONConverter, T <: Product](construct: (A) => T, a: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
  }

  def productConverter2WithNames[A :BSONConverter,B :BSONConverter, T <: Product](construct: (A, B) => T, a: String, b: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
  }

  def productConverter3WithNames[A :BSONConverter,B :BSONConverter,C :BSONConverter, T <: Product](construct: (A, B, C) => T,
      a: String, b: String, c: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
  }

  def productConverter4WithNames[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter, T <: Product](construct: (A, B, C, D) => T,
      a: String, b: String, c: String, d: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
  }

  def productConverter5WithNames[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter,E :BSONConverter, T <: Product](construct: (A, B, C, D, E) => T,
      a: String, b: String, c: String, d: String, e: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter6WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,T <: Product]
      (construct: (A, B, C, D, E, F) => T,
      a:String,b:String,c:String,d:String,e:String,f:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter7WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,T <: Product]
      (construct: (A, B, C, D, E, F, G) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter8WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      T <: Product]
      (construct: (A, B, C, D, E, F, G, H) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }
        
    def productConverter9WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }
        
    def productConverter10WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter11WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J, K) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String,k:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
          .appendValue(k, element.productElement(10).asInstanceOf[K])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j),
            doc.readValue[K](k)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter12WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String,k:String,l:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
          .appendValue(k, element.productElement(10).asInstanceOf[K])
          .appendValue(l, element.productElement(11).asInstanceOf[L])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j),
            doc.readValue[K](k),
            doc.readValue[L](l)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter13WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String,k:String,l:String,m:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
          .appendValue(k, element.productElement(10).asInstanceOf[K])
          .appendValue(l, element.productElement(11).asInstanceOf[L])
          .appendValue(m, element.productElement(12).asInstanceOf[M])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j),
            doc.readValue[K](k),
            doc.readValue[L](l),
            doc.readValue[M](m)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter14WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter,N:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String,k:String,l:String,m:String,n:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
          .appendValue(k, element.productElement(10).asInstanceOf[K])
          .appendValue(l, element.productElement(11).asInstanceOf[L])
          .appendValue(m, element.productElement(12).asInstanceOf[M])
          .appendValue(n, element.productElement(13).asInstanceOf[N])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j),
            doc.readValue[K](k),
            doc.readValue[L](l),
            doc.readValue[M](m),
            doc.readValue[N](n)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }

    def productConverter15WithNames[A:BSONConverter,B:BSONConverter,C:BSONConverter,D:BSONConverter,E:BSONConverter,F:BSONConverter,G:BSONConverter,H:BSONConverter,
      I:BSONConverter,J:BSONConverter,K:BSONConverter,L:BSONConverter,M:BSONConverter,N:BSONConverter,O:BSONConverter, T <: Product]
      (construct: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => T,
      a:String,b:String,c:String,d:String,e:String,f:String,g:String,h:String,i:String,j:String,k:String,l:String,m:String,n:String,o:String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
          .appendValue(e, element.productElement(4).asInstanceOf[E])
          .appendValue(f, element.productElement(5).asInstanceOf[F])
          .appendValue(g, element.productElement(6).asInstanceOf[G])
          .appendValue(h, element.productElement(7).asInstanceOf[H])
          .appendValue(i, element.productElement(8).asInstanceOf[I])
          .appendValue(j, element.productElement(9).asInstanceOf[J])
          .appendValue(k, element.productElement(10).asInstanceOf[K])
          .appendValue(l, element.productElement(11).asInstanceOf[L])
          .appendValue(m, element.productElement(12).asInstanceOf[M])
          .appendValue(n, element.productElement(13).asInstanceOf[N])
          .appendValue(o, element.productElement(14).asInstanceOf[O])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d),
            doc.readValue[E](e),
            doc.readValue[F](f),
            doc.readValue[G](g),
            doc.readValue[H](h),
            doc.readValue[I](i),
            doc.readValue[J](j),
            doc.readValue[K](k),
            doc.readValue[L](l),
            doc.readValue[M](m),
            doc.readValue[N](n),
            doc.readValue[O](o)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }
}