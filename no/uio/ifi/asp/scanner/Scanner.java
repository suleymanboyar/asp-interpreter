// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private int cur;
    private final int TABDIST = 4;
    private static final TokenKind[] keywords = {
            andToken, asToken,
            assertToken, breakToken,
            classToken, continueToken,
            defToken, delToken,
            elifToken, elseToken,
            exceptToken, falseToken,
            finallyToken, forToken,
            fromToken, globalToken,
            ifToken, importToken,
            inToken, isToken,
            lambdaToken, noneToken,
            nonlocalToken, notToken,
            orToken, passToken,
            raiseToken, returnToken,
            trueToken, tryToken,
            whileToken, withToken,
            yieldToken,
    };

    public Scanner(String fileName) {
        this.curFileName = fileName;
        this.indents.push(0);
        try {
            this.sourceFile = new LineNumberReader(
                    new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        } catch (IOException e) {
            scannerError("Cannot read " + fileName + "!");
        }
    }

    private void scannerError(String message) {
        String m = "Asp scanner error";
        if (curLineNum() > 0)
            m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }

    public Token curToken() {
        while (this.curLineTokens.isEmpty()) {
            this.readNextLine();
        }
        return this.curLineTokens.get(0);
    }

    public void readNextToken() {
        if (!this.curLineTokens.isEmpty())
            this.curLineTokens.remove(0);
    }

    private void readNextLine() {
        this.curLineTokens.clear();

        // Read the next line:
        String line = null;
        try {
            line = this.sourceFile.readLine();
            if (line == null) {
                this.sourceFile.close();
                this.sourceFile = null;
            } else {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        } catch (IOException e) {
            this.sourceFile = null;
            scannerError("Unspecified I/O error!");
        }
        // -- Must be changed in part 1:
        // Replace tabs with spaces
        if (line != null) {
            line = expandLeadingTabs(line);

            // Jump over line empty or line-comments
            if (line.isBlank() || line.trim().charAt(0) == '#') {
                return;
            }

            // Find indent level for cur line
            int curIndents = findIndent(line);
            if (curIndents > indents.peek()) {
                indents.push(curIndents);
                curLineTokens.add(0, new Token(TokenKind.indentToken, curLineNum()));
            }

            while (curIndents < indents.peek()) {
                indents.pop();
                curLineTokens.add(0, new Token(TokenKind.dedentToken, curLineNum()));
            }

            if (curIndents != indents.peek()) {
                scannerError("Indentation error!");
            }

            this.cur = 0;
            while (this.cur < line.length()) {
                if (line.charAt(this.cur) == '#')
                    break;
                lexer(line);
            }

            // Terminate line:
            this.curLineTokens.add(new Token(TokenKind.newLineToken, this.curLineNum()));
        } else {
            while (this.indents.peek() > 0) {
                indents.pop();
                curLineTokens.add(0, new Token(TokenKind.dedentToken, curLineNum()));
            }
            curLineTokens.add(new Token(TokenKind.eofToken));
        }

        for (Token t : this.curLineTokens)
            Main.log.noteToken(t);
    }

    private char nextChar(String line) {
        return line.charAt(this.cur++);
    }

    private char peekChar(String line) {
        return line.charAt(this.cur);
    }

    private void lexer(String line) {
        char ch = nextChar(line);
        switch (ch) {
            // blanks
            case ' ':
            case '\n':
            case '\t':
                break;
            // Operators
            case '*':
                curLineTokens.add(new Token(TokenKind.astToken, curLineNum()));
                break;
            case '>':
                if (this.cur < line.length() && peekChar(line) == '=') {
                    curLineTokens.add(new Token(TokenKind.greaterEqualToken, curLineNum()));
                    this.cur++;
                } else {
                    curLineTokens.add(new Token(TokenKind.greaterToken, curLineNum()));
                }
                break;
            case '<':
                if (this.cur < line.length() && peekChar(line) == '=') {
                    curLineTokens.add(new Token(TokenKind.lessEqualToken, curLineNum()));
                    this.cur++;
                } else {
                    curLineTokens.add(new Token(TokenKind.lessToken, curLineNum()));
                }
                break;
            case '!':
                if (this.cur < line.length() && peekChar(line) == '=') {
                    curLineTokens.add(new Token(TokenKind.notEqualToken, curLineNum()));
                    this.cur++;
                } else {
                    scannerError("Got an unexpected " + ch);
                }
                break;
            case '-':
                curLineTokens.add(new Token(TokenKind.minusToken, curLineNum()));
                break;
            case '%':
                curLineTokens.add(new Token(TokenKind.percentToken, curLineNum()));
                break;
            case '+':
                curLineTokens.add(new Token(TokenKind.plusToken, curLineNum()));
                break;
            case '/':
                if (this.cur < line.length() && peekChar(line) == '/') {
                    curLineTokens.add(new Token(TokenKind.doubleSlashToken, curLineNum()));
                    this.cur++;
                } else {
                    curLineTokens.add(new Token(TokenKind.slashToken, curLineNum()));
                }
                break;
            // Delimiters
            case ':':
                curLineTokens.add(new Token(TokenKind.colonToken, curLineNum()));
                break;
            case ';':
                curLineTokens.add(new Token(TokenKind.semicolonToken, curLineNum()));
                break;
            case ',':
                curLineTokens.add(new Token(TokenKind.commaToken, curLineNum()));
                break;
            case '=':
                if (this.cur < line.length() && peekChar(line) == '=') {
                    curLineTokens.add(new Token(TokenKind.doubleEqualToken, curLineNum()));
                    this.cur++;
                } else {
                    curLineTokens.add(new Token(TokenKind.equalToken, curLineNum()));
                }
                break;
            // Parens
            case '{':
                curLineTokens.add(new Token(TokenKind.leftBraceToken, curLineNum()));
                break;
            case '[':
                curLineTokens.add(new Token(TokenKind.leftBracketToken, curLineNum()));
                break;
            case '(':
                curLineTokens.add(new Token(TokenKind.leftParToken, curLineNum()));
                break;
            case '}':
                curLineTokens.add(new Token(TokenKind.rightBraceToken, curLineNum()));
                break;
            case ']':
                curLineTokens.add(new Token(TokenKind.rightBracketToken, curLineNum()));
                break;
            case ')':
                curLineTokens.add(new Token(TokenKind.rightParToken, curLineNum()));
                break;
            // String literal
            case '\'': {
                int start = this.cur;
                do {
                    try {
                        ch = nextChar(line);
                    } catch (StringIndexOutOfBoundsException e) {
                        scannerError("String literal not terminated!");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } while (ch != '\'');

                int end = this.cur - 1;
                Token token = new Token(TokenKind.stringToken, curLineNum());
                String lit = start == end ? "" : line.subSequence(start, end).toString();
                token.setStringLit(lit);
                this.curLineTokens.add(token);
            }
                break;
            case '"': {
                int start = this.cur;
                do {
                    try {
                        ch = nextChar(line);
                    } catch (StringIndexOutOfBoundsException e) {
                        scannerError("String literal not terminated!");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } while (ch != '"');

                int end = this.cur - 1;
                Token token = new Token(TokenKind.stringToken, curLineNum());
                String lit = start == end ? "" : line.subSequence(start, end).toString();
                token.setStringLit(lit);
                this.curLineTokens.add(token);
            }
                break;
            default:
                if (isDigit(ch)) {
                    findNumberOrFloat(line);
                } else if (isLetterAZ(ch)) {
                    findNameTokenOrKeyword(line);
                } else {
                    scannerError("Illegal character: '" + ch + "'!");
                }
                break;
        }
    }

    public int curLineNum() {
        return this.sourceFile != null ? this.sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
        int indent = 0;

        while (indent < s.length() && s.charAt(indent) == ' ')
            indent++;
        return indent;
    }

    private void findNumberOrFloat(String line) {

        int start = this.cur - 1; // store starting point of ch

        while (line.charAt(start) != '0' &&
                this.cur < line.length() &&
                isDigit(peekChar(line))) {
            nextChar(line);
        }
        boolean isFloat = false;
        if (this.cur < line.length() && peekChar(line) == '.') {
            nextChar(line);

            if (this.cur >= line.length() || !isDigit(peekChar(line)))
                scannerError("Expected number after .");

            isFloat = true;
            while (this.cur < line.length() && isDigit(peekChar(line))) {
                nextChar(line);
            }
        }

        String sub;
        if (start == this.cur)
            sub = Character.toString(line.charAt(start));
        else
            sub = line.subSequence(start, this.cur).toString();
        if (isFloat) {
            Double lit = Double.parseDouble(sub);
            Token token = new Token(TokenKind.floatToken, curLineNum());
            token.setFloatLit(lit);
            this.curLineTokens.add(token);
        } else {
            long lit = Integer.parseInt(sub);
            Token token = new Token(TokenKind.integerToken, curLineNum());
            token.setIntegerLit(lit);
            this.curLineTokens.add(token);
        }
    }

    private void findNameTokenOrKeyword(String line) {
        int start = this.cur - 1; // store starting point of ch

        while (this.cur < line.length() && (isLetterAZ(peekChar(line)) || isDigit(peekChar(line)))) {
            nextChar(line);
        }

        TokenKind keyword = null;
        for (TokenKind kw : keywords) {
            try {
                String sub = line.subSequence(start, this.cur).toString();
                if (kw.toString().equals(sub)) {
                    keyword = kw;
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }

        if (keyword != null) {
            curLineTokens.add(new Token(keyword, curLineNum()));
        } else {
            String name = line.subSequence(start, this.cur).toString();
            Token token = new Token(TokenKind.nameToken, curLineNum());
            token.setName(name);
            curLineTokens.add(token);
        }
    }

    private String expandLeadingTabs(String s) {
        // -- Must be changed in part 1:
        char[] ch = s.toCharArray();
        int spaces = 0;
        int n = 0;
        int i;
        for (i = 0; i < s.length(); i++) {
            if (ch[i] != '\t' && ch[i] != ' ') {
                break;
            }
            if (ch[i] == ' ') {
                spaces++;
                n++;
            } else {
                spaces += (TABDIST - (n % TABDIST));
                n = (TABDIST - (n % TABDIST));
            }
        }

        StringBuilder sb = new StringBuilder(spaces + s.length());
        sb.append(" ".repeat(spaces));
        sb.append(s.subSequence(i, s.length()));
        return sb.toString();
    }

    private boolean isLetterAZ(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public boolean isCompOpr() {
        TokenKind k = curToken().kind;
        return k == lessToken || k == greaterToken ||
               k == doubleEqualToken || k == lessEqualToken ||
               k == greaterEqualToken || k == notEqualToken;
    }

    public boolean isFactorPrefix() {
        TokenKind k = curToken().kind;
        // -- Must be changed in part 2:
        return k == plusToken || k == minusToken;
    }

    public boolean isFactorOpr() {
        TokenKind k = curToken().kind;
        return k == astToken || k == slashToken ||
               k == percentToken || k == doubleSlashToken;
    }

    public boolean isTermOpr() {
        TokenKind k = curToken().kind;
        return k == plusToken || k == minusToken;
    }

    public boolean isPrimarySuffix() {
        TokenKind k = curToken().kind;
        return k == leftParToken || k == leftBracketToken;
    }

    public boolean anyEqualToken() {
        for (Token t : this.curLineTokens) {
            if (t.kind == equalToken)
                return true;
            if (t.kind == semicolonToken)
                return false;
        }
        return false;
    }
}
