package cn.chuanwise.networkwatchdog.util;

import java.util.Objects;

/**
 * This class consists of static utility methods for operating on format strings.
 *
 * @author Chuanwise
 */
public class Arguments {
    private Arguments() {
    }
    
    /**
     * Replace ${index} to <code>Objects.toString(arguments[index])</code>,
     * indexes start with 1. If index outs of bound, it will be replaced to
     * <code>${index:bound}</code>.
     *
     * @param format    format string
     * @param arguments arguments
     * @return formatted string
     */
    public static String format(String format, Object... arguments) {
        Objects.requireNonNull(format, "format is null!");
        Objects.requireNonNull(arguments, "arguments is null!");
        
        if (arguments.length == 0) {
            return format;
        }
        final int length = format.length();
        final StringBuilder stringBuilder = new StringBuilder(length);
    
        final int expectLeftBraceState = 1;
        final int acceptIntegerState = 2;
        final int defaultState = 3;
        
        int integer = 0;
        int state = defaultState;
        
        for (int i = 0; i < length; i++) {
            final char ch = format.charAt(i);
    
            switch (state) {
                case expectLeftBraceState:
                    if (ch == '{') {
                        state = acceptIntegerState;
                    } else {
                        stringBuilder.append(ch);
                        state = defaultState;
                    }
                    break;
                case acceptIntegerState:
                    if (Character.isDigit(ch)) {
                        integer *= 10;
                        integer += ch - '0';
                    } else if (ch == '}') {
                        if (integer > 0 && integer <= arguments.length) {
                            stringBuilder.append(arguments[integer - 1]);
                        } else {
                            stringBuilder.append("${").append(integer).append(":").append(arguments.length).append("}");
                        }
                        integer = 0;
                        state = defaultState;
                    } else {
                        stringBuilder.append("${").append(integer);
                        integer = 0;
                        state = defaultState;
                    }
                    break;
                case defaultState:
                    if (ch == '$') {
                        state = expectLeftBraceState;
                    } else {
                        stringBuilder.append(ch);
                    }
                    break;
                default:
                    throw new IllegalStateException("illegal state: " + state);
            }
        }
    
        switch (state) {
            case expectLeftBraceState:
                stringBuilder.append('$');
                break;
            case acceptIntegerState:
                stringBuilder.append('$').append(integer);
                break;
            case defaultState:
                break;
            default:
                throw new IllegalStateException("illegal state: " + state);
        }
        
        return stringBuilder.toString();
    }
}