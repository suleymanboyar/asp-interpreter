// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Token {
    public TokenKind kind;
    public String name, stringLit;
    public long integerLit;
    public double floatLit;
    public int lineNum;

    Token(TokenKind k) {
        this(k, 0);
    }

    Token(TokenKind k, int lNum) {
        this.kind = k;
        this.lineNum = lNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStringLit(String stringLit) {
        this.stringLit = stringLit;
    }

    public void setIntegerLit(long integerLit) {
        this.integerLit = integerLit;
    }

    public void setFloatLit(Double floatLit) {
        this.floatLit = floatLit;
    }

    void checkResWords() {
        if (this.kind != TokenKind.nameToken)
            return;

        for (TokenKind tk : EnumSet.range(TokenKind.andToken, TokenKind.yieldToken)) {
            if (this.name.equals(tk.image)) {
                this.kind = tk;
                break;
            }
        }
    }

    public String showInfo() {
        String t = this.kind + " token";
        if (this.lineNum > 0) {
            t += " on line " + this.lineNum;
        }

        switch (this.kind) {
            case floatToken:
                t += ": " + this.floatLit;
                break;
            case integerToken:
                t += ": " + this.integerLit;
                break;
            case nameToken:
                t += ": " + this.name;
                break;
            case stringToken:
                if (this.stringLit.indexOf('"') >= 0)
                    t += ": '" + this.stringLit + "'";
                else
                    t += ": " + '"' + this.stringLit + '"';
                break;
        }
        return t;
    }

    @Override
    public String toString() {
        return this.kind.toString();
    }
}
