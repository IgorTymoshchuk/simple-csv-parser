package tk.tymoshchuk.csv_utils;

import java.util.ArrayList;
import java.util.List;

public class CSVParser{
    
    private static final char EOL = '\n';
    private static final char DEF_DELIMITER = ',';
    private static final char DEF_QUOTE = '"';
    private static final char[] CHARS_TO_IGNORE = { '\r' };

    public static String[] parseLineToArr( String cvsLine ){
        List<String> resList = parseLineToList( cvsLine );
        String[] stockArr = new String[ resList.size() ];
        stockArr = resList.toArray( stockArr );
        return stockArr;
    }

    public static String[] parseLineToArr( String cvsLine, char delimiter, char quote ){
        List<String> resList = parseLineToList( cvsLine, delimiter, quote );
        String[] stockArr = new String[ resList.size() ];
        stockArr = resList.toArray( stockArr );
        return stockArr;
    }

    public static List<String> parseLineToList( String cvsLine ){
        return parseLine( cvsLine, DEF_DELIMITER, DEF_QUOTE );
    }

    public static List<String> parseLineToList( String cvsLine, char delimiter, char quote ){
        return parseLine( cvsLine, delimiter, quote );
    }

    private static List<String> parseLine( String lineToParse, char delimiter, char quote ){

        List<String> res = new ArrayList<>();

        if( lineToParse == null || lineToParse.isEmpty() ){
            return res;
        }

        StringBuilder currTokenBuilder = new StringBuilder();

        if( lineToParse.charAt( lineToParse.length() - 1 ) != EOL ){
            lineToParse += EOL;
        }
        char[] chars = lineToParse.toCharArray();

        boolean isPreviosCharQuote = false;
        boolean isTokenQuoted = false;
        char currChar;
        outer:
        for( int i = 0; i < chars.length; i++ ){

            currChar = chars[ i ];

            for( char ignoreChar : CHARS_TO_IGNORE ){
                if( ignoreChar == currChar ){
                    continue outer;
                }
            }

            if( EOL == currChar ){
                res.add( currTokenBuilder.toString() );
                break;
            }

            if( quote == currChar ){
                if( isPreviosCharQuote ){
                    currTokenBuilder.append( currChar );
                    isPreviosCharQuote = false;
                } else {
                    isPreviosCharQuote = true;
                }
                continue;
            }

            if( delimiter == currChar ){
                if( isPreviosCharQuote && isTokenQuoted ){
                    res.add( currTokenBuilder.toString() );
                    currTokenBuilder = new StringBuilder();
                    isPreviosCharQuote = false;
                    isTokenQuoted = false;

                } else if( false == isTokenQuoted ){
                    res.add( currTokenBuilder.toString() );
                    currTokenBuilder = new StringBuilder();
                } else {
                    currTokenBuilder.append( currChar );
                    isPreviosCharQuote = false;
                }
                continue;
            }

            if( isPreviosCharQuote && isTokenQuoted == false ){
                isTokenQuoted = true;
                isPreviosCharQuote = false;
            }
            currTokenBuilder.append( currChar );
        }

        return res;
    }

}
