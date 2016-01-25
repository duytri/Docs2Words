package main.scala

import java.io.File
import vn.hus.nlp.sd.SentenceDetector
import vn.hus.nlp.sd.SentenceDetectorFactory
import vn.hus.nlp.tokenizer.TokenizerOptions
import vn.hus.nlp.tokenizer.VietTokenizer
import vn.hus.nlp.utils.FileIterator
import vn.hus.nlp.utils.TextFileFilter
import java.util.Properties
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

object Docs2Words {
  def main(args: Array[String]): Unit = {
    //~~~~~~~~~~Split and tokenize text data~~~~~~~~~~
    var nTokens = 0
    val senDetector = SentenceDetectorFactory.create("vietnamese")

    val currentDir = new File(".").getCanonicalPath
    val currentLibsDir = currentDir //+ File.separator + "libs"

    val inputDirPath = args(0) //currentDir + "/data/in"
    val outputDirPath = args(1)
    //val outputDirPath = currentDir + File.separator + "data" + File.separator + "out"

    //val input0 = inputDirPath + File.separator + "0"
    //val input1 = inputDirPath + File.separator + "1"

    //val inputDirFile0 = new File(input0)
    //val inputDirFile1 = new File(input1)

    val property = new Properties
    property.setProperty("sentDetectionModel", currentLibsDir + File.separator + "models" + File.separator
      + "sentDetection" + File.separator + "VietnameseSD.bin.gz");
    property.setProperty("lexiconDFA", currentLibsDir + File.separator + "models" + File.separator + "tokenization"
      + File.separator + "automata" + File.separator + "dfaLexicon.xml");
    property.setProperty("unigramModel", currentLibsDir + File.separator + "models" + File.separator + "tokenization"
      + File.separator + "bigram" + File.separator + "unigram.xml");
    property.setProperty("bigramModel", currentLibsDir + File.separator + "models" + File.separator + "tokenization"
      + File.separator + "bigram" + File.separator + "bigram.xml");
    property.setProperty("externalLexicon", currentLibsDir + File.separator + "models" + File.separator + "tokenization"
      + File.separator + "automata" + File.separator + "externalLexicon.xml");
    property.setProperty("normalizationRules", currentLibsDir + File.separator + "models" + File.separator
      + "tokenization" + File.separator + "normalization" + File.separator + "rules.txt");
    property.setProperty("lexers", currentLibsDir + File.separator + "models" + File.separator + "tokenization"
      + File.separator + "lexers" + File.separator + "lexers.xml");
    property.setProperty("namedEntityPrefix", currentLibsDir + File.separator + "models" + File.separator
      + "tokenization" + File.separator + "prefix" + File.separator + "namedEntityPrefix.xml");

    val tokenizer = new VietTokenizer(property)
    tokenizer.turnOffSentenceDetection()

    //~~~~~~~~~~Get all input files~~~~~~~~~~
    //var inputFiles0 = FileIterator.listFiles(inputDirFile0, new TextFileFilter(TokenizerOptions.TEXT_FILE_EXTENSION))
    //var inputFiles1 = FileIterator.listFiles(inputDirFile1, new TextFileFilter(TokenizerOptions.TEXT_FILE_EXTENSION))
    val inputFiles = FileIterator.listFiles(new File(inputDirPath), new TextFileFilter(TokenizerOptions.TEXT_FILE_EXTENSION)) //inputFiles0 ++ inputFiles1
    println("Tokenizing all files in the directory, please wait...")
    val startTime = System.currentTimeMillis()

    //Foreach text file
    for (aFile <- inputFiles) {
      // get the simple name of the file
      val outputName = aFile.getName()
      // the output file have the same name with the automatic file
      var content = new ArrayBuffer[String]
      // tokenize the content of file
      val sentences = senDetector.detectSentences(aFile.getAbsolutePath())
      for (i <- 0 to sentences.length - 1) {
        val words = tokenizer.tokenize(sentences(i))
        val wordsTmpArr = words(0).split(" ")
        content.appendAll(D2WUtils.removeSignToGetWords(wordsTmpArr))
      }
      nTokens += content.length
      D2WUtils.writeFile(content.mkString("\n"), outputDirPath, outputName)
    }
    val endTime = System.currentTimeMillis();
    val duration = (endTime - startTime) / 1000;
    println("Tokenized " + nTokens + " words of " + inputFiles.length + " files in " + duration + " (s).\n");
  }
}