package main.scala

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter

object D2WUtils {
  def removeSignToGetWords(wordsTmpArray: Array[String]): ArrayBuffer[String] = {
    var result = new ArrayBuffer[String]
    val specialChars = Array((" "), (";"), ("/"), ("."), (","), ("\""), ("\t"), ("#"), ("\u00a0"), ("("), (")"), ("["), ("]"), ("!"), ("?"), ("'"), (":"), ("&"), ("="), ("-"), ("<"), (">"),("–"), ("{"), ("}"), ("\\"), ("..."), ("*"), ("+"), ("$"), ("@"), ("\u00a9"), ("\u00ae"))
    wordsTmpArray.foreach { x =>
      {
        if (!specialChars.contains(x.trim) && !x.trim.equals("_")) {
          val y = x.toLowerCase().trim
          result += y
        }
      }
    }
    result
  }

  def writeFile(content: String, outputDir: String, fileName: String): Unit = {
    val file = new File(outputDir + fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.flush()
    bw.write(content)
    bw.close()
  }
}