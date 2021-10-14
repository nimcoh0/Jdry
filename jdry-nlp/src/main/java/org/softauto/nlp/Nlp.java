package org.softauto.nlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Nlp {

     POSModel model;
     POSTaggerME tagger;

    public Nlp(){
        try {
            InputStream modelStream = Nlp.class.getResourceAsStream("/en-pos-maxent.bin");
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public  HashMap<String,String> tag(String sentence) {
        HashMap<String,String> map = new HashMap<>();
        try {
             if (tagger != null) {
                    String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                            .tokenize(sentence);
                    String[] tags = tagger.tag(whitespaceTokenizerLine);
                    for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                        String word = whitespaceTokenizerLine[i].trim();
                        String tag = tags[i].trim();
                        //System.out.print(tag + ":" + word + "  ");
                        map.put(word,tag);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
