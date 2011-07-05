package com.coconut_palm_software.xscalawt
import scala.collection.GenTraversableOnce

/**
 * Mix in to implementations of IContentProvider, ILabelProvider, etc.
 * to avoid most casts. Element is the type of elements.
 */
trait ProviderImplicits[Element <: AnyRef] {
  protected implicit def castElement(x: AnyRef) = x.asInstanceOf[Element]
  
  protected implicit def upcastArray(array: Array[Element]) = 
    array.asInstanceOf[Array[AnyRef]]
  
  protected implicit def javaListToArray(list: java.util.List[Element]) = 
    list.toArray
  
  protected implicit def traversableToArray(seq: GenTraversableOnce[Element]) = 
    seq.toArray[AnyRef]
}