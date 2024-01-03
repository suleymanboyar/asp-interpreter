package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactorPrefix extends AspSyntax{
    Token token = null;

    AspFactorPrefix(int n){
        super(n);
    }

    public static AspFactorPrefix parse(Scanner s){
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
        test(s, plusToken, minusToken);
        afp.token = s.curToken();
        skip(s, afp.token.kind);

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(token.toString() + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // NOTE 2023-11-26: term prefix is a terminal symbol and its eval should
        // not be used
        return null;
    }

}
