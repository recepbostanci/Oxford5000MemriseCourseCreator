package com.example.demo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    private static List<MyWorkBook> myWorkBooks;

    public static void main (String args[]) throws IOException{

        System.out.println("Deneme");
        String content = getURLSource("https://www.oxfordlearnersdictionaries.com/wordlists/oxford3000-5000");

        Document doc = Jsoup.parse(content);
        Elements wordsElements = doc.select("li");


        wordsElements.removeIf(a->!a.hasAttr("data-hw"));

        System.out.println(wordsElements.size());

        List<Word> wordList = new ArrayList<>();

        int index=0;
        for (Element we:wordsElements) {

            Word word = new Word(we.attr("data-hw"), we.attr("data-ox5000"));

            if(wordList.stream().map(a->a.getName()).collect(Collectors.toList()).contains(word.getName())){
                continue;
            }

            word.setType(we.select("span").first().text());
            adjustCambridge(word);


            wordList.add(word);
            index++;

            System.out.println(index +" - "+ word.getName());
            if (index==210000) {
                break;
            };

        };


        write(wordList);
    }


    private static void write(List<Word> wordList){

        myWorkBooks = new ArrayList<>();

        write2Excel(wordList);

        try {
            for (MyWorkBook workbook: myWorkBooks) {
                FileOutputStream outputStream = new FileOutputStream(workbook.getFilename()+".xlsx");
                workbook.getXssfWorkbook().write(outputStream);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static XSSFSheet getSheet(XSSFWorkbook workbook, String type){
        XSSFSheet sheet = workbook.getSheet(type);
        if(Objects.isNull(sheet)){
            sheet = workbook.createSheet(type);
        }
        return sheet;
    }

    private static MyWorkBook getWorkBook(String level){

        Optional<MyWorkBook> optionalMyWorkBook = myWorkBooks.stream().filter(a->a.getFilename().equalsIgnoreCase(level)).findFirst();
        if(optionalMyWorkBook.isPresent()){
            return optionalMyWorkBook.get();
        }
        MyWorkBook myWorkBook= new MyWorkBook(new XSSFWorkbook(),level);
        myWorkBooks.add(myWorkBook);
        return myWorkBook;


    }

    private static int getSheetRowCount(XSSFSheet sheet){
        return sheet.getPhysicalNumberOfRows();
    }

    private static void write2Excel(List<Word> wordList){


        for (Word w : wordList) {
            XSSFSheet sheet = getSheet(getWorkBook(w.getLevel()).getXssfWorkbook(), w.getType());
            int rowCount = getSheetRowCount(sheet);
            Row row = sheet.createRow(++rowCount);

            int columnCount = 0;


            Cell cell = row.createCell(++columnCount);
            cell.setCellValue(w.getName());

            cell = row.createCell(++columnCount);
            cell.setCellValue(w.getMeaning());

            cell = row.createCell(++columnCount);
            cell.setCellValue(w.getExample());

            cell = row.createCell(++columnCount);
            cell.setCellValue(w.getMeaningTR());

            cell = row.createCell(++columnCount);
            cell.setCellValue(w.getLevel());

        }




    }

    private static void adjustCambridge(Word word){
        try{
            String content = getURLSource("https://dictionary.cambridge.org/us/dictionary/english-turkish/".concat(word.getName()));
            Document doc = Jsoup.parse(content);

            Elements means = doc.getElementsByClass("def-block ddef_block ");

            for (Element mean: means) {
                for(Node a : mean.getElementsByClass("def ddef_d db").first().childNodes()){
                    word.setMeaning(word.getMeaning().concat(a instanceof Element? ((Element) a).text() : a.toString()));
                }

                word.setMeaningTR(mean.getElementsByClass("trans dtrans dtrans-se ").first().text());

                if(mean.getElementsByClass("examp dexamp").first()!=null) {
                    for (Node a : mean.getElementsByClass("examp dexamp").first().childNodes()) {
                        word.setExample(word.getExample().concat(a instanceof Element ? ((Element) a).text() : a.toString()));
                    }
                }

                break;

            }

//            System.out.println(word.getName()+" "+word.getLevel()+" "+word.getType());
//            System.out.print(word.getExample());
//            System.out.println(word.getMeaning());
//            System.out.println(word.getMeaningTR());
//            System.out.println();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static String getURLSource(String url) throws IOException
    {
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }
}
